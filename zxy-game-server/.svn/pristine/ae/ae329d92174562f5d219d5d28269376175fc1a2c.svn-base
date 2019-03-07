package com.jtang.gameserver.module.notify.facade;

import java.util.List;
import java.util.Map;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.FightVideo;
import com.jtang.gameserver.module.battle.constant.WinLevel;
import com.jtang.gameserver.module.notify.model.AbstractNotifyVO;
import com.jtang.gameserver.module.snatch.type.SnatchEnemyType;

public interface NotifyFacade {
	
	/**
	 * 获取战斗录像
	 * @param actorId
	 * @param nId
	 * @return
	 */
	TResult<FightVideo> getFightVideo(long actorId, long nId);
	
	/**
	 * 获取单条通知
	 * @param actorId
	 * @param nId
	 * @return
	 */
	AbstractNotifyVO get(long actorId, long nId);
	
	/**
	 * 获取toActorId的所有通知
	 * @param toActorId
	 * @return
	 */
	List<AbstractNotifyVO> getList(long toActorId);
	
	/**
	 * 领取该信息通知的奖励附件
	 * @param toActorId
	 * @param nId
	 * @return
	 */
	Result getReward(long toActorId, long nId);
	
	/**
	 * 若该信息通知可以转通知给盟友，则执行通知，并修改此通知为已通知状态
	 * @param toActorId
	 * @param nId
	 * @return
	 */
	Result notifyAlly(long toActorId, long nId);
	
	/**
	 * 将信息设置为已读
	 * @param toActorId
	 * @param nIds 
	 * @return
	 */
	Result setReaded(long toActorId, List<Long> nIds);
	
	/**
	 * 删除多条通知
	 * @param toActorId
	 * @param nIds
	 * @return
	 */
	Result remove(long toActorId, List<Long> nIds);
	
	/**
	 * 删除一条通知
	 * @param toActorId
	 * @param nId
	 * @return
	 */
	Result remove(long toActorId, long nId);
	
	/**
	 * 可否抢夺：通过系统信息进行抢夺需调用此方法判断，可能是反抢或盟友参与抢夺
	 * @param actorId
	 * @param targetActorId
	 * @param nId 通知的Id
	 * @return
	 */
	boolean ableSnatch(long actorId, long targetActorId, long nId);
	
	/**
	 * 创建抢夺通知信息
	 * @param actorId
	 * @param actorLevel
	 * @param enemyType
	 * @param targetActorId
	 * @param targetActorName
	 * @param targetActorLevel
	 * @param isWin
	 * @param snatchType
	 * @param score
	 * @param goodsId
	 * @param goodsNum
	 * @param snatchedGoodsId
	 * @param snatchedGoodsNum
	 * @param fightVideo
	 */
	void createSnatch(long actorId, int actorLevel, SnatchEnemyType enemyType, long targetActorId, String targetActorName, int targetActorLevel, boolean isWin,
			int score, int goodsId, int goodsNum, int snatchedGoodsId, int snatchedGoodsNum, byte[] fightVideo);
	
	/**
	 * 创建切磋信息通知
	 * @param actorId
	 * @param targetActorId
	 * @param isWin
	 * @param rewards
	 * @param successNum
	 * @param failNum
	 * @param fightVideo
	 */
	void createAllyFight(long actorId, long targetActorId, boolean isWin, Map<Long, Integer> rewards, int successNum, int failNum, byte[] fightVideo);
	
	/**
	 * 创建试炼通知
	 * @param actorId
	 * @param allyActorId
	 * @param battleId
	 * @param isWin
	 * @param allyReputation
	 * @param rewardGoods
	 * @param fightVideo
	 */
	void createTrial(long actorId, long allyActorId, int battleId, boolean isWin, int allyReputation, Map<Integer, Integer> rewardGoods,
			byte[] fightVideo);
	
	/**
	 * 创建故事通关的系统通知
	 * @param actorId
	 * @param allyActorId 盟友Id
	 * @param battleId
	 * @param isWin
	 * @param allyReputation
	 * @param rewardGoods
	 * @param isReward
	 */
	void createStory(long actorId, long allyActorId, int battleId, boolean isWin, int allyReputation, Map<Integer, Integer> rewardGoods,boolean isReward);
	
	/**
	 * 添加盟友信息通知
	 * @param actorId
	 * @param allyActorId
	 */
	void createAddAlly(long actorId, long allyActorId);
	
	/**
	 * 删除盟友信息通知
	 * @param actorId
	 * @param allyActorId
	 */
	void createRemoveAlly(long actorId, long allyActorId);
	
	/**
	 * 势力排行挑战成功后发送的信息
	 * @param isWin
	 * @param isCaptureRankSuccess
	 * @param actorId
	 * @param rank
	 * @param targetActorId
	 * @param targetRank
	 */
	void createPowerFight(boolean isWin, boolean isCaptureRankSuccess, long actorId, int rank, long targetActorId, int targetRank, byte[] fightVideo);
	
//	/**
//	 * 势力排行奖励通知信息
//	 * @param actorId
//	 * @param rank
//	 * @param heroSoulId
//	 * @param heroSoulNum
//	 * @param goods
//	 */
//	void createPowerReward(long actorId, int rank, int heroSoulId, int heroSoulNum, Map<Integer, Integer> goods);

	/**
	 * 创建洞府通知
	 * @param actorId		发送者角色id
	 * @param toActorId		接收者角色id
	 * @param id			洞府自增id
	 * @param holeId       触发洞府配置id
	 */
	void createHole(long actorId, long toActorId, long id, int holeId);
	
//	/**
//	 * 创建感谢对方
//	 * @param actorId	发送者角色id
//	 * @param toActorId	接收者角色id
//	 * @param type		感谢类型
//	 */
//	void createThankAlly(long actorId, long toActorId, ThankAllyType type);
	
//	/**
//	 * 送礼给对方发一个通知
//	 * @param actorId		发送者角色id
//	 * @param toActorId		接收者角色id
//	 */
//	void createGiveGift(long actorId,long toActorId);
	
	/**
	 * 抢夺报仇通知对方
	 * @param actorId
	 * @param notifyId
	 * @param snatchType
	 * @param goodsId
	 * @param goodsNum
	 * @param winLevel
	 */
	void createSnatchRevenge(long actorId, long notifyId, int goodsId, int goodsNum, WinLevel winLevel);

	/**
	 * 赠送装备(天财地宝)
	 * @param actorId		发送者角色id
	 * @param toActorId		接收者角色id
	 * @param equipId		装备id
	 * @param equipNum		装备数量
	 */
	void createGiveEquip(long actorId, long toActorId, int equipId,int equipNum);

//	/**
//	 * 集众降魔奖励
//	 * @param actorId				角色id
//	 * @param firstDemonReward		第一名奖励
//	 * @param featsRankReward		功勋值排名奖励
//	 * @param winCampReward			获胜阵营奖励
//	 * @param useTicketReward		使用点券奖励
//	 * @param rewardScore			奖励积分
//	 * @param isWin					阵营是否获胜 1:获胜，0：失败
//	 * @param rank					最终排名
//	 */
//	void createDemonReward(long actorId, List<RewardObject> firstDemonReward, List<RewardObject> featsRankReward, List<RewardObject> winCampReward,
//			List<RewardObject> useTicketReward, long rewardScore, byte isWin, int rank);
	
}
