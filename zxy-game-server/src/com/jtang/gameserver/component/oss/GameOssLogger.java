package com.jtang.gameserver.component.oss;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jiatang.common.model.EquipType;
import com.jiatang.common.oss.BaseOssLogger;
import com.jiatang.common.oss.OssLogType;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.Game;
import com.jtang.gameserver.module.battle.constant.WinLevel;
import com.jtang.gameserver.module.delve.model.DoDelveResult;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.equip.type.EquipDecreaseType;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.hero.type.HeroAddType;
import com.jtang.gameserver.module.hero.type.HeroDecreaseType;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.hero.type.HeroSoulDecreaseType;
import com.jtang.gameserver.module.user.type.EnergyAddType;
import com.jtang.gameserver.module.user.type.EnergyDecreaseType;
import com.jtang.gameserver.module.user.type.GoldAddType;
import com.jtang.gameserver.module.user.type.GoldDecreaseType;
import com.jtang.gameserver.module.user.type.ReputationAddType;
import com.jtang.gameserver.module.user.type.TicketAddType;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.module.user.type.VITAddType;
import com.jtang.gameserver.module.user.type.VITDecreaseType;

/**
 * 游戏服oss日志记录
 * @author 0x737263
 *
 */
public class GameOssLogger extends BaseOssLogger {
	private static byte[] lockObject = new byte[1];
	private static GameOssLogger ossLogger;
	
	private GameOssLogger() {
	}
	
	private static GameOssLogger getInstance() {
		synchronized (lockObject) {
			if (ossLogger == null) {
				ossLogger = new GameOssLogger();
			}
		}
		return ossLogger;
	}
	
	public static void reflushLogger() {
		getInstance().reflushLogger(GameOssType.getEnumArray());
	}
	
	private void write(GameOssType ossType, String message) {
		getInstance().write(ossType.getName(), message);
	}
	
	public static void ossTest(long actorId, int platformId, int serverId, int writeTime) {
		StringBuilder sb = new StringBuilder();
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(writeTime).append(ROW_SPLIT);

		getInstance().write(GameOssType.OSS_TEST, sb.toString());
	}
	
	/**
	 * 新用户进入
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 */
	public static void newUser(String uid, int platformId, int channelId, int serverId) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.NEW_USER, sb.toString());
	}
	
	/**
	 * 角色退出日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param loginTime		上次登陆时间
	 * @param logoutTime	退出时间
	 * @param sim
	 * @param mac
	 * @param imei
	 * @param ip
	 */
	public static void actorLogout(String uid, int platformId, int channelId, int serverId, long actorId, int loginTime, int logoutTime, String sim,
			String mac, String imei, String ip) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(loginTime).append(COLUMN_SPLIT);
		sb.append(logoutTime).append(COLUMN_SPLIT);
		sb.append(sim).append(COLUMN_SPLIT);
		sb.append(mac).append(COLUMN_SPLIT);
		sb.append(imei).append(COLUMN_SPLIT);
		sb.append(ip).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.ACTOR_LOGOUT, sb.toString());
	}
	
	/**
	 * 角色升级日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param oldLevel		旧等级
	 * @param newLevel		新等级
	 * @param upgradeTime	积累升级时间(每次上下线累积增加时长,当前升级后清零)
	 */
	public static void actorUpgrade(String uid, int platformId, int channelId, int serverId, long actorId, int oldLevel, int newLevel, int upgradeTime) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(oldLevel).append(COLUMN_SPLIT);
		sb.append(newLevel).append(COLUMN_SPLIT);
		sb.append(upgradeTime).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.ACTOR_UPGRADE, sb.toString());
	}
	
	/**
	 * 角色动作行为日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param mainType		主类型id
	 * @param subType		子类型id
	 * @param value			动作值
	 */
	public static void actorMontion(String uid, int platformId, int channelId, int serverId, long actorId, int mainType, int subType, int value) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(mainType).append(COLUMN_SPLIT);
		sb.append(subType).append(COLUMN_SPLIT);
		sb.append(value).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.ACTOR_MONTION, sb.toString());
	}
	
	/**
	 * 金币产出日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param addType		产出类型  详情 {@code GoldAddType}
	 * @param goldNum		添加数量
	 * @param totalGoldNum	加完后身上总计金币数量
	 */
	public static void goldAdd(String uid, int platformId, int channelId, int serverId, long actorId, GoldAddType addType, long goldNum, long totalGoldNum) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(OssLogType.ADD.getId()).append(COLUMN_SPLIT);// 1-增加，2消耗
		sb.append(addType.getId()).append(COLUMN_SPLIT);
		sb.append(goldNum).append(COLUMN_SPLIT);
		sb.append(totalGoldNum).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.GOLD, sb.toString());
	}
	
	/**
	 * 金币消耗日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param decreaseType	消耗类型 详情 {@code GoldDecreaseType}
	 * @param goldNum		扣除数量
	 * @param totalGoldNum	扣完后身上总计金币数量
	 */
	public static void goldDecrease(String uid, int platformId, int channelId, int serverId, long actorId, GoldDecreaseType decreaseType,
			int goldNum, long totalGoldNum) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(OssLogType.DECREASE.getId()).append(COLUMN_SPLIT);// 1-增加，2消耗
		sb.append(decreaseType.getId()).append(COLUMN_SPLIT);
		sb.append(goldNum).append(COLUMN_SPLIT);
		sb.append(totalGoldNum).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.GOLD, sb.toString());
	}
	
	/**
	 * 点券产出日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param addType		点券添加类型 详情{@code TicketAddType}
	 * @param recharegeNum	充值点券数
	 * @param giveNum		赠送点券数
	 */
	public static void ticketAdd(String uid, int platformId, int channelId, int serverId, long actorId, TicketAddType addType, int recharegeNum,
			int giveNum) {
		
		if (recharegeNum < 1 && giveNum < 1) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(OssLogType.ADD.getId()).append(COLUMN_SPLIT);// 1-增加，2消耗
		sb.append(addType.getId()).append(COLUMN_SPLIT);
		sb.append(recharegeNum).append(COLUMN_SPLIT);
		sb.append(giveNum).append(COLUMN_SPLIT);
		sb.append(0).append(COLUMN_SPLIT);
		sb.append(0).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.TICKET, sb.toString());
	}
	
	/**
	 * 点券消耗日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param decreaseType	扣除类型	
	 * @param recharegeNum	充值的点券消耗数
	 * @param giveNum		赠送的点券消耗数
	 * @param id			id
	 * @param num			数量
	 */
	public static void ticketDecrease(String uid, int platformId, int channelId, int serverId, long actorId, TicketDecreaseType decreaseType,
			int recharegeNum, int giveNum, int id, int num) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(OssLogType.DECREASE.getId()).append(COLUMN_SPLIT);// 1-增加，2消耗
		sb.append(decreaseType.getId()).append(COLUMN_SPLIT);
		sb.append(recharegeNum).append(COLUMN_SPLIT);
		sb.append(giveNum).append(COLUMN_SPLIT);
		sb.append(id).append(COLUMN_SPLIT);
		sb.append(num).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.TICKET, sb.toString());
	}
		
	/**
	 * 仙人产出日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param addType		增加类型 详情{@code HeroAddType}
	 * @param heroId		仙人id
	 */
	public static void heroAdd(String uid, int platformId, int channelId, int serverId, long actorId, HeroAddType addType, int heroId) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(OssLogType.ADD.getId()).append(COLUMN_SPLIT);// 1-增加，2消耗
		sb.append(addType.getId()).append(COLUMN_SPLIT);
		sb.append(heroId).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.HERO, sb.toString());
	}
	
	/**
	 * 仙人消耗日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param decreaseType	消耗类型 详情{@code HeroDecreaseType}
	 * @param heroId		仙人id(传多个写多条日志)
	 */
	public static void heroDecrease(String uid, int platformId, int channelId, int serverId, long actorId, HeroDecreaseType decreaseType,
			List<Integer> heroIds) {
		
		int time = DateUtils.getNowInSecondes();
		StringBuilder sb = new StringBuilder();
		for (int heroId : heroIds) {
			sb.append(uid).append(COLUMN_SPLIT);
			sb.append(platformId).append(COLUMN_SPLIT);
			sb.append(channelId).append(COLUMN_SPLIT);
			sb.append(serverId).append(COLUMN_SPLIT);
			sb.append(actorId).append(COLUMN_SPLIT);
			sb.append(OssLogType.DECREASE.getId()).append(COLUMN_SPLIT);// 1-增加，2消耗
			sb.append(decreaseType.getId()).append(COLUMN_SPLIT);
			sb.append(heroId).append(COLUMN_SPLIT);
			sb.append(time).append(ROW_SPLIT);
		}
		
		getInstance().write(GameOssType.HERO, sb.toString());
	}
	
	/**
	 * 仙人升级日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param heroId		仙人id
	 * @param oldLevel		旧等级
	 * @param newLevel		新等级
	 */
	public static void heroUpgrade(String uid, int platformId, int channelId, int serverId, long actorId, int heroId, int oldLevel, int newLevel) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(heroId).append(COLUMN_SPLIT);
		sb.append(oldLevel).append(COLUMN_SPLIT);
		sb.append(newLevel).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.HERO_UPGRADE, sb.toString());
	}
	
	/**
	 * 聚仙阵升级日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param oldLevel
	 * @param newLevel
	 */
	public static void recruitUpgrade(String uid, int platformId, int channelId, int serverId, long actorId, int oldLevel, int newLevel) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(oldLevel).append(COLUMN_SPLIT);
		sb.append(newLevel).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.RECRUIT_UPGRADE, sb.toString());
	}
	
	/**
	 * 试炼洞重置日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param oldLevel		重置前次数
	 * @param newLevel		重置后次数
	 */
	public static void trialReset(String uid, int platformId, int channelId, int serverId, long actorId, int oldLevel, int newLevel) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(oldLevel).append(COLUMN_SPLIT);
		sb.append(newLevel).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.TRIAL_RESET, sb.toString());
	}
	
	/**
	 * 试炼洞战斗日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param trialType		试炼关卡类型(1,2两种类型)
	 * @param newLevel		试炼战斗结果
	 */
	public static void trialBattle(String uid, int platformId, int channelId, int serverId, long actorId, int trialType, int winLevel) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(trialType).append(COLUMN_SPLIT);
		sb.append(winLevel).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);
		
		getInstance().write(GameOssType.TRIAL_BATTLE, sb.toString());
	}

	/**
	 * 强化室升级日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param oldLevel
	 * @param newLevel
	 */
	public static void enhancedUpgrade(String uid, int platformId, int channelId, int serverId, long actorId, int oldLevel, int newLevel) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(oldLevel).append(COLUMN_SPLIT);
		sb.append(newLevel).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.ENHANCED_UPGRADE, sb.toString());
	}
	
	/**
	 * 潜修室升级日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param oldLevel		旧等级
	 * @param newLevel		新等级
	 */
	public static void delveUpgrade(String uid, int platformId, int channelId, int serverId, long actorId, int oldLevel, int newLevel) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(oldLevel).append(COLUMN_SPLIT);
		sb.append(newLevel).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.DELVE_UPGRADE, sb.toString());
	}
	
	/**
	 * 吸灵室升级日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param oldLevel		旧等级
	 * @param newLevel		新等级
	 */
	public static void vampiirUpgrade(String uid, int platformId, int channelId, int serverId, long actorId, int oldLevel, int newLevel) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(oldLevel).append(COLUMN_SPLIT);
		sb.append(newLevel).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);
		getInstance().write(GameOssType.VAMPIIR_UPGRADE, sb.toString());
	}
	
	/**
	 * 精炼室升级日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param oldLevel
	 * @param newLevel
	 */
	public static void refineUpgrade(String uid, int platformId, int channelId, int serverId, long actorId, int oldLevel, int newLevel) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(oldLevel).append(COLUMN_SPLIT);
		sb.append(newLevel).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);
		getInstance().write(GameOssType.REFINE_UPGRADE, sb.toString());
	}
	
	/**
	 * 盟友添加日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param allyActorId	盟友角色id
	 */
	public static void allyAdd(String uid, int platformId, int channelId, int serverId, long actorId, long allyActorId) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(allyActorId).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.ALLY_ADD, sb.toString());
	}

	/**
	 * 盟友删除日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param allyActorId	盟友角色id
	 */
	public static void allyDel(String uid, int platformId, int channelId, int serverId, long actorId, long allyActorId) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(allyActorId).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.ALLY_DEL, sb.toString());
	}

	/**
	 * 故事打关卡日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param battleId		战场id
	 * @param winLevel		胜利级别
	 */
	public static void storyBattle(String uid, int platformId, int channelId, int serverId, long actorId, long battleId, WinLevel winLevel) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(battleId).append(COLUMN_SPLIT);
		sb.append(winLevel.getCode()).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.STORY_BATTLE, sb.toString());
	}
	
	/**
	 * 阵型格子解锁日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param gridNum
	 * @param auto			是否自动解锁  0.手动  1.自动  
	 */
	public static void lineupGridUnlock(String uid, int platformId, int channelId, int serverId, long actorId, int gridNum, boolean auto) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(gridNum).append(COLUMN_SPLIT);
		sb.append(auto ? 1 : 0).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.LINEUP_GRID_UNLOCK, sb.toString());
	}
	
	/**
	 * 登天塔战斗结束日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param bableId		登天塔id
	 * @param lastFloor		止步的楼层
	 * @param totalStar		本次累计获取星数
	 * @param remainStar	剩余星数
	 * @param 
	 */
	public static void bableBattle(String uid, int platformId, int channelId, int serverId, long actorId, int bableId, int lastFloor,int totalStar,int remainStar) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(bableId).append(COLUMN_SPLIT);
		sb.append(lastFloor).append(COLUMN_SPLIT);
		sb.append(totalStar).append(COLUMN_SPLIT);
		sb.append(remainStar).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.BABLE_BATTLE_FAIL, sb.toString());
	}

	/**
	 * 抢夺结果日志
	 * @param uid				帐号uid
	 * @param platformId		平台id
	 * @param channelId			渠道id
	 * @param serverId			游戏服id
	 * @param actorId			角色id
	 * @param targetActorId		目标角色id(有可能是机器人)
	 * @param snatchType		抢夺类型
	 * @param winLevel			战斗结果
	 * @param score				获得的积分
	 * @param goodsId			物品id（有可能有金币id)
	 * @param goodsNum			物品数量
	 */
	public static void snatchResult(String uid, int platformId, int channelId, int serverId, long actorId, long targetActorId,
			WinLevel winLevel, int score, int goodsId, int goodsNum) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(targetActorId).append(COLUMN_SPLIT);
		sb.append(winLevel.getCode()).append(COLUMN_SPLIT);
		sb.append(score).append(COLUMN_SPLIT);
		sb.append(goodsId).append(COLUMN_SPLIT);
		sb.append(goodsNum).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.SNATCH_RESULT, sb.toString());
	}
	
	/**
	 * 精力增加日志
	 * @param uid				帐号uid
	 * @param platformId		平台id
	 * @param channelId			渠道id
	 * @param serverId			游戏服id
	 * @param actorId			角色id
	 * @param energyAddType
	 * @param addNum
	 */
	public static void energyAdd(String uid, int platformId, int channelId, int serverId, long actorId, EnergyAddType type, int num) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(OssLogType.ADD.getId()).append(COLUMN_SPLIT); // 1-增加，2消耗
		sb.append(type.getId()).append(COLUMN_SPLIT);
		sb.append(num).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.ACTOR_ENERGY, sb.toString());
	}
	
	/**
	 * 精力消耗日志
	 * @param uid				帐号uid
	 * @param platformId		平台id
	 * @param channelId			渠道id
	 * @param serverId			游戏服id
	 * @param actorId			角色id
	 * @param type
	 * @param num
	 */
	public static void energyDecrease(String uid, int platformId, int channelId, int serverId, long actorId, EnergyDecreaseType type, int num) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(OssLogType.DECREASE.getId()).append(COLUMN_SPLIT); // 1-增加，2消耗
		sb.append(type.getId()).append(COLUMN_SPLIT);
		sb.append(num).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.ACTOR_ENERGY, sb.toString());
	}
	
	/**
	 * 活力增加日志
	 * @param uid				帐号uid
	 * @param platformId		平台id
	 * @param channelId			渠道id
	 * @param serverId			游戏服id
	 * @param actorId			角色id
	 * @param type
	 * @param num
	 */
	public static void vitAdd(String uid, int platformId, int channelId, int serverId, long actorId, VITAddType type, int num) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(OssLogType.ADD.getId()).append(COLUMN_SPLIT); // 1-增加，2消耗
		sb.append(type.getId()).append(COLUMN_SPLIT);
		sb.append(num).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.ACTOR_VIT, sb.toString());
	}
	
	/**
	 * 活力消耗日志
	 * @param uid				帐号uid
	 * @param platformId		平台id
	 * @param channelId			渠道id
	 * @param serverId			游戏服id
	 * @param actorId			角色id
	 * @param type
	 * @param num
	 */
	public static void vitDecrease(String uid, int platformId, int channelId, int serverId, long actorId, VITDecreaseType type, int num) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(OssLogType.DECREASE.getId()).append(COLUMN_SPLIT); // 1-增加，2消耗
		sb.append(type.getId()).append(COLUMN_SPLIT);
		sb.append(num).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.ACTOR_VIT, sb.toString());
	}
	
	/**
	 * 声望增加日志
	 * @param uid				帐号uid
	 * @param platformId		平台id
	 * @param channelId			渠道id
	 * @param serverId			游戏服id
	 * @param actorId			角色id
	 * @param type				声望增加类型
	 * @param reputation		当前声望值
	 * @param addNum			新增加声望值
	 */
	public static void reputationAdd(String uid, int platformId, int channelId, int serverId, long actorId, ReputationAddType type, long reputation,
			long addNum) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(type.getId()).append(COLUMN_SPLIT);
		sb.append(reputation).append(COLUMN_SPLIT);
		sb.append(addNum).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.ACTOR_REPUTATION, sb.toString());
	}
	
	/**
	 * 物品增加日志
	 * @param uid				帐号uid
	 * @param platformId		平台id
	 * @param channelId			渠道id
	 * @param serverId			游戏服id
	 * @param actorId			角色id
	 * @param type
	 * @param goodsId
	 * @param num
	 */
	public static void goodsAdd(String uid, int platformId, int channelId, int serverId, long actorId, GoodsAddType type, int goodsId, int num) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(OssLogType.ADD.getId()).append(COLUMN_SPLIT); // 1-增加，2消耗
		sb.append(type.getId()).append(COLUMN_SPLIT);
		sb.append(goodsId).append(COLUMN_SPLIT);
		sb.append(num).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.ACTOR_GOODS, sb.toString());
	}
	
	/**
	 * 物品消耗日志
	 * @param uid				帐号uid
	 * @param platformId		平台id
	 * @param channelId			渠道id
	 * @param serverId			游戏服id
	 * @param actorId			角色id
	 * @param type
	 * @param goodsId
	 * @param num
	 */
	public static void goodsDecrease(String uid, int platformId, int channelId, int serverId, long actorId, GoodsDecreaseType type, int goodsId,
			int num) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(OssLogType.DECREASE.getId()).append(COLUMN_SPLIT); // 1-增加，2消耗
		sb.append(type.getId()).append(COLUMN_SPLIT);
		sb.append(goodsId).append(COLUMN_SPLIT);
		sb.append(num).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.ACTOR_GOODS, sb.toString());
	}

	/**
	 * 装备增加日志
	 * @param uid				帐号uid
	 * @param platformId		平台id
	 * @param channelId			渠道id
	 * @param serverId			游戏服id
	 * @param actorId			角色id
	 * @param type				装备添加类型
	 * @param equipId			装备id
	 * @param uuid				装备唯一id
	 */
	public static void equipAdd(String uid, int platformId, int channelId, int serverId, long actorId, EquipAddType type, int equipId, long uuid) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(OssLogType.ADD.getId()).append(COLUMN_SPLIT); // 1-增加，2消耗
		sb.append(type.getId()).append(COLUMN_SPLIT);
		sb.append(equipId).append(COLUMN_SPLIT);
		sb.append(uuid).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.ACTOR_EQUIP, sb.toString());
	}
	
	/**
	 * 装备消耗日志
	 * @param uid				帐号uid
	 * @param platformId		平台id
	 * @param channelId			渠道id
	 * @param serverId			游戏服id
	 * @param actorId			角色id
	 * @param type				装备添加类型
	 * @param equipId			装备id
	 * @param uuid				装备唯一id
	 */
	public static void equipDecrease(String uid, int platformId, int channelId, int serverId, long actorId, EquipDecreaseType type, int equipId,
			long uuid) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(OssLogType.DECREASE.getId()).append(COLUMN_SPLIT); // 1-增加，2消耗
		sb.append(type.getId()).append(COLUMN_SPLIT);
		sb.append(equipId).append(COLUMN_SPLIT);
		sb.append(uuid).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.ACTOR_EQUIP, sb.toString());
	}
	
	/**
	 * 首次充值角色信息日志
	 * @param uid				帐号uid
	 * @param platformId		平台id
	 * @param channelId			渠道id
	 * @param serverId			游戏服id
	 * @param actorId			角色id
	 * @param actorLevel		角色等级
	 * @param vit				活力值
	 * @param maxVit			活力上限值
	 * @param energy			精力值
	 * @param maxEnergy			精力上限值
	 * @param rechargeNum		充值的点券数
	 * @param giveNum			赠送的点券数
	 * @param lastBattleId		最新的战场id
	 * @param lineupHero		阵型中仙人信息  格式:位置_仙人id_等级|位置_仙人id_等级
	 * @param lineupEquip		阵型中装备信息  格式:位置_装备id_等级|位置_装备id_等级
	 * @param rechargeId 		充值id
	 */
	public static void firstRecharge(String uid, int platformId, int channelId, int serverId, long actorId, int actorLevel, int vit, int maxVit,
			int energy, int maxEnergy, int rechargeNum, int giveNum, int lastBattleId, String lineupHero, String lineupEquip, int rechargeId) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(actorLevel).append(COLUMN_SPLIT);
		sb.append(vit).append(COLUMN_SPLIT);
		sb.append(maxVit).append(COLUMN_SPLIT);
		sb.append(energy).append(COLUMN_SPLIT);
		sb.append(maxEnergy).append(COLUMN_SPLIT);
		sb.append(rechargeNum).append(COLUMN_SPLIT);
		sb.append(giveNum).append(COLUMN_SPLIT);
		sb.append(lastBattleId).append(COLUMN_SPLIT);
		sb.append(lineupHero).append(COLUMN_SPLIT);
		sb.append(lineupEquip).append(COLUMN_SPLIT);
		sb.append(rechargeId).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.FIRST_RECHARGE_INFO, sb.toString());
	}
	
	/**
	 * 仙人经验增加
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param heroId		仙人id
	 * @param oldExp		当前经验
	 * @param addExp		新增加经验
	 */
	public static void heroExp(String uid, int platformId, int channelId, int serverId, long actorId, int heroId, int oldExp, int addExp) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(heroId).append(COLUMN_SPLIT);
		sb.append(oldExp).append(COLUMN_SPLIT);
		sb.append(addExp).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.HERO_EXP, sb.toString());
	}

	/**
	 * 仙人吸灵日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param fromHeroId	吸收者的仙人id
	 * @param heroIds		被吸收的仙人id列表
	 * @param heroSouls		被吸收的魂魄id列表
	 * @param addExp		吸收者增加的经验
	 */
	public static void heroVampiir(String uid, int platformId, int channelId, int serverId, long actorId, int fromHeroId, List<Integer> heroIds,
			Map<Integer, Integer> heroSouls, int addExp) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(fromHeroId).append(COLUMN_SPLIT);
		sb.append(StringUtils.intArray2DelimiterString(heroIds)).append(COLUMN_SPLIT);
		String heroSoulsString = StringUtils.map2DelimiterString(heroSouls, Splitable.ATTRIBUTE_SPLIT, Splitable.ELEMENT_DELIMITER);
		sb.append(heroSoulsString).append(COLUMN_SPLIT);
		sb.append(addExp).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.HERO_VAMPIIR, sb.toString());
	}
	
	/**
	 * 仙人潜修日志
	 * @param uid					帐号uid
	 * @param platformId			平台id
	 * @param channelId				渠道id
	 * @param serverId				游戏服id
	 * @param actorId				角色id
	 * @param heroId				潜修的仙人id
	 * @param upResult				潜修属性结果
	 * @param usedDelveCount		已使用的潜修次数
	 * @param maxDelveCount			最大潜修次数
	 */
	public static void heroDelve(String uid, int platformId, int channelId, int serverId, long actorId, int heroId,
			Collection<DoDelveResult> upResult, int usedDelveCount, int maxDelveCount) {

		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(heroId).append(COLUMN_SPLIT);

		Map<Integer, Integer> attributeMaps = new HashMap<>();
		for (DoDelveResult result : upResult) {
			attributeMaps.put((int) result.heroVOAttributeKey.getCode(), result.value);
		}
		String attributeResult = StringUtils.map2DelimiterString(attributeMaps, Splitable.ATTRIBUTE_SPLIT, Splitable.ELEMENT_DELIMITER);

		sb.append(attributeResult).append(COLUMN_SPLIT);
		sb.append(usedDelveCount).append(COLUMN_SPLIT);
		sb.append(maxDelveCount).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.HERO_DELVE, sb.toString());
	}
	
	/**
	 * 仙人魂魄产出日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param addType		增加类型 详情{@code HeroSoulAddType}
	 * @param heroSoulId	魂魄id
	 * @param num			魂魄数量
	 */
	public static void heroSoulAdd(String uid, int platformId, int channelId, int serverId, long actorId, HeroSoulAddType addType, int heroSoulId,
			int num) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(OssLogType.ADD.getId()).append(COLUMN_SPLIT);// 1-增加，2消耗
		sb.append(addType.getId()).append(COLUMN_SPLIT);
		sb.append(heroSoulId).append(COLUMN_SPLIT);
		sb.append(num).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.HERO_SOUL, sb.toString());
	}
	
	/**
	 * 仙人魂魄消耗日志
	 * @param uid			帐号uid
	 * @param platformId	平台id
	 * @param channelId		渠道id
	 * @param serverId		游戏服id
	 * @param actorId		角色id
	 * @param addType		消耗类型 详情{@code HeroSoulDecreaseType}
	 * @param heroSoulId	魂魄id
	 * @param num			魂魄数量
	 */
	public static void heroSoulDecrease(String uid, int platformId, int channelId, int serverId, long actorId, HeroSoulDecreaseType type,
			int heroSoulId, int num) {

		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(OssLogType.DECREASE.getId()).append(COLUMN_SPLIT);// 1-增加，2消耗
		sb.append(type.getId()).append(COLUMN_SPLIT);
		sb.append(heroSoulId).append(COLUMN_SPLIT);
		sb.append(num).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.HERO_SOUL, sb.toString());
	}
	
	/**
	 * 装备强化
	 * @param uid				帐号uid
	 * @param platformId		平台id
	 * @param channelId			渠道id
	 * @param serverId			游戏服id
	 * @param actorId			角色id
	 * @param uuid				装备唯一id
	 * @param equipId			装备配置id
	 * @param equipType			装备类型
	 * @param attributeValue	本次增加的属性值
	 * @param oldLevel
	 * @param newLevel
	 */
	public static void equipEnhanced(String uid, int platformId, int channelId, int serverId, long actorId, long uuid, int equipId,
			EquipType equipType, int attributeValue, int oldLevel, int newLevel) {

		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(uuid).append(COLUMN_SPLIT);
		sb.append(equipId).append(COLUMN_SPLIT);
		sb.append(equipType.getId()).append(COLUMN_SPLIT);
		sb.append(attributeValue).append(COLUMN_SPLIT);
		sb.append(oldLevel).append(COLUMN_SPLIT);
		sb.append(newLevel).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.EQUIP_ENHANCED, sb.toString());
	}
	
	/**
	 * 装备突破
	 * @param uid				帐号uid
	 * @param platformId		平台id
	 * @param channelId			渠道id
	 * @param serverId			游戏服id
	 * @param actorId			角色id
	 * @param uuid				装备唯一id
	 * @param equipId			装备配置id
	 * @param equipType			装备类型
	 * @param attributeValue	本次增加的属性值
	 * @param oldLevel
	 * @param newLevel
	 */
	public static void equipDevelop(String uid, int platformId, int channelId, int serverId, long actorId, long uuid, int equipId,
			EquipType equipType, int attributeValue, int oldDevelopNum, int newDevelopNum) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(uuid).append(COLUMN_SPLIT);
		sb.append(equipId).append(COLUMN_SPLIT);
		sb.append(equipType.getId()).append(COLUMN_SPLIT);
		sb.append(attributeValue).append(COLUMN_SPLIT);
		sb.append(oldDevelopNum).append(COLUMN_SPLIT);
		sb.append(newDevelopNum).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);
		
		getInstance().write(GameOssType.EQUIP_ENHANCED, sb.toString());
	}
	
	
	
	/**
	 * 装备精炼
	 * @param uid				帐号uid
	 * @param platformId		平台id
	 * @param channelId			渠道id
	 * @param serverId			游戏服id
	 * @param actorId			角色id
	 * @param uuid				装备唯一id
	 * @param equipId			装备配置id
	 * @param equipType			装备类型
	 * @param attributeValue	本次增加的属性值
	 * @param refineNum			已经精炼次数
	 * @param maxRefineNum		最大可精炼次数
	 */
	public static void equipRefine(String uid, int platformId, int channelId, int serverId, long actorId, long uuid, int equipId,
			EquipType equipType, int attributeValue, int refineNum, int maxRefineNum) {

		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(uuid).append(COLUMN_SPLIT);
		sb.append(equipId).append(COLUMN_SPLIT);
		sb.append(equipType.getId()).append(COLUMN_SPLIT);
		sb.append(attributeValue).append(COLUMN_SPLIT);
		sb.append(refineNum).append(COLUMN_SPLIT);
		sb.append(maxRefineNum).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.EQUIP_REFINE, sb.toString());
	}
	
	/**
	 * 集众降魔积分增加日志
	 * @param uid				帐号uid
	 * @param platformId		平台id
	 * @param channelId			渠道id
	 * @param serverId			游戏服id
	 * @param actorId			角色id
	 * @param newScore			新增加的积分
	 * @param totalScore		当前累计积分(操作之后的累计积分)
	 */
	public static void demonScoreAdd(String uid, int platformId, int channelId, int serverId, long actorId, int newScore, long totalScore) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(OssLogType.ADD.getId()).append(COLUMN_SPLIT);// 1-增加，2消耗
		sb.append(newScore).append(COLUMN_SPLIT);
		sb.append(totalScore).append(COLUMN_SPLIT);
		sb.append(0).append(COLUMN_SPLIT);
		sb.append(0).append(COLUMN_SPLIT);
		sb.append(0).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.DEMON_SCORE, sb.toString());
	}
	
	/**
	 * 集众降魔积分扣除日志
	 * @param uid				帐号uid
	 * @param platformId		平台id
	 * @param channelId			渠道id
	 * @param serverId			游戏服id
	 * @param actorId			角色id
	 * @param newScore			扣除 的积分
	 * @param totalScore		当前累计积分(操作之后的累计积分)
	 * @param rewardObject		购买的物品 (类型,id,数量)
	 */
	public static void demonScoreDecrease(String uid, int platformId, int channelId, int serverId, long actorId, int decreaseScore, long totalScore,
			RewardObject rewardObject) {
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(OssLogType.DECREASE.getId()).append(COLUMN_SPLIT);// 1-增加，2消耗
		sb.append(decreaseScore).append(COLUMN_SPLIT);
		sb.append(totalScore).append(COLUMN_SPLIT);
		sb.append(rewardObject.rewardType.getCode()).append(COLUMN_SPLIT); // 物品类型
		sb.append(rewardObject.id).append(COLUMN_SPLIT); // 物品id
		sb.append(rewardObject.num).append(COLUMN_SPLIT); // 物品数量
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(GameOssType.DEMON_SCORE, sb.toString());
	}
	
	/**
	 * 每天定时的最强势力排名
	 * @param serverId
	 * @param powerRank
	 */
	public static void demonJoinList(List<Long> demonJoinList) {
		try {
			int utcSecond = DateUtils.getNowInSecondes();
			// 游戏服id,排名,角色id，统计时间
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < demonJoinList.size(); i++) {
				sb.append(Game.getServerId()).append(COLUMN_SPLIT);
				sb.append(i + 1).append(COLUMN_SPLIT);
				sb.append(demonJoinList.get(i)).append(COLUMN_SPLIT);
				sb.append(utcSecond).append(ROW_SPLIT);
			}

			getInstance().write(GameOssType.DEMON_JOIN, sb.toString());
		} catch (Exception ex) {
			LOGGER.error("", ex);
		}
	}
	
	/**
	 * 每天最强势力发奖的排名
	 * @param rewardActorMaps
	 */
	public static void powerRewardActor(Map<Long, Integer> rewardActorMaps){
		StringBuffer sb = new StringBuffer();
		for(Entry<Long,Integer> entry:rewardActorMaps.entrySet()){
			long actorId = entry.getKey();
			long rank = entry.getValue();
			sb.append(Game.getServerId()).append(COLUMN_SPLIT);
			sb.append(actorId).append(COLUMN_SPLIT);
			sb.append(rank).append(COLUMN_SPLIT);
			sb.append(TimeUtils.getNow()).append(ROW_SPLIT);
		}
		getInstance().write(GameOssType.POWER_REWARD_ACTOR, sb.toString());
	}
	
	/**
	 * 删除系统邮件OSS
	 * @param serverId
	 * @param sysMailId
	 * @param ownerActorId
	 * @param content
	 * @param attachGoods
	 * @param sendTime
	 * @param isGet
	 */
	public static void sysmailRemove(int serverId, long sysMailId, long ownerActorId, String content, String attachGoods, int sendTime, int isGet) {
		StringBuilder sb = new StringBuilder();
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(sysMailId).append(COLUMN_SPLIT);
		sb.append(ownerActorId).append(COLUMN_SPLIT);
		String temp = content.replace(",", "，");
		temp = temp.replace("\\n", "");
		sb.append(temp).append(COLUMN_SPLIT);
		sb.append(attachGoods).append(COLUMN_SPLIT);
		sb.append(sendTime).append(COLUMN_SPLIT);
		sb.append(isGet).append(COLUMN_SPLIT);
		sb.append(TimeUtils.getNow()).append(ROW_SPLIT);
		getInstance().write(GameOssType.SYSMAIL_REMOVE, sb.toString());
	}

	/**
	 * 集众降魔击杀boss
	 * @param serverId
	 * @param actorId
	 * @param difficult
	 */
	public static void demonKillBoss(int serverId, long actorId, int difficult) {
		StringBuilder sb = new StringBuilder();
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(difficult).append(COLUMN_SPLIT);
		sb.append(TimeUtils.getNow()).append(ROW_SPLIT);
		getInstance().write(GameOssType.DEMON_KILL_BOSS, sb.toString());
	}
	
	/**
	 * 云游商店刷新商品列表
 	 * @param uid 帐号uid
	 * @param platformId 平台id
	 * @param channelId 渠道id
	 * @param serverId
	 * @param actorId
	 * @param costTicket 刷新消耗的点券
	 * @param flushNum 第几次刷新
	 */
	public static void traderShopFlush(String uid, int platformId, int channelId,int serverId,long actorId,int costTicket,int flushNum){
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(costTicket).append(COLUMN_SPLIT);
		sb.append(flushNum).append(COLUMN_SPLIT);
		sb.append(TimeUtils.getNow()).append(ROW_SPLIT);
		getInstance().write(GameOssType.TRADER_FLUSH, sb.toString());
	}
	
	/**
	 * 云游商人购买商品
 	 * @param uid 帐号uid
	 * @param platformId 平台id
	 * @param channelId 渠道id
	 * @param serverId
	 * @param actorId
	 * @param shopId 商品配置id
	 * @param itemId 物品配置表id
	 * @param itemNum 商品数量
	 * @param discount 折扣(千分比)
	 * @param costTicket 消耗的点券
	 * @param costGolds 消耗的金币
	 */
	public static void traderShopBuy(String uid, int platformId, int channelId,int serverId,long actorId,int shopId,int itemId,int itemNum,int discount,int costTicket,int costGolds){
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(shopId).append(COLUMN_SPLIT);
		sb.append(itemId).append(COLUMN_SPLIT);
		sb.append(itemNum).append(COLUMN_SPLIT);
		sb.append(discount).append(COLUMN_SPLIT);
		sb.append(costTicket).append(COLUMN_SPLIT);
		sb.append(costGolds).append(COLUMN_SPLIT);
		sb.append(TimeUtils.getNow()).append(ROW_SPLIT);
		getInstance().write(GameOssType.TRADER_BUY, sb.toString());
	}
	
	/**
	 * 答题活动oss
	 * @param uid
	 * @param platformId
	 * @param channelId
	 * @param serverId
	 * @param actorId
	 * @param questionsId
	 */
	public static void answerQuestions(String uid,int platformId,int channelId,int serverId,long actorId,int questionsId){
		StringBuilder sb = new StringBuilder();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(questionsId).append(COLUMN_SPLIT);
		sb.append(TimeUtils.getNow()).append(ROW_SPLIT);
		getInstance().write(GameOssType.QUESTIONS, sb.toString());
	}
	
	/**
	 * 天梯挑战记录
	 * @param uid
	 * @param platformId
	 * @param channelId
	 * @param serverId
	 * @param actorId
	 * @param isWin 0.输 1.赢
	 */
	public static void ladderFight(String uid,int platformId,int channelId,int serverId,long actorId,int isWin){
		StringBuffer sb = new StringBuffer();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(isWin).append(COLUMN_SPLIT);
		sb.append(TimeUtils.getNow()).append(ROW_SPLIT);
		getInstance().write(GameOssType.LADDER_FIGHT, sb.toString());
	}
	
	/**
	 * 天梯每天排行
	 * @param uid
	 * @param platformId
	 * @param channelId
	 * @param serverId
	 * @param actorId
	 * @param rank
	 */
	public static void ladderDayRank(String uid,int platformId,int channelId,int serverId,long actorId,int rank){
		StringBuffer sb = new StringBuffer();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(rank).append(COLUMN_SPLIT);
		sb.append(TimeUtils.getNow()).append(ROW_SPLIT);
		getInstance().write(GameOssType.LADDER_DAY_RANK, sb.toString());
	}
	
	/**
	 * 天梯赛季排行
	 * @param uid
	 * @param platformId
	 * @param channelId
	 * @param serverId
	 * @param actorId
	 * @param rank
	 */
	public static void ladderSportRank(String uid,int platformId,int channelId,int serverId,long actorId,int rank){
		StringBuffer sb = new StringBuffer();
		sb.append(uid).append(COLUMN_SPLIT);
		sb.append(platformId).append(COLUMN_SPLIT);
		sb.append(channelId).append(COLUMN_SPLIT);
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(rank).append(COLUMN_SPLIT);
		sb.append(TimeUtils.getNow()).append(ROW_SPLIT);
		getInstance().write(GameOssType.LADDER_SPORT_RANK, sb.toString());
	}
	
	/**
	 * 摇奖活动OSS
	 * @param serverId 		服务器id
	 * @param appId			活动id
	 * @param actorId		角色id
	 * @param ernieCount	摇奖次数	
	 * @param rewardType	奖励物品类型
	 * @param rewardId		奖励物品id
	 * @param rewardNum		奖励物品数量
	 */
	public static void runErnie(int serverId, long appId, long actorId, int ernieCount, int rewardType, int rewardId, int rewardNum){
		StringBuffer sb = new StringBuffer();
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(appId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(ernieCount).append(COLUMN_SPLIT);
		sb.append(rewardType).append(COLUMN_SPLIT);
		sb.append(rewardId).append(COLUMN_SPLIT);
		sb.append(rewardNum).append(COLUMN_SPLIT);
		sb.append(TimeUtils.getNow()).append(ROW_SPLIT);
		getInstance().write(GameOssType.ERNIE, sb.toString());
	}
	
}