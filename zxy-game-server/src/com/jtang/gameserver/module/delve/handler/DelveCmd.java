package com.jtang.gameserver.module.delve.handler;

/**
 * 潜修命令
 * @author 0x737263
 *
 */
public interface DelveCmd {
	
//	/**
//	 * 获取潜修房信息
//	 * 请求：{@code Request}
//	 * 回复：{@code DelveInfoResponse}
//	 */
//	byte GET_DELVE_INFO = 1;
//	
//	/**
//	 * 潜修房升级
//	 * <pre>
//	 * 请求：{@code Request}
//	 * 回复：{@code DelveLevelUpResponse}
//	 * </pre>
//	 */
//	byte LEVEL_UP = 2;
	
	/**
	 * 潜修
	 * <pre>
	 * 请求：{@code DoDelveRequest}
	 * 回复：{@code DoDelveResponse}
	 * </pre>
	 */
	byte DO_DELVE = 3;
	
	/**
	 * 属性更新
	 * <pre>
	 * 推送：{@code DelveAttributeResponse}
	 * </pre>
	 */
	byte PUSH_DELVE_ATTRIBUTE = 4;
	
	/**
	 * 重修
	 * <pre>
	 * 推送：{@code RepeatDelveRequest}
	 * 回复：{@code UpdateHeroResponse}若成功则返回ModuleName.HERO, HeroCmd.PUSH_UPDATE_HERO对应的UpdateHeroResponse
	 * </pre>
	 */
	byte REPEAT_DELVE = 5;
	
	/**
	 * 获取上次潜修属性
	 * 请求:{@code LastDelveRequest}
	 * 回复:{@code LastDelveResponse}
	 */
	byte LAST_DELVE = 6;
	
	/**
	 * 一键潜修
	 * 请求:{@code OneKeyDelveRequest}
	 * 返回:{@code OneKeyDelveResponse}
	 */
	byte ONE_KEY_DELVE = 7;
}
