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
import com.jtang.gameserver.dataconfig.service.LoveService;
import com.jtang.gameserver.module.love.model.LoveShopVO;

/**
 * 结婚商店
 * @author jianglf
 *
 */
@TableName(name="loveShop", type = DBQueueType.IMPORTANT)
public class LoveShop extends Entity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6333311503331535046L;
	
	
	/**
	 * 玩家id
	 */
	@Column(pk=true)
	public long actorId;
	
	/**
	 * 刷新次数
	 */
	@Column
	public int flushNum;
	
	/**
	 * 点券刷新时间
	 */
	@Column
	public int ticketFlushTime;
	
	/**
	 * 刷新时间
	 */
	@Column
	public int flushTime;
	
	/**
	 * 商品列表
	 */
	@Column
	public String reward;
	
	public Map<Integer,LoveShopVO> rewardMap = new ConcurrentHashMap<>();

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
		LoveShop loveShop = new LoveShop();
		loveShop.actorId = rs.getLong("actorId");
		loveShop.flushNum = rs.getInt("flushNum");
		loveShop.ticketFlushTime = rs.getInt("ticketFlushTime");
		loveShop.flushTime = rs.getInt("flushTime");
		loveShop.reward = rs.getString("reward");
		return loveShop;
	}

	@Override
	protected void hasReadEvent() {
		List<String[]> list = StringUtils.delimiterString2Array(reward);
		for (String[] str : list) {
			LoveShopVO loveShopVO = LoveShopVO.valueOf(str);
			rewardMap.put(loveShopVO.id,loveShopVO);
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> values = new ArrayList<>();
		if(containsPK){
			values.add(actorId);
		}
		values.add(flushNum);
		values.add(ticketFlushTime);
		values.add(flushTime);
		values.add(reward);
		return values;
	}

	@Override
	protected void beforeWritingEvent() {
		StringBuffer sb = new StringBuffer();
		for(LoveShopVO vo : rewardMap.values()){
			sb.append(vo.parser2String()).append(Splitable.ELEMENT_DELIMITER);
		}
		if(rewardMap.isEmpty() == false){
			sb.deleteCharAt(sb.length() - 1);
		}
		this.reward = sb.toString();
	}

	@Override
	protected void disposeBlob() {
		this.reward = EMPTY_STRING;
	}

	public static LoveShop valueOf(long actorId) {
		LoveShop loveShop = new LoveShop();
		loveShop.actorId = actorId;
		loveShop.rewardMap = LoveService.initShop();
		return loveShop;
	}

}
