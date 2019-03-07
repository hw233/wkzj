package com.jtang.gameserver.component.oss;

/**
 * 游戏服的oss类型
 * @author 0x737263
 *
 */
public enum GameOssType {
	
	/**
	 * 程序用于测试日志写出
	 */
	OSS_TEST("OSSTEST"),
	
	/**
	 * 新进帐号
	 */
	NEW_USER("newUser"),
	
	/**
	 * 角色退出日志
	 */
	ACTOR_LOGOUT("actorLogout"),
	
	/**
	 * 角色升级日志
	 */
	ACTOR_UPGRADE("actorUpgrade"),
	
	/**
	 * 角色动作行为日志
	 */
	ACTOR_MONTION("actorMontion"),
	
	/**
	 * 金币日志
	 */
	GOLD("gold"),

	/**
	 * 点券日志
	 */
	TICKET("ticket"),
	
	/**
	 * 阵仙阵升级日志
	 */
	RECRUIT_UPGRADE("recruitUpgrade"),
	
	/**
	 * 试炼洞重置日志
	 */
	TRIAL_RESET("trialReset"),
	
	/**
	 * 试炼洞战斗日志
	 */
	TRIAL_BATTLE("trialBattle"),
	
	/**
	 * 强化室升级日志
	 */
	ENHANCED_UPGRADE("enhancedUpgrade"),
	
	/**
	 * 潜修室升级日志(升级时记录)
	 */
	DELVE_UPGRADE("delveUpgrade"),
	
	/**
	 * 吸灵室升级日志(升级时记录)
	 */
	VAMPIIR_UPGRADE("vampiirUpgrade"),
	
	/**
	 * 精炼室升级日志
	 */
	REFINE_UPGRADE("refineUpgrade"),
	
	/**
	 * 秘法堂升级日志
	 * 阵法堂升级日志
	 */
	
	/**
	 * 盟友添加
	 */
	ALLY_ADD("allyAdd"),
	
	/**
	 * 盟友删除
	 */
	ALLY_DEL("allyDel"),
	
	/**
	 * 故事战斗日志
	 */
	STORY_BATTLE("storyBattle"),
	
	/**
	 * TODO 这个没什么意义
	 * 格子解锁日志
	 */
	LINEUP_GRID_UNLOCK("lineupGirdUnlock"),
	
	/**
	 * 登天塔战斗结果日志(止步某一层的时候记一条)
	 */
	BABLE_BATTLE_FAIL("bableBattleFail"),
	
	/**
	 * 抢夺日志(每抢夺一次记录一次)
	 */
	SNATCH_RESULT("snatchResult"),
	
	/**
	 * 角色精力日志
	 */
	ACTOR_ENERGY("energy"),
	
	/**
	 * 角色活力日志
	 */
	ACTOR_VIT("vit"),
	
	/**
	 * 角色声望日志
	 */
	ACTOR_REPUTATION("actorReputation"),
	
	/**
	 * 角色物品日志
	 */
	ACTOR_GOODS("actorGoods"),
	
	/**
	 * 角色装备日志
	 */
	ACTOR_EQUIP("actorEquip"),
	
	/**
	 * 首次充值信息日志
	 */
	FIRST_RECHARGE_INFO("firstRechargeInfo"),

	/**
	 * 仙人日志
	 */
	HERO("hero"),
	
	/**
	 * 仙人升级日志
	 */
	HERO_UPGRADE("heroUpgrade"),
	
	/**
	 * 仙人经验日志
	 */
	HERO_EXP("heroExp"),
	
	/**
	 * 仙人吸灵日志
	 */
	HERO_VAMPIIR("heroVampiir"),

	/**
	 * 仙人潜修日志
	 */
	HERO_DELVE("heroDelve"),
	
	/**
	 * 仙人魂魄日志
	 */
	HERO_SOUL("heroSoul"),

	/**
	 * 装备强化日志
	 */
	EQUIP_ENHANCED("equipEnhanced"),
	
	/**
	 * 装备精炼日志
	 */
	EQUIP_REFINE("equipRefine"),
	
	/**
	 * 集众降魔积分日志
	 */
	DEMON_SCORE("demonScore"),
	
	/**
	 * 每天达到要求可参与集众降魔的玩家日志
	 */
	DEMON_JOIN("demonJoin"), 
	/**
	 * 系统邮件删除
	 */
	SYSMAIL_REMOVE("sysmailRemove"),
	
	/**
	 * 每天最强势力发奖名单
	 */
	POWER_REWARD_ACTOR("powerRewardActor"), 
	/**
	 * 集众降魔boss击杀
	 */
	DEMON_KILL_BOSS("demonKillBoss"), 
	/**
	 * 云游商店刷新
	 */
	TRADER_FLUSH("traderFlush"),
	/**
	 * 云游商店购买
	 */
	TRADER_BUY("traderBuy"),
	/**
	 * 答题活动
	 */
	QUESTIONS("questions"),
	/**
	 * 天梯战斗
	 */
	LADDER_FIGHT("ladderFight"),
	/**
	 * 天梯每日排名
	 */
	LADDER_DAY_RANK("ladderDayRank"),
	/**
	 * 天梯赛季排行
	 */
	LADDER_SPORT_RANK("ladderSportRank"),
	
	/**
	 * 摇奖活动
	 */
	ERNIE("ernie");
	
	/**
	 * 日志名称
	 */
	private String name = "";
	
	GameOssType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public static String[] getEnumArray() {
		GameOssType[] ossTypeArray = GameOssType.values();
		String[] array = new String[ossTypeArray.length];
		for (int i = 0; i < ossTypeArray.length; i++) {
			array[i] = ossTypeArray[i].getName();
		}
		return array;
	}
	
}
