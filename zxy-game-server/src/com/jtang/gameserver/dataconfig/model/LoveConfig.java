package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RandomExprRewardObject;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.StringUtils;

/**
 * 结婚配置
 * @author ludd
 *
 */
@DataFile(fileName = "loveConfig")
public class LoveConfig implements ModelAdapter {

	private int marrayRequestTimeOut;
	
	private int marryConsumTicketNum;
	
	private int marryConsumeGoodsId;
	
	private int marryConsumeGoodsNum;
	
	private int marryRequestMax;
	
	private String gift;
	
	private int unloveUseTicket;
	
	private String systemMsg;
	
	private String privateMsg;
	
	private int actorLimitLevel;
	
	@FieldIgnore
	private List<RandomExprRewardObject> giftList = new ArrayList<>();
	@Override
	public void initialize() {
		List<String[]> list = StringUtils.delimiterString2Array(gift);
		for (String[] str : list) {
			RandomExprRewardObject obj = RandomExprRewardObject.valueOf(str);
			giftList.add(obj);
		}
	}
	public int getMarrayRequestTimeOut() {
		return marrayRequestTimeOut;
	}
	public int getMarryConsumTicketNum() {
		return marryConsumTicketNum;
	}
	public int getMarryConsumeGoodsId() {
		return marryConsumeGoodsId;
	}
	public int getMarryConsumeGoodsNum() {
		return marryConsumeGoodsNum;
	}
	
	public int getMarryRequestMax() {
		return marryRequestMax;
	}
	
	public List<RewardObject> getGiftList(int level) {
		List<RewardObject> rewardObjects = new ArrayList<>();
		List<RewardObject> result = new ArrayList<>();
		Map<Integer, Integer> rate = new HashMap<Integer, Integer>();
		for (int i = 0; i < giftList.size(); i++) {
			RandomExprRewardObject rewardObject = giftList.get(i);
			rewardObject = rewardObject.clone();
			rewardObject.calculateNum(level);
			rate.put(i, rewardObject.rate);
			result.add(rewardObject);
		}
		
		Integer index = RandomUtils.randomHit(1000, rate);
		if(index !=null){
			rewardObjects.add(result.get(index));
		}
		return rewardObjects;
	}
	
	public int getUnloveUseTicket() {
		return unloveUseTicket;
	}
	
	public String getSystemMsg() {
		return systemMsg;
	}
	
	public String getPrivateMsg() {
		return privateMsg;
	}
	
	
	public int getActorLimitLevel() {
		return actorLimitLevel;
	}
	

	
}
