package com.jtang.gameserver.module.chat.facade;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.result.Result;
import com.jtang.gameserver.module.chat.handler.response.ChatResponse;

public interface ChatFacade {
	
	/**
	 * 添加聊天
	 * @param chatResponse
	 */
	void putChat(ChatResponse chatResponse);
	
	/**
	 * 历史信息
	 * 
	 * */
	public List<ChatResponse> getChat();
	
	/**
	 * 聊天验证,包括字符长度和聊天道具消耗
	 * @param actorId
	 * @param msg
	 * @return
	 */
	Result chat(long actorId, String content);
	
	/**
	 * 发送集众降魔消息
	 * @param actorName 发起人名称
	 * @param actorId 发起人id
	 * @param level 发起人等级
	 * @param vipLevel 发起人vip等级
	 * @param boosName boss名称
	 * @param reward 奖励列表
	 * @return
	 */
	Result sendDemonBossChat(String actorName,long actorId,int level,int vipLevel,String boosName,List<RewardObject> reward);

	/**
	 * 发送最强势力排行榜消息
	 * @param actorName 发起人名称
	 * @param actorId 发起人id
	 * @param level 发起人等级
	 * @param vipLevel 发起人vip等级
	 * @param targetLevel 目标等级
	 * @param targetVipLevel 目标vip等级
	 * @param targetName 目标名称
	 * @param isFirst 是否第一名
	 */
	Result sendPowerChat(String actorName, long actorId, int level, int vipLevel, int isWin, int targetLevel, int targetVipLevel, String targetName, int isFirst);
	
	/**
	 * 发送抢夺消息
	 * @param actorName 发起人名称
	 * @param actorId 发起人id
	 * @param level 发起人等级
	 * @param vipLevel 发起人vip等级
	 * @param targetName 目标名称
	 * @param targetLevel 目标等级
	 * @param targetVipLevel 目标vip等级
	 * @param num 抢夺积分或金币数量
	 * 
	 */
	Result sendNoticeChat(String actorName, long actorId, int level, int vipLevel, String otherActorName,
			int otherLevel, int otherVipLevel, int num, int isWin);
	
	/**
	 * 发送集众降魔最高难度获胜奖励消息
	 * @param actorId
	 * @param firstDemonReward
	 * @param winCampReward
	 * @return
	 */
	Result sendDemonWinChat(long actorId,List<RewardObject> firstDemonReward,List<RewardObject> winCampReward);

	/**
	 * 发送迷宫寻宝获得大奖消息
	 * @param actorId
	 * @param rewardObject
	 */
	Result sendMazeTreasureChat(long actorId, RewardObject rewardObject);
	
	/**
	 * 发送仙人图鉴获得奖励消息
	 * @param actorName
	 * @param actorId 
	 * @param level
	 * @param vipLevel
	 * @param num
	 * @param heroStar
	 * @param rewardObject
	 * @return
	 */
	Result sendHeroBookChat(long actorId, int num, int heroStar, List<RewardObject> rewardObject);
	
	/**
	 * 发送天财地宝消息
	 * @param actorId
	 * @param targetId
	 * @param equipType
	 * @param equipId
	 * @return
	 */
	Result sendTreasureChat(long actorId,long targetId,int equipType,int equipId);
	
	/**
	 * 发送种植额外奖励信息
	 * @param actorId
	 * @param plantId
	 * @param rewardObject
	 * @return
	 */
	Result sendPlantChat(long actorId, int plantId, RewardObject rewardObject);

	/**
	 * 发送天宫探物信息
	 * @param actorId
	 * @param reward
	 * @param type
	 */
	void sendWelkinChat(long actorId, List<RewardObject> reward, int type);

	/**
	 * 发送天梯消息
	 * @param actorId
	 * @param type
	 */
	void sendLadderChat(long actorId, Integer type,int winNum);
	
	/**
	 * 发放仙侣排行变更公告
	 */
	void sendLoveRankChat(long actorId,long targetId);
	

}
