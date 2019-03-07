package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.model.ExprRewardObject;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.utility.Splitable;
import com.jtang.gameserver.dataconfig.model.CraftsmanBuildConfig;
import com.jtang.gameserver.dataconfig.model.CraftsmanGlobalConfig;
import com.jtang.gameserver.dataconfig.model.GoodsConfig;

/**
 * 试炼洞service类
 * @author lig
 */
@Service
public class CraftsmanService extends ServiceAdapter {
	
	public static CraftsmanGlobalConfig CRAFTSMAN_GLOBAL_CONFIG = new CraftsmanGlobalConfig();
	public static Map<Integer, CraftsmanBuildConfig> CRAFTSMAN_BUILD_CONFIG_MAP = new HashMap<Integer, CraftsmanBuildConfig>();

	@Override
	public void clear() {
		CRAFTSMAN_GLOBAL_CONFIG = new CraftsmanGlobalConfig();
		CRAFTSMAN_BUILD_CONFIG_MAP.clear();
	}
	
	
	@Override
	public void initialize() {
		List<CraftsmanGlobalConfig> list = dataConfig.listAll(this, CraftsmanGlobalConfig.class);
		for (CraftsmanGlobalConfig craftsmanGlobalConfig : list) {
			CRAFTSMAN_GLOBAL_CONFIG = craftsmanGlobalConfig;
		}
		
		List<CraftsmanBuildConfig> list1 = dataConfig.listAll(this, CraftsmanBuildConfig.class);
		for (CraftsmanBuildConfig craftsmanBuildConfig : list1) {
			CRAFTSMAN_BUILD_CONFIG_MAP.put(craftsmanBuildConfig.star, craftsmanBuildConfig);
		}
	}

	public static int getProbilityByStar(int equipStar, int buildId) {
		return CRAFTSMAN_BUILD_CONFIG_MAP.get(equipStar).buildProbList.get(buildId);
	}
	
	public static int getCostByStar(int equipStar, int buildId) {
		return CRAFTSMAN_BUILD_CONFIG_MAP.get(equipStar).buildCostList.get(buildId);
	}

	public static int getReturnPrecByStar(int equipStar) {
		return CRAFTSMAN_BUILD_CONFIG_MAP.get(equipStar).returnPerc;
//		return CRAFTSMAN_BUILD_CONFIG_MAP.get(equipStar).buildCostList.get(buildId);
	}

	public static CraftsmanGlobalConfig getCraftsmanGlobalConfig() {
		return CRAFTSMAN_GLOBAL_CONFIG;
	}
	
	private static int getDependsNum(int goodsId) {
		GoodsConfig goodsConfig = GoodsService.get(goodsId);
		if (goodsConfig.depends != null && goodsConfig.depends != ""){
			String[] depends = goodsConfig.depends.split(Splitable.ATTRIBUTE_SPLIT);
			int dependsNum = Integer.valueOf(depends[1]);
			return dependsNum;
		}
		return 0;
	}
	
	public static List<RewardObject> getRewardList(boolean isSuccess, int buildId, int equipId,  int equipStar, int actorLevel) {
		List<RewardObject> list = new ArrayList<RewardObject>();
		
		if (isSuccess) {
			int buildEquipId = CRAFTSMAN_GLOBAL_CONFIG.buildEquipsMap.get(buildId);
			RewardObject newObject = new RewardObject(RewardType.EQUIP, buildEquipId, 1);
			list.add(newObject);
			return list;
		} else {
			//3星以上有碎片
			if (equipStar > 2) {
				int fragmentId = EquipService.getFragmentIdByEquipId(equipId);
				if (fragmentId < 0) {
					return list;
				}
				int returnNum = getDependsNum(fragmentId);
				RewardObject newObject = new RewardObject(RewardType.GOODS, fragmentId, returnNum * getReturnPrecByStar(equipStar) / 100);
				list.add(newObject);
			}
			List<ExprRewardObject> exprRewardObjects = CRAFTSMAN_BUILD_CONFIG_MAP.get(equipStar).buildReturnList;
			boolean isNeedReturn = exprRewardObjects.isEmpty();
			if (isNeedReturn == false) {
				for (ExprRewardObject rewardObject : exprRewardObjects) {
					RewardObject newObject = rewardObject.clone(actorLevel);
					list.add(newObject);
				}
			}
			return list;
		}
		
	}
}
