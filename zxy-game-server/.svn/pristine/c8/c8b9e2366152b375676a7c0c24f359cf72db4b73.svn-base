package com.jtang.gameserver.module.user.model;

import com.jtang.gameserver.dataconfig.model.VipConfig;
import com.jtang.gameserver.dataconfig.service.VipService;

public abstract class VipPrivilege {

	/**
	 * 增加合成仙人次数
	 */
	public int addComposeHeroNum;
	/**
	 * 增加合成装备次数
	 */
	public int addComposeEquipNum;
	/**
	 * 增加仙人装备重置次数
	 */
	public int addHeroEquipReset;
	/**
	 * 活力购买最大次数
	 */
	public int vitNum;
	/**
	 * 精力购买最大次数
	 */
	public int energyNum;
	/**
	 * 金币购买最大次数
	 */
	public int goldNum;
	/**
	 * 主力仙人属性增加千分比
	 */
	public int mainHeroAttributePercent;
	/**
	 * 主力仙人属性增加公式
	 */
	public String mainHeroAddExp;
	/**
	 * 妖邪之地VIP玩家可以试炼的次数
	 */
	public int VIPEntrance1TrialNum;
	/**
	 * 魔灵地窖VIP玩家可以试炼的次数
	 */
	public int VIPEntrance2TrialNum;
	/**
	 *  VIP玩家可以重置试炼的次数
	 */
	public int VIPResetTrialNum;
	/**
	 * 重置返还的金币比率(千分比)
	 */
	public int returnGold;
	/**
	 * 重置返还的物品比率(千分比)
	 */
	public int returnGoods;
	/**
	 * 合作关卡盟友奖励千分比
	 */
	public int allyBattleReward;
	/**
	 * 登天塔额外的奖励
	 */
	public int bableExtStar;
	/**
	 * 碎片合成减少的时间
	 */
	private int composeGoods;
	/**
	 * 大聚仙减少的时间(千分比)
	 */
	public int recruitTime;
	/**
	 * 仙人突破后重置返还精魄比例
	 */
	public int breakThrough;
	
	public VipPrivilege() {
		VipConfig vipConfig = VipService.getByLevel(this.getVipLevel());
		addComposeHeroNum = vipConfig.addComposeHeroNum;
		addComposeEquipNum = vipConfig.addComposeEquipNum;
		addHeroEquipReset = vipConfig.addHeroEquipReset;
		mainHeroAttributePercent = vipConfig.mainHeroAttributePercent;
		mainHeroAddExp = vipConfig.mainHeroAddExp;
		vitNum = vipConfig.vitNum;
		energyNum = vipConfig.snatchNum;
		goldNum = vipConfig.goldNum;
		VIPEntrance1TrialNum = vipConfig.VIPEntrance1TrialNum;
		VIPEntrance2TrialNum = vipConfig.VIPEntrance2TrialNum;
		VIPResetTrialNum = vipConfig.VIPResetTrialNum;
		returnGold = vipConfig.returnGold;
		returnGoods = vipConfig.returnGoods;
		allyBattleReward = vipConfig.allyBattleReward;
		bableExtStar = vipConfig.bableExtStar;
		composeGoods = vipConfig.composeGoods;
		recruitTime = vipConfig.recruitTime;
		breakThrough = vipConfig.breakThrough;
	}
	public abstract int getVipLevel();
	
	public abstract void init(String[] strArr);
	
	public static VipPrivilege createInstance(int vipLevel){
		VipPrivilege instance = null;
		switch (vipLevel) {
		case 1:
			instance = new Vip1Privilege();
			break;
		case 2:
			instance = new Vip2Privilege();
			break;
		case 3:
			instance = new Vip3Privilege();
			break;
		case 4:
			instance = new Vip4Privilege();
			break;
		case 5:
			instance = new Vip5Privilege();
			break;
		case 6:
			instance = new Vip6Privilege();
			break;
		case 7:
			instance = new Vip7Privilege();
			break;
		case 8:
			instance = new Vip8Privilege();
			break;
		case 9:
			instance = new Vip9Privilege();
			break;
		case 10:
			instance = new Vip10Privilege();
			break;
		case 11:
			instance = new Vip11Privilege();
			break;
		case 12:
			instance = new Vip12Privilege();
			break;
		case 13:
			instance = new Vip13Privilege();
			break;
		case 14:
			instance = new Vip14Privilege();
			break;
		case 15:
			instance = new Vip15Privilege();
			break;
		default:
			break;
		}
		
		return instance;
	}
	
	/**
	 * 计算装备仙人重置返回金币的数量
	 * @param gold
	 * @return
	 */
	public long getGold(long gold){
		if(gold == 0 || returnGold == 0){
			return 0;
		}
		return Double.valueOf(Math.ceil(returnGold/1000.0d * gold)).longValue();
	}
	
	/**
	 * 计算装备仙人重置返回物品的数量
	 * @param goods
	 * @return
	 */
	public int getGoods(int goods){
		if(goods == 0|| returnGoods == 0){
			return 0;
		}
		return Double.valueOf(Math.ceil(returnGoods/1000.0d * goods)).intValue();
	}
	
	/**
	 * 计算盟友合作关卡给予的奖励
	 * @param num 配置奖励总数
	 * @return
	 */
	public int getAllyReward(int num){
		if(num == 0|| allyBattleReward == 0){
			return 0;
		}
		return Double.valueOf(Math.ceil(allyBattleReward/1000.0d * num)).intValue();
	}
	
	/**
	 * 计算碎片合成需要时间
	 * @param useTime 碎片合成配置需要时间
	 * @return
	 */
	public int getComposeGoods(int useTime){
		if(useTime == 0 || composeGoods == 0){
			return 0;
		}
		return Double.valueOf(Math.ceil(composeGoods/1000.0d * useTime)).intValue();
	}
	public int getBreakThrough(int soulNum) {
		if(soulNum == 0 || breakThrough == 0){
			return 0;
		}
		return Double.valueOf(Math.ceil(breakThrough/1000.0d * soulNum)).intValue();
	}
}
