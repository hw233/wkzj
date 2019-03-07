package com.jtang.gameserver.dataconfig.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
/**
 * vip等级配置
 * @author ludd
 *
 */
@DataFile(fileName = "vipConfig")
public class VipConfig implements ModelAdapter, Comparable<VipConfig> {

	/**
	 * VIP等级
	 */
	public int vipLevel;
	/**
	 * 充值点券
	 */
	public int rechargeTicket;
	/**
	 * 需要充值的对应人民币数
	 */
	public int needMoney;
	/**
	 * 赠送仙人（格式：仙人ID_仙人ID)
	 */
	private String giveHeros;
	/**
	 * 赠送金币：格式：金币个数
	 */
	public int giveGold;
	/**
	 * 赠送装备（格式：装备ID_装备ID)
	 */
	private String giveEquips;
	/**
	 * 赠送物品（格式：物品ID_物品数量|物品ID_物品数量）
	 */
	private String giveGoods;
	/**
	 * 扩展字段
	 */
	private String extValue;
	/**
	 * 主力仙人属性千分比
	 */
	public int mainHeroAttributePercent;
	/**
	 * 主力仙人增加经验公式
	 */
	public String mainHeroAddExp;
	/**
	 * 活力最大补满次数
	 */
	public int vitNum;
	/**
	 * 精力最大补满次数
	 */
	public int snatchNum;
	/**
	 * 金币购买最大次数
	 */
	public int goldNum;
	
	/**
	 * 仙人合成次数
	 */
	public int addComposeHeroNum;
	/**
	 * 装备合成次数
	 */
	public int addComposeEquipNum;
	/**
	 * 装备仙人重置次数
	 */
	public int addHeroEquipReset;
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
	 * 登天塔额外的通天币奖励
	 */
	public int bableExtStar;
	/**
	 * 碎片合成减少时间
	 */
	public int composeGoods;
	/**
	 * 大聚仙减少的时间
	 */
	public int recruitTime;
	/**
	 * 仙人突破返还精魄比例
	 */
	public int breakThrough;
	@FieldIgnore
	public String[] extArray;
	@FieldIgnore
	private Set<Integer> giveHerosList = new HashSet<>();
	@FieldIgnore
	private Set<Integer> giveEquipList = new HashSet<>();
	@FieldIgnore
	private Map<Integer, Integer> giveGoodsMap = new HashMap<Integer, Integer>();
	@Override
	public void initialize() {
		if (giveHeros != null && giveHeros != ""){
			giveHerosList = StringUtils.splitString2Set(giveHeros);
			giveHeros = null;
		}
		if (giveEquips != null && giveEquips != ""){
			giveEquipList = StringUtils.splitString2Set(giveEquips);
			giveEquips = null;
		}
		if (giveGoods != null && giveGoods != ""){
			giveGoodsMap = StringUtils.delimiterString2IntMap(giveGoods);
			giveGoods = null;
		}
		if (StringUtils.isNotBlank(extValue)) {
			extArray = StringUtils.split(extValue, Splitable.ATTRIBUTE_SPLIT);
			extValue = null;
		}
	}
	@Override
	public int compareTo(VipConfig o) {
		if (this.rechargeTicket < o.rechargeTicket){
			return -1;
		} else if (this.rechargeTicket ==  o.rechargeTicket){
			return 0;
		} else {
			return 1;
		}
	}
	public Set<Integer> getGiveHerosList() {
		return giveHerosList;
	}
	public Set<Integer> getGiveEquipList() {
		return giveEquipList;
	}
	public Map<Integer, Integer> getGiveGoodsMap() {
		return giveGoodsMap;
	}
	
	public int getAllyReward(int num){
		if(num == 0|| allyBattleReward == 0){
			return 0;
		}
		return Double.valueOf(Math.ceil(allyBattleReward/1000.0d * num)).intValue();
	}
	
}
