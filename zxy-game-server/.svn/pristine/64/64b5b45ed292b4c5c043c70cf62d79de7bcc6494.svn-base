package com.jtang.gameserver.dataconfig.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName="traderGlobalConfig")
public class TraderGlobalConfig implements ModelAdapter {
	
	@FieldIgnore
	private static final Logger LOGGER = LoggerFactory.getLogger(BlackShopConfig.class);
	
	/**
	 * 初始化触发条件的个数
	 */
	public int conditionNum;
	
	/**
	 * 免费刷新次数
	 */
	public int flushNum;
	
	/**
	 * 刷新时间 24小时制(时间1_时间2)
	 */
	public String flushTime;
	
	/**
	 * 模块解锁等级
	 */
	public int level;
	
	/**
	 * 模块解锁vip等级
	 */
	public int vipLevel;
	
	/**
	 * 同类型同id物品最多出现个数
	 */
	public int rewardRule;
	
	/**
	 * 商品列表最多有几个打折的物品
	 */
	public int discountNum;
	
	/**
	 * 只有哪几种类型的商品可以打折
	 */
	private String discountType;
	
	/**
	 * 必须打折的商品id
	 */
	private String mastDiscountId;
	
	/**
	 * 第一次触发必出列表
	 */
	private String firstInit;
	
	/**
	 * 刷新道具id
	 */
	public int flushGoods;
	
	/**
	 * 指向性装备碎片和魂魄的星级
	 */
	public int goodsStar;
	
	/**
	 * 指向性装备碎片和魂魄出现的次数
	 */
	public int goodsViewNum;
	
	/**
	 * 指向性装备碎片和魂魄合成欠缺个数
	 */
	public int goodsNum;
	
	/**
	 * 指向性装备出现的次数
	 */
	public int equipNum;
	
	@FieldIgnore
	public List<String> flushList = new ArrayList<>();
	@FieldIgnore
	public List<Integer> discountTypeList = new ArrayList<>();
	@FieldIgnore
	public List<Integer> mastDiscountList = new ArrayList<>();
	@FieldIgnore
	public Map<Integer,Integer> firstList = new HashMap<>();
	

	@Override
	public void initialize() {
		flushList = StringUtils.delimiterString2List(flushTime, Splitable.ATTRIBUTE_SPLIT);
		flushTime = null;
		discountTypeList = StringUtils.delimiterString2IntList(discountType, Splitable.ATTRIBUTE_SPLIT);
		discountType = null;
		mastDiscountList = StringUtils.delimiterString2IntList(mastDiscountId, Splitable.ATTRIBUTE_SPLIT);
		mastDiscountId = null;
		firstList = StringUtils.delimiterString2IntMap(firstInit);
		firstInit = null;
	}
	
	public List<Date> getFlushTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		List<Date> list = new ArrayList<>();
		for(String str:flushList){
			try {
				Date date = sdf.parse(str);
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.SECOND, c.get(Calendar.SECOND));
				calendar.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
				calendar.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY));
				list.add(calendar.getTime());
			} catch (Exception e){
				LOGGER.error("{}",e);
			}
		}
		return list;
	}
	
}
