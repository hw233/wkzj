package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName = "rechargeAppConfig")
public class RechargeAppConfig implements ModelAdapter  {
	
	/**
	 * 充值的id
	 */
	public int rechargeId;
	
	/**
	 * 充值金额
	 */
	public int money;
	
	/**
	 * 赠送点券
	 */
	public int giveTicket;
	
	/**
	 * 充值次数
	 */
	public int rechargeCount;
	
	/**
	 * 奖励物品
	 */
	private String goods;
	
	/**
	 * 开始时间
	 */
	private String startTime;
	
	/**
	 * 结束时间
	 */
	private String endTime;
	
	@FieldIgnore
	public int start;
	@FieldIgnore
	public int end;
	@FieldIgnore
	public List<RewardObject> rewardList = new ArrayList<>();

	@Override
	public void initialize() {
		Date dateStart = DateUtils.string2Date(startTime, "yyyy-MM-dd HH:mm:ss");
		Long ls = dateStart.getTime() / 1000;
		start = ls.intValue();
		Date dateEnd = DateUtils.string2Date(endTime, "yyyy-MM-dd HH:mm:ss");
		Long le = dateEnd.getTime() / 1000;
		end = le.intValue();
		
		List<String[]> reward = StringUtils.delimiterString2Array(goods, Splitable.ELEMENT_SPLIT, Splitable.ATTRIBUTE_SPLIT);
		for(String []array:reward){
			RewardObject rewardObject = RewardObject.valueOf(array);
			rewardList.add(rewardObject);
		}
	}

}
