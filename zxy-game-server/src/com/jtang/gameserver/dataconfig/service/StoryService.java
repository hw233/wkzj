package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.model.RandomRewardObject;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.BattleConfig;
import com.jtang.gameserver.dataconfig.model.StoryAppConfig;
import com.jtang.gameserver.dataconfig.model.StoryBattleRecordConfig;
import com.jtang.gameserver.dataconfig.model.StoryConfig;
import com.jtang.gameserver.dataconfig.model.StoryFightConfig;

@Component
public class StoryService extends ServiceAdapter {
//	private static final Logger LOGGER = LoggerFactory.getLogger(SnatchService.class);
	
	/**
	 * 战场配置,格式是Map<BatttleId, BattleConfig>
	 */
	static Map<Integer, BattleConfig> BATTLE_CONFIG_MAP = new HashMap<Integer, BattleConfig>();
	
	/**
	 * 故事战场对应配置
	 */
	static Map<Integer,List<BattleConfig>> STORY_BATTLE_MAP = new HashMap<>();
	
	/**
	 * 故事配置,格式是Map<StoryId, BattleConfig>
	 */
	static Map<Integer, StoryConfig> STORY_CONFIG_MAP = new HashMap<Integer, StoryConfig>();
	
	static StoryFightConfig STORY_FIGHT_CONFIG = new StoryFightConfig();
	
	static Map<Integer,StoryBattleRecordConfig> BATTLE_RECORD_MAP = new HashMap<>();
	
	/** 闯关活动配置  */
	private static StoryAppConfig STORY_APP = new StoryAppConfig();
	
	@Override
	public void clear() {
		BATTLE_CONFIG_MAP.clear();
		STORY_CONFIG_MAP.clear();
		STORY_BATTLE_MAP.clear();
		STORY_FIGHT_CONFIG = new StoryFightConfig();
		BATTLE_RECORD_MAP.clear();
		STORY_APP = new StoryAppConfig();
	}
	
	@Override
	public void initialize() {
		List<BattleConfig> battleList = dataConfig.listAll(this, BattleConfig.class);
		List<StoryConfig> storyList = dataConfig.listAll(this, StoryConfig.class);
		
		//将所有的战场配置缓存
		for (BattleConfig conf : battleList) {
			BATTLE_CONFIG_MAP.put(conf.getBattleId(), conf);
		}
		
		//将所有的故事配置缓存.
		for (StoryConfig conf : storyList) {
			STORY_CONFIG_MAP.put(conf.getStoryId(), conf);		
		}
		
		for(BattleConfig conf : battleList){
			List<BattleConfig> list = null;
			if(STORY_BATTLE_MAP.containsKey(conf.getStoryId())){
				list = STORY_BATTLE_MAP.get(conf.getStoryId());
			}else{
				list = new ArrayList<>();
				STORY_BATTLE_MAP.put(conf.getStoryId(), list);
			}
			list.add(conf);
		}
		
		//为各个故事注入战场配置(按战场顺序注入).
//		for (BattleConfig conf : battleList) {
//			StoryConfig story = STORY_CONFIG_MAP.get(conf.getStoryId());
//			if (story == null) {
//				LOGGER.error(String.format("BattleConfig里面的故事ID在StoryConfig里面找不到, " +
//						"BattleId=%d, StoryId=%d", conf.getBattleId(), conf.getStoryId()));
//			}
//			story.getBattleList().add(conf);
//		}
		
		List<StoryFightConfig> list = dataConfig.listAll(this, StoryFightConfig.class);
		STORY_FIGHT_CONFIG = list.get(0);
		
		List<StoryBattleRecordConfig> recordList = dataConfig.listAll(this, StoryBattleRecordConfig.class);
		for(StoryBattleRecordConfig config:recordList){
			BATTLE_RECORD_MAP.put(config.battleId, config);
		}
		
		List<StoryAppConfig> appList = dataConfig.listAll(this, StoryAppConfig.class);
		for(StoryAppConfig appConfig:appList){
			STORY_APP = appConfig;
		}
	}
	
	/**
	 * 根据战场id获取战场配置
	 * @param battleId
	 * @return
	 */
	public static BattleConfig get(int battleId) {
		return BATTLE_CONFIG_MAP.get(battleId);
	}
	
	/**
	 * 获取故事配置
	 * @param storyId
	 * @return
	 */
	public static StoryConfig getStory(int storyId) {
		return STORY_CONFIG_MAP.get(storyId);
	}
	
	/**
	 * 获取所有的战场配置
	 * @return
	 */
	public static Collection<BattleConfig> getAllBattle() {
		return BATTLE_CONFIG_MAP.values();
	}
	
	/**
	 * 获取主线关卡
	 * @param storyId
	 * @return
	 */
	public static List<BattleConfig> getMainLineBattle(int storyId) {
		List<BattleConfig> list = new ArrayList<>();
		for(BattleConfig battle : STORY_BATTLE_MAP.get(storyId)) {
			if (battle.getBattleType() == 1 ) {
				list.add(battle);
			}
		}
		return list;
	}
	
	/**
	 * 是否为主线最后一个关卡？
	 * @param battleId
	 * @return
	 */
	public static boolean isLastMainBattleInStory(int battleId) {
		BattleConfig battle = get(battleId);
		List<BattleConfig> list = getMainLineBattle(battle.getStoryId());
		BattleConfig lastMainBattle = list.get(list.size() - 1); 
		return lastMainBattle.getBattleId() == battleId;
	}

	/**
	 * 获取前一个关卡
	 * @param battleId
	 * @return
	 */
	public static BattleConfig getBeforBattle(int battleId) {
		BattleConfig battle = get(battleId);
		return get(battle.getDependBattle());
	}

	public static BattleConfig getNextBattle(int battleId) {
		BattleConfig battle = get(battleId);
		List<BattleConfig> list = STORY_BATTLE_MAP.get(battle.getStoryId());
		for(BattleConfig battleConfig:list){
			if(battleConfig.getDependBattle() == battleId){
				return battleConfig;
			}
		}
		return null;
	}
	
//	public static Map<Integer, MonsterVO> getMonsters(int battleId) {
//		BattleConfig battle = get(battleId);
//		Map<Integer, MonsterVO> monsterMap = new HashMap<>();
//		Map<Integer, Integer> list = battle.getMonsterList();
//		for (Entry<Integer, Integer> entry : list.entrySet()) {
//			int gridIndex = entry.getKey();
//			int monsterCfgId = entry.getValue();
//			MonsterVO monsterVO = MonsterService.getMonsterVO(monsterCfgId);
//			monsterMap.put(gridIndex, monsterVO);
//		}
//		return monsterMap;
//	}
	
	public static StoryFightConfig getFightConfig(){
		return STORY_FIGHT_CONFIG;
	}
	
	public static StoryBattleRecordConfig getLeastReward(int battleId){
		return BATTLE_RECORD_MAP.get(battleId);
	}

	public static boolean hasGoods(int battleId) {
		return BATTLE_RECORD_MAP.containsKey(battleId);
	}
	
	public static List<RewardObject> getAppReward(int nowTime){
		List<RewardObject> list = new ArrayList<>();
		if(STORY_APP.start <= nowTime && nowTime <= STORY_APP.end){
			List<RandomRewardObject> rewardList = STORY_APP.rewardList;
			if(STORY_APP.propertionType == 1){//几率和1000,多随一,必定有一个
				Map<Integer,Integer> map = new HashMap<>();
				for (int i = 0; i < rewardList.size(); i++) {
					map.put(i, rewardList.get(i).rate);
				}
				Integer index = RandomUtils.randomHit(1000, map);
				list.add(rewardList.get(index));
			}else if(STORY_APP.propertionType == 2){//几率和不为1000,多随一,有几率没有
				int rate = RandomUtils.nextInt(0, 1000);
				int propertion = 0;
				for(RandomRewardObject randomRewardObject : rewardList){
					propertion += randomRewardObject.rate;
					if(rate <= propertion){
						list.add(randomRewardObject);
						return list;
					}
				}
			}else if(STORY_APP.propertionType == 3){//几率和都为1000,所有都给
				list.addAll(STORY_APP.rewardList);
			}
		}
		return list;
	}
}
