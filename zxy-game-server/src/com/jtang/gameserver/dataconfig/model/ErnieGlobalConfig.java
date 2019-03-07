package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RandomRewardObject;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
/**
 * 欢乐摇奖
 * @author lig
 *
 */
@DataFile(fileName = "ernieGlobalConfig")
public class ErnieGlobalConfig implements ModelAdapter {

	/**
	 *免费次数
	 */
	public int freeTimes;
	
	/*
	 *活动描述 
	 */
	public String desc;
	
	/*
	 *活动描述 
	 */
	public String bigRewardMsg;
	
	/*
	 *活动描述 
	 */
	public String billMsg;
	
	/**
	 * 开放日期
	 */
	private String openDate;
	/**
	 * 开放时间
	 */
	private String closeDate;
	
	/**
	 * 奖品兑换时间
	 */
	private String exchangeStart;
	
	/**
	 * 奖品兑换时间
	 */
	private String exchangeEnd;
	
	/**
	 * 活动展示装备
	 */
	private String ernieGoods;
	
	/**
	 * 活动展示装备
	 */
	private String showGoods;
	
	/**
	 * 活动保底装备
	 */
	private String leastEquip;
	
	/**
	 * 保底次数
	 */
	public int leastNum;
	
	/**
	 * 单服最大话费数量
	 */
	public int maxBillNum;
	
	
	@FieldIgnore
	public Date openDateTime = new Date();
	
	@FieldIgnore
	public Date closeDateTime = new Date();
	
	@FieldIgnore
	public Date exchangeStartTime = new Date();
	
	@FieldIgnore
	public Date exchangeEndTime = new Date();
	
	@FieldIgnore
	public List<RewardObject> ernieGoodsList = null;
	
	@FieldIgnore
	public List<RewardObject> showGoodsList = null;
	
	@FieldIgnore
	public List<RandomRewardObject> leastEquipList = null;
	
	@FieldIgnore
	public List<Integer> leastEquipIDList = null;
	
	@Override
	public void initialize() {
		openDateTime = DateUtils.string2Date(this.openDate, "yyyy-MM-dd HH:mm:ss");
		closeDateTime = DateUtils.string2Date(this.closeDate, "yyyy-MM-dd HH:mm:ss");
		
		exchangeStartTime = DateUtils.string2Date(this.exchangeStart, "yyyy-MM-dd HH:mm:ss");
		exchangeEndTime = DateUtils.string2Date(this.exchangeEnd, "yyyy-MM-dd HH:mm:ss");
		
		showGoodsList = new ArrayList<RewardObject>();
		List<String> list = StringUtils.delimiterString2List(showGoods, Splitable.ELEMENT_SPLIT);
		for (String str : list) {
			String[] value = StringUtils.split(str, Splitable.ATTRIBUTE_SPLIT);
			RewardObject e = RewardObject.valueOf(value);
			showGoodsList.add(e);
		}
		
		leastEquipIDList = new ArrayList<Integer>();
		leastEquipList = new ArrayList<RandomRewardObject>();
		List<String> list1 = StringUtils.delimiterString2List(leastEquip, Splitable.ELEMENT_SPLIT);
		for (String str : list1) {
			String[] value = StringUtils.split(str, Splitable.ATTRIBUTE_SPLIT);
			RandomRewardObject e = RandomRewardObject.valueOf(value);
			leastEquipList.add(e);
			leastEquipIDList.add(e.id);
		}
		
		ernieGoodsList = new ArrayList<RewardObject>();
		List<String> list2 = StringUtils.delimiterString2List(ernieGoods, Splitable.ELEMENT_SPLIT);
		for (String str : list2) {
			String[] value = StringUtils.split(str, Splitable.ATTRIBUTE_SPLIT);
			RewardObject e = RewardObject.valueOf(value);
			ernieGoodsList.add(e);
		}
		this.openDate = null;
		this.closeDate = null;
		this.leastEquip = null;
		this.showGoods = null;
		this.ernieGoods = null;
	}
	
	
	
	
	
}
