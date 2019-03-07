package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RandomExprRewardObject;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.battle.constant.WinLevel;

/**
 * <pre>
 * 	storyId：			所属的故事id
 *	battleId:			战场Id(副本)
 *	battleOrder:		副本顺序
 *	name:				战场名称
 *	costEnergy:			消耗精力值
 *	awardReputation:	大败1_声望值|失败2_声望值|惜败3_声望值|小胜4_声望值|中胜5_声望值|大胜6_声望值
 *	awardGoods:			奖励物品 格式:大败1_物品id_掉落概率|失败2_物品id_掉落概率|惜败3_物品id_掉落概率|小胜4_物品id_掉落概率|中胜5_物品id_掉落概率|大胜6_物品id_掉落概率
 *	awardHeroExp:		奖励所有仙人经验值   格式： 大败1_经验值|失败2_经验值|惜败3_经验值|小胜4_经验值|中胜5_经验值|大胜6_经验值
 *	monster:			战场里的怪物(最大9个否则服务器启不来) 格式:怪物id_位置(左上角1-9)|怪物id_位置(左上角1-9)
 *	text:				战场的一些文字提示
 * </pre>
 * 
 * @author vinceruan
 */
@DataFile(fileName = "battleConfig")
public class BattleConfig implements ModelAdapter {
	@FieldIgnore
	private static final Log LOGGER = LogFactory.getLog(BattleConfig.class);
			
	/**
	 * 战场id
	 */
	private int battleId;
	
	/**
	 * 所属的故事id
	 */
	private int storyId;
	
	/**
	 * 地图id
	 */
	private int mapId;

	/**
	 * 战场顺序
	 */
	private int dependBattle;
	
	/**
	 * 战场类型:0支线(合作关卡)  1主线
	 */
	private int battleType;

	/**
	 * 战场名称
	 */
	private String name;

	/**
	 * 消耗活力值
	 */
	private int costVit;

	/**
	 * 关卡对应的标准角色等级(高于该等级通关时获得的声望有所减少)
	 */
	private int defaultActorLevel;
	
	/**
	 * <pre>
	 * 奖励角色声望 格式: 大败1_声望值|失败2_声望值|惜败3_声望值|小胜4_声望值|中胜5_声望值|大胜6_声望值
	 * </pre>
	 */
	private String awardReputation;
	
	/**
	 * <pre>
	 * 奖励金币,格式:大败1_金币值|失败2_金币值|惜败3_金币值|小胜4_金币值值|中胜5_金币值|大胜6_金币值
	 * </pre>
	 */
	private String awardGold;
	
	/**
	 * <pre>
	 * 首次胜利奖励,类型取值(1.金币 2.物品 3.装备  4.魂魄 )格式: 奖励类型_物品id_数量.  (如果奖励金币物品id填0即可)
	 * </pre>
	 */
	private String firstWinAward;
	
	/**
	 * <pre>
	 * 奖励类型, 取值(1.金币 2.物品 3.装备  4.魂魄 ) 格式： 奖励类型_百分比|奖励类型_百分比
	 * </pre>
	 */
	private String awardType;

	/**
	 * <pre>
	 * 奖励物品 格式:大败1_物品id_数量_掉落概率|失败2_物品id_数量_掉落概率|惜败3_物品id_数量_掉落概率|小胜4_物品id_数量_掉落概率|中胜5_物品id_数量_掉落概率|大胜6_物品id_数量_掉落概率
	 * </pre>
	 */
	private String awardGoods;

	/**
	 * <pre>
	 * 奖励所有仙人经验值   格式：大败1_经验值|失败2_经验值|惜败3_经验值|小胜4_经验值|中胜5_经验值|大胜6_经验值
	 * </pre>
	 */
	private String awardHeroExp;
	
	/**
	 * <pre>
	 * 大败1_物品id_数量_掉落概率|失败2_物品id_数量_掉落概率|惜败3_物品id_数量_掉落概率|小胜4_物品id_数量_掉落概率|中胜5_物品id_数量_掉落概率|大胜6_物品id_数量_掉落概率
	 * </pre>
	 */
	private String awardEquips;
	
	/**
	 * <pre>
	 * 大败1_仙人id_数量_掉落概率|失败2_仙人id_数量_掉落概率|惜败3_仙人id_数量_掉落概率|小胜4_仙人id_数量_掉落概率|中胜5_仙人id_数量_掉落概率|大胜6_仙人id_数量_掉落概率
	 * </pre>
	 */
	private String awardHeroSoul;
	
	/**
	 * 奖励给盟友的声望值,格式:大败1_声望值|失败2_声望值|惜败3_声望值|小胜4_声望值|中胜5_声望值|大胜6_声望值
	 */
	private String awardAllyReputation;
	
	/**
	 * 奖励给盟友的物品 格式:大败1_物品id_数量_掉落概率|失败2_数量_物品id_掉落概率|惜败3_物品id_数量_掉落概率|小胜4_物品id_数量_掉落概率|中胜5_物品id_数量_掉落概率|大胜6_物品id_数量_掉落概率
	 */
	private String awardAllyGoods;

	/**
	 * <pre>
	 * 战场里的怪物(最大9个否则服务器启不来) 格式:怪物id_位置(左上角1-9)|怪物id_位置(左上角1-9)
	 * </pre>
	 */
	private String monster;
	
	/**
	 * 怪物方的气势值
	 */
	public int morale;
	
	
	/**
	 * 需要掌教等级
	 */
	private int needActorLevel;
	
	/**
	 * 额外的奖励
	 */
	private String extReward;
	
	/**
	 * 扫荡的奖励
	 */
	private String battleReward;

	/**
	 * <pre>
	 * 怪物列表
	 * 格式是:
	 * Map<LineupGridIndex,HeroId>
	 * </pre>
	 */
	@FieldIgnore
	private Map<Integer, Integer> monsterList = new HashMap<Integer, Integer>();

	/**
	 * <pre>
	 * 声望奖励
	 * 格式是:
	 * Map<WinLevel,ReputationCount>
	 * </pre>
	 */
	@FieldIgnore
	private Map<WinLevel, Integer> awardReputationMap = new HashMap<WinLevel, Integer>();
	
	
	/**
	 * <pre>
	 * 奖励的类型
	 * key=类型
	 * value=概率
	 * </pre>
	 */
	@FieldIgnore
	private Map<Integer, Integer> awardTypeMap;
	/**
	 * <pre>
	 * 奖励金币集合
	 * </pre>
	 */
	@FieldIgnore
	private Map<WinLevel,Integer> awardGoldMap = new HashMap<WinLevel,Integer>();

	/**
	 * <pre>
	 * 仙人经验奖励
	 * 格式是:
	 * Map<WinLevel,ExpCount>
	 * </pre>
	 */
	@FieldIgnore
	private Map<WinLevel, Integer> awardHeroExpMap = new HashMap<WinLevel, Integer>();
	
	/**
	 * <pre>
	 * 物品掉落奖励
	 * 格式是:
	 * Map<WinLevel,Map<GoodId,Rate>>;
	 * </pre>
	 */
	@FieldIgnore
	private Map<WinLevel, List<AwardGoodsConfig>> awardGoodsMap = new HashMap<>();
	
	/**
	 * 奖励装备集合
	 *  <pre>
	 * 格式是:
	 * Map<WinLevel,Map<EquipId,Rate>>;
	 * </pre>
	 */
	@FieldIgnore
	private Map<WinLevel, List<AwardGoodsConfig>> awardEquipMap = new HashMap<>();
	
	/**
	 * 奖励英雄魂魄集合
	 *  <pre>
	 * 物品掉落奖励
	 * 格式是:
	 * Map<WinLevel,Map<HeroSoulId,Rate>>;
	 * </pre>
	 */
	@FieldIgnore
	private Map<WinLevel, List<AwardGoodsConfig>> awardHeroSoulMap = new HashMap<>();
	
	/**
	 * 奖励给盟友的声望(合作关卡才有效)
	 */
	@FieldIgnore
	private Map<WinLevel, Integer> awardAllyReputationMap = new HashMap<WinLevel, Integer>();
	
	/**
	 * 奖励的盟友的物品集合(合作关卡才有效)
	 */
	@FieldIgnore
	private Map<WinLevel, List<AwardGoodsConfig>> awardAllyGoodsMap = new HashMap<>();
	
	@FieldIgnore
	private AwardItemConfig firstWinAwardConfig;
	
	@FieldIgnore
	public List<RandomExprRewardObject> extRewardList = new ArrayList<>();
	
	@FieldIgnore
	public List<RewardObject> battleRewardList = new ArrayList<>();

	@Override
	public void initialize() {
		//将怪物字符串转化成实体列表
		parseMonsters();
		
		//将声望奖励字符串转化成实体
		parseReputation(this.awardReputation, this.awardReputationMap);
		parseReputation(this.awardAllyReputation, this.awardAllyReputationMap);
		
		//金币转换为maps
		parseGold();
		
		//将物品掉落字符串转换成实体
		parseLootItem(this.awardGoods, this.awardGoodsMap);
		parseLootItem(this.awardAllyGoods, this.awardAllyGoodsMap);
		
		//将经验奖励字符串转换成实体
		parseExp();
		
		//将魂魄奖励字符串转换成实体
		parseHeroSoul();
		
		//将装备奖励字符串转换成实体
		parseEquip();
		
		awardTypeMap = StringUtils.delimiterString2IntMap(awardType);
		
		//首次胜利奖励
		parseFirstAward();
		
		//额外奖励
		parserExtReward();
		
		//扫荡奖励
		parserBattleReward();
		
		this.monster = null;
		this.awardReputation = null;
		this.awardAllyReputation = null;
		this.awardGoods = null;
		this.awardAllyGoods = null;
		this.awardHeroExp = null;
		this.awardHeroSoul = null;
		this.awardEquips = null;
		this.awardType = null;
		this.firstWinAward = null;
		this.extReward = null;
		this.battleReward = null;
	}

	private void parserBattleReward() {
		List<String[]> list = StringUtils.delimiterString2Array(battleReward);
		for(String[] str:list){
			RewardObject rewardObject = RewardObject.valueOf(str);
			battleRewardList.add(rewardObject);
		}
	}

	private void parserExtReward() {
		List<String[]> list = StringUtils.delimiterString2Array(extReward);
		for(String[] str:list){
			RandomExprRewardObject rewardObject = new RandomExprRewardObject();
			rewardObject.rewardType = RewardType.getType(Integer.valueOf(str[0]));
			rewardObject.id = Integer.valueOf(str[1]);
			rewardObject.num = Integer.valueOf(str[2]);
			rewardObject.rate = Integer.valueOf(str[3]);
			extRewardList.add(rewardObject);
		}
	}

	/**
	 * 
	 */
	protected void parseFirstAward() {
		List<String> firstAwards = StringUtils.delimiterString2List(firstWinAward, Splitable.ATTRIBUTE_SPLIT);
		if (firstAwards != null && firstAwards.size() > 0) {
			Assert.isTrue(firstAwards.size() == 3, "首次胜利只能奖励一种类型");
			AwardItemConfig config = new AwardItemConfig();
			config.setAwardType(Integer.valueOf(firstAwards.get(0)));
			config.setGoodsId(Integer.valueOf(firstAwards.get(1)));
			config.setNum(Integer.valueOf(firstAwards.get(2)));
			firstWinAwardConfig = config;
		}
	}
	
	private void parseHeroSoul() {
		List<String> list = StringUtils.delimiterString2List(this.awardHeroSoul, Splitable.ELEMENT_SPLIT);
		for(String str : list) {
			List<String> winLvAwards = StringUtils.delimiterString2List(str, Splitable.ATTRIBUTE_SPLIT);
			if (winLvAwards.size() != 0 && winLvAwards.size() < 4) {
				LOGGER.error("BattleConfig.xml配置错误,正确格式:胜利类型_魂魄id_数量_概率, 错误字符串:" + awardHeroSoul);
			}
			WinLevel level = WinLevel.getByCode(Integer.valueOf(winLvAwards.get(0)));
			awardHeroSoulMap.put(level, new ArrayList<AwardGoodsConfig>());			
			for(int i = 1; i < winLvAwards.size(); i+=3) {
				//魂魄id
				int heroId = Integer.valueOf(winLvAwards.get(i));
				//数量
				int num = Integer.valueOf(winLvAwards.get(i+1));
				//掉落概率
				int rate = Integer.valueOf(winLvAwards.get(i+2));
				awardHeroSoulMap.get(level).add(new AwardGoodsConfig(heroId, num, rate));
			}
		}
	}
	
	private void parseEquip() {
		List<String> list = StringUtils.delimiterString2List(this.awardEquips, Splitable.ELEMENT_SPLIT);	
		for(String str : list) {
			List<String> winLvAwards = StringUtils.delimiterString2List(str, Splitable.ATTRIBUTE_SPLIT);
			
			if (winLvAwards.size() !=0 && winLvAwards.size() < 4) {
				LOGGER.error("BattleConfig.xml配置错误,正确格式:胜利类型_装备id_数量_概率, 错误字符串:" + awardEquips);
			}
			
			WinLevel level = WinLevel.getByCode(Integer.valueOf(winLvAwards.get(0)));
			
			awardEquipMap.put(level, new ArrayList<AwardGoodsConfig>());
			for(int i = 1; i < winLvAwards.size(); i+=3) {
				//装备id
				int equipId = Integer.valueOf(winLvAwards.get(i));
				//数量
				int num = Integer.valueOf(winLvAwards.get(i+1));
				//掉落概率
				int rate = Integer.valueOf(winLvAwards.get(i+2));
				awardEquipMap.get(level).add(new AwardGoodsConfig(equipId, num, rate));	
			}
		}
	}

	/**
	 * 将经验奖励字符串转换成实体
	 */
	private void parseExp() {
		if (StringUtils.isBlank(this.awardHeroExp)) {
			LOGGER.error("BattleConfig.xml配置错误,awardHeroExp字段不能为空");
		}
		 
		List<String> list = StringUtils.delimiterString2List(this.awardHeroExp, Splitable.ELEMENT_SPLIT);
		for (String item : list) {
			List<String> expInfo = StringUtils.delimiterString2List(item, Splitable.ATTRIBUTE_SPLIT);
			//胜利类型
			int level = Integer.valueOf(expInfo.get(0));
			//经验值
			int expCount = Integer.valueOf(expInfo.get(1));
			WinLevel lv = WinLevel.getByCode(level);
			this.awardHeroExpMap.put(lv, expCount);
		}
	}

	/**
	 * 将物品掉落字符串转换成实体
	 */
	private void parseLootItem(String goods, Map<WinLevel, List<AwardGoodsConfig>> map) {		
		List<String> list = StringUtils.delimiterString2List(goods, Splitable.ELEMENT_SPLIT);//拆分"|"分隔
		for(String str : list) {
			List<String> winLvAwards = StringUtils.delimiterString2List(str, Splitable.BETWEEN_ITEMS);//拆分","分隔
			for (String item : winLvAwards) {
				List<String> child = StringUtils.delimiterString2List(item, Splitable.ATTRIBUTE_SPLIT);//拆分"_"分隔
				WinLevel level = WinLevel.getByCode(Integer.valueOf(child.get(0)));
				List<AwardGoodsConfig> goodsList = null;
				if (map.containsKey(level)) {
					goodsList = map.get(level);
				} else {
					goodsList = new ArrayList<>();
					map.put(level, goodsList);
				}
				
				for(int i = 1; i < child.size(); i+=3) {
					//物品id
					int itemId = Integer.valueOf(child.get(i));
					//数量
					int num = Integer.valueOf(child.get(i+1));
					//掉落概率
					int rate = Integer.valueOf(child.get(i+2));
					goodsList.add(new AwardGoodsConfig(itemId, num, rate));
				}
			}
		}
	}

	/**
	 * 将声望奖励字符串转化成实体
	 */
	private void parseReputation(String str, Map<WinLevel, Integer> map) {		 
		List<String> list = StringUtils.delimiterString2List(str, Splitable.ELEMENT_SPLIT);
		for (String item : list) {			
			List<String> reputationInfo = StringUtils.delimiterString2List(item, Splitable.ATTRIBUTE_SPLIT);			
			//胜利的类型
			int level = Integer.valueOf(reputationInfo.get(0));
			//奖励声望值
			int count = Integer.valueOf(reputationInfo.get(1));
			WinLevel winLv = WinLevel.getByCode(level);
			map.put(winLv, count);
		}
	}
	
	/**
	 * 将金币奖励字符串转化成实体
	 */
	private void parseGold() {		
		List<String> list = StringUtils.delimiterString2List(this.awardGold, Splitable.ELEMENT_SPLIT);
		for (String item : list) {			
			List<String> goldInfo = StringUtils.delimiterString2List(item, Splitable.ATTRIBUTE_SPLIT);			
			//胜利的类型
			int level = Integer.valueOf(goldInfo.get(0));
			//奖励金币
			int nums = Integer.valueOf(goldInfo.get(1));
			WinLevel winLv = WinLevel.getByCode(level);
			this.awardGoldMap.put(winLv, nums);
		}
	}

	/**
	 * 将怪物字符串转化成实体列表
	 */
	private void parseMonsters() {
		List<String> list = StringUtils.delimiterString2List(monster, Splitable.ELEMENT_SPLIT);		
		for (String item : list) {			
			List<String> attrs = StringUtils.delimiterString2List(item,Splitable.ATTRIBUTE_SPLIT);
			//仙人id
			int heroId = Integer.valueOf(attrs.get(0));
			//仙人在阵型中的位置
			int gridIndex = Integer.valueOf(attrs.get(1));
			monsterList.put(gridIndex, heroId);
		}
	}

	public int getStoryId() {
		return storyId;
	}

	public int getBattleId() {
		return battleId;
	}

	public String getName() {
		return name;
	}

	public String getAwardGoods() {
		return awardGoods;
	}

	public String getMonster() {
		return monster;
	}

	public Map<Integer, Integer> getMonsterList() {
		return monsterList;
	}

	/**
	 * 获取奖励的声望值
	 * @param winLevel	胜负的等级
	 * @return
	 */
	public int getAwardReputation(WinLevel winLevel) {
		return awardReputationMap.get(winLevel);
	}
	
	public Map<WinLevel,Integer> getAwardGold() {
		return awardGoldMap;
	}

	/**
	 * 获取上阵仙人经验
	 * @param winLevel	胜负的等级
	 * @return
	 */
	public int getAwardHeroExp(WinLevel winLevel) {
		return awardHeroExpMap.get(winLevel);
	}

	

	public String getAwardEquips() {
		return awardEquips;
	}

	public int getDependBattle() {
		return dependBattle;
	}
	
	public int getMapId() {
		return mapId;
	}

	public int getBattleType() {
		return battleType;
	}

	public int getCostVit() {
		return costVit;
	}

	public Map<Integer, Integer> getAwardTypeMap() {
		return awardTypeMap;
	}

	public Map<WinLevel, Integer> getAwardAllyReputationMap() {
		return awardAllyReputationMap;
	}

	public int getDefaultActorLevel() {
		return defaultActorLevel;
	}

	public AwardItemConfig getFirstWinAwardConfig() {
		return firstWinAwardConfig;
	}

	public Map<WinLevel, Integer> getAwardGoldMap() {
		return awardGoldMap;
	}

	public Map<WinLevel, List<AwardGoodsConfig>> getAwardGoodsMap() {
		return awardGoodsMap;
	}

	public Map<WinLevel, List<AwardGoodsConfig>> getAwardEquipMap() {
		return awardEquipMap;
	}

	public Map<WinLevel, List<AwardGoodsConfig>> getAwardHeroSoulMap() {
		return awardHeroSoulMap;
	}

	public Map<WinLevel, List<AwardGoodsConfig>> getAwardAllyGoodsMap() {
		return awardAllyGoodsMap;
	}
	
	public int getNeedActorLevel() {
		return needActorLevel;
	}
}
