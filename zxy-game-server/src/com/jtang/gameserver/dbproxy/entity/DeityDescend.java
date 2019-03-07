package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.extapp.deitydesc.model.DeityDescendVO;

/**
 * 天神下凡活动数据实体
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author lig
 *
 */
@TableName(name="deityDescend", type = DBQueueType.IMPORTANT)
public class DeityDescend extends Entity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6032936509831194513L;

	/**
	 * 角色ID
	 */
	@Column (pk = true)
	private long actorId;
	
	/**
	 * 当前需要点亮的字
	 */
	@Column
	public byte currentCharIndex;
	
	/**
	 * 总共的砸蛋数
	 */
	@Column
	public int totalHit;
	
	/**
	 * 砸蛋历史记录
	 * <pre>
	 * 格式：DeityDescendVO对象|DeityDescendVO对象|DeityDescendVO对象
	 * {@code DeityDescendVO}
	 * </pre>
	 */
	@Column
	public String hitHistory;
	
	
	/**
	 * 仙人砸蛋记录VO集合
	 * <pre>
	 * hitHistory转换成的对象
	 * key: DeityDescendVO的heroId: {@code DeityDescendVO}
	 * </pre>
	 */
	private Map<Integer, DeityDescendVO> deityDescendMap = new ConcurrentHashMap<Integer, DeityDescendVO>();
	
	@Override
	public Long getPkId() {
		return actorId;
	}

	@Override
	public void setPkId(Long pk) {
		actorId = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum)
			throws SQLException {
		DeityDescend deityDescend = new DeityDescend();
		deityDescend.actorId = rs.getLong("actorId");
		deityDescend.currentCharIndex = rs.getByte("currentCharIndex");
		deityDescend.totalHit = rs.getInt("totalHit");
		deityDescend.hitHistory = rs.getString("hitHistory");
		return deityDescend;
	}

	@Override
	protected void hasReadEvent() {
		
		//初始化hitHistory转换为对象
		if (StringUtils.isNotBlank(this.hitHistory)) {
			List<String[]> list = StringUtils.delimiterString2Array(this.hitHistory);
			for (String[] array : list) {
				DeityDescendVO vo = DeityDescendVO.valueOf(array);
				deityDescendMap.put(vo.heroId, vo);
			}
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.actorId);
		}
		value.add(this.currentCharIndex);
		value.add(this.totalHit);
		value.add(this.hitHistory);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		Map<Integer, DeityDescendVO> deityDescendMap = this.deityDescendMap;
		List<String> deityStringList = new ArrayList<String>();
		for (DeityDescendVO vo : deityDescendMap.values()) {
			deityStringList.add(vo.parse2String());
		}
		this.hitHistory = StringUtils.collection2SplitString(deityStringList, Splitable.ELEMENT_DELIMITER);
	}

	@Override
	protected void disposeBlob() {
		this.hitHistory = EMPTY_STRING;		
	}


	public static DeityDescend valueOf(long actorId) {
		DeityDescend deity = new DeityDescend();
		deity.actorId = actorId;
		deity.currentCharIndex = 0;
		deity.totalHit = 0;
		deity.hitHistory = EMPTY_STRING;
		return deity;
	}
	
	public DeityDescendVO getDeityDescendVO(int heroId) {
		if (deityDescendMap.containsKey(heroId)) {
			DeityDescendVO existVo = deityDescendMap.get(heroId);
			this.currentCharIndex = existVo.curIndex;
			this.totalHit = existVo.totalHit;
			return existVo;
		}
		DeityDescendVO newVo = DeityDescendVO.valueOf(heroId);
		deityDescendMap.put(heroId, newVo);
		return newVo;
	}
}
