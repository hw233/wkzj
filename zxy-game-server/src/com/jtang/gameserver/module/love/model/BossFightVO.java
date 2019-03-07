package com.jtang.gameserver.module.love.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;


public class BossFightVO {

	/**
	 * id
	 */
	public int id;
	
	/**
	 * 状态
	 * 0.锁定
	 * 1.可攻击
	 * 2.可领奖
	 * 3.已领奖
	 */
	public int state;
	
	/**
	 * 合作已使用挑战次数
	 */
	public int monsterFightNum;
	
	/**
	 * 合作已使用刷新次数(补满挑战次数)
	 */
	public int monsterFlushNum;
	
	/**
	 * 没有出现额外奖励次数
	 */
	public int noRewardNum;

	/**
	 * 额外奖励保底次数
	 */
	public int leastNum;
	
	/**
	 * 奖励
	 */
	public List<RewardObject> rewardList = new ArrayList<>();
	
	public BossFightVO(){
		
	}
	
	public static BossFightVO valueOf(String str[]){
		BossFightVO bossFightVO = new BossFightVO();
		String[] vostr = StringUtils.fillStringArray(str, 6, "0");
		bossFightVO.id = Integer.valueOf(vostr[0]);
		bossFightVO.state = Integer.valueOf(vostr[1]);
		bossFightVO.monsterFightNum = Integer.valueOf(vostr[2]);
		bossFightVO.monsterFlushNum = Integer.valueOf(vostr[3]);
		bossFightVO.noRewardNum = Integer.valueOf(vostr[4]);
		bossFightVO.leastNum = Integer.valueOf(vostr[5]);
		if (str.length >= 7) {
			String rewardString = str[6];
			List<String> list = StringUtils.delimiterString2List(rewardString, Splitable.DELIMITER_ARGS);
//			List<String[]> list = StringUtils.delimiterString2Array(rewardString, Splitable.BETWEEN_ITEMS, Splitable.DELIMITER_ARGS);
			for(String string : list){
				String[] preStrings = StringUtils.split(string, Splitable.BETWEEN_ITEMS);
				String[] rewardStrings = StringUtils.fillStringArray(preStrings, 3, "0");
				RewardObject rewardObject = RewardObject.valueOf(rewardStrings);
				if (rewardObject.id == 0) {
					continue;
				}
				bossFightVO.rewardList.add(rewardObject);
			}
		}
		return bossFightVO;
	}
	
	public String parser2String(){
		StringBuffer sb = new StringBuffer();
		sb.append(id).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(state).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(monsterFightNum).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(monsterFlushNum).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(noRewardNum).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(leastNum).append(Splitable.ATTRIBUTE_SPLIT);
		for(RewardObject rewardObject : rewardList){
			if (rewardObject.id == 0) {
				continue;
			}
			sb.append(rewardObject.rewardType.getCode()).append(Splitable.BETWEEN_ITEMS);
			sb.append(rewardObject.id).append(Splitable.BETWEEN_ITEMS);
			sb.append(rewardObject.num).append(Splitable.DELIMITER_ARGS);
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	protected void beforeWritingEvent() {
		StringBuffer sb = new StringBuffer();
		for(BossFightVO vo : fightStateMap.values()){
			sb.append(vo.parser2String()).append(Splitable.ELEMENT_DELIMITER);
		}
		if(fightStateMap.size() > 0){
			sb.deleteCharAt(sb.length() - 1);
		}
		this.fightState = sb.toString();
	}
	
	protected void hasReadEvent() {
		List<String[]> fightList = StringUtils.delimiterString2Array(fightState);
		for (String[] str : fightList) {
			BossFightVO vo = BossFightVO.valueOf(str);
			fightStateMap.put(vo.id, vo);
		}
	}
	
	
	public static Map<Integer,BossFightVO> fightStateMap = new HashMap<>();
	
	public String fightState;
	
	public static void main(String[] args) {
		BossFightVO vo = null;
		List<String[]> fightList = StringUtils.delimiterString2Array("1_3_1_0_0_5_0,8080,1|2_3_7_0_2_4_|3_2_3_0_3_4_");
		for (String[] str : fightList) {
			vo = BossFightVO.valueOf(str);
			fightStateMap.put(vo.id, vo);
			System.out.println(vo.parser2String());
		}
		
		for (int i = 0; i < 10; i++) {
			//读出来
			vo.hasReadEvent();
			
			//下线写进去
			vo.beforeWritingEvent();
			
			//数据清空
			
			//最终打死boss时,fightState数据超过65K字节,数据库保存不进去,boss状态不能改变, 领取奖励时判断状态不符合 未发奖励.
			
			System.out.println(vo.fightState);
		}
	}
}
