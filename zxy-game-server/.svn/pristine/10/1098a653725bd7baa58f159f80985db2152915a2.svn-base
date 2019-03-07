package com.jtang.gameserver.module.user.facade;

import java.util.List;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.user.handler.response.ActorBuyResponse;
import com.jtang.gameserver.module.user.model.ActorInfo;
import com.jtang.gameserver.module.user.type.EnergyAddType;
import com.jtang.gameserver.module.user.type.EnergyDecreaseType;
import com.jtang.gameserver.module.user.type.GoldAddType;
import com.jtang.gameserver.module.user.type.GoldDecreaseType;
import com.jtang.gameserver.module.user.type.ReputationAddType;
import com.jtang.gameserver.module.user.type.VITAddType;
import com.jtang.gameserver.module.user.type.VITDecreaseType;

public interface ActorFacade {

//	/**
//	 * 用户token验证
//	 * @param platformId	平台id
//	 * @param token			平台令牌
//	 * @return				平台uid
//	 */
//	TResult<PlatformLoginResult> validateToken(int platformId, String token);
	
	/**
	 * 根据uid获取当前服的角色Id
	 * @param platformId	平台Id
	 * @param uid			平台uid
	 * @param serverId		选择的游戏服Id
	 * @param actorId       角色id
	 * @return actor        角色实体
	 */
	Actor getActorId(int platformId, String uid, int serverId,long actorId);
	
	/**
	 * 获取角色id
	 * @param platformId	平台Id
	 * @param uid			平台uid
	 * @param serverId		选择的游戏服Id
	 * @return
	 */
	List<ActorInfo> getActorId(int platformType, String uid, int serverId);
	
	/**
	 * 获取角色信息
	 * @param actorId
	 * @return
	 */
	Actor getActor(long actorId);
	
	/**
	 * 角色id是否存在
	 * @param actorId
	 * @return
	 */
	boolean isExists(long actorId);
	
	/**
	 * 创建角色
	 * @param platformType	平台类型
	 * @param uid			平台uid
	 * @param channelId		渠道id
	 * @param serverId		选择的游戏服Id
	 * @param heroId		选择的仙人Id
	 * @param actorName		角色名
	 * @param ip			ip地址
	 * @param sim			sim信息
	 * @param mac			mac地址
	 * @param imei			imei号
	 * @return
	 */
	TResult<Long> createActor(int platformType, String uid, int channelId, int serverId, int heroId, String actorName, String ip, String sim,
			String mac, String imei);
	
	/**
	 * 增加声望（声望满足后自动推送升级数据)
	 * @param actorId			角色id
	 * @param type				声望添加类型
	 * @param reputationNum		增加的声望值
	 * @return
	 */
	boolean addReputation(long actorId, ReputationAddType type, long reputationNum);
	
	/**
	 * 是否有足够金币
	 * @param actorId		角色id
	 * @param goldNum		金币数量
	 * @return
	 */
	boolean hasGold(long actorId,int goldNum);
	
	/**
	 * 添加金币(自动推送变更值)
	 * @param actorId		角色Id
	 * @param type			金币添加类型
	 * @param goldNum		添加的金币数	
	 * @return
	 */
	boolean addGold(long actorId, GoldAddType type, long goldNum);

	/**
	 * 扣除金币(自动推送变更值)
	 * @param actorId		角色id
	 * @param type			扣除类型
	 * @param goldNum		金币数量
	 * @return
	 */
	boolean decreaseGold(long actorId, GoldDecreaseType type, int goldNum);

	/**
	 * 增加精力值(自动推送变更值)
	 * @param actorId		角色Id
	 * @param energyAddType 精力添加类型
	 * @param energyNum		精力值
	 * @return
	 */
	boolean addEnergy(long actorId, EnergyAddType energyAddType, int energyNum);
	
	/**
	 * 补满精力值(自动推送变更值)
	 * @param actorId		角色Id
	 * @param energyAddType 精力添加类型
	 * @return
	 */
	boolean fullEnergy(long actorId, EnergyAddType energyAddType);
	
	/**
	 * 增加精力上限值(自动推送变更值)
	 * @param actorId		角色Id
	 * @param vitLimitNum	新增加的精力上限值
	 * @return
	 */
	boolean addEnergyLimit(long actorId, int vitLimitNum);

	/**
	 * 扣除精力值(自动推送变更值)
	 * @param actorId		角色Id
	 * @param type			精力扣除类型
	 * @param energyNum		精力值
	 * @return
	 */
	boolean decreaseEnergy(long actorId, EnergyDecreaseType type, int energyNum);

	/**
	 * 添加活力值(自动推送变更值)
	 * @param actorId	角色Id
	 * @param type		活力增加类型
	 * @param vitNums	活力值
	 * @return
	 */
	boolean addVIT(long actorId, VITAddType type, int vitNums);
	
	/**
	 * 被满活力值(自动推送变更值)
	 * @param actorId	角色Id
	 * @param type		活力增加类型
	 * @return
	 */
	boolean fullVIT(long actorId, VITAddType type);
	
	/**
	 * 增加活力上限(自动推送变更值)
	 * @param actorId		角色id
	 * @param vitMaxNum		新增加的活力上限值
	 * @return
	 */
	boolean addVITLimit(long actorId, int vitMaxNum);

	/**
	 * 扣除活力值(自动推送变更值)
	 * @param actorId	角色id
	 * @param type		活力扣除类型
	 * @param vitNums	活力值
	 * @return
	 */
	boolean decreaseVIT(long actorId, VITDecreaseType type, int vitNums);

	/**
	 * 添加气势(自动推送变更值)
	 * @param actorId	角色id
	 * @param morale	气势值
	 */
	void addMorale(long actorId, int morale);
	
	/**
	 * 扣除气势
	 */
	int costMorale(long actorId,int morale);
	
	/**
	 * 设置新手引导步骤 
	 * @param actorId	角色id
	 * @param key		引导类型id
	 * @param value		引导值
	 */
	void saveGuideStep(long actorId, int key,int value);
	
	/**
	 * 保存百度云推送服务key
	 * @param actorId	角色id
	 * @param type		类型
	 * @param pushKey	key
	 */
	void savePushKey(long actorId, int type, String pushKey);
	
	/**
	 * 后续增加接口
	 * >保存用户新手引导步骤
	 * >领取教学信息奖励
	 * >保存玩家防沉迷信息
	 */
	
	
	/**
	 * 修改玩家名字
	 * 
	 */
	Result rename(long actorId,String actorName);

	/**
	 * 点券补满精力
	 * @param actorId
	 * @return
	 */
	Result costTicketFullEnergy(long actorId);
	
	/**
	 * 点券补满活力
	 * @param actorId
	 * @return
	 */
	Result costTicketFullVit(long actorId);
	
	/**
	 * 点券购买金币
	 * @param actorId
	 * @return
	 */
	Result costTicketBuyGold(long actorId);
	
	/**
	 * 是否可以增加精力
	 */
	boolean isAddEnergy(long actorId,EnergyAddType type);
	
	/**
	 * 是否可以增加活力
	 */
	boolean isAddVit(long actorId,VITAddType type);
	
	/**
	 * 获取角色主页购买信息
	 * @param actorId
	 * @return
	 */
	TResult<ActorBuyResponse> getActorBuy(long actorId);

}
