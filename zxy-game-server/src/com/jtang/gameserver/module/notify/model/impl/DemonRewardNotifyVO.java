package com.jtang.gameserver.module.notify.model.impl;
//package com.jtang.sm2.module.notify.model;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.jtang.core.protocol.WritablePacket;
//import com.jtang.core.utility.Splitable;
//import com.jtang.core.utility.StringUtils;
//import com.jtang.sm2.component.model.RewardObject;
//
///**
// * 集众降魔奖励结果通知
// * 格式: 第一名奖励类型:id:数量,第一名奖励类型:id:数量|功勋奖励类型:id:数量,功勋奖励类型:id:数量|获胜阵营类型:id:数量,获胜阵营类型:id:数量|奖励积分|是否获胜|最终排名
// * @author 0x737263
// *
// */
//public class DemonRewardNotifyVO implements BaseNotifyVO {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -1585621085271806713L;
//
//	/**
//	 * 第一名奖励
//	 */
//	private List<RewardObject> firstDemonReward = new ArrayList<>();
//	
//	/**
//	 * 功勋值排名奖励
//	 */
//	private List<RewardObject> featsRankReward = new ArrayList<>();
//	
//	/**
//	 * 获胜阵营奖励
//	 */
//	private List<RewardObject> winCampReward = new ArrayList<>();
//	
//	/**
//	 * 使用点券奖励
//	 */
//	private List<RewardObject> useTicketReward = new ArrayList<>();
//	
//	/**
//	 * 奖励积分
//	 */
//	private long rewardScore;
//	
//	/**
//	 * 阵营是否获胜 1:获胜，0：失败
//	 */
//	private byte isWin;
//	
//	/**
//	 * 最终排名
//	 */
//	private int rank;
//	
//	
//	public DemonRewardNotifyVO(List<RewardObject> firstDemonReward, List<RewardObject> featsRankReward, List<RewardObject> winCampReward,
//			List<RewardObject> useTicketReward, long rewardScore, byte isWin, int rank) {
//		this.firstDemonReward = firstDemonReward;
//		this.featsRankReward = featsRankReward;
//		this.winCampReward = winCampReward;
//		this.useTicketReward = useTicketReward;
//		this.rewardScore = rewardScore;
//		this.isWin = isWin;
//		this.rank = rank;
//	}
//	
//	public static DemonRewardNotifyVO valueOf(String extension) {
//		List<String> strList = StringUtils.delimiterString2List(extension, Splitable.ELEMENT_SPLIT);
//
//		List<RewardObject> firstDemonReward = new ArrayList<>();
//		List<RewardObject> featsRankReward = new ArrayList<>();
//		List<RewardObject> winCampReward = new ArrayList<>();
//		List<RewardObject> useTicketReward = new ArrayList<>();
//		long rewardScore = 0;
//		byte isWin = 0;
//		int rank = 0;
//
//		//格式: 第一名奖励类型:id:数量,第一名奖励类型:id:数量|功勋奖励类型:id:数量,功勋奖励类型:id:数量|获胜阵营类型:id:数量,获胜阵营类型:id:数量|奖励积分|是否获胜|最终排名
//		
//		if (strList != null && strList.size() == 7) {
//			fillRewardList(strList.get(0), firstDemonReward);
//			fillRewardList(strList.get(1), featsRankReward);
//			fillRewardList(strList.get(2), winCampReward);
//			fillRewardList(strList.get(3), useTicketReward);
//
//			rewardScore = Long.valueOf(strList.get(4));
//			isWin = Byte.valueOf(strList.get(5));
//			rank = Integer.valueOf(strList.get(6));
//		}
//
//		return new DemonRewardNotifyVO(firstDemonReward, featsRankReward, winCampReward, useTicketReward, rewardScore, isWin, rank);
//	}
//	
//	private static void fillRewardList(String str, List<RewardObject> rewardList) {
//		List<String[]> winCampList = StringUtils.delimiterString2Array(str, Splitable.BETWEEN_ITEMS, Splitable.DELIMITER_ARGS);
//		for (String[] itemArray : winCampList) {
//			rewardList.add(RewardObject.valueOf(itemArray));
//		}
//	}
//	
//	@Override
//	public void writePacket(WritablePacket packet) {		
//		writeRewardPacket(packet, firstDemonReward);
//		writeRewardPacket(packet, featsRankReward);
//		writeRewardPacket(packet, winCampReward);
//		writeRewardPacket(packet, useTicketReward);
//
//		packet.writeLong(rewardScore);
//		packet.writeByte(isWin);
//		packet.writeInt(rank);
//	}
//	
//	private void writeRewardPacket(WritablePacket packet, List<RewardObject> rewardList) {
//		packet.writeShort((short) rewardList.size());
//		for (RewardObject reward : rewardList) {
//			packet.writeBytes(reward.getBytes());
//		}
//	}
//
//	@Override
//	public String parseToString() {
//
//		List<String> allList = new ArrayList<>();
//		allList.add(StringUtils.collection2SplitString(parseToStringReward(firstDemonReward), Splitable.BETWEEN_ITEMS));
//		allList.add(StringUtils.collection2SplitString(parseToStringReward(featsRankReward), Splitable.BETWEEN_ITEMS));
//		allList.add(StringUtils.collection2SplitString(parseToStringReward(winCampReward), Splitable.BETWEEN_ITEMS));
//		allList.add(StringUtils.collection2SplitString(parseToStringReward(useTicketReward), Splitable.BETWEEN_ITEMS));
//		allList.add(String.valueOf(rewardScore));
//		allList.add(String.valueOf(isWin));
//		allList.add(String.valueOf(rank));
//
//		return StringUtils.collection2SplitString(allList, Splitable.ELEMENT_DELIMITER);
//	}
//	
//	private List<String> parseToStringReward(List<RewardObject> rewardList) {
//		List<String> stringList = new ArrayList<>();
//		for (RewardObject reward : rewardList) {
//			stringList.add(reward.parse2String(Splitable.DELIMITER_ARGS));
//		}
//		return stringList;
//	}
//	
//}
