package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dataconfig.model.FavorRightConfig;
import com.jtang.gameserver.dataconfig.service.FavorRightService;
import com.jtang.gameserver.module.adventures.favor.constant.FavorRule;
import com.jtang.gameserver.module.adventures.favor.model.PrivilegeVO;

/**
 * 福神眷顾
 * @author pengzy
 * <pre>
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 */
@TableName(name = "favor", type = DBQueueType.IMPORTANT)
public class Favor extends Entity<Long> {
	private static final long serialVersionUID = 2138010486971154147L;

	@Column(pk = true)
	private long actorId;
	
	/**
	 * 此奇遇出现的时间，用于此奇遇时效性的判断
	 */
	@Column
	private int appearTime;
	
	/**
	 * 共三种特权
	 * 特权1_已使用次数|特权2_已使用次数|特权3_已使用次数
	 * privilege1_usedNum|privilege2_usedNum|privilege3_usedNum：
	 */
	@Column
	private String privileges;
	
	/**
	 * 刷新时间
	 */
	@Column
	public int resetTime;
	
	/**
	 * 特权触发记录
	 */
	@Column
	private String triggerRecord;

	/**
	 * privileges转换的对象
	 */
	private Map<Integer, PrivilegeVO> privilegeList = new ConcurrentHashMap<>();
	
	/**
	 * triggerRecord转换的对象
	 */
	private Set<Integer> triggerList = Collections.newSetFromMap(new ConcurrentHashMap<Integer, Boolean>());
	
	@Override
	public Long getPkId() {
		return actorId;
	}

	@Override
	public void setPkId(Long pk) {
		actorId = pk;
	}
	
	public static Favor valueOf(long actorId) {
		Favor favor = new Favor();
		favor.actorId = actorId;
		favor.resetTime = TimeUtils.getNow();
		favor.appearTime = 0;

		for (FavorRightConfig favorConfig : FavorRightService.CONFIG_MAP.values()) {
			PrivilegeVO privilegeVO = PrivilegeVO.valueOf(favorConfig.id);
			favor.addPrivilegeVO(privilegeVO);
		}
		favor.triggerRecord = "";
		return favor;
	}
	
	public void addPrivilegeVO(PrivilegeVO privilegeVO) {
		getPrivilegeList().put(privilegeVO.getPrivilegeId(), privilegeVO);
	}
	
	public void removePrivilegeVO(int id) {
		getPrivilegeList().remove(id);
	}
	
	public PrivilegeVO getPrivilegeVO(int id) {
		return getPrivilegeList().get(id);
	}
	
	public boolean hasPrivilegeVO(int id) {
		return getPrivilegeList().containsKey(id);
	}
	
	public void setAppearTime(int appearTime) {
		if (this.appearTime == 0) {
			this.appearTime = appearTime;
		}
	}
	
	public void addTrigger(int type){
		this.getTriggerList().add(type);
	}

	public boolean hasTrigger(int triggerType) {
		if (this.getTriggerList().contains(triggerType)) {
			return true;
		}
		return false;
	}

	public int getAppearTime() {
		return appearTime;
	}
	
	public int getCountDown() {
		if (this.getAppearTime() == 0){
			return -1;
		}
		Long containTime = FavorRule.CONTAIN_TIME;
		Long lastTime = containTime - (TimeUtils.getNow() - this.getAppearTime());
		return lastTime.intValue();
	}
	
	public Map<Integer, PrivilegeVO> getPrivilegeList() {
		return this.privilegeList;
	}

	public Set<Integer> getTriggerList() {
		return this.triggerList;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Favor favor = new Favor();
		favor.actorId = rs.getLong("actorId");
		favor.appearTime = rs.getInt("appearTime");
		favor.privileges = rs.getString("privileges");
		favor.resetTime = rs.getInt("resetTime");
		return favor;
	}

	@Override
	protected void hasReadEvent() {
		
		if (StringUtils.isNotBlank(this.privileges)) {
			List<String[]> list = StringUtils.delimiterString2Array(this.privileges);
			for (String[] privilege : list) {
				PrivilegeVO vo = PrivilegeVO.valueOf(privilege);
				this.privilegeList.put(vo.getPrivilegeId(), vo);
			}
		}
		
		if (!StringUtils.isNotBlank(this.triggerRecord)) {
			this.triggerList = StringUtils.splitString2Set(this.triggerRecord);
		}
		
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if(containsPK){
			value.add(actorId);
		}
		value.add(appearTime);
		value.add(privileges);
		value.add(resetTime);
		value.add(triggerRecord);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		
		List<String> list = new ArrayList<>();
		for (PrivilegeVO privilegeVO1 : getPrivilegeList().values()) {
			list.add(privilegeVO1.parseToString());
		}
		this.privileges = StringUtils.collection2SplitString(list, Splitable.ELEMENT_DELIMITER);
		
		//触发记录转换回字符串
		this.triggerRecord = StringUtils.Set2SplitString(this.triggerList, Splitable.ATTRIBUTE_SPLIT);
	}
	
	public void reset() {
		this.appearTime = 0;
		this.privilegeList.clear();
		this.triggerList.clear();
		this.resetTime = 0;
		for (FavorRightConfig favorConfig : FavorRightService.CONFIG_MAP.values()) {
			PrivilegeVO privilegeVO = PrivilegeVO.valueOf(favorConfig.id);
			this.addPrivilegeVO(privilegeVO);
		}
	}
	@Override
	protected void disposeBlob() {
		this.privileges = EMPTY_STRING;
	}
	
	

	
}
