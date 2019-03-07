package com.jtang.gameserver.module.story.handler;

/**
 * 故事模块命令码
 * @author vinceruan
 *
 */
public interface StoryCmd {
	/**
	 * 加载故事模块进度数据
	 * <pre>
	 * 请求：{@code Request}
	 * 响应:{@code StoryResponse}
	 * </pre>
	 */
	byte LOAD_STORY = 1;
	
	/**
	 * 开始战斗
	 * <pre>
	 * 请求:{@code StartBattleRequest}
	 * 响应:{@code BattleDataResponse}
	 * 同步接口:
	 * {@code UpdateStoryStarResponse}
	 * {@code UpdateBattleStarResponse}
	 * </pre>
	 */
	byte START_BATTLE = 2;
	
	/**
	 * 推送故事星级变化
	 * <pre>
	 * 响应:{@code UpdateStoryStarResponse}
	 * </pre>
	 */
	byte PUSH_STORY_STAR = 3;
	
	/**
	 * 推送战场星级变化
	 * <pre>
	 * 响应:{@code UpdateBattleStarResponse}
	 * </pre>
	 */
	byte PUSH_BATTLE_STAR = 4;
	
	/**
	 * 故事通关奖励
	 * <pre>
	 * 	请求:{@code ClearStoryAwardRequest}
	 *  响应:{@code Response}
	 *  同步接口:
	 *  推送角色属性改变:{@code ActorAttributeResponse}
	 * </pre>
	 */
	byte CLEAR_STORY_AWARD = 6;
	
	/**
	 * 扫荡
	 * 请求:{@code StoryFightRequest}
	 * 响应:{@code StoryFightListResponse}
	 */
	byte STORY_FIGHT = 7;
	
	/**
	 * 购买扫荡符
	 * 请求:{@code Request}
	 * 返回:{@code Response}
	 */
	byte BUY_FIGHT_GOODS = 8;
	
	/**
	 * 扫荡信息
	 * 请求:{@code Request}
	 * 返回:{@code StoryInfoResponse}
	 * 推送:{@code StoryInfoResponse}
	 */
	byte GET_STORY_FIGHT = 9;
}
