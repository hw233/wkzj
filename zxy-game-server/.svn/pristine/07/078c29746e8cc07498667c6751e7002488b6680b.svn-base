package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName="traderRewardPoolConfig")
public class TraderRewardPoolConfig implements ModelAdapter {
	
	/**
	 * id
	 */
	public int id;

	/**
	 * vip等级区间(x_x)
	 */
	public String vipLevel;
	
	/**
	 * 掌教等级区间(x_x)
	 */
	public String level;
	
	/**
	 * 物品类型
	 */
	public int itemType;
	
	/**
	 * 物品id
	 */
	public int itemId;
	
	/**
	 * 物品数量区间
	 */
	public String itemNum;
	
	/**
	 * 几率
	 */
	public int rate;
	
	/**
	 * 每天可出现最大次数
	 */
	public int viewNum;
	
	/**
	 * 购买单个物品消耗的金币
	 */
	public float costGolds;
	
	/**
	 * 购买单个物品消耗的点券
	 */
	public float costTicket;
	
	/**
	 * 是否必出
	 */
	public int isMust;
	
	@FieldIgnore
	public List<Integer> levelList = new ArrayList<>();
	
	@FieldIgnore
	public List<Integer> vipLevelList = new ArrayList<>();
	
	@FieldIgnore
	public List<String> itemNumList = new ArrayList<>();
	
	@Override
	public void initialize() {
		levelList = StringUtils.delimiterString2IntList(level, Splitable.ATTRIBUTE_SPLIT);
		vipLevelList = StringUtils.delimiterString2IntList(vipLevel, Splitable.ATTRIBUTE_SPLIT);
		itemNumList = StringUtils.delimiterString2List(itemNum, Splitable.ATTRIBUTE_SPLIT);
	}

	public boolean inLevel(int level) {
		return levelList.get(0) <= level && level <= levelList.get(1);
	}

	public boolean inVipLevel(int vipLevel) {
		return vipLevelList.get(0) <= vipLevel && vipLevel <= vipLevelList.get(1);
	}
	
	public int getItemNum(int level){
		int minNum = FormulaHelper.executeCeilInt(itemNumList.get(0), level);
		int maxNum = FormulaHelper.executeCeilInt(itemNumList.get(1), level);
		return RandomUtils.nextInt(minNum,maxNum);
	}

}
