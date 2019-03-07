package com.jtang.gameserver.module.adventures.achievement.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveState;

/**
 * 成就结构体
 * @author pengzy
 *
 */
public class AchieveVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2046322223981714195L;

	/**
	 * 成就Id
	 */
	private int achieveId;

	/**
	 * 0未完成、1完成、2已领取
	 */
	private byte state;
	
	/**
	 * 成就达成条件
	 */
	private List<Integer> conditionList;

	public int getAchieveId() {
		return achieveId;
	}

	public List<Integer> getConditionList() {
		return conditionList;
	}

	public byte getState() {
		return state;
	}

	public static AchieveVO valueOf(String[] array) {
		AchieveVO achievementVO = new AchieveVO();
		if(array.length == 1){
			achievementVO.achieveId = Integer.valueOf(array[0]);
		}else{
			achievementVO.achieveId = Integer.valueOf(array[0]);
			achievementVO.state = Byte.valueOf(array[1]);
			
			achievementVO.conditionList = new ArrayList<>();
			for (int i = 2; i < array.length; i++) {
				achievementVO.conditionList.add(Integer.valueOf(array[i]));
			}
		}
		return achievementVO;
	}

	public String parseToString() {
		List<Object> attributes = new ArrayList<Object>();
		if(conditionList == null || conditionList.size() == 0){
			attributes.add(achieveId);
		}else{
			attributes.add(achieveId);
			attributes.add(state);
			for(Integer condition : conditionList){
				attributes.add(condition);
			}
		}
		return StringUtils.collection2SplitString(attributes, Splitable.ATTRIBUTE_SPLIT);
	}

	public void writeIn(IoBufferSerializer packet) {
		if(conditionList == null || conditionList.size() == 0){
			packet.writeInt(achieveId);
			packet.writeByte(AchieveState.GETED.getState());
			packet.writeIntList(new ArrayList<Integer>());
		}else{
			packet.writeInt(achieveId);
			packet.writeByte(state);
			packet.writeIntList(conditionList);
		}
	}

	/**
	 * 设置已领取
	 */
	public void setGeted() {
		state = AchieveState.GETED.getState();
		conditionList = null;
	}

	/**
	 * 设置已达成
	 */
	public void setFinished() {
		state = AchieveState.AHIEVED.getState();
	}
	
	/**
	 * 是否已经达成
	 * @return
	 */
	public boolean isAchieved() {
		return state == AchieveState.GETED.getState() || state == AchieveState.AHIEVED.getState();
	}

	public static AchieveVO valueOf(int achieveId, int condition) {
		AchieveVO achieveVO = new AchieveVO();
		achieveVO.achieveId = achieveId;
		achieveVO.state = AchieveState.UN_ACHIEVED.getState();
		achieveVO.conditionList = new ArrayList<>();
		achieveVO.conditionList.add(condition);
		return achieveVO;
	}

	public static AchieveVO valueOf(boolean isFinished, int achieveId, final List<Integer> conditionList) {
		AchieveVO achieveVO = new AchieveVO();
		achieveVO.achieveId = achieveId;
		achieveVO.conditionList = new ArrayList<>();
		achieveVO.conditionList.addAll(conditionList);
		if(isFinished){
			achieveVO.state = AchieveState.AHIEVED.getState();
		}
		else{
			achieveVO.state = AchieveState.UN_ACHIEVED.getState();
			achieveVO.conditionList.set(achieveVO.conditionList.size() - 1, 1);
		}
		return achieveVO;
	}
	
	public static AchieveVO valueOf(int achieveId, Integer... conditionList) {
		AchieveVO achieveVO = new AchieveVO();
		achieveVO.achieveId = achieveId;
		achieveVO.conditionList = new ArrayList<>();
		for (int condition : conditionList) {
			achieveVO.conditionList.add(condition);
		}
		achieveVO.state = 0;  //未完成

		return achieveVO;
	}
	

	/**
	 * 增加完成次数
	 * @return
	 */
	public int increaseFinishNum() {
		int num = conditionList.get(conditionList.size() - 1) + 1;
		conditionList.set(conditionList.size() - 1, num);
		return num;
	}

	public int getFinishedCondition() {
		return conditionList.get(0);
	}

	public void increaseFirstCondition() {
		conditionList.set(0, conditionList.get(0) + 1);
	}

	/**
	 * 设置condition集合的第一个参数
	 * @param needCondition
	 */
	public void setCondition1(int needCondition) {
		conditionList.set(0, needCondition);
	}

	/**
	 * 设置condition集合的第二个参数
	 * @param needCondition
	 */
	public void setCondition2(int needCondition) {
		conditionList.set(1, needCondition);
	}
}
