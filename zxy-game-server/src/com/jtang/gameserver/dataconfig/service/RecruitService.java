package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.RecruitConfig;
import com.jtang.gameserver.dataconfig.model.RecruitDropConfig;
import com.jtang.gameserver.dataconfig.model.RecruitLeastConfig;
import com.jtang.gameserver.dataconfig.model.RecruitStarConfig;

/**
 * 聚仙阵配置
 * @author 0x737263
 *
 */
@Component
public class RecruitService extends ServiceAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(RecruitService.class);
	
	/** key:type value:config */
	private static Map<Integer,RecruitConfig> RECRUIT_MAP = new HashMap<>();
	/** key:type value:{key:star value:configList} */
	private static Map<Integer,Map<Integer,List<RecruitDropConfig>>> RECRUIT_DROP_MAP = new HashMap<>();
	/** key:type value:config*/
	private static List<RecruitLeastConfig> RECRUIT_LEAST_MAP = new ArrayList<>();
	/** key:type value:configList */
	private static Map<Integer,List<RecruitStarConfig>> RECRUIT_STAR_MAP = new HashMap<>();

	@Override
	public void clear() {
		RECRUIT_MAP.clear();
		RECRUIT_DROP_MAP.clear();
		RECRUIT_LEAST_MAP.clear();
		RECRUIT_STAR_MAP.clear();
	}

	@Override
	public void initialize() {
		List<RecruitConfig> recruitList = dataConfig.listAll(this, RecruitConfig.class);
		for (RecruitConfig config : recruitList) {
			RECRUIT_MAP.put(config.getRecruitType(), config);
		}
		
		List<RecruitDropConfig> recruitDropList = dataConfig.listAll(this, RecruitDropConfig.class);
		for (RecruitDropConfig config : recruitDropList) {
			if (RECRUIT_DROP_MAP.containsKey(config.type)) {
				if(RECRUIT_DROP_MAP.get(config.type).containsKey(config.star)){
					RECRUIT_DROP_MAP.get(config.type).get(config.star).add(config);
				}else{
					List<RecruitDropConfig> list = new ArrayList<>();
					list.add(config);
					RECRUIT_DROP_MAP.get(config.type).put(config.star, list);
				}
			} else {
				Map<Integer,List<RecruitDropConfig>> map = new HashMap<>();
				List<RecruitDropConfig> list = new ArrayList<>();
				list.add(config);
				map.put(config.star, list);
				RECRUIT_DROP_MAP.put(config.type, map);
			}
		}
		
		List<RecruitLeastConfig> recruitLeastList = dataConfig.listAll(this, RecruitLeastConfig.class);
		for (RecruitLeastConfig config : recruitLeastList) {
			RECRUIT_LEAST_MAP.add( config);
		}
		
		List<RecruitStarConfig> recruitStarList = dataConfig.listAll(this, RecruitStarConfig.class);
		for(RecruitStarConfig config:recruitStarList){
			if(RECRUIT_STAR_MAP.containsKey(config.type)){
				RECRUIT_STAR_MAP.get(config.type).add(config);
			}else{
				List<RecruitStarConfig> list = new ArrayList<>();
				list.add(config);
				RECRUIT_STAR_MAP.put(config.type, list);
			}
		}
		
	}

	/**
	 * 获取聚仙配置
	 * @param type 聚仙类型
	 * @return
	 */
	public static RecruitConfig getRecruitConfig(byte type){
		int recruitType = type;
		return RECRUIT_MAP.get(recruitType);
	}
	
	/**
	 * 聚仙
	 * @param exType 大小聚仙
	 * @return
	 */
	public static RewardObject getReward(byte recruitType){
		RecruitConfig recruitConfig = getRecruitConfig(recruitType);
		byte dropType = recruitConfig.getDropType().byteValue();
		RewardObject rewardObject = null;
		rewardObject = getRandomReward(dropType, recruitType);
		if (rewardObject == null){
			LOGGER.error(String.format("聚仙错误:[%s],dropType:[%s]", recruitType, dropType));
		}
		return rewardObject;
	}

	/**
	 * 获取装备或者装备碎片
	 * @param exType
	 * @return
	 */
	private static RewardObject getRandomReward(int dropType, byte recruitType) {
		//根据类型随机获取星级
		List<RecruitStarConfig> starConfig = RECRUIT_STAR_MAP.get(dropType);
		Map<Integer,Integer> map = new HashMap<>();
		for(RecruitStarConfig config:starConfig){
			if (config.recruitType == recruitType) {
				map.put(config.star, config.rate);
			}
		}
		Integer star = RandomUtils.randomHit(1000, map);
		if(star == null){
			return null;
		}
		//根据类型、星级随机获取奖励
		List<RecruitDropConfig> dropConfigList = RECRUIT_DROP_MAP.get(dropType).get(star);
		List<RecruitDropConfig> singleDropList = new ArrayList<>();
		for (RecruitDropConfig recruitDropConfig : dropConfigList) {
			if (recruitDropConfig.recruitType == recruitType) {
				singleDropList.add(recruitDropConfig);
			}
		}
		Map<Integer,Integer> randomMap = new HashMap<>();
		for (int i = 0; i < singleDropList.size(); i++) {
			RecruitDropConfig config = singleDropList.get(i);
			randomMap.put(i, config.rate);
		}
		int index = RandomUtils.randomHit(1000, randomMap);
		RecruitDropConfig dropConfig = singleDropList.get(index);
		RewardObject rewardObject = new RewardObject();
		rewardObject.rewardType = RewardType.getType(dropConfig.type);
		rewardObject.id = dropConfig.id;
		rewardObject.num = dropConfig.num;
		return rewardObject;
	}
	
	
	
	public static List<RewardObject> getMutiLeastReward(byte type) {
		RecruitConfig cfg = getRecruitConfig(type);
		
		return cfg.getSeriesLeastList();
	}

	/**
	 * 获取保底奖励
	 * @param type
	 * @param num
	 * @param useTicket
	 * @return
	 */
	public static List<RewardObject> getLeastReward(byte type, long num) {
//		RecruitLeastConfig cfg = RECRUIT_LEAST_MAP.get(leastKey(type, num, useTicket));
//		if (cfg == null) {
//			return null;
//		} 
//		RewardObject obj = cfg.get();
//		RecruitRewardType recruitRewardType = RecruitRewardType.getType(obj.rewardType.getCode());
//		return new RewardObject(toRewardType(recruitRewardType),obj.id,obj.num);
 		if (num <= 1) {
			return null;
		}
		List<RewardObject> result = new ArrayList<>();
		RecruitLeastConfig recruitLeastConfig = null;
		for (RecruitLeastConfig cfg : RECRUIT_LEAST_MAP) {
			if (cfg.num == num && cfg.numType == 0 && cfg.type == type) {
				recruitLeastConfig = cfg;
				break;
			} 
		}
		if (recruitLeastConfig != null) {
			result.add(recruitLeastConfig.get());
			return result;
		}
		
		for (RecruitLeastConfig cfg : RECRUIT_LEAST_MAP) {
			if (cfg.type == type) {
				
				int resultNum = 0;
				if (num >= cfg.num) {
					resultNum = (int) (num % cfg.num);
				} else {
					continue;
				}
				if (resultNum == 0 && cfg.numType == 1) {
					recruitLeastConfig = cfg;
				} 
			}
		}
		if (recruitLeastConfig != null) {
			result.add(recruitLeastConfig.get());
			return result;
		}

		
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}
	
	public static List<RewardObject> getFirstReward(byte type) {
		RecruitConfig cfg = getRecruitConfig(type);
		
		return cfg.getFirstList();
	}
	
	public static int getLeastNum(byte type, Long num) {
		RecruitLeastConfig recruitLeastConfig = null;
		for (RecruitLeastConfig cfg : RECRUIT_LEAST_MAP) {
			if (cfg.num > num && cfg.numType == 0 && cfg.type == type) {
				recruitLeastConfig = cfg;
				break;
			} 
		}
		if (recruitLeastConfig != null) {
			if (recruitLeastConfig.num ==  num.intValue()){
				return recruitLeastConfig.num;
			}
			int result = recruitLeastConfig.num - num.intValue();
			return result - 1;
		}
		
		for (RecruitLeastConfig cfg : RECRUIT_LEAST_MAP) {
			if (cfg.numType == 1 && cfg.type == type) {
				recruitLeastConfig = cfg;
			} 
		}
		if (recruitLeastConfig != null) {
			int result = 0;
			if(num == 0){
				return recruitLeastConfig.num - 1;
			}
			if (num < recruitLeastConfig.num) {
				result = (int) (recruitLeastConfig.num - num);
			} else {
				result = (int) (recruitLeastConfig.num - num % recruitLeastConfig.num);
			}
			return result -1 ;
		}
		
		return 0;
	}
	
}
