package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
/**
 * 成就配置
 * @author pengzy
 *
 */
@DataFile(fileName = "achievementConfig")
public class AchievementConfig implements ModelAdapter {
	/**
	 * 成就Id
	 */
	private int achieveId;
	/**
	 * 成就的类型
	 */
	private int achieveType;
	/**
	 * 成就达成条件
	 */
	private String conditions;
	/**
	 * 完成此成就后触发的服务器动作
	 */
	private int reaction;
	/**
	 * 成就的奖励Id
	 * 
	 */
	private String reward;
	
	@FieldIgnore
	private List<Integer> conditionList;
	/**
	 * reward:奖励  1.金币 2.点券 
	 * 3.物品类（一般物品，道具物品，宝箱） 
	 * 4.装备 5.仙人魂魄 6.仙人 7.聚仙免费次数 
	 * 8.聚仙免费次数补满9.恢复精力
	 * 10.精力补满 11.恢复活力 
	 * 12.活力补满
	 */
	@FieldIgnore
	private int golds;
	@FieldIgnore
	private int tickets;
	@FieldIgnore
	private int recruit;
	@FieldIgnore
	private boolean recruitFull;
	@FieldIgnore
	private int energy;
	@FieldIgnore
	private boolean energyFull;
	@FieldIgnore
	private int vit;
	@FieldIgnore
	private boolean vitFull;
	@FieldIgnore
	private int reputation;
	@FieldIgnore
	private Map<Integer, Integer> goods = new HashMap<>();
	@FieldIgnore
	private Map<Integer, Integer> equips = new HashMap<>();
	@FieldIgnore
	private Map<Integer, Integer> heros = new HashMap<>();
	@FieldIgnore
	private Map<Integer, Integer> heroSouls = new HashMap<>();
	
	@Override
	public void initialize() {
		List<String> strList = StringUtils.delimiterString2List(conditions, Splitable.ATTRIBUTE_SPLIT);
		conditionList = new ArrayList<>(strList.size());
		for(String str : strList){
			conditionList.add(Integer.valueOf(str));
		}
		/**
		 * reward:奖励 
		 * 1.金币 
		 * 2.点券 
		 * 3.物品类（一般物品，道具物品，宝箱） 
		 * 4.装备 
		 * 5.仙人魂魄 
		 * 6.仙人 
		 * 7.聚仙免费次数 
		 * 8.聚仙免费次数补满 
		 * 9.恢复精力
		 * 10.精力补满 
		 * 11.恢复活力 
		 * 12.活力补满
		 * 13.掌教经验
		 */
		List<String[]> rewardList = StringUtils.delimiterString2Array(reward);
//		List<String> rewardList = StringUtils.delimiterString2List(reward, Splitable.ELEMENT_DELIMITER);
		for(String[] strs : rewardList){
			if(Integer.valueOf(strs[0]) == 1){
				golds = Integer.valueOf(strs[1]);
			}
			else if(Integer.valueOf(strs[0]) == 2){
				tickets = Integer.valueOf(strs[1]);
			}
			else if(Integer.valueOf(strs[0]) == 3){
				goods.put(Integer.valueOf(strs[1]), Integer.valueOf(strs[2]));
			}
			else if(Integer.valueOf(strs[0]) == 4){
				if(strs.length > 2){
					equips.put(Integer.valueOf(strs[1]), Integer.valueOf(strs[2]));
				}
				else{
					equips.put(Integer.valueOf(strs[1]), 1);
				}
			}
			else if(Integer.valueOf(strs[0]) == 5){
				heroSouls.put(Integer.valueOf(strs[1]), Integer.valueOf(strs[2]));
			}
			else if(Integer.valueOf(strs[0]) == 6){
				heros.put(Integer.valueOf(strs[1]), Integer.valueOf(strs[2]));
			}
			else if(Integer.valueOf(strs[0]) == 7){
				recruit = Integer.valueOf(strs[1]);
			}
			else if(Integer.valueOf(strs[0]) == 8){
				recruitFull = true;
			}
			else if(Integer.valueOf(strs[0]) == 9){
				energy = Integer.valueOf(strs[1]);
			}
			else if(Integer.valueOf(strs[0]) == 10){
				energyFull = true;
			}
			else if(Integer.valueOf(strs[0]) == 11){
				vit = Integer.valueOf(strs[1]);
			}
			else if(Integer.valueOf(strs[0]) == 12){
				vitFull = true;
			}else if(Integer.valueOf(strs[0]) == 13){
				reputation = Integer.valueOf(strs[1]);
			}
		}
		
		conditions = null;
		reward = null;
	}
	
	public int getAchieveId() {
		return achieveId;
	}
	
	public int getAchieveType() {
		return achieveType;
	}
	
	public List<Integer> getConditionList() {
		return conditionList;
	}
	
	public int getCondition(){
		return conditionList.get(0);
	}
	
	public int getGolds() {
		return golds;
	}
	public int getTickets() {
		return tickets;
	}
	public int getRecruit() {
		return recruit;
	}
	public boolean isRecruitFull() {
		return recruitFull;
	}
	public int getEnergy() {
		return energy;
	}
	public boolean isEnergyFull() {
		return energyFull;
	}
	public int getVit() {
		return vit;
	}
	public boolean isVitFull() {
		return vitFull;
	}
	public Map<Integer, Integer> getGoods() {
		return goods;
	}
	public Map<Integer, Integer> getEquips() {
		return equips;
	}
	public Map<Integer, Integer> getHeros() {
		return heros;
	}
	public Map<Integer, Integer> getHeroSouls() {
		return heroSouls;
	}
	public int getNeedFinishNum() {
		return conditionList.get(conditionList.size() - 1);
	}
	public int getReaction() {
		return reaction;
	}
	
	public int getReputation(){
		return reputation;
	}
	
}
