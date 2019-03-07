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
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName="snatchExchangeGlobalConfig")
public class SnatchExchangeGlobalConfig implements ModelAdapter  {
	
	@FieldIgnore
	private static final Logger LOGGER = LoggerFactory.getLogger(SnatchExchangeGlobalConfig.class);
	
	/**
	 * 刷新时间(分钟)
	 */
	public String flushTime;
	
	/**
	 * 消耗点券
	 */
	private String needTicket;
	
	/**
	 * 物品刷新id
	 */
	public int goodsId;
	
	/**
	 * 需要物品数量
	 */
	public int goodsNum;
	
	/**
	 * 奖励组成
	 */
	public String rewardPool;
	
	@FieldIgnore
	public Map<Integer,Integer> goodsPoolMap = new HashMap<>();
	
	@FieldIgnore
	public List<String> flushList = new ArrayList<>();

	public int getNeedTicket(int ticketFlushNum){
		return FormulaHelper.executeCeilInt(needTicket, ticketFlushNum);
	}
	
	@Override
	public void initialize() {
		
		flushList = StringUtils.delimiterString2List(flushTime, Splitable.ATTRIBUTE_SPLIT);
		
		goodsPoolMap = StringUtils.delimiterString2IntMap(rewardPool);
		rewardPool = null;
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
