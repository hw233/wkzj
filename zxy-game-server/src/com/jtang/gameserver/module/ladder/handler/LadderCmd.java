package com.jtang.gameserver.module.ladder.handler;

public interface LadderCmd {

	/**
	 * 获取天梯信息
	 * 请求:{@code Request}
	 * 返回:{@code LadderResponse}
	 */
	byte GET_INFO = 1;
	
	/**
	 * 请求战斗
	 * 请求:{@code LadderFightRequest}
	 * 推送:{@code LadderFightResponse}
	 */
	byte START_FIGHT = 2;
	
	/**
	 * 刷新对手
	 * 请求:{@code Request}
	 * 返回:{@code LadderActorResponse}
	 */
	byte FLUSH_ACTOR = 3;
	
	/**
	 * 查看排行榜
	 * 请求:{@code Request}
	 * 返回:{@code LadderRankResponse}
	 */
	byte LADDER_RANK = 4;
	
	/**
	 * 查看战斗记录
	 * 请求:{@code Request}
	 * 返回:{@code LadderFightInfoResponse}
	 */
	byte LADDER_FIGHT_INFO = 5;
	
	/**
	 * 领取赛季奖励
	 * 请求:{@code Request}
	 * 返回:{@code LadderRewardResponse}
	 */
	byte LADDER_REWARD = 6;
	
	/**
	 * 补满战斗次数
	 * 请求:{@code Request}
	 * 返回:{@code BuyFightNumResponse}
	 */
	byte BUY_FIGHT_NUM = 7;
	
	/**
	 * 推送战斗次数变更
	 * 推送:{@code FightNumResponse}
	 */
	byte PUSH_FIGHT_NUM = 8;
	
	/**
	 * 获取录像
	 * 请求:{@code FightVideoRequest}
	 * 返回:{@code FightVideoResponse}
	 */
	byte GET_FIGHT_VIDEO = 9;
	
	/**
	 * 跨天推送
	 * 推送:{@code LadderPushResponse}
	 */
	byte PUSH_LADDER = 10;
	
	/**
	 * 赛季结束推送
	 * 推送:{@code Response}
	 */
	byte PUSH_SPORT_END = 11;
	
	/**
	 * 推送战斗消息更新
	 * 推送:{@code LadderFightInfoResponse}
	 */
	byte PUSH_FIGHT_INFO = 12;


}
