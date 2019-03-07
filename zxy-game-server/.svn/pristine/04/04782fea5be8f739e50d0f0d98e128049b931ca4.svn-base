package com.jtang.gameserver.module.love.handler;


public interface LoveCmd {
	/**
	 * 获取婚姻信息
	 * 请求:{@code Request}
	 * 响应:{@code LoveInfoResponse}
	 */
	byte GET_LOVE_INFO = 1;
	
	/**
	 * 请求结婚
	 * 请求:{@code MarryRequest}
	 * 响应:{@code Response}
	 */
	byte MARRY = 2;
	
	/**
	 * 接受结婚
	 * 请求:{@code AcceptMarryRequest}
	 * 响应:{@code LoveInfoResponse}
	 */
	byte ACCEPT_MARRAY = 3;
	
	/**
	 * 离婚
	 * 请求:{@code Request}
	 * 响应:{@code LoveInfoResponse}
	 */
	byte UN_MARRY = 4;
	/**
	 * 送礼
	 * 请求:{@code Request}
	 * 响应:{@code Response}
	 */
	byte GIVE_GIFT = 5;

	/**
	 * 领取礼物
	 * 请求:{@code Request}
	 * 响应:{@code GetMarryGiftResponse}
	 */
	byte ACCEPT_GIFT = 6;
	
	/**
	 * 结婚信息推送
	 * 推送:{@code MarriedResponse}
	 */
	byte PUSH_MARRY_INFO = 7;
	
	/**
	 * 请婚列表推送
	 *  推送:{@code AcceptListResponse}
	 */
	byte PUSH_ACCEPT_LIST = 8;
	/**
	 * 推送礼物状态
	 * 推送:{@code LoveGiftResponse}
	 */
	byte PUSH_GIFT_STATE = 9;
	
	/**
	 * 请求战斗
	 * 请求:{@code LoveFightRequest}
	 * 返回:{@code LoveFightResponse}
	 */
	byte LOVE_FIGHT = 10;
	
	/**
	 * 获取排行榜信息
	 * 请求:{@code Request}
	 * 返回:{@code LoveRankInfoResponse}
	 */
	byte GET_RANK_INFO = 11;
	
	/**
	 * 补满挑战次数
	 * 请求:{@code Request}
	 * 返回:{@code Response}
	 */
	byte BUY_FIGHT_NUM = 12;
	
	/**
	 * 请求固定排行榜
	 * 请求:{@code Request}
	 * 返回:{@code LoveRankListResponse}
	 */
	byte GET_TOP_RANK = 13;
	
	/**
	 * 获取可挑战的人
	 * 请求:{@code Request}
	 * 返回:{@code LoveRankListResponse}
	 */
	byte GET_FIGHT_RANK = 14;
	
	/**
	 * 查看战斗记录
	 * 请求:{@code Request}
	 * 返回:{@code LoveFightInfoResponse}
	 */
	byte LOVE_FIGHT_INFO = 15;
	
	/**
	 * 获取录像
	 * 请求:{@code FightVideoRequest}
	 * 返回:{@code FightVideoResponse}
	 */
	byte GET_FIGHT_VIDEO = 16;
	
	/**
	 * 推送战斗消息更新
	 * 推送:{@code LoveFightInfoResponse}
	 */
	byte PUSH_FIGHT_INFO = 17;
	
	/**
	 * 推送跨天
	 * 推送:{@code Response}
	 */
	byte PUSH_LOVE_FIGHT_RESET = 18;
	
	/**
	 * 请求合作信息
	 * 请求:{@code Request}
	 * 返回:{@code LoveMonsterInfoResponse}
	 */
	byte GET_LOVE_MONSTER_INFO = 19;
	
	/**
	 * 挑战boss
	 * 请求:{@code LoveMonsterRequest}
	 * 返回:{@code LoveMonsterFightResponse}
	 */
	byte LOVE_MONSTER_FIGHT = 20;
	
	/**
	 * 解锁难度
	 * 请求:{@code LoveMonsterRequest}
	 * 返回:{@code Response}
	 */
	byte LOVE_MONSTER_UNLOCK = 21;
	
	/**
	 * 刷新合作挑战次数
	 * 请求:{@code LoveMonsterRequest}
	 * 返回:{@code Response}
	 */
	byte FLUSH_MONSTER_FIGHT = 22;
	
	/**
	 * 推送boss血量更新
	 * 推送:{@code LoveMonsterBossResponse}
	 */
	byte PUSH_BOSS_HP = 23;
	
	/**
	 * 领取奖励
	 * 请求:{@code LoveMonsterRequest}
	 * 返回:{@code Response}
	 */
	byte LOVE_MONSTER_REWARD = 24;
	
	/**
	 * 获取店铺信息
	 * 请求{@code Request}
	 * 返回{@code LoveShopInfoResponse}
	 * 推送{@code LoveShopInfoResponse}
	 */
	byte LOVE_SHOP_INFO = 25;
	
	/**
	 * 购买
	 * 请求{@code LoveShopRequest}
	 * 返回{@code Response}
	 */
	byte LOVE_SHOP_BUY = 26;
	
	/**
	 * 刷新商品列表
	 * 请求{@code Request}
	 * 返回{@code LoveShopInfoResponse}
	 */
	byte LOVE_SHOP_FLUSH = 27;
	
	
}
