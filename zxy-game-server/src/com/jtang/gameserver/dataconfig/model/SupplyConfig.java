package com.jtang.gameserver.dataconfig.model;

import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName = "supplyConfig")
public class SupplyConfig implements ModelAdapter {

	/**
	 * 配置id
	 */
	public int id;

	/**
	 * 开始时间点
	 */
	public int beginTime;

	/**
	 * 结束时间点
	 */
	public int endTime;

	/**
	 * 奖励扩展
	 */
	public String rewardExpr;

	@FieldIgnore
	public SupplyRewardConfig reward;

	@Override
	public void initialize() {
		List<String> list = StringUtils.delimiterString2List(rewardExpr, Splitable.ATTRIBUTE_SPLIT);
		SupplyRewardConfig supplyRewardConfig = new SupplyRewardConfig(list);
		reward = supplyRewardConfig;
		
		this.rewardExpr = null;
	}

}
