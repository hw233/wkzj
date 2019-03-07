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
 * 任务配置
 * @author pengzy
 *
 */
@DataFile(fileName = "taskConfig")
public class TaskConfig implements ModelAdapter{
	/**
	 * 任务Id
	 */
	private int taskId;
	
	private int npcId;
	
	/**
	 * 下一任务Id，即每一个任务都指向下一个任务，一旦该任务完成，就能触发下一个任务的生成
	 */
	private int nextTaskId;
	
	/**
	 * 任务的类型
	 */
	private int taskType;
	
	/**
	 * 任务的达成条件
	 */
	private String conditions;
	
	/**
	 * 任务的奖励
	 * rewards:奖励的结构"金币数量|点券数量|仙人魂魄id_数量|物品id_数量|装备id_数量"
	 * 格式goldsNum|ticketNum|heroId_heroNum,heroID_heroNum|goodsId_goodsNum,goodsId_goodsNum...."
	 */
	private String rewards;
	
	//-----------
	@FieldIgnore
	private int golds;
	@FieldIgnore
	private int tickets;
	@FieldIgnore
	private int freeRecruit;
	@FieldIgnore
	private int energy;
	@FieldIgnore
	private int vit;
	@FieldIgnore
	private Map<Integer, Integer> heroSoulReward = new HashMap<>();
	@FieldIgnore
	private Map<Integer, Integer> goodsReward = new HashMap<>();
	@FieldIgnore
	private Map<Integer, Integer> equipReward = new HashMap<>();
	@FieldIgnore
	private List<Integer> conditionList = new ArrayList<>();
	
	@Override
	public void initialize() {
		parseReward();
		parseConditions();
		
		this.rewards = null;
		this.conditions = null;
	}
	private void parseConditions() {
		List<String> list = StringUtils.delimiterString2List(conditions, Splitable.ATTRIBUTE_SPLIT);
		for(String condition : list){
			conditionList.add(Integer.valueOf(condition));
		}
	}
	private void parseReward() {
		List<String> rewardList = StringUtils.delimiterString2List(rewards, Splitable.ELEMENT_SPLIT);
		if(!rewardList.get(0).equals("") && !rewardList.get(0).equals(" ")){
			golds = Integer.valueOf(rewardList.get(0));
		}
		if(!rewardList.get(1).equals("") && !rewardList.get(1).equals(" ")){
			tickets = Integer.valueOf(rewardList.get(1));
		}
		if(rewardList.size() > 2){
			if(!rewardList.get(2).equals("") && !rewardList.get(2).equals(" ")){
				List<String> heroSoulList = StringUtils.delimiterString2List(rewardList.get(2), Splitable.BETWEEN_ITEMS);
				if(heroSoulList.size() > 0){
					for(String heroStr : heroSoulList){
						List<String> heroSoul = StringUtils.delimiterString2List(heroStr, Splitable.ATTRIBUTE_SPLIT);
						if(heroSoul.size() > 1){
							heroSoulReward.put(Integer.valueOf(heroSoul.get(0)), Integer.valueOf(heroSoul.get(1)));
						}
					}
				}
			}
		}
		if(rewardList.size() > 3){
			if(!rewardList.get(3).equals("") && !rewardList.get(3).equals(" ")){
				List<String> goodsList = StringUtils.delimiterString2List(rewardList.get(3), Splitable.BETWEEN_ITEMS);
				if(goodsList.size() > 0){
					for(String goodsStr : goodsList){
						List<String> goods = StringUtils.delimiterString2List(goodsStr, Splitable.ATTRIBUTE_SPLIT);
						if(goods.size() > 1){
							goodsReward.put(Integer.valueOf(goods.get(0)), Integer.valueOf(goods.get(1)));
						}
					}
				}
			}
		}
		if(rewardList.size() > 4){
			if(!rewardList.get(4).equals("") && !rewardList.get(4).equals(" ")){
				List<String> equipList = StringUtils.delimiterString2List(rewardList.get(4), Splitable.BETWEEN_ITEMS);
				if(equipList.size() > 0){
					for(String equipStr : equipList){
						List<String> equip = StringUtils.delimiterString2List(equipStr, Splitable.ATTRIBUTE_SPLIT);
						if(equip.size() > 1){
							equipReward.put(Integer.valueOf(equip.get(0)), Integer.valueOf(equip.get(1)));
						}
					}
				}
			}
		}
		if(rewardList.size() > 5){
			freeRecruit = Integer.valueOf(rewardList.get(5));
		}
		if(rewardList.size() > 6){
			energy = Integer.valueOf(rewardList.get(6));
		}
		if(rewardList.size() > 7){
			vit = Integer.valueOf(rewardList.get(7));
		}
	}
	public int getNpcId() {
		return npcId;
	}
	
	public String getRewards() {
		return rewards;
	}
	public int getTaskId() {
		return taskId;
	}
	public int getNextTaskId() {
		return nextTaskId;
	}
	public int getGolds() {
		return golds;
	}
	public int getTickets() {
		return tickets;
	}
	public Map<Integer, Integer> getGoods() {
		return goodsReward;
	}
	public int getTaskType() {
		return taskType;
	}
	public String getValue() {
		return conditions;
	}
	
	public Map<Integer, Integer> getHeroSoulReward() {
		return heroSoulReward;
	}
	public Map<Integer, Integer> getGoodsReward() {
		return goodsReward;
	}
	public Map<Integer, Integer> getEquipReward() {
		return equipReward;
	}
	public List<Integer> getConditionList() {
		return conditionList;
	}
	
	public int getFreeRecruit() {
		return freeRecruit;
	}
	public int getEnergy() {
		return energy;
	}
	public int getVit() {
		return vit;
	}
	public List<Integer> getInitValues() {
		List<Integer> initValues = new ArrayList<>();
		for(int i = 0; i < conditionList.size(); i++){
			initValues.add(0);
		}
		return initValues;
	}
	public List<Integer> getTaskConditions() {
		return conditionList;
	}
}
