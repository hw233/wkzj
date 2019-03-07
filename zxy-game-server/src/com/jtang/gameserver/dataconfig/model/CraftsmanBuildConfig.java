package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.ExprRewardObject;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
/**
 * 神匠来袭配置
 * @author lig
 *
 */
@DataFile(fileName = "craftsmanBuildConfig")
public class CraftsmanBuildConfig implements ModelAdapter {

	/**
	 * 打造次数
	 */
	public int star;
	/**
	 * 活动要打造的装备
	 */
	private String buildProb;
	
	/**
	 * star星装备打造成功概率
	 */
	private String buildCost;

	/**
	 * 打造装备失败返还物品
	 */
	private String buildReturn;
	
	/**
	 * 打造装备失败返还物品
	 */
	public int returnPerc;
	
	@FieldIgnore
	public List<Integer> buildProbList = null;
	
	@FieldIgnore
	public List<Integer> buildCostList = null;

	@FieldIgnore
	public List<ExprRewardObject> buildReturnList = null;
	
	
	@Override
	public void initialize() {
		buildProbList = StringUtils.delimiterString2IntList(buildProb, Splitable.ATTRIBUTE_SPLIT);
		buildCostList = StringUtils.delimiterString2IntList(buildCost, Splitable.ATTRIBUTE_SPLIT);
		buildReturnList = new ArrayList<ExprRewardObject>();
		List<String[]> failList = StringUtils.delimiterString2Array(buildReturn);
		for (String[] winstr : failList) {
			ExprRewardObject obj = ExprRewardObject.valueOf(winstr);
			buildReturnList.add(obj);
		}
		
		this.buildProb = null;
		this.buildCost = null;
		this.buildReturn = null;
	}
	
	
	
	
}
