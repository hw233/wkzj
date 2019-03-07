package com.jiatang.common;

import com.jtang.core.protocol.StatusCode;

/**
 * 返回状态码(id全局唯一,模块id + 0-99)
 * 
 * <pre>
 * 使用方法：
 * 导入 import static com.jtang.sm2.module.StatusCodeConstant.*;
 * </pre>
 * 
 * @author 0x737263
 * 
 */
public interface GameStatusCodeConstant extends StatusCode {

	/** 用户登陆错误 */
	short USER_LOGIN_ERROR = 100;
	/** 创建角色失败 */
	short CREATE_ACTOR_FAIL = 101;
	/** 角色名称已存在 */
	short ACTOR_NAME_EXISTS = 102;
	/** token验证错误 */
	short TOKEN_VALIDATE_ERROR = 103;
	/** 禁止创建角色 */
	short DENY_CREATE_ACTOR = 104;
	/** 角色Id验证错误 */
	short ACTOR_ID_VALIDATE_ERROR = 105;
	/** 获取角色信息失败 */
	short GET_ACTOR_INFO_FAIL = 106;
	/** 平台类型错误 */
	short PLATFORM_TYPE_ERROR = 107;
	/** 禁用角色重复登陆. */
	short DENY_ACTOR_REPEAT_LOGIN = 108;
	/** 用户已经登陆 */
	short USER_IS_LOGINED = 109;
	/** 选择的仙人Id错误 */
	short SELECT_HERO_ID_ERROR = 110;
	/** 角色id错误 */
	short ACTOR_ID_ERROR = 111;
	/** 角色未找到 */
	short ACTOR_NOT_FOUND = 112;
	/** serverid错误. */
	short SERVER_ID_ERROR = 113;
	/** 登陆授权验证平台类型错误 */
	short AUTH_LOGIN_PLATFORM_TYPE_ERROR = 114;
	/** 首次选择的仙人不存在于配置文件中 */
	short FIRST_SELECT_HERO_NOT_IN_CONFIG = 115;
	/** token超时 */
	short TOKEN_TIMEOUT = 116;
	/** 角色未被封禁 */
	short ACTOR_USEABLE = 117;
	/** 角色已重生过*/
	short ACTOR_HAD_RESET = 118;
	/** 重连id错误*/
	short USER_RECONNECT_ID_ERROR = 119;
	/** 重连超时*/
	short USER_RECONNECT_TIME_OUT = 120;
	/** 配置文件名不能为空  */
	short DATA_CONFIG_NAME_NOT_NULL = 121;
	/** 配置文件内容不能为空 */
	short DATA_CONFIG_NOT_NULL = 122;
	/** 配置文件刷新失败 */
	short DATA_CONFIG_FLUSH_ERROR = 123;
	/** 活力购买已达上限 */
	short VIT_BUY_MAX = 124;
	/** 精力购买已达上限 */
	short ENERGY_BUY_MAX = 125;
	/** 金币购买已达上限 */
	short GOLD_BUY_MAX = 126;

	
	
	/** 仙人阵型为空 */
	short ATK_TEAM_LINEUP_NULL = 401;
	/** 对方阵型为空 */
	short DEF_TEAM_LINEUP_NULL = 402;
	/** 重复进入战场 */
	short REPLICA_DUPLICATE_ENTRANCE_ERROR = 403;
	/** 服务器忙 */
	short BATTLE_SERVER_BUSY = 404;
	/** 战斗服务器正在关闭 */
	short BATTLE_SERVER_STOPING = 405;
	/** 服务器内部错误 */
	short BATTLE_SERVER_ERROR = 406;
	/** 不支持的地图类型 */
	short MAP_CONFIG_NOT_SUPPORT = 407;
	/** 提交战斗请求失败 */
	short BATTLE_REQUEST_SUBMIT_FAIL = 408;
	/** 队伍错误，攻防方有相同角色 */
	short BATTLE_TEAM_ERROR = 409;
	/** 地图不存在*/
	short MAP_CONFIG_NOT_EXSIT = 410;

	
	
	/** 仙人已经存在 */
	short HERO_EXISTS = 1001;
	/** 魂魄不存在 */
	short HERO_SOUL_NOT_EXITS = 1002;
	/** 魂魄不足 */
	short HERO_SOUL_NOT_ENOUGH = 1003;
	/** 仙人等级已满 */
	short LEVEL_REACH_LIMIT = 1004;
	/** 已达到最大突破次数 */
	short BREAK_THROUGH_LIMIT = 1005;
	/** 英雄数量已到上限 */
	short TOO_MUCH_HEROS = 1006;
	/** 英雄魂魄数量已到上限 */
	short TOO_MUCH_HEROS_SOUL = 1007;
	/** 仙人配置数据未找到 */
	short HERO_CONFIG_NOT_EXITST = 1008;
	/** 仙人升级经验值错误 */
	short HERO_UPGRADE_EXP_VALUE_ERROR = 1009;
	/** 仙人未找到 */
	short HERO_NOT_FOUND = 1010;
	/** 仙人更新的属性为空 */
	short HERO_ATTRIBUTE_NULL = 1011;
	/** 仙人删除失败 */
	short HERO_DEL_FAIL = 1012;
	/** 主力仙人不能删除*/
	short DELETE_HERO_IS_MAIN = 1013;
	/** 上阵仙人不能删除 */
	short DELETE_HERO_IN_LINEUP = 1014;
	/** 掌教等级不足,无法突破*/
	short BREAK_THROUGH_LEVEL_NOT_ENOUGH = 1015;

	
	
	/** 潜修室升级失败 */
	short DELVE_LEVEL_UP_FAIL = 1100;
	/** 潜修室升级失败,潜修室已达最大等级 */
	short DELVE_LEVEL_UP_IS_MAX = 1102;
	/** 潜修失败 */
	short DELVE_FAIL = 1103;
	/** 潜修失败，金币不够 */
	short DELVE_FAIL_GOLD_NOT_ENOUGH = 1104;
	/** 潜修失败, 仙人属性更新错误 */
	short DELVE_FAIL_UP_PROP_ERROR = 1105;
	/** 潛修失敗，潛修石不足 */
	short DELVE_FAIL_STONE_NOT_ENOUGH = 1106;
	/** 潜修室数据错误 */
	short DELVE_INFO_ERROR = 1107;
	/** 潜修失败，已达到最大潜修次数 */
	short DELVE_HERO_MAX_TIMES = 1108;
	/** 缺少重修配置 */
	short REPEAT_DELVE_LACK_CONFIG = 1109;
	/** 重修失败 */
	short REPEAT_DELVE_FAIL = 1110;
	/** 重修功能未开启 */
	short REPEAT_DELVE_FUNCTION_UNAVAILABLE = 1111;
	/** 潛修失敗，潛修的仙人等级不够 */
	short DELVE_HERO_LEVEL_NOT_ENOUGH = 1112;
	/** 只能重置前一次的潜修 */
	short DELVE_DO_LAST = 1113;

	
	
	/** 吸灵室升级失败 */
	short VAMPIIR_LEVEL_UP_FAIL = 1200;
	/** 吸灵室升级失败,潜修室已达最大等级 */
	short VAMPIIR_LEVEL_UP_IS_MAX = 1202;
	/** 吸灵室数据错误 */
	short VAMPIIR_INFO_ERROR = 1203;
	/** 吸灵失败 */
	short VAMPIIR_FAIL = 1204;
	/** 吸灵失败，金币不够 */
	short VAMPIIR_FAIL_GOLD_NOT_ENOUGH = 1205;
	/** 吸灵失敗，潛修石不足 */
	short VAMPIIR_FAIL_STONE_NOT_ENOUGH = 1206;
	/** 吸灵失敗，声望不足 */
	short VAMPIIR_FAIL_REPUTATION_NOT_ENOUGH = 1207;
	/** 吸灵失敗，超过了可升级上限 */
	short VAMPIIR_HERO_LV_LIMIT = 1208;
	/** 吸灵失敗，超过了可被吸收的仙人上限 */
	short VAMPIIR_SELECTED_HEROS_LIMIT = 1209;
	/** 吸灵失败,物品不足 */
	short VAMPIIR_FAIL_GOODS_NOT_ENOUGH = 1210;

	
	
	/** 聚仙金币不足 */
	short RECRUIT_GOLD_NOT_ENOUGH = 1301;
	/** 聚仙阵类型错误.只有1.小 2.中 3.大 */
	short RECRUIT_TYPE_ERROR = 1303;
	/** 招募失败，剩余机会不够 */
	short RAND_FAILED_CHANGE_NOT_ENOUGH = 1304;
	/** 聚仙点券不足 */
	short RECRUIT_TICKET_NOT_ENOUGH = 1306;
	/** 招募失败,对应星级的仙人配置未找到 */
	short RAND_FAILED_HERO_STAR_CONFIG_NOT_FOUND = 1307;
	/** 聚仙开放条件不满足 */
	short RECRUIT_OPEN_CONDITION_NOT_UNGRATEFUL = 1308;
	/** 聚仙物品不足*/
	short RECRUIT_GOODS_NOT_ENOUGH = 1309;

	
	
	/** 删除装备失败 */
	short EQUIP_DEL_FAIL = 1401;
	/** 装备不存在 */
	short EQUIP_NOT_FOUND = 1402;
	/** 需要更新的属性值为null */
	short EQUIP_ATTRIBUTE_NULL = 1403;
	/** 装备添加已达最大上限 */
	short EQUIP_ADD_IS_MAX = 1404;
	/** 添加装备失败 */
	short EQUIP_ADD_FAIL = 1405;
	/** 装备新增加的等级错误 */
	short EQUIP_NEW_ADD_LEVEL_ERROR = 1406;
	/** 装备升级配置错误 */
	short EQUIP_UPGRADE_CONFIG_ERROR = 1407;
	/** 装备在阵形中不能删除*/
	short EQUIP_IN_LINEUP_NOT_DELETE = 1408;
	/** 掌教等级不足,不能使用此装备 */
	short EQUIP_LEVEL_NOT_ENOUGH = 1409;

	
	
	/** 获取强化室时强化室不存在 */
	short ENHANCED_NOT_EXISTS = 1501;
	/** 强化室升级失败 */
	short ENHANCED_UPGRADE_FAIL = 1502;
	/** 达到了强化室的等级上限 */
	short ENHANCED_LEVEL_LIMIT = 1503;
	/** 强化室升级失败:盟友等级总和不够 */
	short ALLY_LEVEL_SUM_NOT_ENOUGH = 1504;
	/** 装备升级等级达到上限,需提升掌教等级 */
	short EQUIP_UPGRADE_LIMIT = 1506;
	/** 装备升级需要的金币数不够 */
	short EQUIP_UPGRADE_GOLD_NOT_ENOUGH = 1507;
	/** 装备不存在 */
	short EQUIP_NOT_EXISTS = 1508;
	/** 装备升级失败 */
	short EQUIP_UPGRADE_FAIL = 1509;

	
	
	/** 精炼装备金币不够 */
	short REFINE_STONE_OR_MONEY_NOT_ENOUGH = 1601;
	/** 精炼装备精炼石不够 */
	short REFINE_STONE_NOT_ENOUGH = 1602;
	/** 精炼装备精炼次数达到最大 */
	short REFINE_NUMBER_MAX = 1603;
	/** 精炼室配置为null */
	short REFINE_ROOM_CONFIG_NULL = 1605;
	/** 精炼配置为null */
	short REFINE_CONFIG_NULL = 1606;
	/** 精炼室升级已达到上限 */
	short REFINE_LEVEL_CAP = 1607;
	/** 精炼的物品等及不够不能使用精炼石 */
	short REFINE_GOODS_LEVEL_NOT_ENOUGH = 1608;

	
	
	/** 秘法堂数据错误 */
	short CABALA_ERROR = 1801;
	/** 秘法堂达到最大等级 */
	short CABALA_MAX_LEVEL = 1802;
	/** 升级需要的盟友数不够 */
	short CABALA_NEED_FRIENDS_NOT_ENOUGH = 1803;
	/** 提升属性ID错误 */
	short CABALA_ATTR_ID_ERROR = 1805;
	/** 属性已达最大难度 */
	short CABALA_ATTR_MAX_LEVEL = 1806;
	/** 消耗个数超过配置 */
	short CABALA_CONSUME_MAX_ERROR = 1807;

	
	
	/** 仙人已经上阵(不能重复上阵) */
	short HERO_ALREADY_IN_LINEUP = 1901;
	/** 已激活的格子已经 */
	short ACTIVE_GRID_RAN_OUT = 1902;
	/** 指定索引的格子不存在 */
	short GRID_NOT_EXISTS = 1903;
	/** 格子上面已经有仙人,不能放置仙人 */
	short GRID_NOT_AVAILABLE = 1904;
	/** 仙人不存在 */
	short HERO_NOT_EXITS = 1905;
	/** 仙人没有上阵 */
	short HERO_NOT_IN_LINEUP = 1906;
	/** 阵型不存在 */
	short LINEUP_NOT_EXITS = 1907;
	/** 无法识别的武器 */
	short EQUIP_TYPE_INVALID = 1908;
	/** 装备已经在格子上面 */
	short EQUIP_EXITS_IN_GRID = 1909;
	/** 装备不在阵型中 */
	short EQUIP_NOT_EXITS_IN_GRID = 1910;
	/** 阵型不能为空 */
	short LINEUP_SHOULD_NOT_EMPTY = 1911;
	/** 解锁配置不存在 */
	short LINEUP_UNLOCK_CONFIG_MISS = 1912;
	/** 等级不足 */
	short LEVEL_NOT_REACH = 1913;

	
	
	/** 物品配置不存在 */
	short GOODS_CONFIG_NOT_EXISTS = 2001;
	/** 物品不足够 */
	short GOODS_NOT_ENOUGH = 2002;
	/** 物品不存在 */
	short GOODS_NOT_EXISTS = 2003;
	/** 禁止添加金币(请走专门的添加通道) */
	short GOODS_DENY_ADD_GOLD = 2004;
	/** 禁止添加点券(请走专门的添加通道) */
	short GOODS_DENY_ADD_TICKET = 2005;
	/** 精力值满了，不能增加 */
	short ENERGY_FULL_ERROR = 2006;
	/** 活力值满了，不能增加 */
	short VIT_FULL_ERROR = 2007;
	/** 增加精力失败 */
	short USE_GOODS_ADD_ENERGY_FAIL = 2008;
	/** 增加精力失败 */
	short USE_GOODS_ADD_VIT_FAIL = 2009;
	/** 物品使用错误 */
	short USE_GOODS_ERROR = 2010;
	/** 该物品无法使用 */
	short USE_GOODS_NOT_SUPPORT = 2011;
	/** 聚仙次数满了 */
	short USE_GOODS_RECRUIT_FULL = 2012;
	/** 添加物品，数量参数错误 */
	short GOODS_NUM_ADD_ERROR = 2013;
	/** 使用物品等级不够 */
	short USE_GOODS_LEVEL_NOT_ENOUGH = 2014;
	/** 不是装备碎片,不能合成 */
	short GOODS_NOT_COMPOSE = 2015;
	/** 合成物品数量不足 */
	short GOODS_NUM_NOT_ENOUGH = 2016;
	/** 物品合成中,不能使用 */
	short GOODS_COMPOSE_NOT_USE = 2017;
	
	
	/** 战场未开放 */
	short BATTLE_NOT_OPEN = 2201;
	/** 活力不足 */
	short VIT_NOT_ENOUGH = 2202;
	/** 战场不存在 */
	short BATTLE_NOT_EXISTS = 2203;
	/** 副本id错误 */
	short REPLICA_ID_ERROR = 2204;
	/** 掉落物品不存在 */
	short DROP_GOODS_NOT_EXISTS = 2205;
	/** 故事未开启 */
	short STORY_NOT_OPEN = 2206;
	/** 重复领取奖励 */
	short STORY_HAD_AWARDED = 2207;
	/** 不能领取,条件不满足 */
	short AWARD_CONDITION_NOT_REACH = 2208;
	/** 未知的奖励类型 */
	short UNKNOWN_AWARD_TYPE = 2209;
	/** 故事未通关 */
	short STORY_NOT_CLEAR = 2210;
	/** 故事不存在 */
	short STORY_NOT_FOUND = 2211;
	/** 奖励类型错误*/
	short STORY_REWARD_TYPE_ERROR = 2212;
	/** 星级未达到,不能扫荡 */
	short STORY_STAR_NOT_ENOUGH = 2213;
	/** 合作关卡不能扫荡 */
	short STORY_NOT_FIGHT = 2214;
	/** 盟友故事未通关 */
	short ALLY_STORY_NOT_CLEAN = 2215;
	/** 扫荡符不足 */
	short FIGHT_NOT_ENOUGH = 2216;
	/** vip等级不足,不能扫荡 */
	short VIP_LEVEL_ENOUGH = 2217;
	/** 故事未达到掌教等级*/
	short ACTOR_LEVEL_ENOUGH = 2218;
	/** 未解锁不能扫荡 */
	short UNLOCK_STORY_NOT_FIGHT = 2219;
	/** 盟友不存在*/
	short ALLY_NOT_FIND = 2220;
	
	/** 加为盟友失败:您查找的目标不在线 */
	short ALLY_ACTOR_NOT_ONLINE = 2301;
	/** 加为盟友失败:对方盟友数量已达上限 */
	short OPPOSITE_ALLY_SUM_LIMIT = 2302;
	/** 加为盟友失败:重复加为盟友的时间限制 */
	short REPEAT_ALLY_TIME_LIMIT = 2303;
	/** 加为盟友失败:你的盟友数量已达上限 */
	short ALLY_SUM_LIMIT = 2304;
	/** 该盟友不存在 */
	short ALLY_UNEXISTS = 2305;
	/** 切磋次数达到上限 */
	short FIGHT_TIMES_REACH_LIMIT = 2306;
	/** 能量不够 */
	short FIGHT_ENERGE_NOT_ENOUGH = 2307;
	/** 盟友没有仙人在阵型中 */
	short ALLY_NO_HERO_IN_LINEUP = 2308;
	/** 日切磋次数达到上限 */
	short ALLY_DAY_FIGHT_NUM_LIMIT = 2309;
	/** 加为盟友失败:盟友已经存在列表 */
	short ALLY_ALREADY_EXISTS = 2310;
	/** 加为盟友失败:加自己为好友 */
	short ALLY_CAN_NOT_ADD_SELF = 2311;
	/** 请求的角色坐标不存在 */
	short ALLY_COORDINATE_UNEXISTS = 2312;
	/** 添加的盟友不存在*/
	short ALLY_ACTOR_UNEXISTS = 2313;
	/** 添加盟友失败*/
	short ALL_ADD_ACTOR_FAIL = 2314;
	/** 删除盟友失败已达每日删除上限 */
	short ALLY_REMOVE_CAP = 2315;
	/** 通关3章7关解锁盟友功能 */
	short ALLY_STORY_LOCK = 2316;
	/** 对方还未解锁盟友功能 */
	short TARGET_ALLY_STORY_LOCK = 2317;

	
	
	/** 抢夺失败 **/
	short SNATCH_FAIL = 2401;
	/** 换夺失败，您的积分大于对手敌人 **/
	short SNATCH_SCORE_GREATER_ENEMY = 2402;
	/** 物品合成失败 没有合成对应的装备 **/
	short COMPOSE_FAIL = 2403;
	/** 抢夺配置不存在 **/
	short SNATCH_CONFIG_NOT_EXISTS = 2404;
	/** 抢夺失败，消耗的精力不足 **/
	short SNATCH_ENERGY_NOT_ENOUGH = 2405;
	/** 物品合成失败，碎片不够 **/
	short COMPOSE_FAIL_NOT_ENOUGH_PIECE = 2406;
	/** 先拥有该魂魄才能抢夺 */
	short SOURL_NOT_FOUND_FOR_SNATCH = 2407;
	/** 先拥有该品质装备或宝箱的任意一块碎片后才能抢夺 */
	short PIECE_NOT_FOUND_FOR_SNATCH = 2408;
	/** 已经有足够碎片用于合成 */
	short PIECE_ENOUGH = 2409;
	/** 抢夺目标不存在 */
	short SNATCH_TARGET_NOT_FOUND = 2410;
	/** 合成配置不存在 */
	short COMPOSE_CONF_NOT_FOUND = 2411;
	/** 物品配置不存在 */
	short COMPOSE_GOODS_CONF_NOT_FOUND = 2412;
	/** 兑换失败,条件未达成 */
	short EXCHANGE_FAIL_COND_NOT_MEET = 2413;
	/** 兑换失败,兑换配置不存在 */
	short EXCHANGE_FAIL_CONF_NOT_FOUND = 2414;
	/** 没有抢夺仇恨记录,不能反抢 */
	short SNATCH_BACK_FAIL = 2415;
	/** 该魂魄已经足够,不能再抢 */
	short SNATCH_FAIL_SOUL_ENOUGH = 2416;
	/** 该碎片已经足够,不能再抢 */
	short SNATCH_FAIL_PIECE_ENOUGH = 2417;
	/** 抢夺物品不是碎片 */
	short SNATCH_FAIL_NOT_A_PIECE = 2418;
	/** 当前时间暂未开 放抢夺 */
	short SNATCH_TIME_UNOPENED = 2419;
	/** 当前抢夺对手没有在对手列表中 */
	short SNATCH_ENEMY_NOT_IN_LIST = 2420;
	/** 抢夺对手被抢金币已达上限，禁止抢夺 */
	short SNATCH_ENEMY_GOLD_LIMIT = 2421;
	/** 自己被反抢得太多，不能再进行抢夺 */
	short SNATCH_ACTOR_GOLD_LIMIT = 2422;
	/** 积分不足,兑换失败 */
	short EXCHANGE_FAIL_SCORE_NOT_ENOUGH = 2423;
	/** 该物品已经兑换完 */
	short EXCHANGE_FAIL_ITEM_NOT_ENOUGH = 2424;
	/** 购买失败,今天购买次数已达上限 */
	short SNATCH_NUM_NOT_ENOUGH = 2425;
	/** 购买失败,还有未使用的抢夺次数*/
	short SNATCH_NUM_NOT_USED = 2426;
	/** 领取失败,还有未使用的抢夺次数*/
	short SNATCH_GET_NUM_FAIL = 2427;
	/** 领取失败,今天已经领取*/
	short SNATCH_GET = 2428;
	
	
	/** 角色等级不够， 进入登天塔错误 */
	short BABLE_ACTOR_LEVEL_NOT_ENOUGH = 2501;
	/** 可兑换星星不足 */
	short BABLE_STAR_NOT_ENOUGH = 2502;
	/** 兑换物品不足 */
	short BABLE_EXCHANGE_GOODS_NOT_ENOUGH = 2503;
	/** 必须进入登天塔才能兑换 */
	short BABLE_MUST_EXCHANGE = 2504;
	/** 跳层角色等级不够*/
	short BABLE_SKIP_ACTOR_LEVEL_ERROR = 2505;
	/** 跳层昨日楼层不够*/
	short BABLE_SKIP_LAST_FLOOR_ERROR = 2506;
	/** 已经在登天塔中 */
	short ACTOR_IN_BABLE = 2507;
	/** 还未选择登天塔,不能进行战斗 */
	short NO_CHOICE_BABLE = 2508;
	/** 登天塔已结束,请重置 */
	short BABLE_END = 2509;
	/** 没有进行过登塔,无需重置 */
	short BABLE_NOT_IN = 2510;
	/** 重置次数已用完 */
	short BABLE_RESET_NUM = 2511;
	/** 没有开始登塔不能跳过 */
	short BABLE_NOT_SKIP = 2512;
	/** 没有历史记录,不能跳塔 */
	short BABLE_NOT_HISTORY = 2513;
	/** 跳塔机会已经使用 */
	short BABLE_SKIP_USER = 2514;
	/** 自动登塔层数小于当前层数,不能自动登塔 */
	short BABLE_SKIP_NOT_USE = 2515;
	/** 自动登塔消耗物品不足*/
	short BABLE_CONSUME_GOODS_NOT_ENOUGH = 2516;

	
	
	/** 聊天时金币不足 */
	short CHAT_GOLD_NOT_ENOUGH = 2601;
	/** 消息内容为空字符 */
	short CHAT_UN_VALID_MSG = 2602;
	/** 聊天消息连续发送的时间间隔 */
	short CHAT_INTERVAL_TIME_LIMIT = 2603;
	/** 聊天 已经被禁言 */
	short CHAT_IN_FORBIDDEN = 2604;
	

	/** 试炼重置次数已用完 */
	short TRIAL_RESET_COUNT_RAN_OUT = 2703;
	/** 试炼次数已经用完 */
	short TRIAL_COUNT_RAN_OUT = 2704;
	/** 试炼战场不存在 */
	short TRIAL_BATTLE_NOT_EXISTS = 2705;
	/** 战斗冷却 时间未到*/
	short BATTLE_CD_NOT_PASS = 2706;
	/** 战斗队伍不存在 */
	short LINE_UP_EMPTY = 2707;
	/** 盟友不存在 */
	short ALLY_NOT_EXISTS = 2708;
	/** 已经邀请过盟友 */
	short INVITE_RECEIVED_ALL = 2709;
	/** 试炼次数未用完不能重置 */
	short TRIAL_COUNT_CAN_NOT_RESET = 2710;
	
	
	/** 礼物不存在 */
	short GIFT_NOT_EXISTS = 2801;
	/** 今天的大礼包已经领取 */
	short GIFT_PACKAGE_HAD_ACCEPT = 2802;
	/** 收取礼物次数未够 */
	short GIFT_NOT_REACH_MAX_COUNT = 2803;
	/** 礼物配置不存在 */
	short GIFT_CONFIG_NOT_FOUND = 2804;
	/** 不能重复送礼 */
	short DUP_GIVE_GIFT = 2805;
	/** 送礼次数达到上限 */
	short GIVE_GIFT_NUM_REACH_LIMIT = 2806;
	/** 收礼次数已达上限 */
	short ACCEPT_GIFT_NUM_REACH_LIMIT = 2807;

	
	
	/** 发送消息失败 */
	short MSG_SEND_FAIL = 2901;
	/** 消息内容为空字符 */
	short MSG_UN_VALID_MSG = 2902;

	
	
	/** notification不存在 */
	short NOTIFICATION_UNEXISTS = 3001;
	/** 不存在领取奖励的notification */
	short NOTIFICATION_NO_REWARD_TYPE = 3002;
	/** 奖励已领取 */
	short NOTIFICATION_REWARD_ALREADY_GET = 3003;
	/** 非可通知的信息类型 */
	short NOTIFICATION_NO_NOTICE_TYPE = 3004;
	/** 已通知过盟友 */
	short NOTIFICATION_ALREADY_NOTICE = 3005;
	/** 您还没有盟友，通知失败 */
	short NOTIFICATION_ALLY_NOT_FOUND = 3006;
	/** 录像未找到 */
	short NOTIFICATION_VIDEO_NOT_FOUND = 3007;
	
	
	
	/** 任务不存在 */
	short TASK_UNEXISTS = 3101;
	/** 奖励已领取 */
	short TASK_REWARD_ALREADY_GETED = 3102;
	/** 任务未完成 */
	short TASK_UNFINISHED = 3103;

	
	
	/** 签到记录不存在 */
	short SIGN_RECORD_UNEXISTS = 3201;
	/** 签到奖励已领取 */
	short SIGN_REWARD_ALREADY_GET = 3202;
	/** 未生成奖励 */
	short SIGN_REWARD_RECORD_UN_GENERATE = 3203;
	/** 奖励已过期 */
	short SIGN_REWARD_OVER_DUE = 3204;
	/** 未达到累计天数 */
	short SIGN_REWARD_UN_ACHIVE_REQUIRE_DAYS = 3205;
	/** 已为最新信息 */
	short SIGN_REWARD_ALREADY_LAST_INFO = 3206;
	/** 奖励不可领取*/
	short SIGN_NOT_REWARD = 3207;

	
	
	/** 该奇遇不存在：发生在客户端请求奇遇或删除此奇遇时，如果该角色没有福神眷顾的奇遇，则返回此状态码 */
	short FAVOR_UN_EXISTS = 3301;
	/** 该特权使用次数已经耗尽：发生在使用特权时次数已经耗尽的情形 */
	short FAVOR_PRIVILEGE_NUM_ALREADY_EXHAUST = 3302;
	/** 该特权该特权未激活 */
	short FAVOR_NOT_ACIVE = 3303;

	
	
	/** 城池不存在 */
	short CITY_NOT_EXISTS = 3401;
	/** 不是敌城 */
	short CITY_NOT_HAS_ENEMIES = 3402;
	/** 无法攻打获取排名,你没有城池 */
	short CITY_WAR_MY_CITY_LOST = 3403;
	/** 开战失败,战斗超时 */
	short CITY_WAR_TIME_OUT = 3404;
	/** 开战失败,攻击队伍已经阵亡 */
	short CITY_WAR_ATK_TEAM_NOT_ALIVE = 3405;
	/** 开战失败,精力不足 */
	short CITY_WAR_FAIL_LACK_ENERGY = 3406;
	/** 开战失败,守城队伍已经阵亡 */
	short CITY_WAR_DEFENSE_TEAM_NOT_ALIVE = 3407;
	/** 正在城战,不能调整阵型或开始战斗 */
	short CITY_WAR_PREVENT_LINEUP_AND_BATTLE = 3408;
	/** 防御队伍已经死亡 */
	short CITY_WAR_DEF_TEAM_NOT_ALIVE = 3409;
	/** 正在城战,不能观察其他城池 */
	short CITY_WAR_BEGAN = 3410;
	/** 你没有城池 */
	short CITY_NOT_FOUND = 3411;
	/** 无法攻占,你已经拥有城池 */
	short CITY_WAR_OCCUPIED_FAIL = 3412;

	
	
	/** 该势力已经降级或出榜 */
	short POWER_ALREADY_DEGRADATION_OR_OUT = 3501;
	/** 不能挑战自己 */
	short POWER_UN_ABLE_CHALLENGE_SELF = 3502;
	/** 没有仙人在阵型中 */
	short POWER_NO_HERO_IN_LINEUP = 3503;
	/** 该势力不存在 */
	short POWER_UN_EXISTS = 3504;
	/** 选择了不存在的盟友 */
	short POWER_CHALLENGE_SELECT_WRONG_ALLY = 3505;
	/** 不能挑战比自己排名低的势力 */
	short POWER_UNABLE_CHALLENGE_LOWER_POWER = 3506;
	/** 禁止挑战比自己低的排名 */
	short POWER_CHALLENGE_FORBID_LOW_RANK = 3507;
	/** 精力不足，无法挑战 */
	short POWER_CHALLENGE_ENERGY_NOT_ENOUGH = 3508;
	/** 挑战目标对手正在战斗中，请稍等 */
	short POWER_TARGET_FIGHTING = 3509;
	/** 等级不足不能进入排行榜 */
	short POWER_LEVEL_NOT_ENOUGH = 3510;
	
	/** 势力币不足,不能兑换 */
	short POWER_NUM_NOT_ENOUGH = 3511;
	/** 挑战时间还未冷却,不可挑战 */
	short POWER_TIME_NOT_OVER = 3512;
	/** 挑战已冷却,无需购买 */
	short POWER_FIGHT_NOT_USE = 3513;
	/** 势力币不足,不能刷新 */
	short POWER_NOT_ENOUGH = 3514;
	/** 物品数量不足,不能兑换 */
	short POWER_SHOP_NOT_ENOUGH = 3515;
	
	
	/** 成就不存在 */
	public static final short ACHIEVEMENT_UN_EXISTS = 3701;
	/** 奖励已经领取过 */
	public static final short ACHIEVEMENT_REWARD_ALREADY_GETED = 3702;
	/** 奖励配置不存在 */
	public static final short ACHIEVEMENT_REWARD_CONFIG_UN_EXISTS = 3703;
	/** 成就未完成 */
	public static final short ACHIEVEMENT_UN_ACHIEVED = 3704;

	
	
	/** vip等级不够 */
	public static final short VIP_LEVEL_NO_ENOUGH = 3800;
	/** 主力仙人已设置过 */
	public static final short MAIN_HERO_ALREADY_SETED = 3801;
	/** 炼器宗师装备类型错误 */
	public static final short EQUIP_COMPOSE_TYPE_ERROR = 3802;
	/** 炼器宗师装备uuid错误 */
	public static final short EQUIP_COMPOSE_UUID_ERROR = 3803;
	/** 炼器宗师装备星级错误 */
	public static final short EQUIP_COMPOSE_STAR_DIFFERENT = 3804;
	/** 炼器宗师 装备合成的数量错误 */
	public static final short EQUIP_COMPOSE_REQUIRE_NUM_ERROR = 3805;
	/** 炼器宗师 合成错误 */
	public static final short EQUIP_COMPOSE_ERROR = 3807;
	/** 今日已使用炼器宗师 */
	public static final short EQUIP_COMPOSE_TODAY_IS_USED = 3809;
	/** 装备在阵型中禁止出售 */
	public static final short EQUIP_IN_LINEUP_DENY_COMPOSE = 3810;
	/** 仙人合成 星级错误 */
	public static final short HERO_COMPOSE_STAR_DIFFERENT = 3820;
	/** 仙人合成 的数量错误 */
	public static final short HERO_COMPOSE_REQUIRE_NUM_ERROR = 3821;
	/** 仙人在阵型中禁止出售 */
	public static final short HERO_IN_LINEUP_DENY_COMPOSE = 3823;
	/** 今日已使用仙人合成次数达上限 */
	public static final short HERO_COMPOSE_TODAY_IS_USED = 3824;
	/** 仙人是主力仙人禁止合成*/
	public static final short HERO_IS_MAIN_DENY_COMPOSE = 3825;
	/** 今天已经赠送过装备了 */
	public static final short TREASURE_SEND_EQUIP =3830;
	/** 设置主力仙人金币不足*/
	public static final short MAIN_HERO_GOLD_NOT_ENOUGH =3831;
	/** 仙人没有潜修过,不能重置 */
	public static final short HERO_NOT_DELVE = 3832;
	/** 装备没有强化或者精炼,不能重置 */
	public static final short EQUIP_NOT_UP = 3833;
	/** 仙人、装备重置次数已用完 */
	public static final short RESET_NUM_NOT_ENOUGH = 3834;
	/** 等级不足不能使用炼器宗师 */
	public static final short EQUIP_COMPOSE_LEVEL_NOT_ENOUGH = 3835;

	
	
	/** 商品购买次数已达到最大 */
	public static final short SHOP_BUY_MAX = 3901;
	/** 购买失败 */
	public static final short SHOP_BUY_FAIL = 3902;
	/** 商品配置不存在*/
	public static final short SHOP_NOT_EXIST = 3903;
	/** 商品已下架 */
	public static final short SHOP_NOT_BUY = 3904;
	/** 掌教等级不足 */
	public static final short SHOP_LEVEL_NOT_ENOUGH = 3905;
	/** 金币不足 */
	public static final short SHOP_GOLD_NOT_ENOUGH = 3906;
	/** 月卡用户才能购买 */
	public static final short SHOP_MONTH_CARD = 3907;
	
	
	/** 洞府战场未达到星级 **/
	public static final short HOLE_NO_STAR = 4001;
	/** 奖励已经领取过了 **/
	public static final short HOLE_REWARD_ACCEPT = 4002;
	/** 洞府已经开启 **/
	public static final short HOLE_OPEN = 4003;
	/** 已经过关了 **/
	public static final short HOLE_ACCEPT_FIGHT = 4004;
	/** 没有洞府 **/
	public static final short HOLE_NOT_FIND = 4005;
	/** 没有到达奖励领取的条件 **/
	public static final short HOLE_CONDITION_NOT_REACH = 4006;
	/** 通关的盟友数不够 **/
	public static final short HOLE_ALLY_NOT_ENOUGH = 4007;
	/** 盟友没有邀请 **/
	public static final short HOLE_ALLY_NOT_INVITE = 4008;
	/** 盟友邀请开启的洞府不能邀请盟友 **/
	public static final short HOLE_INVITE_FAIL = 4009;
	/** 邀请的洞府已经关闭  **/
	public static final short HOLE_CLOSE = 4010;
	/** 盟友邀请的洞府参与次数过多 **/
	public static final short HOLE_ALLY_COUNT_NOT_ENOUGH = 4011;

	
	
	/** 激活码已经使用过了 **/
	public static final short CDKEY_IS_USE = 4101;
	/** 激活码不存在 **/
	public static final short CDKEY_NOT_EXISTS = 4102;
	/** 该角色已经领取过激活码了 **/
	public static final short CDKEY_GET = 4103;
	/** 已经领取过这种类型的礼包 **/
	public static final short CDKEY_TYPE_IS_GET = 4104;


	
	/** 未达到领取条件*/
	public static final short APP_NOT_FINISH = 4201;
	/** 活动已过期 **/
	public static final short APP_CLOSED = 4202;
	/** 活动不存在*/
	public static final short APP_NOT_EXSIT = 4203;
	/** 不能领取*/
	public static final short APP_NOT_REWARD = 4204;
	/** 领取参数错误*/
	public static final short APP_ARGS_ERROR = 4205;
	/** 已经领取 */
	public static final short APP_GET = 4206;
	/** 领取失败 */
	public static final short APP_GET_FAIL = 4207;
	/** 等级不足 */
	public static final short APP_LEVEL_NOT_REACH = 4208;
	/** 精力不足,不能购买 */
	public static final short APP_ENERGY_NOT_ENOUGH = 4209;
	/** 没有此仙人不可购买 */
	public static final short APP_NOT_HAVE_HERO = 4210;
	/** 该仙人今日购买次数已达上限 */
	public static final short HERO_BUY_MAX = 4211;
	
	
	
	/** 不能参与*/
	public static final short DEMON_NOT_JION = 4301;
	/** 已选择阵营*/
	public static final short DEMON_CAMP_HAD_JION = 4302;
	/** 选择阵营错误*/
	public static final short DEMON_CAMP_ERROR = 4303;
	/** 未选择阵营*/
	public static final short DEMON_CAMP_NOT_JOIN = 4304;
	/** 集众降魔未开启*/
	public static final short DEMON_NOT_OPEN = 4305;
	/** 不在同一个难度*/
	public static final short DEMON_IN_DIFFRENT = 4306;
	/** 在同一个阵营*/
	public static final short DEMON_IN_SAME_CAMP = 4307;
	/** 攻击不能使用点券*/
	public static final short DEMON_NOT_USE_TICKET = 4308;
	/** 不能攻击*/
	public static final short DEMON_NOT_ATTACK = 4309;
	/**  降魔积分不足*/
	public static final short DEMON_SOCRE_NOT_ENOUGH = 4310;
	/** BOSS已死亡 */
	public static final short DEMON_MONSTER_DEAD = 4311;
	/** 当前角色已加入集众降魔，不能攻击 */
	public static final short DEMON_JOIN_IN = 4312;
	/** 不在兑换时间内，不能兑换降魔积分*/
	public static final short DEMON_NOT_EXCHANGE = 4313;
	/** 上次未参与*/
	public static final short DEMON_LAST_NOT_JION = 4314;
	
	
	/** 系统邮件不存在 */
	public static final short SYSMAIL_NOT_EXISTS = 4401;
	/** 附件已领取 */
	public static final short SYSMAIL_ATTACH_HAS_RECEIVED = 4402;
	
	
	/** 补给已经领取过了 */
	public static final short SUPPLY_IS_GET = 4501;
	/** 补给不可领取 */
	public static final short SUPPLY_NOT_GET = 4502;
	/** 等级不足,补给不开放 */
	public static final short SUPPLY_ACTOR_LEVEL_NOT_ENOUGH = 4503;
	/** 此功能该平台不开放  */
	public static final short SUPPLY_CHANNEL_NOT_OPEN = 4504;
	/** 补给还未开启 */
	public static final short SUPPLY_NOT_OPEN = 4505;
	
	
	
	/** 好评激活奖励已领取*/
	public static final short PRAISE_ACTIVE_REWARD_HAS_GET = 4601;
	/** 好评奖励已领取*/
	public static final short PRAISE_REWARD_HAS_GET = 4602;
	/** 好评奖励类型错误*/
	public static final short PRAISE_REWARD_TYPE_ERROR = 4603;
	/** 好评未激活*/
	public static final short PRAISE_NOT_ACTIVE = 4604;
	
	/**幸运星次数不够*/
	public static final short LUCK_STAR_NUM_REACH_LIMIT = 4701;
	/** 未达到开放等级 */
	public static final short LUCK_STAR_NOT_OPEN_LEVEL = 4702;
	
	
	/** 未达到领取条件*/
	public static final short HERO_BOOK_REWARD_NOT_FINISH = 4801;
	/** 不能领取*/
	public static final short HERO_BOOK_REWARD_ERROR = 4802;
	/** 角色等级不够*/
	public static final short HERO_BOOK_ACTOR_LEVEL_NOT_ENOUGH = 4803;
	
	
	/** 格子已经全部走完了  **/
	public static final short GRID_MOVE_OVER = 4901;
	/** 发起战斗错误 **/
	public static final short FIGHT_ERROR = 4902;
	/** 活动还未开启 */
	public static final short TREASURE_NOT_OPEN = 4903;
	/** 格子已经领取了 **/
	public static final short TREASURE_GRID_MOVED = 4904;
	/** 兑换物品不足 */
	public static final short TREASURE_EXCHANGE_GOODS_ERROR = 4905;
	/** 迷宫已经关闭 */
	public static final short TREASURE_CLOSE = 4906;
	
	/** 还没有种植 */
	public static final short PLANT_NOT = 5001;
	/** 可收获不可加速 */
	public static final short PLANT_HARVECT = 5002;
	/** 成长中不可收获 */
	public static final short GROW_NOT_HARVECT = 5003;
	/** 现在不能种植 */
	public static final short NOW_NOT_PLANT = 5004;
	/** 已达每日种植上限 */
	public static final short PLANT_MAX = 5005;
	/** 还不可以收获 */
	public static final short NOT_HARVECT = 5006;
	
	/** 金币不足,不能使用 */
	public static final short WELKIN_GOLD_NOT_ENOUGH = 5101;
	/** 还未有玩家上榜 */
	public static final short WELKIN_ACTOR_NOT_RANK = 5102;
	
	/** 还未充值终身卡 */
	public static final short LIFELONG_NOT_RECHARGE = 5201;
	/** 月卡已过期 */
	public static final short NOT_MONTH_CARD = 5202;
	/** 今天月卡奖励已领取 */
	public static final short MONTH_CARD_GET = 5203;
	/** 今天终身卡奖励已领取 */
	public static final short LIFELONG_CARD_GET = 5204;
	/** 年卡已过期 */
	public static final short NOT_YEAR_CARD = 5205;
	/** 今天年卡奖励已领取 */
	public static final short YEAR_CARD_GET = 5206;
	
	
	/**冲级礼包状态码**/
	/** 该礼包已经领取过 */
	public static final short GIFT_HAD_RECEIVED = 5501;
	/** 未达到指定等级不能领取该礼包*/
	public static final short GIFT_LEVEL_NOT_REACH = 5502;
	/** 请先领取前一级等级礼包*/
	public static final short PERVIOUS_GIFT_NOT_RECEIVE = 5503;
	
	/** 头像未解锁,不能使用 */
	public static final short ICON_UNLOCK = 5701;
	/** 边框未解锁,不能使用 */
	public static final short FRAM_UNLOCK = 5702;
	/** 结婚状态不能改性别 */
	public static final short MARRIED_NOT_SET_SEX = 5703;
	
	/** 没到时间不能领取 */
	public static final short RANDOM_REWARD_COOLING = 5801;
	/** 等级不足不能使用 */
	public static final short RANDOM_REWARD_NOT_USE = 5802;
	
	/** 魂不足,不能兑换 */
	public static final short SOUL_NOT_ENOUTH = 5901;
	/** 还未获得过此仙人 */
	public static final short HERO_NOT_GET = 5902;
	
	/** 等级不足,不能参与天梯 */
	public static final short LADDER_LEVEL_NOT_ENOUGHT = 6101;
	/** 可战斗次数不足 */
	public static final short LADDER_FIGHT_NUM_NOT_ENOUGHT = 6102;
	/** 本赛季未获得排名奖励 */
	public static final short LADDER_NOT_REWARD = 6103;
	/** 战斗次数未用完不能购买 */
	public static final short LADDER_FIGHT_NUM_NOT_UESD = 6104;
	/** 赛季还未开启 */
	public static final short LADDER_NOT_OPEN = 6105;
	/** 刷新对手金币不够 */
	public static final short LADDER_GOLD_NOT_ENOUGH = 6106;
	/** 战斗录像已过期或不存在 */
	public static final short FIGHT_VIDEO_NOT_FIND = 6107;
	/** 对手不在挑战列表内 */
	public static final short ACTOR_NOT_IN_FIGHT_LIST = 6108;
	
	/*** 等级不足,不能参与活动*/
	public static final short INVITE_LEVEL_NOT_ENOUGH = 6201;
	/*** 输入的邀请码不存在*/
	public static final short INVITE_CODE_NOT_EXIST = 6202;
	/*** 未邀请过任何好友*/
	public static final short INVITE_NOT_USED = 6203;
	/*** 您不能输入自己的邀请码*/
	public static final short INVITE_CODE_BELONGS_TO_OWN = 6204;
	/*** 您已经被邀请过*/
	public static final short SELF_HAD_RECEIVED = 6205;
	/*** 对应礼包 不存在*/
	public static final short INVITE_REWARD_NOT_EXIST = 6206;
	/*** 未达到礼包领取条件*/
	public static final short REWARD_CONDITIONS_NOT_ACHIEVED = 6207;
	/***已经领取过该礼包*/
	public static final short REWARD_HAD_RECEIVED = 6208;
	/***该邀请码已经使用过*/
	public static final short INVITE_CODE_HAD_USED = 6209;
	
	/** 未满足开启条件 */
	public static final short TRADER_NOT_OPEN = 6301;
	/** 云游商人已经离开 */
	public static final short TRADER_CLOSE = 6302;
	/** 物品数量不足 */
	public static final short TRADER_ITEM_NOT_ENOUGH = 6303;
	/** 金币不足,不能购买 */
	public static final short TRADER_GOLD_NOT_ENOUGH = 6304;
	/** 没有邀请常驻,不能使用此功能 */
	public static final short TRADER_FLUSH_NOT_USE = 6305;
	/** 不能重复邀请入驻 */
	public static final short TRADER_NOT_REPEAT = 6306;
	/** 已经达到最大刷新次数 */
	public static final short TRADER_NOT_FLUSH = 6307;
	
	/** 问答活动未开启 */
	public static final short QUESTIONS_NOT_OPEN = 6401;
	/** 题目已经回答过了 */
	public static final short QUESTIONS_ANSWER = 6402;
	/** 没有这个题目 */
	public static final short QUESTIONS_NOT_EXSIT = 6403;
	/** 答题次数已用完 */
	public static final short QUESTIONS_NOT_ANSWER_NUM = 6404;
	
	/** 聚宝盆活动已关闭 */
	public static final short BASIN_NOT_OPEN = 6501;
	/** 还未达到该奖励领取条件 */
	public static final short BASIN_NOT_GET = 6502;
	
	
	/** 已结婚不能重复结婚 */
	public static final short HAS_MARRIED = 6601;
	/** 已请求过结婚*/
	public static final short HAS_REQUEST_MARRY = 6602;
	/** 接受请求已超时*/
	public static final short MARRY_ACCEPT_TIMEOUT = 6603;
	/** 未结婚 不能离婚*/
	public static final short NOT_MARRY = 6604;
	/** 请求结婚达到上限*/
	public static final short HAS_REQUEST_MARRY_MAX = 6605;
	/** 已送过礼物*/
	public static final short HAS_GIFT = 6606;
	/** 没有礼物*/
	public static final short NOT_HAS_GIFT = 6607;
	/** 结婚物品不足*/
	public static final short MARRY_USE_GOODS_NOT_ENOUGH = 6608;
	/** 结婚冷却中，今日不能再结婚*/
	public static final short MARRY_COOL = 6609;
	/** 性别相同 不能结婚*/
	public static final short MARRY_SEX_SAME = 6610;
	/** 结婚等级不足 */
	public static final short MARRY_LEVEL_NOT_ENOUGH = 6611;
	/** 还未结婚不能参与排行榜 */
	public static final short NOT_RANK = 6612;
	/** 对方已离婚不能挑战 */
	public static final short TARGET_NOT_RANK = 6613;
	/** 挑战次数未使用不能补满次数 */
	public static final short FIGHT_NUM_NOT_USE = 6614;
	/** 今天挑战次数补满已购买完 */
	public static final short FLUSH_FIGHT_NUM_MAX = 6615;
	/** 没有结婚不能使用仙侣商城 */
	public static final short NOT_SHOP = 6616;
	/** 商店刷新次数已达上限 */
	public static final short FLUSH_SHOP_MAX = 6616;
	/** 还未结婚不能使用仙侣合作 */
	public static final short NOT_MONSTER = 6617;
	/** 还未解锁boss不能挑战*/
	public static final short BOSS_LOCKED = 6618;
	/** boss已经被杀死不能挑战*/
	public static final short BOOS_KILLED = 6619;
	/** boss挑战次数不足 */
	public static final short FIGHT_NUM_ENOUGH = 6620;
	/** boss已经解锁 */
	public static final short BOSS_UNLOCK = 6621;
	/** boss未解锁不能刷新次数 */
	public static final short BOSS_LOCKED_NOT_FLUSH = 6622;
	/** boss已经被杀死不能刷新次数 */
	public static final short BOSS_DIE_NOT_FLUSH = 6623;
	/** 还有挑战次数没用玩不能刷新 */
	public static final short NOT_FLUSH = 6624;
	/** 物品不足不能购买 */
	public static final short LOVE_SHOP_GOODS_NOT_ENOUGH = 6625;
	/** 物品已经购买完 */
	public static final short LOVE_SHOP_NOT_ENOUGH = 6626;
	/** 排行榜挑战次数不足 */
	public static final short LOVE_RANK_FIGHT_NOT_ENOUGH = 6627;
	
	/** 天神下凡活动未开启*/
	public static final short DEITY_DESCEND_NOT_OPEN = 6701;
	/** 仙人未达到条件不能领取*/
	public static final short DEITY_DESCEND_CAN_NOT_RECEIVE = 6702;
	/** 仙人已经领取*/
	public static final short DEITY_DESCEND_HAD_RECEIVED = 6703;
	
	
	/***错误的装备id*/
	public static final short ERROR_EQUIP_UUID = 6801;
	/***错误的打造装备*/
	public static final short ERROR_BUILD_EQUIP = 6802;
	/***装备不存在*/
	public static final short EQUIP_NOT_EXIST = 6803;
	/***装备打造失败*/
	public static final short BUILD_EQUIP_FIAL = 6804;
	/***打造次数已经用完*/
	public static final short BUILD_COUNT_HAD_USED = 6805;

	/***等级不足不能参与在线礼包*/
	public static final short ONLINE_GIFT_LEVEL_NOT_ENOUGH = 6901;
	/***在线时间不足,不能领取在线礼包*/
	public static final short ONLINE_GIFT_CAN_NOT_RECEIVE = 6902;
	/***在线礼包已经全部领取*/
	public static final short ONLINE_GIFT_ALL_RECEIVED = 6903;
	
	
	/***欢乐摇奖活动未开始*/
	public static final short ERNIE_NOT_OPEN = 7001;
	/***欢乐摇奖活动兑换时间已过,系统将自动回收物品*/
	public static final short ERNIE_EXCHANGE_RAN_OUT = 7002;
	/***输入的电话号码不能为空*/
	public static final short PHONE_NUM_CAN_NOT_EMPTY = 7003;
	/***输入的电话号码错误*/
	public static final short PHONE_NUM_ERROR = 7004;
	/***输入的电话号码不能有奇怪的东西哦*/
	public static final short PHONE_NUM_FORMAT_ERROR = 7005;

	/**攻击年兽需要等级不足*/
	public static final short BEAST_LEVEL_LIMIT = 7101;
	/**不在活动时间内*/
	public static final short BEAST_TIME_OUT = 7102;
	/**攻击年兽冷却时间中*/
	public static final short BEAST_IN_CD = 7103;
	/**年兽已经死亡*/
	public static final short BEAST_BOSS_DEAD = 7104;
	/**年兽不存在*/
	public static final short BEAST_BOSS_NOT_BORN = 7105;
	
	/** vip箱子已经领取过了 */
	short VIP_BOX_IS_GET = 7201;
	/** vip箱子已经满了不能领取 */
	short VIP_BOX_MAX = 7202;
	/** vip箱子不存在不能开启 */
	short VIP_BOX_NOT_ENOUGH = 7203;
	/** vip箱子活动已经关闭 */
	short VIP_BOX_CLOSED = 7204;
	/** 每日vip箱子开启数量已达上限,不能再开启 */
	short VIP_BOX_OPEN_MAX = 7205;
	/** vip等级不足,不能领取 */
	short VIP_BOX_VIP_LEVEL_NOT_ENOUGH = 7206;
	
	
	/** 贡献点不足以兑换 */
	public static final short CROSS_BATTLE_POINT_NOT_ENOUGH = 10001;
	/** 玩家已死亡*/
	public static final short CROSS_BATTLE_ACTOR_DEAD = 10002;
	/** 跨服战未开始*/
	public static final short CROSS_BATTLE_NOT_START = 10003;
	/** 服务器参赛条件不足 */
	public static final short CROSS_BATTLE_CONDITION_NOT_REACH = 10004;
	/** 玩家等级未满足参赛条件 */
	public static final short CROSS_BATTLE_LEVEL_NOT_ENOUGH = 10005;
	/** 排行榜排名未达到参赛资格 */
	public static final short CROSS_BATTLE_POWER_NOT_ENOUGH = 10006;
	/** 下次攻击还在冷却中 */
	public static final short NEXT_ATTACK_COOLDOWN = 10007;
	/** 掌教复活中请稍后 */
	public static final short CROSS_BATTLE_REFIVE = 10008;
	/** 对战数据不存在*/
	public static final short CROSS_DATA_NOT_EXSIT = 10009;
	/** 跨服战已开始*/
	public static final short CROSS_BATTLE_START = 10010;
	/** 世界服不存在*/
	public static final short WORLD_SERVER_NOT_EXSIT = 10011;
	/** 攻击间隔中*/
	public static final short CROSS_BATTLE_CD = 10012;
	/** 本服赛季未获得奖励资格 */
	public static final short CROSS_BATTLE_SERVER_NOT_ENOUGH = 10013;
	/** 本赛季已经领取过奖励 */
	public static final short CROSS_BATTLE_REWARD_GET = 10014;
	/** 复活保护中*/
	public static final short CROSS_BATTLE_PROTECT = 10015;
	
	
	/********************装备突破错误码****************************/
	/** 该装备不可提炼*/
	public static final short EQUIP_CAN_NOT_CONVERT = 10201;
	/** 该碎片不可提炼*/
	public static final short PIECE_CAN_NOT_CONVERT = 10202;
	/** 该装备不可突破*/
	public static final short EQUIP_CAN_NOT_DEVELOP = 10203;
	/** 装备突破失败*/
	public static final short EQUIP_DEVELOP_FAIL = 10204;
}
