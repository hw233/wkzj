package com.jtang.gameserver.module.hero.handler;


/**
 * 仙人模块命令接口
 * @author vinceruan
 *
 */
public interface HeroCmd {

	/**
	 * 推送仙人信息(添加仙人)
	 * <pre>
	 * 推送:{@code HeroListResponse}
	 * </pre>
	 */
	byte PUSH_ADD_HERO = 1;
	
	/**
	 * 推送仙人魂魄信息(heroId和数量)
	 * <pre>
	 * 推送:{@code HeroSoulListResponse}
	 * </pre>
	 */
	byte PUSH_HERO_SOUL = 2;
	
	/**
	 * 获取仙人列表
	 * <pre>
	 * 请求：{@code Request}
	 * 响应:{@code HeroListResponse}
	 * </pre>
	 */
	byte LOAD_HERO_LIST = 3;
	
	/**
	 * 获取仙人列表
	 * <pre>
	 * 请求：{@code Request}
	 * 响应:{@code HeroSoulListResponse}
	 * </pre>
	 */
	byte LOAD_HERO_SOUL_LIST= 4;
	
	/**
	 * 消耗魂魄招募仙人
	 * <pre>
	 * 请求:{@code Soul2HeroRequest}
	 * 响应:{@code Soul2HeroResponse}
	 * 推送:
	 * {@code HeroListResponse} -- PUSH_ADD_HERO
	 * {@code HeroSoulListResponse} -- PUSH_HERO_SOUL
	 * </pre>
	 */
	byte SOUL2HERO = 5;
	
	/**
	 * 消耗魂魄突破
	 * <pre>
	 * 请求:{@code BreakThroughRequest}
	 * 响应:{@code Response}
	 * 推送:
	 * {@code HeroSoulListResponse}
	 * {@code UpdateHeroResponse}
	 * </pre>
	 */
	byte BREAK_THROUGH = 6;

	/**
	 * 推送信息更新仙人
	 * <pre>
	 * 推送:{@code UpdateHeroResponse}
	 * </pre>
	 */
	byte PUSH_UPDATE_HERO = 8;
	
	/**
	 * 推送仙人身上的buffer列表
	 * 推送:{@code UpdateHerosBuffResponse}
	 */
	byte PUSH_HERO_BUFFER = 10;
	
	/**
	 * 推送删除的仙人
	 * 推送:{@code HeroRemoveListResponse}
	 */
	byte PUSH_HERO_REMOVE = 11;
}
