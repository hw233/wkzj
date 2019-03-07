package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.module.adventures.shop.shop.model.ExchangeVO;
import com.jtang.gameserver.module.adventures.shop.shop.model.ShopVO;

/**
 * 商店
 * <pre>
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色对应一条记录
 * </pre>
 *
 */
@TableName(name = "shop", type = DBQueueType.IMPORTANT)
public class Shop extends Entity<Long> {
	private static final long serialVersionUID = -1770513979798381953L;

	/**
	 * 角色id
	 * */
	@Column(pk=true)
	private long actorId;
	
	/**
	 * 购买信息
	 * */
	@Column
	private String buyInfo;
	
	/**
	 * 最后一次购买时间
	 * */
	@Column
	public int buyTime;
	
	/**
	 * 上次点券刷新时间
	 */
	@Column
	public int resetTime;
	
	/**
	 * 上一次奖励刷新时间
	 */
	@Column
	public int flushTime;
	
	/**
	 * 点券刷新次数
	 */
	@Column
	public int ticketFlush;
	
	/**
	 * 奖励列表
	 */
	@Column
	private String reward;
	
	/**
	 * 奖励列表
	 * key: 兑换id  value:奖励
	 */
	public Map<Integer, ExchangeVO> rewardMap = new ConcurrentHashMap<>();
	
	/**
	 * 商品列表
	 */
	private Map<Integer, ShopVO> shopsMap = new ConcurrentHashMap<>();

	@Override
	public Long getPkId() {
		return actorId;
	}

	@Override
	public void setPkId(Long pk) {
		actorId=pk;
	}
	
	/**
	 * 获得shop中购买商品信息map
	 * */
	public Map<Integer,ShopVO> getShopMap() {
		return this.shopsMap;
	}
	
	public static Shop valueOf(long actorId) {
		Shop shop = new Shop();
		shop.actorId = actorId;
		shop.buyInfo = "";
		shop.buyTime = 0;
		shop.shopsMap = shop.getShopMap();
		shop.flushTime = TimeUtils.getNow();
		shop.ticketFlush = 0;
		shop.resetTime = DateUtils.getNowInSecondes();
		return shop;
	}
	
	/**
	 * 获取所有购买信息
	 * */
	public List<ShopVO> getShopVOs(){
		Map<Integer,ShopVO> shopMap=getShopMap();
		List<ShopVO> shopVOs=new ArrayList<ShopVO>();
		for(Integer key:shopMap.keySet()){
			shopVOs.add(shopMap.get(key));
		}
		return shopVOs;
	}
	
	/**
	 * 将shopVO添加到shop中并转换为字符串
	 * */
	public void parseToShop(ShopVO shopVO) {
		getShopMap().put(shopVO.shopId, shopVO);
	}

	public void cleanBuyInfo() {
		cleanEveryDayShop();
		this.buyTime = 0;
	}
	
	public void cleanEveryDayShop() {
		Map<Integer, ShopVO> shopMap = getShopMap();
		Set<Integer> shopSets = shopMap.keySet();
		Iterator<Integer> it = shopSets.iterator();
		while (it.hasNext()) {
			int shopId = it.next();
			ShopVO shopVO = shopMap.get(shopId);
			if (shopVO.resetTime != 0) {
				it.remove();
			}
		}
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Shop shop = new Shop();
		shop.actorId = rs.getLong("actorId");
		shop.buyInfo = rs.getString("buyInfo");
		shop.buyTime = rs.getInt("buyTime");
		shop.resetTime = rs.getInt("resetTime");
		shop.reward = rs.getString("reward");
		shop.flushTime = rs.getInt("flushTime");
		shop.ticketFlush = rs.getInt("ticketFlush");
		return shop;
	}

	@Override
	protected void hasReadEvent() {

		//
		if (StringUtils.isNotBlank(this.buyInfo)) {
			List<String[]> list = StringUtils.delimiterString2Array(this.buyInfo);
			for (String[] array : list) {
				ShopVO vo = ShopVO.valueOf(array);
				this.shopsMap.put(vo.shopId, vo);
			}
		}
		
		List<String[]> list = StringUtils.delimiterString2Array(reward);
		for(String[] str:list){
			ExchangeVO exchangeVO = new ExchangeVO(str);
			rewardMap.put(exchangeVO.exchangeId, exchangeVO);
		}
		
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK){
		    value.add(actorId);
		}
		value.add(buyInfo);
	    value.add(buyTime);
	    value.add(this.resetTime);
		value.add(this.flushTime);
		value.add(this.ticketFlush);
		value.add(this.reward);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		
		Map<Integer, ShopVO> shopMap = this.getShopMap();
		List<String> shopList = new ArrayList<String>();
		for (ShopVO vo : shopMap.values()) {
			shopList.add(vo.parse2String());
		}
		this.buyInfo = StringUtils.collection2SplitString(shopList, Splitable.ELEMENT_DELIMITER);
		
		StringBuffer stringBuffer = new StringBuffer();
		for(Integer key:rewardMap.keySet()){
			stringBuffer.append(rewardMap.get(key).parser2String()).append(Splitable.ELEMENT_DELIMITER);
		}
		if(stringBuffer.length() > 0){
			stringBuffer.deleteCharAt(stringBuffer.length() - 1);
		}
		this.reward = stringBuffer.toString();
		
	}
	
	public void reset() {
		this.buyTime = 0;
		this.shopsMap.clear();
	}

	@Override
	protected void disposeBlob() {
		buyInfo = EMPTY_STRING;
		reward = EMPTY_STRING;
	}

}