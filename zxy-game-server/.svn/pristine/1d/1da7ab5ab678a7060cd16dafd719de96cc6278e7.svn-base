package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.ExprRewardObject;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
/**
 * 集众降魔配置
 * @author lig
 *
 */
@DataFile(fileName = "trialCaveGlobalConfig")
public class TrialCaveGlobalConfig implements ModelAdapter {

	/**
	 * 开放等级
	 */
	public int openLv;
	
	/**
	 * 开放日期
	 */
	private String openDate;
	/**
	 * 开放时间
	 */
	private String closeDate;
	/**
	 * 间隔时间
	 */ 
	public int intervalTime;
	
	/**
	 * 战斗失败奖励百分比
	 */
	public int failRewardPrecent;
	/**
	 * 关卡1地图id
	 */
	public int entrance1MapId;

	/**
	 * 关卡2地图id
	 */
	public int entrance2MapId;
	
	/**
	 * 关卡1（妖邪之地）产出物品
	 * 	格式:  物品类型_物品id_数量或者产出公式|物品类型_物品id_数量或者产出公式
	 */
	public String entrance1Reward;
	
	/**
	 * 关卡2（魔灵地窖) 产出物品
	 * 	格式:  物品类型_物品id_数量或者产出公式|物品类型_物品id_数量或者产出公式
	 */
	public String entrance2Reward;
	
	/**
	 *关卡1 （妖邪之地）非V玩家可以试炼的次数（VIP0-VIP13）
	 */
	public int entrance1TrialNum;
	
	/**
	 * 关卡2（魔灵地窖）非V玩家可以试炼的次数（VIP0-VIP13）
	 */
	public int entrance2TrialNum;
	
	/**
	 * 非V玩家可以购买重置试炼的次数
	 */
	public int resetTrialNum;
	
	/**
	 * 怪物阵容随机概率
	 */
	private String lineupMonster;
	
	/**
	 * 购买价格
	 */
	private String resetTrialPrice;
	
	/**
	 * 血量加成
	 */
	public String hpExpr;
	/**
	 * 防御加成
	 */
	public String defenseExpr;
	/**
	 * 攻击加成
	 */
	public String attackExpr;
	
	/**
	 * 活动开放时间(一周中的某天  ,1 周一....7 周日)
	 */
	public String activityOpenDate;
	
	/**
	 * 奖励翻倍倍数
	 */
	public int activityRatio;
	
	/**
	 * 额外奖励
	 */
	private String extraReward;
	
	/**
	 * 额外奖励次数
	 */
	public int extraRewardTimes;
	
	@FieldIgnore
	public List<String> lineupMonsterList = new ArrayList<String>();

	@FieldIgnore
	public List<Integer> resetTrialPriceList = new ArrayList<Integer>();

	@FieldIgnore
	public List<ExprRewardObject> entrance1RewardList = new ArrayList<ExprRewardObject>();
	
	@FieldIgnore
	public List<ExprRewardObject> entrance2RewardList = new ArrayList<ExprRewardObject>();

	@FieldIgnore
	public List<ExprRewardObject> extraRewardList = new ArrayList<ExprRewardObject>();

	@FieldIgnore
	public List<Integer> openDateList = new ArrayList<Integer>();
	
	@FieldIgnore
	public Date openDateTime = new Date();
	
	@FieldIgnore
	public Date closeDateTime = new Date();

	@Override
	public void initialize() {
		openDateTime = DateUtils.string2Date(this.openDate, "yyyy-MM-dd HH:mm:ss");
		closeDateTime = DateUtils.string2Date(this.closeDate, "yyyy-MM-dd HH:mm:ss");
		resetTrialPriceList = StringUtils.delimiterString2IntList(resetTrialPrice, Splitable.ATTRIBUTE_SPLIT);
		lineupMonsterList = StringUtils.delimiterString2List(lineupMonster, Splitable.ATTRIBUTE_SPLIT);
		
		List<String> list = StringUtils.delimiterString2List(entrance1Reward, Splitable.ELEMENT_SPLIT);
		for (String str : list) {
			String[] value = StringUtils.split(str, Splitable.ATTRIBUTE_SPLIT);
			ExprRewardObject e = ExprRewardObject.valueOf(value);
			entrance1RewardList.add(e);
		}

		List<String> list1 = StringUtils.delimiterString2List(entrance2Reward, Splitable.ELEMENT_SPLIT);
		for (String str : list1) {
			String[] value = StringUtils.split(str, Splitable.ATTRIBUTE_SPLIT);
			ExprRewardObject e = ExprRewardObject.valueOf(value);
			entrance2RewardList.add(e);
		}
		
		List<String> list2 = StringUtils.delimiterString2List(extraReward, Splitable.ELEMENT_SPLIT);
		for (String str : list2) {
			String[] value = StringUtils.split(str, Splitable.ATTRIBUTE_SPLIT);
			ExprRewardObject e = ExprRewardObject.valueOf(value);
			extraRewardList.add(e);
		}
		
		List<String> list3 = StringUtils.delimiterString2List(activityOpenDate, Splitable.ATTRIBUTE_SPLIT);
		for (String str : list3) {
			Integer day = Integer.parseInt(str);
			openDateList.add(day);
		}
	}

}
