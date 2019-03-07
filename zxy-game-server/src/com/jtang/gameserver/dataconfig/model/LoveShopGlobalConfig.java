package com.jtang.gameserver.dataconfig.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName = "loveShopGlobalConfig")
public class LoveShopGlobalConfig implements ModelAdapter {

	@FieldIgnore
	private static final Logger LOGGER = LoggerFactory.getLogger(BlackShopConfig.class);
	
	/**
	 * 刷新时间
	 */
	public String flushTime;
	
	/**
	 * 购买物品消耗的GoodsId
	 */
	public int costGoods;
	
	/**
	 * 每天刷新次数上限
	 */
	public int flushMax;
	
	@FieldIgnore
	public List<String> flushList = new ArrayList<>();
	
	@Override
	public void initialize() {
		flushList = StringUtils.delimiterString2List(flushTime, Splitable.ATTRIBUTE_SPLIT);
		flushTime = null;
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
