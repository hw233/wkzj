package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.module.adventures.bable.model.BableExchangeVO;
import com.jtang.gameserver.module.adventures.bable.model.BableHistoryVO;
import com.jtang.gameserver.module.adventures.bable.model.BableStateVO;

/**
 * 奇遇-通天塔
 * <pre>
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author 0x737263
 *
 */
@TableName(name="bable", type = DBQueueType.IMPORTANT)
public class Bable extends Entity<Long> { 
	private static final long serialVersionUID = -4840038551693031450L;

	@Column(pk = true)
	public long actorId;

	/**
	 * 已重置次数
	 */
	@Column
	public int resetNum;
	
	/**
	 * 历史登塔记录
	 * 登天塔类型_层数_通天币|...
	 */
	@Column
	public String history;
	
	/**
	 * 本次登塔状态
	 * 登天塔类型_层数_星数_已用星数_已使用重试次数_登塔状态(0.登塔中1.已结束)_是否已跳层(0.否1.是)
	 */
	@Column
	public String bableState;
	
	/**
	 * 本次兑换列表
	 * 物品id_类型_数量_需要星星数量|...
	 */
	@Column
	public String exchange;
	
	/**
	 * 下一关阵容
	 */
	@Column
	public String monster;
	
	/**
	 * 上次重置时间
	 */
	@Column
	public int resetTime;
	
	/**
	 * 登塔剩余时间
	 */
	@Column
	public int autoTime;
	
	/**
	 * 自动登塔类型
	 */
	@Column
	public int autoBableType;
	
	/**
	 * 使用物品id
	 */
	@Column
	public int autoUseGoodsId;
	/**
	 * 自动登塔层数
	 */
	@Column
	public int autoFloor;
	
	
	public Map<Integer,BableHistoryVO> historyMap = new HashMap<>(); 
	
	public BableStateVO bableStateVO;
	
	public Map<Integer,BableExchangeVO> exchangeMap = new HashMap<>();
	
	public Map<Integer,Integer> monsterMap = new HashMap<>();
	
	@Override
	public Long getPkId() {
		return actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum)
			throws SQLException {
		Bable bable = new Bable();
		bable.actorId = rs.getLong("actorId");
		bable.resetNum = rs.getInt("resetNum");
		bable.history = rs.getString("history");
		bable.bableState = rs.getString("bableState");
		bable.exchange = rs.getString("exchange");
		bable.monster = rs.getString("monster");
		bable.resetTime = rs.getInt("resetTime");
		bable.autoTime = rs.getInt("autoTime");
		bable.autoBableType = rs.getInt("autoBableType");
		bable.autoUseGoodsId = rs.getInt("autoUseGoodsId");
		bable.autoFloor = rs.getInt("autoFloor");
		return bable;
	}

	@Override
	protected void hasReadEvent() {
		List<String[]> historyList = StringUtils.delimiterString2Array(history);
		for(String[] str:historyList){
			BableHistoryVO historyVO = new BableHistoryVO(str);
			historyMap.put(historyVO.type, historyVO);
		}
		
		if("".equals(bableState) == false){
			bableStateVO = new BableStateVO(bableState);
		}
		
		List<String[]> exchangeList = StringUtils.delimiterString2Array(exchange);
		for(String[] str:exchangeList){
			BableExchangeVO exchangeVO = new BableExchangeVO(str);
			exchangeMap.put(exchangeVO.exchangeId, exchangeVO);
		}
		
		monsterMap = StringUtils.delimiterString2IntMap(monster);
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> values = new ArrayList<>();
		if(containsPK){
			values.add(actorId);
		}
		values.add(resetNum);
		values.add(history);
		values.add(bableState);
		values.add(exchange);
		values.add(monster);
		values.add(resetTime);
		values.add(autoTime);
		values.add(autoBableType);
		values.add(autoUseGoodsId);
		values.add(autoFloor);
		return values;
	}

	@Override
	protected void beforeWritingEvent() {
		StringBuffer historyString = new StringBuffer();
		for(BableHistoryVO historyVO : historyMap.values()){
			historyString.append(historyVO.parser2String());
			historyString.append(Splitable.ELEMENT_DELIMITER);
		}
		if(historyString.length() > 0){
			history = historyString.deleteCharAt(historyString.length() - 1).toString();
		}else{
			history = "";
		}
		
		if(bableStateVO == null){
			bableState = "";
		}else{
			bableState = bableStateVO.parser2String();
		}
		
		StringBuffer exchangeString = new StringBuffer();
		for(BableExchangeVO exchangeVO : exchangeMap.values()){
			exchangeString.append(exchangeVO.parser2String());
			exchangeString.append(Splitable.ELEMENT_DELIMITER);
		}
		if(exchangeString.length() > 0){
			exchange = exchangeString.deleteCharAt(exchangeString.length() - 1).toString();
		}else{
			exchange = "";
		}
		
		monster = StringUtils.map2DelimiterString(monsterMap,Splitable.ATTRIBUTE_SPLIT,Splitable.ELEMENT_DELIMITER);
	}
	
	public BableHistoryVO getHostoryVO(int bableType){
		return historyMap.get(bableType);
	}

	public static Bable valueOf(long actorId) {
		Bable bable = new Bable();
		bable.actorId = actorId;
		bable.resetNum = 0;
		bable.history = "";
		bable.bableState = "";
		bable.exchange = "";
		bable.monster = "";
		bable.resetTime = TimeUtils.getNow();
		return bable;
	}
	
	@Override
	protected void disposeBlob() {
		this.history = EMPTY_STRING;
		this.bableState = EMPTY_STRING;
		this.exchange = EMPTY_STRING;
		this.monster = EMPTY_STRING;
	}
	
	
}
