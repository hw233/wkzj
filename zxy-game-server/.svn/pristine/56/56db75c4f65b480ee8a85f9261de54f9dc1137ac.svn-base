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
import com.jtang.gameserver.dataconfig.model.TraderConditionConfig;
import com.jtang.gameserver.dataconfig.service.TraderService;
import com.jtang.gameserver.module.adventures.shop.trader.helper.TraderShopHelp;
import com.jtang.gameserver.module.adventures.shop.trader.model.ItemVO;

/**
 * 云游商人表
 * @author jianglf
 *
 */
@TableName(name="trader", type = DBQueueType.IMPORTANT)
public class Trader extends Entity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6043357606822453728L;
	
	/**
	 * 玩家id
	 */
	@Column(pk=true)
	public long actorId;
	
	/**
	 * 触发条件列表
	 * 条件id_已完成次数|...
	 */
	@Column
	public String conditions;
	
	/**
	 * 点券刷新次数
	 */
	@Column
	public int flushNum;
	
	/**
	 * 点券刷新时间
	 */
	@Column
	public int ticketFlushTime;
	
	/**
	 * 上一次奖励刷新时间
	 */
	@Column
	public int flushTime;
	
	/**
	 * 免费刷新次数
	 */
	@Column
	public int freeNum;
	
	/**
	 * 免费刷新使用时间
	 */
	@Column
	public int freeTime;
	
	/**
	 * 商品列表
	 */
	@Column
	public String itemList;
	
	/**
	 * 今天已物品出现次数列表
	 * 配置id_出现次数|...
	 */
	@Column
	public String goodsViewNum;
	
	/**
	 * 保底次数
	 * 保底配置id_已经刷新次数|...
	 */
	@Column
	public String least;
	
	/**
	 * 指向性碎片和魂魄出现的次数
	 */
	@Column
	public int goodsNum;
	
	/**
	 * 指向性碎片和魂魄出现的时间
	 */
	@Column
	public int goodsTime;
	
	/**
	 * 指向性装备出现的次数
	 */
	@Column
	public int equipNum;
	
	/**
	 * 指向性装备出现的时间
	 */
	@Column
	public int equipTime;
	
	public Map<Integer,Integer> conditionMap = new ConcurrentHashMap<>();
	
	public Map<Integer,ItemVO> itemMap = new ConcurrentHashMap<>();
	
	public Map<Integer,Integer> goodsViewMap = new ConcurrentHashMap<>();
	
	public Map<Integer,Integer> leastMap = new ConcurrentHashMap<>();


	@Override
	public Long getPkId() {
		return this.actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum)
			throws SQLException {
		Trader trader = new Trader();
		trader.actorId = rs.getLong("actorId");
		trader.conditions = rs.getString("conditions");
		trader.flushNum = rs.getInt("flushNum");
		trader.ticketFlushTime = rs.getInt("ticketFlushTime");
		trader.flushTime = rs.getInt("flushTime");
		trader.freeNum = rs.getInt("freeNum");
		trader.freeTime = rs.getInt("freeTime");
		trader.itemList = rs.getString("itemList");
		trader.goodsViewNum = rs.getString("goodsViewNum");
		trader.least = rs.getString("least");
		trader.goodsNum = rs.getInt("goodsNum");
		trader.goodsTime = rs.getInt("goodsTime");
		trader.equipNum = rs.getInt("equipNum");
		trader.equipTime = rs.getInt("equipTime");
		return trader;
	}

	@Override
	protected void hasReadEvent() {
		conditionMap = StringUtils.delimiterString2IntMap(conditions);
		
		List<String[]> list = StringUtils.delimiterString2Array(itemList);
		for(String[] str:list){
			ItemVO itemVO = ItemVO.valueOf(str);
			itemMap.put(itemVO.id, itemVO);
		}
		
		goodsViewMap = StringUtils.delimiterString2IntMap(goodsViewNum);
		
		leastMap = StringUtils.delimiterString2IntMap(least);
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> values = new ArrayList<>();
		if(containsPK){
			values.add(actorId);
		}
		values.add(conditions);
		values.add(flushNum);
		values.add(ticketFlushTime);
		values.add(flushTime);
		values.add(freeNum);
		values.add(freeTime);
		values.add(itemList);
		values.add(goodsViewNum);
		values.add(least);
		values.add(goodsNum);
		values.add(goodsTime);
		values.add(equipNum);
		values.add(equipTime);
		return values;
	}

	@Override
	protected void beforeWritingEvent() {
		conditions = StringUtils.numberMap2String(conditionMap);
		
		StringBuffer sb = new StringBuffer();
		for(ItemVO itemVO:itemMap.values()){
			sb.append(itemVO.parser2String()).append(Splitable.ELEMENT_DELIMITER);
		}
		if(sb.length() > 0){
			sb.deleteCharAt(sb.length() - 1);
		}
		itemList = sb.toString();
		
		goodsViewNum = StringUtils.numberMap2String(goodsViewMap);
		
		least = StringUtils.numberMap2String(leastMap);
	}

	@Override
	protected void disposeBlob() {
		conditions = EMPTY_STRING;
		itemList = EMPTY_STRING;
		goodsViewNum = EMPTY_STRING;
		least = EMPTY_STRING;
	}

	public static Trader valueOf(long actorId) {
		Trader trader = new Trader();
		trader.actorId = actorId;
		TraderShopHelp.initFirst(trader);
		for(TraderConditionConfig config:TraderService.randomConditionConfig()){
			trader.conditionMap.put(config.id, 0);
		}
		return trader;
	}

}
