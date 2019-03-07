package com.jiatang.common.crossbattle;


public interface CrossBattleCmd {

	/**
	 * 请求对战数据
	 * <pre>
	 * 请求{@code Request}
	 * 响应{@code ActorCrossDataResponse}
	 * </pre>
	 */
	byte GET_CROSS_DATA = 1;
	
	/**
	 * 请求角色数据
	 * 请求:{@code Request}
	 * 返回:{@code ActorPointResponse}
	 */
	byte GET_ACTOR_POINT = 2;
	
	/**
	 * 获取玩家阵型
	 * <pre>
	 * 请求:{@code ViewLineupRequest}
	 * 响应:{@code ViewLineupResponse}
	 * </pre>
	 */
	byte GET_LINEUP = 3;
	
	/**
	 * 提交战斗
	 * <pre>
	 * 请求:{@code AttackActorRequest}
	 * 响应:{@code Response}
	 * 推送:{@code AttackActorResponse}
	 * </pre>
	 */
	byte ATTACK_ACTOR = 4;
	
	/**
	 * 推送角色属性变化（血，复活，杀人数等）
	 * <pre>
	 * 推送:{@code ActorAttributeChangeResponse}
	 * </pre>
	 */
	byte PUSH_ACTOR_ATTRIBUTE_CHANGE = 6;
	
	/**
	 * 推送总伤害
	 * <pre>
	 * 推送:{@code ServerTotalHurtResponse}
	 * </pre>
	 */
	byte PUSH_SERVER_TOTAL_HURT = 8;
	
	/**
	 * 请求所有服务器积分排名
	 * <pre>
	 * 请求:{@code Request}
	 * 响应:{@code AllServerScoreResponse}
	 * </pre>
	 */
	byte GET_SERVER_SCORE_LIST = 9;
	
	/**
	 * 获取本服排名
	 * <pre>
	 * 请求:{@code Request}
	 * 响应:{@code HomeServerRankResponse}
	 * </pre>
	 */
	byte GET_HOME_SERVER_RANK = 10;
	
	/**
	 * 每日比赛结束奖励结果
	 * <pre>
	 * 请求:{@code Request}
	 * 响应:{@code DayBattleEndRewardResponse}
	 * 推送:{@code DayBattleEndRewardResponse}
	 * </pre>
	 */
	byte DAY_BATTLE_END_REWARD_RESULT = 11;
	
	/**
	 * 角色贡献点兑换物品
	 * <pre>
	 * 请求:{@code ExchangePointRequest}
	 * 响应:{@code ExchangePointResponse}
	 * </pre>
	 */
	byte EXCHANGE_POINT = 12;
	
	/**
	 * 请求上次对阵结果
	 * <pre>
	 * 请求:{@code Request}
	 * 响应:{@code LastBattleResultResponse}
	 * 请求世界服
	 * 请求:{@code LastBattleResultG2W}
	 * 响应:{@code LastBattleResultW2G}
	 * </pre>
	 */
	byte GET_LAST_BATTLE_RESULT = 13;
	
	/**
	 * 请求跨服战配置
	 * 请求:{@code Request}
	 * 响应:{@code CrossBattleConfigResponse}
	 * 请求世界服
	 * 请求:{@code CrossBattleConfigG2W}
	 * 响应:{@code CrossBattleConfigW2G}
	 */
	byte GET_CROSS_BATTLE_CONFIG = 14;
	
	/**
	 * 推送结束系统通知
	 * <pre>
	 * 推送：{@code EndNoticeResponse}
	 * 世界服推送
	 * 推送：{@code EndNoticeW2G}
	 * </pre>
	 */
	byte SYS_END_NOTICE = 15;
	
	/**
	 * 设置结束奖励已读取
	 * 请求:{@code Requst}
	 * 响应:{@code Response}
	 */
	byte SET_READ_FLAG = 16;
	
	/**
	 * 推送攻击消息
	 * 推送：{@code AttackNoticeVO}
	 */
	byte PUSH_ATTACK_NOTICE = 17;
	
	/**
	 * 获取跨服战赛季结束奖励
	 * 请求:{@code Request}
	 * 响应:{@code CrossBattleRewardResponse}
	 */
	byte GET_ALL_END_REWARD = 18;
	
	/**
	 * 是否可以领取跨服战赛季结束奖励
	 * 请求:{@Code Request}
	 * 响应:{code CrossBattleIsRewardResponse}
	 */
	byte IS_GET_ALL_EN_REWARD = 19;

	
	
	
	
	
	//----------------------以下为gameserver与worldserver协议----------------------------------
	/**
	 * 跨服战报名
	 * <pre>
	 * 请求:{@code SignupG2W}
	 * 响应:{@code SignupW2G}
	 * </pre>
	 */
	byte W2G_SIGN_UP = 61;
	
	/**
	 * 提交战斗结果
	 * <pre>
	 * 请求:{@code AttackPlayerG2W}
	 * 响应:{@code AttackPlayerW2G}
	 * </pre> 
	 */
	byte G2W_POST_BATTLE_RESULT = 62;
	
	/**
	 * 通知跨服战(开始\结束)
	 * <pre>
	 * 推送：{@code NoticeGameW2G}
	 * </pre>
	 */
	byte W2G_NOTICE_GAME = 63;
	
	/**
	 * 赛季结束奖励
	 * <pre>
	 * 推送：{@code AllEndW2G}
	 * </pre>
	 */
	byte ALL_END_REWARD = 64;
	
	/**
	 * 当日奖励
	 * 推送{@code EndRewardW2G}
	 */
	byte W2G_PUSH_DAY_END_REWARD = 65;

}
