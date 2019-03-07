package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.ally.model.AllyVO;
import com.jtang.gameserver.module.ally.model.RemoveAllyVO;

/**
 * 盟友关系表
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author vinceruan
 */
@TableName(name = "ally", type = DBQueueType.IMPORTANT)
public class Ally extends Entity<Long> {
	private static final long serialVersionUID = 3402296128655747955L;

	@Column(pk = true)
	private long actorId;
	
	/**
	 * 最新切磋时间
	 */
	@Column
	public int lastFightTime;
	
	/**
	 * 日切磋总次数
	 */
	@Column
	public int dayFightCount;
	
	/**
	 * <pre>
	 * 名称：盟友信息
	 * 解析：盟友id_切磋时间_切磋次数_切磋失败数_切磋胜利数|盟友id_切磋时间_切磋次数_切磋失败数_切磋胜利数
	 * </pre>
	 */
	@Column
	private String ally;
	
	/**
	 * <pre>
	 * 名称：删除盟友信息
	 * 解析：盟友id_被删除时间|盟友id_被删除时间
	 * </pre>
	 */
	@Column
	private String removeAlly;
	
	/**
	 * ally 盟友的对象列表
	 */
	private Map<Long, AllyVO> allyMap = new ConcurrentHashMap<>();

	/**
	 * removeAlly 被删除盟友的对象列表
	 */
	private Map<Long, RemoveAllyVO> removeAllyMap = new ConcurrentHashMap<>();

	/**
	 * 获取所有盟友
	 * @return
	 */
	public Map<Long, AllyVO> getAllyMap() {
		return this.allyMap;
	}

	/**
	 * 获取所有被删除的盟友
	 * @return
	 */
	public Map<Long, RemoveAllyVO> getRemoveAllyMap() {
		return removeAllyMap;
	}

	/**
	 * 添加盟友
	 * @param allyVO
	 */
	public void addAlly(AllyVO allyVO) {
		Map<Long, AllyVO> map = getAllyMap();
		map.put(allyVO.actorId, allyVO);
	}

	/**
	 * 删除盟友,并将删除的盟友添加到被删除的盟友列表中
	 * @param actorId
	 */
	public void removeAlly(long actorId) {
		Map<Long, AllyVO> map = getAllyMap();
		if (map.containsKey(actorId)) {
			map.remove(actorId);
			addRemoveAlly(actorId);
		}
	}

//	/**
//	 * 在修改盟友之后比如删除或添加,更新代表盟友列表的字符串以便之后更新到数据库持久化
//	 */
//	private void updateAllyStr() {
//
//		Map<Long, AllyVO> allyMap = getAllyMap();
//		if (allyMap.isEmpty()) {
//			ally = "";
//			return;
//		}
//		List<String> alliesList = new ArrayList<>();
//		for (AllyVO vo : allyMap.values()) {
//			alliesList.add(vo.parseToString());
//		}
//		ally = StringUtils.collection2SplitString(alliesList, Splitable.ELEMENT_DELIMITER);
//	}

	private void addRemoveAlly(long actorId) {
		Map<Long, RemoveAllyVO> map = getRemoveAllyMap();
		RemoveAllyVO removedVO = RemoveAllyVO.valueOf(actorId);
		map.put(actorId, removedVO);
	}

//	/**
//	 * 更新被删除的盟友字符串
//	 */
//	private void updateRemoveAllyStr() {
//		Map<Long, RemoveAllyVO> map = getRemoveAllyMap();
//		List<String> alliesList = new ArrayList<>();
//		for (RemoveAllyVO vo : map.values()) {
//			alliesList.add(vo.parseToString());
//		}
//		removeAlly = StringUtils.collection2SplitString(alliesList, Splitable.ELEMENT_DELIMITER);
//	}

	/**
	 * 获取某个盟友
	 * 
	 * @param actorId
	 */
	public AllyVO getAlly(long actorId) {
		Map<Long, AllyVO> map = getAllyMap();
		return map.get(actorId);
	}

	/**
	 * 返回所有盟友
	 * 
	 * @return
	 */
	public Collection<AllyVO> getAlly() {
		Map<Long, AllyVO> map = getAllyMap();
		return map.values();
	}

	/**
	 * 是否包含盟友
	 * 
	 * @param actorId
	 * @return
	 */
	public boolean containAlly(long actorId) {
		Map<Long, AllyVO> map = getAllyMap();
		return map.containsKey(actorId);
	}

	/**
	 * 返回已有的盟友数量
	 * 
	 * @return
	 */
	public int getAllySize() {
		return getAllyMap().size();
	}

	public boolean containRemoveAlly(long actorId) {
		return getRemoveAllyMap().containsKey(actorId);
	}

	/**
	 * 删除那些已经超过了时间限制的RemoveAllyVO
	 * @param intervalTime
	 */
	public void removeOverdueRemoveAlly(int intervalTime) {
		
		Map<Long, RemoveAllyVO> removeAllyMap = getRemoveAllyMap();
		Iterator<Long> iterator = removeAllyMap.keySet().iterator();
		while (iterator.hasNext()) {
			long key = iterator.next();
			RemoveAllyVO vo = removeAllyMap.get(key);
			if (DateUtils.beyondTheTime(vo.getRemoveTime(), intervalTime)) {
				iterator.remove();
			}
		}
	}


	/**
	 * 判断删除盟友的时间是否超过了时间限制
	 * @param actorId
	 * @param intervalTime
	 * @return
	 */
	public boolean removeAllyBeyondTheTime(long actorId, int intervalTime) {
		return DateUtils.beyondTheTime(getRemoveAllyMap().get(actorId).getRemoveTime(), intervalTime);
	}

	@Override
	public Long getPkId() {
		return actorId;
	}

	@Override
	public void setPkId(Long pk) {
		actorId = pk;
	}
	
	public static Ally valueOf(long actorId) {
		Ally ally = new Ally();
		ally.actorId = actorId;
		ally.lastFightTime = 0;
		ally.dayFightCount = 0;
		ally.ally = "";
		ally.removeAlly = "";
		return ally;
	}
	
	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Ally ally = new Ally();
		ally.actorId = rs.getLong("actorId");
		ally.lastFightTime = rs.getInt("lastFightTime");
		ally.dayFightCount = rs.getInt("dayFightCount");
		ally.ally = rs.getString("ally");
		ally.removeAlly = rs.getString("removeAlly");
		return ally;
	}
	
	@Override
	protected void hasReadEvent() {
		
		// 将盟友的字符串集合转换成盟友对象集合
		if (StringUtils.isNotBlank(this.ally)) {
			List<String[]> allyList = StringUtils.delimiterString2Array(this.ally);
			for (String[] vo : allyList) {
				AllyVO allyVO = AllyVO.valueOf(vo);
				this.allyMap.put(allyVO.actorId, allyVO);
			}
		}
		
		//将被删除的盟友字符串转换成对象形式
		if (StringUtils.isNotBlank(this.removeAlly)) {
			List<String[]> removedAllyList = StringUtils.delimiterString2Array(this.removeAlly);
			for (String[] vo : removedAllyList) {
				RemoveAllyVO removedVO = RemoveAllyVO.valueOf(vo);
				this.removeAllyMap.put(removedVO.getActorId(), removedVO);
			}
		}
	}
	
	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.actorId);
		}
		value.add(this.lastFightTime);
		value.add(this.dayFightCount);
		value.add(this.ally);
		value.add(this.removeAlly);
		return value;
	}
	
	@Override
	protected void beforeWritingEvent() {
		
		//在修改盟友之后比如删除或添加,更新代表盟友列表的字符串以便之后更新到数据库持久化
		Map<Long, AllyVO> allyMap = getAllyMap();
		List<String> allyList = new ArrayList<>();
		for (AllyVO vo : allyMap.values()) {
			allyList.add(vo.parseToString());
		}
		this.ally = StringUtils.collection2SplitString(allyList, Splitable.ELEMENT_DELIMITER);
		
		
		//
		Map<Long, RemoveAllyVO> removeAllyMap = getRemoveAllyMap();
		List<String> removeAllyList = new ArrayList<>();
		for (RemoveAllyVO vo : removeAllyMap.values()) {
			removeAllyList.add(vo.parseToString());
		}
		this.removeAlly = StringUtils.collection2SplitString(removeAllyList, Splitable.ELEMENT_DELIMITER);
	}
	
	public void reset() {
		this.dayFightCount = 0;
		this.lastFightTime = 0;
		for (AllyVO allyVO : getAlly()) {
			allyVO.fightNum = 0;
			allyVO.fightTime = 0;
		}
	}
	
	@Override
	protected void disposeBlob() {
		this.ally = EMPTY_STRING;
		this.removeAlly = EMPTY_STRING;
		
	}
	
}
