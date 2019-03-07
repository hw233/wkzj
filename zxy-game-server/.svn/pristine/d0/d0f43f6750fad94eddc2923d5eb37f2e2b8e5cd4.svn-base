package com.jtang.gameserver.module.notify.facade.impl;
//package com.jtang.sm2.module.notify.facade.impl;
//
//import static com.jtang.sm2.module.StatusCodeConstant.*;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.jtang.core.result.Result;
//import com.jtang.core.result.TResult;
//import com.jtang.sm2.component.model.RewardObject;
//import com.jtang.sm2.dataconfig.service.SnatchService;
//import com.jtang.sm2.dbproxy.entity.Actor;
//import com.jtang.sm2.dbproxy.entity.FightVideo;
//import com.jtang.sm2.dbproxy.entity.Notification;
//import com.jtang.sm2.gateway.session.PlayerSession;
//import com.jtang.sm2.module.ally.facade.AllyFacade;
//import com.jtang.sm2.module.ally.model.AllyVO;
//import com.jtang.sm2.module.battle.constant.WinLevel;
//import com.jtang.sm2.module.equip.facade.EquipFacade;
//import com.jtang.sm2.module.equip.type.EquipAddType;
//import com.jtang.sm2.module.goods.facade.GoodsFacade;
//import com.jtang.sm2.module.goods.type.GoodsAddType;
//import com.jtang.sm2.module.notify.constant.NotifyRule;
//import com.jtang.sm2.module.notify.dao.FightVideoDao;
//import com.jtang.sm2.module.notify.dao.NotifyDao;
//import com.jtang.sm2.module.notify.facade.NotifyFacade;
//import com.jtang.sm2.module.notify.helper.NotifyPushHelper;
//import com.jtang.sm2.module.notify.model.AbstractNotifyVO;
//import com.jtang.sm2.module.notify.model.AllyFightNotifyVO;
//import com.jtang.sm2.module.notify.model.AllyNotifyVO;
//import com.jtang.sm2.module.notify.model.BaseNotifyVO;
//import com.jtang.sm2.module.notify.model.DemonRewardNotifyVO;
//import com.jtang.sm2.module.notify.model.GiveEquipNotifyVO;
//import com.jtang.sm2.module.notify.model.GiveGiftNotifyVO;
//import com.jtang.sm2.module.notify.model.HoleInviteNotifyVO;
//import com.jtang.sm2.module.notify.model.PowerNotifyVO;
//import com.jtang.sm2.module.notify.model.PowerRewardNotifyVO;
//import com.jtang.sm2.module.notify.model.SnatchAllyNotifyVO;
//import com.jtang.sm2.module.notify.model.SnatchNotifyVO;
//import com.jtang.sm2.module.notify.model.SnatchRevengeNotifyVO;
//import com.jtang.sm2.module.notify.model.StoryNotifyVO;
//import com.jtang.sm2.module.notify.model.ThankAllyNotifyVO;
//import com.jtang.sm2.module.notify.model.TrialNotifyVO;
//import com.jtang.sm2.module.notify.type.AttackType;
//import com.jtang.sm2.module.notify.type.BattleResultType;
//import com.jtang.sm2.module.notify.type.BooleanType;
//import com.jtang.sm2.module.notify.type.NotifyType;
//import com.jtang.sm2.module.notify.type.ThankAllyType;
//import com.jtang.sm2.module.snatch.type.SnatchEnemyType;
//import com.jtang.sm2.module.snatch.type.SnatchType;
//import com.jtang.sm2.module.user.facade.ActorFacade;
//import com.jtang.sm2.module.user.type.ReputationAddType;
//
///**
// * 
// * @author pengzy
// * 
// */
//@Component
//public class NotifyFacadeImpl implements NotifyFacade {
//	@Autowired
//	private NotifyDao notifyDao;
//	@Autowired
//	private FightVideoDao fightVideoDao;
//	@Autowired
//	private ActorFacade actorFacade;
//	@Autowired
//	private GoodsFacade goodsFacade;
//	@Autowired
//	private EquipFacade equipFacade;
//	@Autowired
//	private AllyFacade allyFacade;
//
//	@Autowired
//	private PlayerSession playerSession;
//	
//	@Override
//	public TResult<FightVideo> getFightVideo(long actorId, long nId) {
//		AbstractNotifyVO notifyVO = get(actorId, nId);
//		if (notifyVO == null) {
//			return TResult.valueOf(NOTIFICATION_UNEXISTS);
//		}
//		FightVideo fightVideo = fightVideoDao.get(nId);
//		if (fightVideo == null) {
//			return TResult.valueOf(NOTIFICATION_VIDEO_NOT_FOUND);
//		}
//		return TResult.sucess(fightVideo);
//	}
//	
//	@Override
//	public AbstractNotifyVO get(long actorId, long nId) {
//		return notifyDao.getNotifyVO(actorId, nId);
//	}
//
//	@Override
//	public List<Notification> getList(long ownerActorId) {
//		return notifyDao.getList(ownerActorId);
//	}
//
//	@Override
//	public Result getReward(long actorId, long nId) {
//		Notification notify = notifyDao.get(actorId, nId);
//		if (notify == null) {
//			return Result.valueOf(NOTIFICATION_UNEXISTS);
//		}
//		
//		NotifyType notifyType = NotifyType.get(notify.type);
//		if (notifyType != NotifyType.TRIAL && notifyType != NotifyType.STORY && notifyType != NotifyType.VIP_GIVE_EQUIP) {
//			return Result.valueOf(NOTIFICATION_NO_REWARD_TYPE);
//		}
//		
//		int reputationNum = 0;
//		ThankAllyType thankAllyType = ThankAllyType.NONE;
//		ReputationAddType reputationAddType = null;
//		GoodsAddType goodsAddType = null;
//		
//		Map<Integer, Integer> giveGoodsMaps = new HashMap<>();
//		Map<Integer, Integer> giveEquipMaps = new HashMap<>();
//
//		switch (notifyType) {
//		case TRIAL: {
//			TrialNotifyVO trailNotifyVo = (TrialNotifyVO) notify.getExtension();
//			if(trailNotifyVo.isGet == BooleanType.TRUE.getCode()) {
//				return Result.valueOf(NOTIFICATION_REWARD_ALREADY_GET);
//			}
//			trailNotifyVo.isGet = BooleanType.TRUE.getCode();
//			reputationNum = trailNotifyVo.allyReputation;
//			giveGoodsMaps = trailNotifyVo.rewards;
//			
//			thankAllyType = ThankAllyType.TRAIL;
//			reputationAddType = ReputationAddType.TRAIL_ALLY_REWARD;
//			goodsAddType = GoodsAddType.TRAIL_ALLY_AWARD;
//			break;
//		}
//		case STORY: {
//			StoryNotifyVO storyNotifyVo = (StoryNotifyVO) notify.getExtension();
//			if(storyNotifyVo.isGet == BooleanType.TRUE.getCode()) {
//				return Result.valueOf(NOTIFICATION_REWARD_ALREADY_GET);
//			}
//			storyNotifyVo.isGet = BooleanType.TRUE.getCode();
//			reputationNum = storyNotifyVo.allyReputation;
//			giveGoodsMaps = storyNotifyVo.rewards;
//			
//			thankAllyType = ThankAllyType.STORY;
//			reputationAddType = ReputationAddType.STORY_ALLY_REWARD;
//			goodsAddType = GoodsAddType.STORY_ALLY_AWARD;
//			break;
//		}
//		case VIP_GIVE_EQUIP: {
//			GiveEquipNotifyVO giveEquipNotifyVo = (GiveEquipNotifyVO)notify.getExtension();
//			if(giveEquipNotifyVo.isGet == BooleanType.TRUE.getCode()) {
//				return Result.valueOf(NOTIFICATION_REWARD_ALREADY_GET);
//			}
//			giveEquipNotifyVo.isGet = BooleanType.TRUE.getCode();
//			giveEquipMaps.put(giveEquipNotifyVo.equipId, giveEquipNotifyVo.equipNum);
//			thankAllyType = ThankAllyType.ACCEPT_GIVE_EQUIP;
//			break;
//		}
//		default:
//			break;
//		}
//		
//		notify.flushExtension();
//		notifyDao.update(notify);
//		
//		//添加声望
//		if (reputationNum > 0) {
//			actorFacade.addReputation(actorId, reputationAddType, reputationNum);
//		}
//		
//		//是否感谢对方
//		if (thankAllyType != ThankAllyType.NONE) {
//			createThankAlly(actorId, notify.fromActorId, thankAllyType);
//		}
//	
//		//添加奖励的物品
//		for (Entry<Integer, Integer> entry : giveGoodsMaps.entrySet()) {
//			goodsFacade.addGoodsVO(actorId, goodsAddType, entry.getKey(), entry.getValue());
//		}
//		
//		//添加奖励的装备
//		for (Entry<Integer, Integer> entry : giveEquipMaps.entrySet()) {
//			for (int i = 0; i < entry.getValue(); i++) {
//				equipFacade.addEquip(actorId, EquipAddType.VIP_GIVE_EQUIP, entry.getKey());
//			}
//		}
//		
//		return Result.valueOf();
//	}
//
//	@Override
//	public Result notifyAlly(long actorId, long nId) {
//		Notification notify = notifyDao.get(actorId, nId);
//		if (notify == null) {
//			return Result.valueOf(NOTIFICATION_UNEXISTS);
//		}
//		if (NotifyType.get(notify.type) != NotifyType.SNATCH) {
//			return Result.valueOf(NOTIFICATION_NO_NOTICE_TYPE);
//		}
//		
//		SnatchNotifyVO vo = (SnatchNotifyVO) notify.getExtension();
//		if(vo.isNoticeAlly == BooleanType.TRUE) {
//			return Result.valueOf(NOTIFICATION_ALREADY_NOTICE);
//		}
//		vo.isNoticeAlly = BooleanType.TRUE;
//		notify.flushExtension();
//		
//
//		notifyDao.update(notify);
//		
//		boolean isNotice = noticeAll(actorId, notify);
//		if (isNotice == false) {
//			return Result.valueOf(NOTIFICATION_ALLY_NOT_FOUND);
//		}
//		return Result.valueOf();
//	}
//
//	private boolean noticeAll(long actorId, Notification notify) {
//		Collection<AllyVO> allyCol = allyFacade.getAlly(actorId);
//		if (allyCol.size() < 1) {
//			return false;	
//		}
//
//		Actor actor = actorFacade.getActor(actorId);
//		SnatchNotifyVO fromVo = (SnatchNotifyVO) notify.getExtension();
//		long targetActorId = 0;
//		for (AllyVO allyVO : allyCol) {
//			// 若抢夺双方是盟友，则点通知盟友的时候不产生
//			if (notify.fromActorId == allyVO.actorId || notify.toActorId == allyVO.actorId) {
//				continue;
//			}
//			
//			if(notify.ownerActorId == notify.fromActorId) {
//				targetActorId = notify.toActorId;
//			} else {
//				targetActorId = notify.fromActorId;
//			}
//			
//			SnatchAllyNotifyVO vo = new SnatchAllyNotifyVO(fromVo.attackType,targetActorId,fromVo.snatchType,fromVo.goodsId,fromVo.goodsNum);
//			BattleResultType battleResult = BattleResultType.getByCode(notify.battleResult);
//			createNotify(NotifyType.SNATCH_ALLY, allyVO.actorId, actorId, allyVO.actorId, actor.actorName, actor.level, battleResult, vo);
//		}
//		return true;
//	}
//	
//	@Override
//	public Result setReaded(long toActorId) {
//		notifyDao.setReaded(toActorId);
//		return Result.valueOf();
//	}
//
//	@Override
//	public Result remove(long toActorId, List<Long> nIds) {
//		for (long id : nIds) {
//			remove(toActorId, id);
//		}
//		return Result.valueOf();
//	}
//
//	@Override
//	public Result remove(long actorId, long nId) {
//		Notification notify = notifyDao.remove(actorId, nId);
//		if (notify != null) {
//			NotifyPushHelper.pushRemoveNotify(actorId, nId);
//		}
//		return Result.valueOf();
//	}
//	
//	
//	@Override
//	public boolean ableSnatch(long actorId, long targetActorId, long nId) {
//		Notification notify = notifyDao.get(actorId, nId);
//		if (notify == null) {
//			return false;
//		}
//
//		if (notify.type == NotifyType.SNATCH.getCode()) {
//			if (notify.toActorId == targetActorId || notify.fromActorId == targetActorId) {
//				return true;
//			}
//			return false;
//		} else if (notify.type == NotifyType.SNATCH_ALLY.getCode()) {
//			SnatchAllyNotifyVO snatchAllyVo = (SnatchAllyNotifyVO) notify.getExtension();
//			return targetActorId == snatchAllyVo.targetActorId;
//		}
//		return false;
//	}
//	
//	/**
//	 * 创建一个通用的通知
//	 * @param type
//	 * @param ownerActorId
//	 * @param fromActorId
//	 * @param toActorId
//	 * @param toActorName
//	 * @param toActorLevel
//	 * @param battleResult
//	 * @param extensionVO
//	 */
//	private Notification createNotify(NotifyType type, long ownerActorId, long fromActorId, long toActorId, String toActorName, int toActorLevel,
//			BattleResultType battleResult, BaseNotifyVO extensionVO) {
//
//		Notification notify = notifyDao.createNotify(type, ownerActorId, fromActorId, toActorId, toActorName, toActorLevel, battleResult, extensionVO);
//		
//		//TODO 下面这段清除历史消息的都可以移到dao里做
//		boolean hasInbox = (ownerActorId == toActorId) ? true : false;
//		List<Notification> boxList = getListBox(ownerActorId, hasInbox);
//		if (boxList.size() >= NotifyRule.NOTIFICATION_MAX_NUM_LIMIT) {
//			List<Long> midList = new ArrayList<>();
//			for (int i = NotifyRule.NOTIFICATION_MAX_NUM_LIMIT; i < boxList.size(); i++) {
//				Notification n = boxList.get(i);
//				if (n != null) {
//					midList.add(n.getPkId());
//				}
//			}
//			remove(ownerActorId, midList);
//		}
//
//		NotifyPushHelper.pushNotify(ownerActorId, notify);
//		return notify;
//	}
//	
//	/**
//	 * 获取收/发箱通知列表
//	 * @param ownerActorId	拥有者角色id
//	 * @param hasInbox		是否在收件箱
//	 * @return
//	 */
//	private List<Notification> getListBox(long ownerActorId, boolean hasInbox) {
//		List<Notification> notifyBox = new ArrayList<>();
//
//		List<Notification> list = getList(ownerActorId);
//		for (Notification n : list) {
//			if (hasInbox) {
//				if (n.ownerActorId == n.toActorId) {
//					notifyBox.add(n);
//				}
//			} else {
//				if (n.ownerActorId == n.fromActorId) {
//					notifyBox.add(n);
//				}
//			}
//		}
//		return notifyBox;
//	}
//	
//	@Override
//	public void createSnatch(long actorId, int actorLevel, SnatchEnemyType enemyType, long targetActorId, String targetActorName,
//			int targetActorLevel, boolean isWin, SnatchType snatchType, int score, int goodsId, int goodsNum, int snatchedGoodsId,
//			int snatchedGoodsNum, String fightVideo) {
//		if (snatchType == SnatchType.SCORE) {
//			if (isWin) {
//				goodsNum = score;
//			} else {
//				snatchedGoodsNum = score;
//			}
//		}
//		
//		//如果是抢夺金币则需要过滤 金币为0的消息
//		if(snatchType == SnatchType.GOLD && goodsNum < 1) {
//			return;
//		}
//		
//		
//		//是否需要创建抢夺者的发送消息
//		//判断对方等级是否在范围内，如果不在则不需要创建一条发出的消息
//		boolean needCreateSenderNotify = SnatchService.snatchTargetInLevelLimitScope(actorLevel, enemyType, targetActorLevel); 
//		
//		// 发送给抢夺者的信息
//		if (needCreateSenderNotify) {
//			BattleResultType fromResultType = BattleResultType.getType(isWin);
//			SnatchNotifyVO vo = new SnatchNotifyVO(AttackType.ATTACKING.getCode(), snatchType.getType(), goodsId, goodsNum, snatchedGoodsId,
//					snatchedGoodsNum, BooleanType.FALSE);
//			createNotify(NotifyType.SNATCH, actorId, actorId, targetActorId, targetActorName, targetActorLevel, fromResultType, vo);
//		}
//		
//		//被抢夺者如果是真实玩家，才发送消息给TA
//		if (enemyType == SnatchEnemyType.ACTOR) {			
//			// 发送给被抢夺者的信息
//			BattleResultType toResultType = BattleResultType.getReverseResult(isWin);
//			SnatchNotifyVO targetVo = new SnatchNotifyVO(AttackType.ATTACKED.getCode(), snatchType.getType(), goodsId, goodsNum, snatchedGoodsId,
//					snatchedGoodsNum, BooleanType.FALSE);
//			Actor actor = actorFacade.getActor(actorId);
//			Notification notify = createNotify(NotifyType.SNATCH, targetActorId, actorId, targetActorId, actor.actorName, actor.level, toResultType,
//					targetVo);
//			fightVideoDao.create(notify.getPkId(), fightVideo);
//		}
//
//	}
//
//	@Override
//	public void createAllyFight(long actorId, long targetActorId, boolean isWin, Map<Long, Integer> moraleReward, int successNum, int failNum,
//			String fightVideo) {
//		int morale = moraleReward != null && moraleReward.containsKey(actorId) ? moraleReward.get(actorId) : 0;
//
//		AllyFightNotifyVO vo = new AllyFightNotifyVO(AttackType.ATTACKING.getCode(), morale, successNum, failNum);
//
//		BattleResultType fromResultType = BattleResultType.getType(isWin);
//		Actor targetActor = actorFacade.getActor(targetActorId);
//		// 发送给进行切磋者
//		createNotify(NotifyType.ALLY_FIGHT, actorId, actorId, targetActorId, targetActor.actorName, targetActor.level, fromResultType, vo);
//		
//		morale = moraleReward != null && moraleReward.containsKey(targetActorId) ? moraleReward.get(targetActorId) : 0;
//		AllyFightNotifyVO toVo = new AllyFightNotifyVO(AttackType.ATTACKED.getCode(), morale, failNum, successNum);
//		Actor actor = actorFacade.getActor(actorId);
//		
//		// 发送给被切磋者
//		BattleResultType toResultType = BattleResultType.getReverseResult(isWin);
//		Notification notify = createNotify(NotifyType.ALLY_FIGHT, targetActorId, actorId, targetActorId, actor.actorName, actor.level, toResultType, toVo);
//		fightVideoDao.create(notify.getPkId(), fightVideo);
//	}
//
//	@Override
//	public void createTrial(long actorId, long allyActorId, int battleId, boolean isWin, int allyReputation, Map<Integer, Integer> rewardGoods,String fightVideo) {
//		if(allyActorId < 1) {
//			return;
//		}
//		
//		TrialNotifyVO vo = new TrialNotifyVO(battleId, allyReputation, rewardGoods, BooleanType.FALSE.getCode());
//		BattleResultType type = BattleResultType.getType(isWin);
//		Actor actor = actorFacade.getActor(actorId);
//		
//		// 发送给受邀请者
//		Notification notify = createNotify(NotifyType.TRIAL, allyActorId, actorId, allyActorId, actor.actorName, actor.level, type, vo);
//		fightVideoDao.create(notify.getPkId(), fightVideo);
//	}
//
//	@Override
//	public void createStory(long actorId, long allyActorId, int battleId, boolean isWin, int allyReputation, Map<Integer, Integer> rewardGoods) {
//		if(allyActorId < 1) {
//			return;
//		}
//		
//		StoryNotifyVO vo = new StoryNotifyVO(battleId, allyReputation, rewardGoods, BooleanType.FALSE.getCode());
//		BattleResultType type = BattleResultType.getType(isWin);
//		Actor actor = actorFacade.getActor(actorId);
//		// 发送给受邀请者
//		createNotify(NotifyType.STORY, allyActorId, actorId, allyActorId, actor.actorName, actor.level, type, vo);
////		Notify notify = 
////		fightVideoDao.create(notify.getPkId(), fightVideo);
//	}
//
//	@Override
//	public void createAddAlly(long actorId, long allyActorId) {
//		BattleResultType type = BattleResultType.NONE;
//
//		// 发送给添加盟友者
//		Actor allyActor = actorFacade.getActor(allyActorId);
//		AllyNotifyVO vo = new AllyNotifyVO(AttackType.ATTACKING.getCode());
//		createNotify(NotifyType.ADD_ALLY, actorId, actorId, allyActorId, allyActor.actorName, allyActor.level, type, vo);
//
//		// 发送给被添加者
//		Actor actor = actorFacade.getActor(actorId);
//		AllyNotifyVO targetVo = new AllyNotifyVO(AttackType.ATTACKED.getCode());
//		createNotify(NotifyType.ADD_ALLY, allyActorId, actorId, allyActorId, actor.actorName, actor.level, type, targetVo);
//	}
//
//	@Override
//	public void createRemoveAlly(long actorId, long allyActorId) {
//		BattleResultType type = BattleResultType.NONE;
//
//		// 发送给删除盟友者
//		Actor allyActor = actorFacade.getActor(allyActorId);
//		AllyNotifyVO vo = new AllyNotifyVO(AttackType.ATTACKING.getCode());
//		createNotify(NotifyType.REMOVE_ALLY, actorId, actorId, allyActorId, allyActor.actorName, allyActor.level, type, vo);
//
//		// 发送给被删除者
//		Actor actor = actorFacade.getActor(actorId);
//		AllyNotifyVO targetVo = new AllyNotifyVO(AttackType.ATTACKED.getCode());
//		createNotify(NotifyType.REMOVE_ALLY, allyActorId, actorId, allyActorId, actor.actorName, actor.level, type, targetVo);
//	}
//
//	@Override
//	public void createPowerFight(boolean isWin, boolean isCaptureRankSuccess, long actorId, int rank, long targetActorId, int targetRank,
//			String fightVideo) {
//		
//		// 发送给挑战者
//		BattleResultType fromResultType = BattleResultType.getType(isWin);
//		Actor targetActor = actorFacade.getActor(targetActorId);
//		PowerNotifyVO vo = new PowerNotifyVO(AttackType.ATTACKING.getCode(), isCaptureRankSuccess ? 1 : 0, rank, targetRank);
//		createNotify(NotifyType.POWER_RANK_CHALLENGE, actorId, actorId, targetActorId, targetActor.actorName, targetActor.level, fromResultType, vo);
//
//		// 发送给被挑战者
//		BattleResultType toResultType = BattleResultType.getReverseResult(isWin);
//		Actor actor = actorFacade.getActor(actorId);
//		PowerNotifyVO targetVo = new PowerNotifyVO(AttackType.ATTACKED.getCode(), isCaptureRankSuccess ? 1 : 0, rank, targetRank);
//		Notification notify = createNotify(NotifyType.POWER_RANK_CHALLENGE, targetActorId, actorId, targetActorId, actor.actorName, actor.level, toResultType, targetVo);
//		fightVideoDao.create(notify.getPkId(), fightVideo);
//	}
//	
//	@Override
//	public void createPowerReward(long actorId, int rank, int heroSoulId, int heroSoulNum, Map<Integer, Integer> goods) {
//		PowerRewardNotifyVO vo = new PowerRewardNotifyVO(rank, heroSoulId, heroSoulNum, goods);
//		Actor actor = actorFacade.getActor(actorId);
//		
//		if (heroSoulNum < 1) {
//			heroSoulId = 0;
//		}
//		createNotify(NotifyType.POWER_REWARD, actorId, 0, actorId, actor.actorName, actor.level, BattleResultType.WIN, vo);
//	}
//
//	@Override
//	public void createHole(long actorId, long toActorId, long id) {
//		HoleInviteNotifyVO vo = new HoleInviteNotifyVO(id);
//		Actor actor = actorFacade.getActor(actorId);
//		createNotify(NotifyType.HOLE_INVITE_ALLY, toActorId, actorId, toActorId, actor.actorName, actor.level, BattleResultType.NONE, vo);
//	}
//
//	@Override
//	public void createThankAlly(long actorId, long toActorId, ThankAllyType type) {
//		ThankAllyNotifyVO vo = new ThankAllyNotifyVO(type.getId());
//		Actor actor = actorFacade.getActor(actorId);
//		createNotify(NotifyType.THANK_ALLY, toActorId, actorId, toActorId, actor.actorName, actor.level, BattleResultType.NONE, vo);
//	}
//
//	@Override
//	public void createGiveGift(long actorId, long toActorId) {
//		GiveGiftNotifyVO vo = new GiveGiftNotifyVO();
//		Actor actor = actorFacade.getActor(actorId);
//		createNotify(NotifyType.GIVE_GIFT, toActorId, actorId, toActorId, actor.actorName, actor.level, BattleResultType.NONE, vo);
//	}
//
//	@Override
//	public void createSnatchRevenge(long actorId, long notifyId, SnatchType snatchType, int goodsId, int goodsNum, WinLevel winLevel) {
//		Notification notify = notifyDao.get(actorId, notifyId);
//		if (notify == null) {
//			return;
//		}
//		NotifyType notifyType = NotifyType.get(notify.type);
//		if (notifyType == NotifyType.SNATCH_ALLY) {
//			SnatchAllyNotifyVO snatchAllyNotifyVo = (SnatchAllyNotifyVO) notify.getExtension();
//			SnatchRevengeNotifyVO vo = new SnatchRevengeNotifyVO(snatchType.getType(), goodsId, goodsNum, snatchAllyNotifyVo.targetActorId);
//			Actor actor = actorFacade.getActor(actorId);
//			BattleResultType battleResultType = winLevel.isWin() ? BattleResultType.WIN : BattleResultType.FAIL;
//			long toActorId = notify.fromActorId;
//			createNotify(NotifyType.SNATCH_REVENGE, toActorId, actorId, toActorId, actor.actorName, actor.level, battleResultType, vo);
//		}
//	}
//
//	@Override
//	public void createGiveEquip(long actorId, long toActorId, int equipId, int equipNum) {
//		Actor actor = actorFacade.getActor(actorId);
//		GiveEquipNotifyVO vo = new GiveEquipNotifyVO(equipId, equipNum, (byte) 0);
//		createNotify(NotifyType.VIP_GIVE_EQUIP, toActorId, actorId, toActorId, actor.actorName, actor.level, BattleResultType.NONE, vo);
//	}
//
//	@Override
//	public void createDemonReward(long actorId, List<RewardObject> firstDemonReward, List<RewardObject> featsRankReward,
//			List<RewardObject> winCampReward, List<RewardObject> useTicketReward, long rewardScore, byte isWin, int rank) {
//		DemonRewardNotifyVO vo = new DemonRewardNotifyVO(firstDemonReward, featsRankReward, winCampReward, useTicketReward, rewardScore, isWin, rank);
//		createNotify(NotifyType.DEMON_REWARD, actorId, 0, actorId, "", 0, BattleResultType.NONE, vo);
//	}
//
//}
