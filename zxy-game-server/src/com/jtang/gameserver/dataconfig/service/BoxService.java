package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.BoxConfig;
import com.jtang.gameserver.dataconfig.model.BoxEquipConfig;
import com.jtang.gameserver.dataconfig.model.BoxGoodsConfig;
import com.jtang.gameserver.module.goods.type.BoxRewardType;

@Component
public class BoxService extends ServiceAdapter {
	
	private static Map<Integer,BoxConfig> BOX_CONFIG_MAP = new HashMap<>();
	
	/** key 宝箱id value <key:id>**/
	private static Map<Integer,List<BoxGoodsConfig>> BOX_REWARD_MAP = new HashMap<>();
	
	/** key 宝箱id value <key:类型  value [key:装备类型]> */
	private static Map<Integer,Map<Integer, Map<Integer,List<BoxEquipConfig>>>> BOX_EQUIP_MAP = new HashMap<>();

	@Override
	public void clear() {
		BOX_CONFIG_MAP.clear();
		BOX_REWARD_MAP.clear();
		BOX_EQUIP_MAP.clear();
	}

	@Override
	public void initialize() {
		List<BoxConfig> boxList = dataConfig.listAll(this, BoxConfig.class);
		for(BoxConfig config : boxList){
			BOX_CONFIG_MAP.put(config.boxId, config);
		}
		
		List<BoxGoodsConfig> boxGoodsList = dataConfig.listAll(this, BoxGoodsConfig.class);
		for(BoxGoodsConfig config : boxGoodsList){
			if(BOX_REWARD_MAP.containsKey(config.boxId)){
				BOX_REWARD_MAP.get(config.boxId).add(config);
			}else{
				List<BoxGoodsConfig> list = new ArrayList<>();
				list.add(config);
				BOX_REWARD_MAP.put(config.boxId, list);
			}
		}
		
		List<BoxEquipConfig> boxEquipList = dataConfig.listAll(this, BoxEquipConfig.class);
		for(BoxEquipConfig config : boxEquipList){
			if(BOX_EQUIP_MAP.containsKey(config.boxId)){
				if(BOX_EQUIP_MAP.get(config.boxId).containsKey(config.type)){
					if(BOX_EQUIP_MAP.get(config.boxId).get(config.type).containsKey(config.equipType)){
						BOX_EQUIP_MAP.get(config.boxId).get(config.type).get(config.equipType).add(config);
					}else{
						List<BoxEquipConfig> list = new ArrayList<>();
						list.add(config);
						BOX_EQUIP_MAP.get(config.boxId).get(config.type).put(config.equipType, list);
					}
				}else{
					List<BoxEquipConfig> rewardList = new ArrayList<>();
					rewardList.add(config);
					Map<Integer,List<BoxEquipConfig>> map = new HashMap<>();
					map.put(config.equipType, rewardList);
					BOX_EQUIP_MAP.get(config.boxId).put(config.type, map);
				}
			}else{
				List<BoxEquipConfig> list = new ArrayList<>();
				list.add(config);
				Map<Integer,List<BoxEquipConfig>> equipTypeMap = new HashMap<>();
				equipTypeMap.put(config.equipType, list);
				Map<Integer,Map<Integer,List<BoxEquipConfig>>> typeMap = new HashMap<>();
				typeMap.put(config.type, equipTypeMap);
				BOX_EQUIP_MAP.put(config.boxId, typeMap);
			}
		}
	}
	
	public static RewardObject getReward(int level,int boxId){
		BoxConfig boxConfig = BOX_CONFIG_MAP.get(boxId);
		if(boxConfig == null){
			return null;
		}
		Integer itemType = RandomUtils.randomHit(1000, boxConfig.proportionMap);
		if(itemType == null){
			return null;
		}
		RewardObject rewardObject = null;
		if (itemType == BoxRewardType.EQUIP.getId()) {// 装备
			rewardObject = getEquipReward(boxId, boxConfig);
		} else if (itemType == BoxRewardType.FRAGMENT.getId()) {// 装备碎片
			rewardObject = getFragmentReward(boxId, boxConfig);
		} else if (itemType == BoxRewardType.GOODS.getId()) {// 物品
			rewardObject = getGoodsReward(boxId, boxConfig, level);
		} else {// 金币
			int num = FormulaHelper.executeCeilInt(boxConfig.gold, level);
			rewardObject = new RewardObject(RewardType.GOLD, 0, num);
		}
		return rewardObject;
	}

	private static RewardObject getGoodsReward(int boxId, BoxConfig boxConfig,int level) {
		Integer goodsId = RandomUtils.randomHit(1000, boxConfig.goodsMap);
		if(goodsId == null){
			return null;
		}
		List<BoxGoodsConfig> list = BOX_REWARD_MAP.get(boxId);
		List<BoxGoodsConfig> rewardList = new ArrayList<>();
		Map<Integer,Integer> map = new HashMap<>();
		int i = 0;
		for(BoxGoodsConfig config:list){
			if(goodsId.intValue() == config.id){
				rewardList.add(config);
				map.put(i, config.proportion);
				i++;
			}
		}
		Integer goodsIndex = RandomUtils.randomHit(1000, map);
		if(goodsIndex == null){
			return null;
		}
		BoxGoodsConfig boxRewardConfig = rewardList.get(goodsIndex);
		int num = FormulaHelper.executeCeilInt(boxRewardConfig.num,level);
		return new RewardObject(RewardType.GOODS,boxRewardConfig.id,num);
	}

	private static RewardObject getFragmentReward(int boxId, BoxConfig boxConfig) {
		Integer fragmentStar = RandomUtils.randomHit(1000, boxConfig.fragmentStarMap);
		if(fragmentStar == null){
			return null;
		}
		Integer fragmentType = RandomUtils.randomHit(1000, boxConfig.fragmentTypeMap);
		if(fragmentType == null){
			return null;
		}
		List<BoxEquipConfig> list = BOX_EQUIP_MAP.get(boxId).get(BoxRewardType.FRAGMENT.getId()).get(fragmentType);
		List<BoxEquipConfig> rewardList = new ArrayList<>();
		Map<Integer,Integer> map = new HashMap<>();
		int i = 0;
		for(BoxEquipConfig config:list){
			if(fragmentStar.intValue() == config.star){
				rewardList.add(config);
				map.put(i, config.proportion);
				i++;
			}
		}
		Integer equipIndex = RandomUtils.randomHit(1000, map);
		if(equipIndex == null){
			return null;
		}
		BoxEquipConfig boxEquipConfig = rewardList.get(equipIndex);
		return new RewardObject(RewardType.GOODS,boxEquipConfig.id,Integer.valueOf(boxEquipConfig.num));
	}

	private static RewardObject getEquipReward(int boxId, BoxConfig boxConfig) {
		Integer equipStar = RandomUtils.randomHit(1000, boxConfig.equipStarMap);
		if(equipStar == null){
			return null;
		}
		Integer equipType = RandomUtils.randomHit(1000, boxConfig.equipTypeMap);
		if(equipType == null){
			return null;
		}
		List<BoxEquipConfig> list = BOX_EQUIP_MAP.get(boxId).get(BoxRewardType.EQUIP.getId()).get(equipType);
		List<BoxEquipConfig> rewardList = new ArrayList<>();
		Map<Integer,Integer> map = new HashMap<>();
		int i = 0;
		for(BoxEquipConfig config:list){
			if(equipStar.intValue() == config.star){
				rewardList.add(config);
				map.put(i, config.proportion);
				i++;
			}
		}
		Integer equipIndex = RandomUtils.randomHit(1000, map);
		if(equipIndex == null){
			return null;
		}
		BoxEquipConfig boxEquipConfig = rewardList.get(equipIndex);
		return new RewardObject(RewardType.EQUIP,boxEquipConfig.id,1);
	}

}
