package com.jtang.gameserver.module.app.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dataconfig.model.AppRuleConfig;
import com.jtang.gameserver.module.app.type.AppKey;

/**
 * 活动全局vo
 * 由appGlobal表和appRuleConfig配置组成
 * @author 0x737263
 *
 */
public class AppGlobalVO extends IoBufferSerializer {

	/**
	 * 活动Id
	 */
	public long appId;
	
	/**
	 * 解析器id
	 */
	public int effect;
	
	/**
	 * 开始时间
	 */
	public int startTime;
	/**
	 * 结束时间
	 */
	public int endTime;

	/**
	 * 活动标题
	 */
	public String title;
	
	/**
	 * 图标
	 */
	public String imgeIcon;
	
	/**
	 * 活动图标标题
	 */
	public String imgeIconTitle;
	
	/**
	 * 活动标题
	 */
	public String imgeTitle;
	
	/**
	 * 活动内容
	 */
	public String text;
	
	/**
	 * 扩展配置
	 */
	public String rule;
	
	/**
	 * 奖励物品列表
	 */
	private List<RewardObject> rewardGoods;
	
	/**
	 * 扩展字段
	 * key: {@code AppKey}
	 */
	private Map<AppKey, Object> extMap;
	
	public AppGlobalVO(AppRuleConfig ruleConfig, Map<AppKey, Object> extMap,int level) {
		this.appId = ruleConfig.getAppId();
		this.effect = ruleConfig.getEffect();
		this.startTime = ruleConfig.getStartTime();
		this.endTime = ruleConfig.getEndTime();
		this.title = ruleConfig.getTitle();
		this.imgeIcon = ruleConfig.getImgeIcon();
		this.imgeIconTitle = ruleConfig.getImgeIconTitle();
		this.imgeTitle = ruleConfig.getImgeTitle();
		this.text = ruleConfig.getText();
		this.rule = ruleConfig.getRule();
		this.rewardGoods = ruleConfig.getRewardGoodsList(level,true);
		this.extMap = extMap;
	}
	public AppGlobalVO(AppRuleConfig ruleConfig,int level) {
		this.appId = ruleConfig.getAppId();
		this.effect = ruleConfig.getEffect();
		this.startTime = ruleConfig.getStartTime();
		this.endTime = ruleConfig.getEndTime();
		this.title = ruleConfig.getTitle();
		this.imgeIcon = ruleConfig.getImgeIcon();
		this.imgeIconTitle = ruleConfig.getImgeIconTitle();
		this.imgeTitle = ruleConfig.getImgeTitle();
		this.text = ruleConfig.getText();
		this.rule = ruleConfig.getRule();
		this.rewardGoods = ruleConfig.getRewardGoodsList(level,true);
		this.extMap = new HashMap<AppKey, Object>();
	}
	
	@Override
	public void write() {
		this.writeLong(this.appId);
		this.writeInt(this.effect);
		this.writeInt(this.startTime);
		this.writeInt(this.endTime);
		this.writeString(this.title);
		this.writeString(this.imgeIcon);
		this.writeString(this.imgeIconTitle);
		this.writeString(this.imgeTitle);
		this.writeString(this.text);
		this.writeString(this.rule);
		this.writeShort((short) rewardGoods.size());
		for (RewardObject rewardObject : rewardGoods) {
			this.writeInt(rewardObject.rewardType.getCode());
			this.writeInt(rewardObject.id);
			this.writeInt(rewardObject.num);
		}
		this.writeShort((short) extMap.size());
		for (Map.Entry<AppKey, Object> entry : extMap.entrySet()) {
			this.writeString(entry.getKey().getKey());
			this.writeObject(entry.getValue());
		}
	}

}
