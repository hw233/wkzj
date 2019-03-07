package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RandomExprRewardObject;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.extension.BaseRuleConfigVO;
/**
 * 活动规则配置
 * @author ludd
 *
 */
@DataFile(fileName = "appRuleConfig")
public class AppRuleConfig implements ModelAdapter {

	private int appId;
	
	private String rewardGoods;
	
	private String rule;
	
	private String startTime;
	
	private String endTime;
	
	private String openChannelId;
	
	private int effect;
	
	private String text;
	
	private String title;
	
	private String imgeIcon;
	
	private String imgeIconTitle;
	
	private String imgeTitle;
	
	@FieldIgnore
	private List<RandomExprRewardObject> rewardGoodsList = new ArrayList<RandomExprRewardObject>();
	@FieldIgnore
	private List<Integer> chanelIds = new ArrayList<>();
	
	@FieldIgnore
	private int start;
	
	@FieldIgnore
	private int end;
	
	@FieldIgnore
	private BaseRuleConfigVO configRuleVO;
	
	@Override
	public void initialize() {
		Date dateStart = DateUtils.string2Date(startTime, "yyyy-MM-dd HH:mm:ss");
		Long ls = dateStart.getTime() / 1000;
		start = ls.intValue();
		Date dateEnd = DateUtils.string2Date(endTime, "yyyy-MM-dd HH:mm:ss");
		Long le = dateEnd.getTime() / 1000;
		end = le.intValue();
		
		List<String[]> items = StringUtils.delimiterString2Array(this.rewardGoods);
		for (String[] item : items) {
			RandomExprRewardObject rewardObejct = RandomExprRewardObject.valueOf(item);
			rewardGoodsList.add(rewardObejct);
		}
		chanelIds = StringUtils.delimiterString2IntList(openChannelId, Splitable.ATTRIBUTE_SPLIT);
		
		this.startTime = null;
		this.endTime = null;
		this.openChannelId = null;
	}
	
	public List<RandomExprRewardObject> getRewardGoodsList() {
		return rewardGoodsList;
	}
	
	
	/**
	 * 获取奖励
	 * @param level 玩家等级
	 * @param isAll 多个随机一个false , 所有全给true
	 * @return
	 */
	public List<RewardObject> getRewardGoodsList(int level,boolean isAll){
		int rate = 0;
		int random = RandomUtils.nextInt(0, 1000);
		List<RewardObject> rewardList = new ArrayList<>();
		for(RandomExprRewardObject reward:rewardGoodsList){
			rate += reward.rate;
			if(random < rate){
				reward.calculateNum(level);
				RewardObject rewardObject = new RewardObject(reward.rewardType, reward.id, reward.num);
				rewardList.add(rewardObject);
				if(isAll == false){
					return rewardList;
				}
			}
		}
		return rewardList;
	}
	
	public int getStartTime() {
		return start;
	}
	
	public int getEndTime() {
		return end;
	}
	
	public RewardObject getRewardGoods(int goodsId) {
		for (RewardObject obj : rewardGoodsList) {
			if (obj.id == goodsId) {
				return obj;
			}
		}
		return null;
	}
	
	public void setConfigRuleVO(BaseRuleConfigVO configRuleVO) {
		this.configRuleVO = configRuleVO;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getConfigRuleVO() {
		return (T)configRuleVO;
	}
	
	public long getAppId() {
		return appId;
	}
	
	public String getRule() {
		return rule;
	}
	
	public List<Integer> getChanelIds() {
		return chanelIds;
	}

	public int getEffect() {
		return effect;
	}

	public String getText() {
		return text;
	}

	public String getTitle() {
		return title;
	}

	public String getImgeIcon() {
		return imgeIcon;
	}

	public void setImgeIcon(String imgeIcon) {
		this.imgeIcon = imgeIcon;
	}

	public String getImgeIconTitle() {
		return imgeIconTitle;
	}

	public void setImgeIconTitle(String imgeIconTitle) {
		this.imgeIconTitle = imgeIconTitle;
	}

	public String getImgeTitle() {
		return imgeTitle;
	}

	public void setImgeTitle(String imgeTitle) {
		this.imgeTitle = imgeTitle;
	}
	
}
