package com.jtang.gameserver.module.adventures.vipactivity.handler;


/**
 * vip活动
 * @author pengzy
 *
 */
public interface VipActivityCmd {

	/**
	 * 请求主力仙人
	 * <pre>
	 * 请求：{@code Request}
	 * 回复：{@code MainHeroResponse}
	 * </pre>
	 */
	byte MAIN_HERO_INFO = 1;
	/**
	 * 设置主力仙人
	 * <pre>
	 * 请求：{@code SetMainHeroRequest}
	 * 回复：{@code MainHeroResponse}
	 * </pre>
	 */
	byte MAIN_HERO_SET = 2;

	//空余的留给主力仙人

	/**
	 * 获取炼器宗师信息
	 * <pre>
	 * 请求：{@code Request}
	 * 回复：{@code EquipComposeInfoResponse}
	 * </pre>
	 */
	byte EQUIP_COMPOSE_INFO = 6;
	
	/**
	 * 炼器宗师，装备合成
	 * <pre>
	 * 请求：{@code EquipComposeRequest}
	 * 回复：{@code EquipComposeResultResponse}
	 * </pre>
	 */
	byte EQUIP_COMPOSE = 7;
	
	//空余的留给炼器宗师
	
	/**
	 * 获取仙人合成信息(暂未支持)
	 * 
	 */
	byte HERO_COMPOSE_INFO = 11;
	
	/**
	 * 仙人合成
	 * <pre>
	 * 请求：
	 * 回复：
	 * </pre>
	 */
	byte HERO_COMPOSE = 12;
	
	/**
	 * 角色重生 
	 * 请求：{@code Request}
	 * 回复：{@code Response}
	 */
	byte ACTOR_RESET = 13;
	
	/**
	 * 发送天财地宝
	 * 请求:{@code GiveEquipRequest}
	 * 回复:{@code GiveEquipResponse}
	 */
	byte GIVE_EQUIP_ALLY = 14;
	
	/**
	 * 天财地宝信息
	 * 请求:{@code Request}
	 * 回复:{@code GiveEquipInfoResponse}
	 */
	byte GIVE_EQUIP_INFO = 15;
	
	/**
	 * 推送天财地宝信息
	 * 推送:{@code GiveEquipInfoResponse}
	 */
	byte PUSH_GIVE_EQUIP = 16;
	
	/**
	 * 重置仙人或装备
	 * 请求:{@code ResetEquipRequest}
	 * 返回:{@code Response}
	 */
	byte RESET_EQUIP = 17;
	
	/**
	 * 重置仙人
	 * 请求:{@code ResetHeroRequest}
	 * 返回:{@code Response}
	 */
	byte RESET_HERO = 18;
	
	/**
	 * 获取奇遇信息
	 * 请求:{@code Request}
	 * 返回:{@code VipActivityInfoResponse}
	 */
	byte GET_INFO = 19;
	
	/**
	 * 推送奇遇信息变更
	 * 推送:{@code VipActivityResponse}
	 */
	byte PUSH_INFO_RESET = 20;
	
}
