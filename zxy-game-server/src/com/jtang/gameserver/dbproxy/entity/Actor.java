package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.module.user.constant.ActorRule;


/**
 * 角色表
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author 0x737263
 * 
 */
@TableName(name="actor", type = DBQueueType.IMPORTANT)
public class Actor extends Entity<Long> {
	private static final long serialVersionUID = 589297655559251750L;

	/**
	 * 平台类型id
	 */
	@Column
	public int platformType;
	
	/**
	 * 平台uid
	 */
	@Column
	public String uid;
	
	/**
	 * 创建角色的来源渠道id
	 */
	@Column
	public int channelId;
	
	/**
	 * 游戏服id
	 */
	@Column
	public int serverId;
	
	/**
	 * 角色Id
	 */
	@Column(pk = true)
	private long actorId;
	
	/**
	 * 角色名称
	 */
	@Column
	public String actorName;
	
	/**
	 * 选择的仙人Id
	 */
	@Column
	public int heroId;
	
	/**
	 * 角色等级
	 */
	@Column
	public int level;

	/**
	 * 经验值(声望)
	 */
	@Column
	public long reputation;
	
	/**
	 * 金币数
	 */
	@Column
	public long gold;
	
	/**
	 * 精力值
	 */
	@Column
	public int energy;
	
	/**
	 * 最大精力值
	 */
	@Column
	public int maxEnergy;
	
	/**
	 * 活力值
	 */
	@Column
	public int vit;
	
	/**
	 * 最大活力值
	 */
	@Column
	public int maxVit;
	
	/**
	 * 角色创建时间
	 */
	@Column
	public int createTime;
	
	/**
	 * 登陆时间 
	 */
	@Column
	public int loginTime;
	
	/**
	 * 退出时间
	 */
	@Column
	public int logoutTime;
	
	/**
	 * 登陆总天数(每天登陆都加1)
	 */
	@Column
	public int loginDays;
	
	/**
	 * 连续登陆天数(如果本次登录与上次登录超过一天, 则该值变为0)
	 */
	@Column
	public int continueDays;
	
	/**
	 * 最大连续登陆天数(保存历史最大的连续天数)
	 */
	@Column
	public int continueMaxDays;
	
	/**
	 * 最后一次恢复活力的时间
	 */
	@Column
	public int lastAddVitTime;
	
	/**
	 * 最后一次恢复精力的时间
	 */
	@Column
	public int lastAddEnergyTime;
	
	/**
	 * 气势值
	 */
	@Column
	public int morale;
	
	/**
	 * 新手引导存储区
	 * <pre>
	 * Map<Integer,Integer>
	 * key:引导类型  value:步骤
	 * </pre>
	 */
	@Column
	public String guides;
	
	/**
	 * 累加在线升级时长(每次上下线累积增加时长,当前升级后清零)
	 */
	@Column
	public int upgradeTime;
	
	@Column
	public String sim;
	
	@Column
	public String imei;
	
	@Column
	public String createIP;
	
	@Column
	public String createMAC;
	
	@Column
	public String androidPushkey;
	
	@Column
	public String iosPushKey;
	
	/**
	 * 修改名称次数
	 */
	@Column
	public int renameNum;
	
	/**
	 * 点券购买活力次数
	 */
	@Column
	public int vitNum;
	
	/**
	 * 点券购买精力次数
	 */
	@Column
	public int energyNum;
	
	/**
	 * 点券购买金币次数
	 */
	@Column
	public int goldNum;
	
	/**
	 * 购买操作时间
	 */
	@Column
	public int operationTime;
	
	/**
	 * 登陆时间(只用来记录oss)
	 */
	@Column
	public int loginTimeOSS;
	
	/**
	 * 新手引导map
	 */
	private Map<Integer, Integer> guidesMap = new ConcurrentHashMap<>();
	
	@Override
	public Long getPkId() {
		return actorId;
	}

	@Override
	public void setPkId(Long id) {
		this.actorId = id;
	}
	
	public void setLogoutTime() {
		this.logoutTime = TimeUtils.getNow();
	}
	
	/**
	 * 获取活力倒计时(秒)
	 * @return
	 */
	public int getVitCountdown() {			
		if (this.lastAddVitTime <= 0) {
			this.lastAddVitTime = TimeUtils.getNow();
		}
		
		int time = TimeUtils.getNow() - this.lastAddVitTime;
		if(time >= ActorRule.ACTOR_VIT_FIXED_TIME) {
			return 0;
		}
		return ActorRule.ACTOR_VIT_FIXED_TIME - time;
	}
	
	/**
	 * 获取精力倒计时(秒 )
	 * @return
	 */
	public int getEnergyCountdown() {		
		if (this.lastAddEnergyTime <= 0) {
			this.lastAddEnergyTime = TimeUtils.getNow();
		}
		
		int time = TimeUtils.getNow() - this.lastAddEnergyTime;
		if (time >= ActorRule.ACTOR_ENERGY_FIXED_TIME) {
			return 0;
		}
		return ActorRule.ACTOR_ENERGY_FIXED_TIME - time;
	}
	
	/**
	 * 获取新手引导列表
	 * @return
	 */
	public Map<Integer, Integer> getGuideMap() {
		return guidesMap;
	}
	
	/**
	 * 累加升级在线时长
	 */
	public void sumUpgradeTime() {
		this.upgradeTime += this.logoutTime - this.loginTime;
	}
	
	/**
	 * 升级后清空在线时长
	 */
	public void cleanUpgradeTime() {
		this.upgradeTime = 0;
	}
	
	/**
	 * 设置新手引导步骤
	 * @param key
	 * @param value
	 */
	public void addGuide(int key, int value) {
		Map<Integer, Integer> guideMap = getGuideMap();
		guideMap.put(key, value);
	}
	
	/**
	 * 计算连续登陆天数
	 * @return	是否有变化
	 */
	public boolean calculateContinueDays() {
		boolean setFlag = false;

		synchronized (this) {
			//上一次登陆时间
			int lastLoginTime = this.loginTime;
			//设置最新的登陆时间
			this.loginTime = TimeUtils.getNow();
			
			//最后登陆时间如果是昨天
			if (TimeUtils.inYesterday(lastLoginTime)) {
				this.continueDays += 1;
				setFlag = true;
				if (this.continueDays > this.continueMaxDays) {
					this.continueMaxDays = this.continueDays;
				}
			} else {
				//并且，也不是今天
				if (DateUtils.isToday(lastLoginTime) == false) {
					this.continueDays = 1;
					setFlag = true;
				}
			}

			//累计登陆天数
			if (DateUtils.isToday(lastLoginTime) == false) {
				this.loginDays += 1;
			}
		}

		return setFlag;
	}
	
	/**
	 * 创建Actor
	 * @param platformType
	 * @param uid
	 * @param channelId
	 * @param serverId
	 * @param heroId
	 * @param actorName
	 * @return
	 */
	public static Actor valueOf(int platformType, String uid, int channelId, int serverId, int heroId, String actorName, String ip, String sim,
			String mac, String imei) {
		Actor actor = new Actor();
		actor.platformType = platformType;
		actor.uid = uid;
		actor.channelId = channelId;
		actor.serverId = serverId;
		actor.actorName = actorName;
		actor.heroId = heroId;
		actor.level = ActorRule.ACTOR_INIT_LEVEL;
		actor.reputation = ActorRule.ACTOR_INIT_REPUTATION;
		actor.gold = ActorRule.ACTOR_INIT_GOLD;
		actor.energy = ActorRule.ACTOR_INIT_ENERGY;
		actor.maxEnergy = ActorRule.ACTOR_INIT_ENERGY;
		actor.vit = ActorRule.ACTOR_INIT_VIT;
		actor.maxVit = ActorRule.ACTOR_INIT_VIT;
		actor.createTime = TimeUtils.getNow();
		actor.loginTime  = 0;
		actor.logoutTime = 0;
		actor.loginDays = 0;
		actor.continueDays = 0;
		actor.continueMaxDays = 0;
		actor.lastAddVitTime = 0;
		actor.lastAddEnergyTime = 0;
		actor.morale = ActorRule.ACTOR_INIT_POWER;
		actor.guides = "";		
		actor.upgradeTime = 0;
		actor.sim = sim;
		actor.imei = imei;
		actor.createIP = ip;
		actor.createMAC = mac;
		actor.androidPushkey = "";
		actor.iosPushKey = "";
		actor.renameNum = 0;
		actor.energyNum = 0;
		actor.goldNum = 0;
		actor.vitNum = 0;
		actor.operationTime = 0;
		actor.loginTimeOSS = 0;
		return actor;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Actor actor = new Actor();
		actor.platformType = rs.getInt("platformType");
		actor.uid = rs.getString("uid");
		actor.channelId = rs.getInt("channelId");
		actor.serverId = rs.getInt("serverId");
		actor.actorId = rs.getLong("actorId");
		actor.actorName = rs.getString("actorName");
		actor.heroId = rs.getInt("heroId");
		actor.level = rs.getInt("level");
		actor.reputation = rs.getLong("reputation");
		actor.gold = rs.getLong("gold");
		actor.energy = rs.getInt("energy");
		actor.maxEnergy = rs.getInt("maxEnergy");
		actor.vit = rs.getInt("vit");
		actor.maxVit = rs.getInt("maxVit");
		actor.createTime = rs.getInt("createTime");
		actor.loginTime = rs.getInt("loginTime");
		actor.logoutTime = rs.getInt("logoutTime");
		actor.loginDays = rs.getInt("loginDays");
		actor.continueDays = rs.getInt("continueDays");
		actor.continueMaxDays = rs.getInt("continueMaxDays");
		actor.lastAddVitTime = rs.getInt("lastAddVitTime");
		actor.lastAddEnergyTime = rs.getInt("lastAddEnergyTime");
		actor.morale = rs.getInt("morale");
		actor.guides = rs.getString("guides");
		actor.upgradeTime = rs.getInt("upgradeTime");
		actor.sim = rs.getString("sim");
		actor.imei = rs.getString("imei");
		actor.createIP = rs.getString("createIP");
		actor.createMAC = rs.getString("createMAC");
		actor.androidPushkey = rs.getString("androidPushkey");
		actor.iosPushKey = rs.getString("iosPushKey");
		actor.renameNum = rs.getInt("renameNum");
		actor.energyNum = rs.getInt("energyNum");
		actor.goldNum = rs.getInt("goldNum");
		actor.vitNum = rs.getInt("vitNum");
		actor.operationTime = rs.getInt("operationTime");
		actor.loginTimeOSS = rs.getInt("loginTimeOSS");
		return actor;
	}

	@Override
	protected void hasReadEvent() {
		
		if (StringUtils.isNotBlank(this.guides)) {
			this.guidesMap = StringUtils.delimiterString2IntMap(this.guides);
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		value.add(this.platformType);
		value.add(this.uid);
		value.add(this.channelId);
		value.add(this.serverId);
		if (containsPK) {
			value.add(this.actorId);
		}
		value.add(this.actorName);
		value.add(this.heroId);
		value.add(this.level);
		value.add(this.reputation);
		value.add(this.gold);
		value.add(this.energy);
		value.add(this.maxEnergy);
		value.add(this.vit);
		value.add(this.maxVit);
		value.add(this.createTime);
		value.add(this.loginTime);
		value.add(this.logoutTime);
		value.add(this.loginDays);
		value.add(this.continueDays);
		value.add(this.continueMaxDays);
		value.add(this.lastAddVitTime);
		value.add(this.lastAddEnergyTime);
		value.add(this.morale);
		value.add(this.guides);
		value.add(this.upgradeTime);
		value.add(this.sim);
		value.add(this.imei);
		value.add(this.createIP);
		value.add(this.createMAC);
		value.add(this.androidPushkey);
		value.add(this.iosPushKey);
		value.add(this.renameNum);
		value.add(this.vitNum);
		value.add(this.energyNum);
		value.add(this.goldNum);
		value.add(this.operationTime);
		value.add(this.loginTimeOSS);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		this.guides = StringUtils.map2DelimiterString(this.getGuideMap(), Splitable.ATTRIBUTE_SPLIT, Splitable.ELEMENT_DELIMITER);
	}
	
	public void reset() {
		this.level = ActorRule.ACTOR_INIT_LEVEL;
		this.reputation = ActorRule.ACTOR_INIT_REPUTATION;
		this.gold = ActorRule.ACTOR_INIT_GOLD;
		this.energy = ActorRule.ACTOR_INIT_ENERGY;
		this.maxEnergy = ActorRule.ACTOR_INIT_ENERGY;
		this.vit = ActorRule.ACTOR_INIT_VIT;
		this.maxVit = ActorRule.ACTOR_INIT_VIT;
		this.lastAddVitTime = 0;
		this.lastAddEnergyTime = 0;
		this.morale = ActorRule.ACTOR_INIT_POWER;
		this.guides = "";		
		this.upgradeTime = 0;
		this.energyNum = 0;
		this.goldNum = 0;
		this.vitNum = 0;
	}
	
	@Override
	protected void disposeBlob() {
		this.guides = EMPTY_STRING;
	}
}
