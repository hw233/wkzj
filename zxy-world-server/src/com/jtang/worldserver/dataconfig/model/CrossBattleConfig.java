package com.jtang.worldserver.dataconfig.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.ExprRewardObject;
import com.jtang.core.model.RewardObject;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.StringUtils;
import com.jtang.worldserver.module.crossbattle.type.CrossBattleResult;

/**
 * 比赛全局配置
 * @author 0x737263
 *
 */
@DataFile(fileName = "crossBattleConfig")
public class CrossBattleConfig implements ModelAdapter {

	@FieldIgnore
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	/**
	 * 开始日期
	 */
	public String startDate;
	
	/**
	 * 结束日期
	 */
	public String endDate;
	
	/**
	 * 每日比赛开始时间
	 */
	public String startTime;
	
	/**
	 * 每日报名时间
	 */
	public String signupTime;
	
	
	/**
	 * 每日比赛结束时间
	 */
	public String endTime;
	
	/**
	 * 每天参与的前x名最强势力
	 */
	private int topPowerRank;
	
	/**
	 * 比赛结束后领奖的角色等级
	 */
	private int awardActorLevel;
	
	/**
	 * 攻击后冷确时间(秒)
	 */
	private int atkCDTime;
	
	/**
	 * 复活时间(秒)
	 */
	private int reliveTime;
	
	/**
	 * 胜利服每次增加积分点
	 */
	private int winPoint;
	
	/**
	 * 失败服每次增加积分点
	 */
	private int failPoint;
	
	/**
	 * 平局服每次增加积分点
	 */
	private int drawPoint;
	
	/**
	 * 轮空积分
	 */
	private int notTargetPoint;
	
	/**
	 * 每天胜利服的玩家奖励物品 格式: 类型(0:物品，1：装备，2：仙人魂魄 ，3：金币_物品id_物品数量表达式(x1*1000, x1:表示等级)|后面多个奖励
	 */
	private String winDayReward;
	
	/**
	 * 每天胜利服的玩家奖励贡献点：
	 */
	private int winDayExchangePoint;
	
	/**
	 * 每天胜利服的玩家累计杀人奖劢贡献点： (x1:累计击败人数)
	 */
	private String winDayKillExchangePoint;
	

	/**
	 * 每天失败服的玩家奖励物品(没有则不填) 格式: 类型(0:物品，1：装备，2：仙人魂魄 ，3：金币）_物品id_物品数量表达式(x1*1000, x1:表示等级)|后面多个奖励
	 */
	private String failDayReward;
	
	/**
	 * 每天失败服的玩家奖励贡献点：(没有填0)
	 */
	private int failDayExchangePoint;
	
	/**
	 * 每天失败服的玩家累计杀人奖劢贡献点： (x1:累计击败人数) (没有填0)
	 */
	private String failDayKillExchangePoint;
	
	/**
	 * 参与的掌教最低等级
	 */
	private int joinLevel;
	
	/**
	 * 复活保护时间
	 */
	private int protectTime;
	
	@FieldIgnore
	private List<ExprRewardObject> winDayRewardList = new ArrayList<>();
	
	@FieldIgnore
	private List<ExprRewardObject> failDayRewardList = new ArrayList<>();
	
	
	@Override
	public void initialize() {
		winDayRewardList.clear();
		List<String[]> winList = StringUtils.delimiterString2Array(winDayReward);
		for (String[] str : winList) {
			ExprRewardObject obj = ExprRewardObject.valueOf(str);
			winDayRewardList.add(obj);
		}
		
		failDayRewardList.clear();
		List<String[]> failList = StringUtils.delimiterString2Array(failDayReward);
		for (String[] str : failList) {
			ExprRewardObject obj = ExprRewardObject.valueOf(str);
			failDayRewardList.add(obj);
		}
	}

	public Date getStartDate() {
//		return DateUtils.string2Date(startDate, "yyyy-MM-dd");
		Date date = DateUtils.string2Date(startDate, "yyyy-MM-dd"); 
		Calendar cDate = Calendar.getInstance();
		cDate.setTime(date);
		Date startTime = getStartTime();
		Calendar c = Calendar.getInstance();
		c.setTime(startTime);
		c.set(cDate.get(Calendar.YEAR), cDate.get(Calendar.MONTH), cDate.get(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	public Date getEndDate() {
		Date date = DateUtils.string2Date(endDate, "yyyy-MM-dd"); 
		Calendar cDate = Calendar.getInstance();
		cDate.setTime(date);
		Date endTime = getEndTime();
		Calendar c = Calendar.getInstance();
		c.setTime(endTime);
		c.set(cDate.get(Calendar.YEAR), cDate.get(Calendar.MONTH), cDate.get(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	public Date getStartTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		try {
			Date date = sdf.parse(this.startTime);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			Calendar time = Calendar.getInstance();
			time.set(Calendar.SECOND, c.get(Calendar.SECOND));
			time.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
			time.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY));
			return time.getTime();
		} catch (Exception e){
			return null;
		}
	}

	public Date getSignupTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		try {
			Date date = sdf.parse(this.signupTime);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			Calendar time = Calendar.getInstance();
			time.set(Calendar.SECOND, c.get(Calendar.SECOND));
			time.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
			time.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY));
			return time.getTime();
		} catch (Exception e){
			return null;
		}
	}

	public Date getEndTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		try {
			Date date = sdf.parse(this.endTime);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			Calendar time = Calendar.getInstance();
			time.set(Calendar.SECOND, c.get(Calendar.SECOND));
			time.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
			time.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY));
			return time.getTime();
		} catch (Exception e){
			return null;
		}
	}

	public int getTopPowerRank() {
		return topPowerRank;
	}

	public int getAwardActorLevel() {
		return awardActorLevel;
	}

	public int getAtkCDTime() {
		return atkCDTime;
	}

	public int getReliveTime() {
		return reliveTime;
	}

	/**
	 * 比赛结束每次增加积分点
	 * @param result
	 * @return
	 */
	public int getPoint(CrossBattleResult result) {
		if (result == CrossBattleResult.FAIL) {
			return failPoint;
		}
		if (result == CrossBattleResult.DRAW) {
			return drawPoint;
		}
		if (result == CrossBattleResult.WIN) {
			return winPoint;
		}
		return 0;
	}
	
	/**
	 * 
	 * @param result	战斗结果
	 * @param actorLevel	当前角色等级
	 * @return
	 */
	public List<RewardObject> getReward(CrossBattleResult result,int actorLevel) {
		List<RewardObject> list = new ArrayList<>();
		if (result == CrossBattleResult.WIN) {
			for(ExprRewardObject reward : winDayRewardList) {
				list.add(reward.clone(actorLevel));
			}
			return list;
		}
		
		if (result == CrossBattleResult.FAIL) {
			for (ExprRewardObject reward : failDayRewardList) {
				list.add(reward.clone(actorLevel));
			}
			return list;
		}
		
		return list;
	}
	
	public int getDayExchangePoint(CrossBattleResult result) {
		if (result == CrossBattleResult.WIN) {
			return winDayExchangePoint;
		}
		if (result == CrossBattleResult.FAIL) {
			return failDayExchangePoint;
		}
		return 0;
	}
	
	public int getDayKillExchangePoint(CrossBattleResult result, int killNum) {
		if (result == CrossBattleResult.WIN) {
			return FormulaHelper.executeCeilInt(winDayKillExchangePoint, killNum);
		}
		if (result == CrossBattleResult.FAIL) {
			return FormulaHelper.executeCeilInt(failDayKillExchangePoint, killNum);
		}
		return 0;
	}
	

	public int getNotTargetPoint() {
		return notTargetPoint;
	}
	
	public int getJoinLevel() {
		return joinLevel;
	}

	public int getProtectTime() {
		return this.protectTime;
	}

}
