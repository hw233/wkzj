package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.StringUtils;

/**
 * 仙人图鉴奖励配置
 * @author ludd
 *
 */
@DataFile(fileName = "heroBookRewardConfig")
public class HeroBookRewardConfig implements ModelAdapter {

	@FieldIgnore
	private static final Logger LOGGER = LoggerFactory.getLogger(HeroBookRewardConfig.class);
	/**
	 * 奖励id
	 */
	private int orderId;
	/**
	 * 英雄星级
	 */
	private int heroStar;
	/**
	 * 英雄数量
	 */
	private int heroNum;
	/**
	 * 奖励列表
	 */
	private String reward;
	/**
	 * 下一个奖励id
	 */
	private int nextOrderId;
	
	/**
	 * 是否是开始节点(1:是，0：否）
	 */
	private int isStartNode;
	
	@FieldIgnore
	private List<RewardObject> rewardObjects = new ArrayList<>();

	@Override
	public void initialize() {	
		List<String[]> list = StringUtils.delimiterString2Array(reward);
		for (String[] str : list) {
			RewardObject obj = RewardObject.valueOf(str);
			rewardObjects.add(obj);
		}
		this.reward = null;
	}

	public int getOrderId() {
		return orderId;
	}

	public int getHeroStar() {
		return heroStar;
	}

	public int getHeroNum() {
		return heroNum;
	}

	public int getNextOrderId() {
		return nextOrderId;
	}

	public List<RewardObject> getRewardObjects() {
		return rewardObjects;
	}
	
	public int getIsStartNode() {
		return isStartNode;
	}
	
	

}
