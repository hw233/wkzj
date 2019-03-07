package com.jtang.gameserver.module.notify.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.NOTIFICATION_ALLY_NOT_FOUND;
import static com.jiatang.common.GameStatusCodeConstant.NOTIFICATION_ALREADY_NOTICE;
import static com.jiatang.common.GameStatusCodeConstant.NOTIFICATION_NO_NOTICE_TYPE;
import static com.jiatang.common.GameStatusCodeConstant.NOTIFICATION_NO_REWARD_TYPE;
import static com.jiatang.common.GameStatusCodeConstant.NOTIFICATION_REWARD_ALREADY_GET;
import static com.jiatang.common.GameStatusCodeConstant.NOTIFICATION_UNEXISTS;
import static com.jiatang.common.GameStatusCodeConstant.NOTIFICATION_VIDEO_NOT_FOUND;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.service.SnatchService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.FightVideo;
import com.jtang.gameserver.dbproxy.entity.Notify;
import com.jtang.gameserver.module.ally.facade.AllyFacade;
import com.jtang.gameserver.module.ally.model.AllyVO;
import com.jtang.gameserver.module.battle.constant.WinLevel;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.icon.facade.IconFacade;
import com.jtang.gameserver.module.lineup.facade.LineupFacade;
import com.jtang.gameserver.module.notify.constant.NotifyRule;
import com.jtang.gameserver.module.notify.dao.FightVideoDao;
import com.jtang.gameserver.module.notify.dao.NotifyDao;
import com.jtang.gameserver.module.notify.facade.NotifyFacade;
import com.jtang.gameserver.module.notify.helper.NotifyPushHelper;
import com.jtang.gameserver.module.notify.model.AbstractNotifyVO;
import com.jtang.gameserver.module.notify.model.impl.AllyFightNotifyVO;
import com.jtang.gameserver.module.notify.model.impl.AllyNotifyVO;
import com.jtang.gameserver.module.notify.model.impl.GiveEquipNotifyVO;
import com.jtang.gameserver.module.notify.model.impl.HoleInviteNotifyVO;
import com.jtang.gameserver.module.notify.model.impl.PowerNotifyVO;
import com.jtang.gameserver.module.notify.model.impl.SnatchAllyNotifyVO;
import com.jtang.gameserver.module.notify.model.impl.SnatchNotifyVO;
import com.jtang.gameserver.module.notify.model.impl.StoryNotifyVO;
import com.jtang.gameserver.module.notify.model.impl.TrialNotifyVO;
import com.jtang.gameserver.module.notify.type.AttackType;
import com.jtang.gameserver.module.notify.type.BattleResultType;
import com.jtang.gameserver.module.notify.type.BooleanType;
import com.jtang.gameserver.module.notify.type.FightVideoRemoveType;
import com.jtang.gameserver.module.notify.type.NotifyType;
import com.jtang.gameserver.module.notify.type.ThankAllyType;
import com.jtang.gameserver.module.snatch.type.SnatchEnemyType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.type.ReputationAddType;
import com.jtang.gameserver.server.session.PlayerSession;
//import com.jtang.sm2.module.notify.model.impl.PowerRewardNotifyVO;

/**
 * 通知逻辑实现类
 * @author 0x737263
 *
 */
@Component
public class NotifyFacadeImpl1 implements NotifyFacade,ActorLoginListener { 
	private static final Logger LOGGER = LoggerFactory.getLogger(NotifyFacadeImpl1.class);
	@Autowired
	private NotifyDao notifyDao;
	@Autowired
	private FightVideoDao fightVideoDao;
	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private GoodsFacade goodsFacade;
	@Autowired
	private EquipFacade equipFacade;
	@Autowired
	private AllyFacade allyFacade;
	@Autowired
	private PlayerSession playerSession;
	@Autowired
	private LineupFacade lineupFacade;
	@Autowired
	private IconFacade iconFacade;
	
	@Override
	public TResult<FightVideo> getFightVideo(long actorId, long nId) {
		AbstractNotifyVO notifyVO = get(actorId, nId);
		if (notifyVO == null) {
			return TResult.valueOf(NOTIFICATION_UNEXISTS);
		}
		FightVideo fightVideo = fightVideoDao.get(actorId, nId);
		if (fightVideo == null) {
			return TResult.valueOf(NOTIFICATION_VIDEO_NOT_FOUND);
		}
		return TResult.sucess(fightVideo);
	}

	@Override
	public AbstractNotifyVO get(long actorId, long nId) {
		return notifyDao.getNotifyVO(actorId, nId);
	}

	@Override
	public List<AbstractNotifyVO> getList(long toActorId) {
		List<AbstractNotifyVO> allList = new ArrayList<>();

		Notify notify = notifyDao.get(toActorId);
		List<AbstractNotifyVO> sendVO = notify.getSendNotifyList();
		for(AbstractNotifyVO vo : sendVO){
			vo.iconVO = iconFacade.getIconVO(vo.fromActorId);
		}
		allList.addAll(sendVO);
		List<AbstractNotifyVO> receiveVO = notify.getReceiveNotifyList();
		for(AbstractNotifyVO vo : receiveVO){
			vo.iconVO = iconFacade.getIconVO(vo.fromActorId);
		}
		allList.addAll(receiveVO);
		return allList;
	}

	@Override
	public Result getReward(long actorId, long nId) {
		Notify notify = notifyDao.get(actorId);
		AbstractNotifyVO notifyVO = getNotifyVO(notify, nId);
		if (notifyVO == null) {
			return Result.valueOf(NOTIFICATION_UNEXISTS);
		}
		
		NotifyType notifyType = NotifyType.get(notifyVO.type);
		if (notifyType != NotifyType.TRIAL && notifyType != NotifyType.STORY && notifyType != NotifyType.VIP_GIVE_EQUIP) {
			return Result.valueOf(NOTIFICATION_NO_REWARD_TYPE);
		}
		
		int reputationNum = 0;
		ThankAllyType thankAllyType = ThankAllyType.NONE;
		ReputationAddType reputationAddType = null;
		GoodsAddType goodsAddType = null;
		
		Map<Integer, Integer> giveGoodsMaps = new HashMap<>();
		Map<Integer, Integer> giveEquipMaps = new HashMap<>();

		switch (notifyType) {
		case TRIAL: {
			TrialNotifyVO trailNotifyVo = (TrialNotifyVO) notifyVO;
			if(trailNotifyVo.isGet == BooleanType.TRUE.getCode()) {
				return Result.valueOf(NOTIFICATION_REWARD_ALREADY_GET);
			}
			trailNotifyVo.isGet = BooleanType.TRUE.getCode();
			reputationNum = trailNotifyVo.allyReputation;
			giveGoodsMaps = trailNotifyVo.rewards;
			
			thankAllyType = ThankAllyType.TRAIL;
			reputationAddType = ReputationAddType.TRAIL_ALLY_REWARD;
			goodsAddType = GoodsAddType.TRAIL_ALLY_AWARD;
			break;
		}
		case STORY: {
			StoryNotifyVO storyNotifyVo = (StoryNotifyVO) notifyVO;
			if(storyNotifyVo.isGet == BooleanType.TRUE.getCode()) {
				return Result.valueOf(NOTIFICATION_REWARD_ALREADY_GET);
			}
			storyNotifyVo.isGet = BooleanType.TRUE.getCode();
			reputationNum = storyNotifyVo.allyReputation;
			giveGoodsMaps = storyNotifyVo.rewards;
			
			thankAllyType = ThankAllyType.STORY;
			reputationAddType = ReputationAddType.STORY_ALLY_REWARD;
			goodsAddType = GoodsAddType.STORY_ALLY_AWARD;
			break;
		}
		case VIP_GIVE_EQUIP: {
			GiveEquipNotifyVO giveEquipNotifyVo = (GiveEquipNotifyVO) notifyVO;
			if(giveEquipNotifyVo.isGet == BooleanType.TRUE.getCode()) {
				return Result.valueOf(NOTIFICATION_REWARD_ALREADY_GET);
			}
			giveEquipNotifyVo.isGet = BooleanType.TRUE.getCode();
			giveEquipMaps.put(giveEquipNotifyVo.equipId, giveEquipNotifyVo.equipNum);
			
			thankAllyType = ThankAllyType.ACCEPT_GIVE_EQUIP;
			break;
		}
		default:
			break;
		}
		
		// 更新notify
		notifyDao.update(notify);
		
		//添加声望
		if (reputationNum > 0) {
			actorFacade.addReputation(actorId, reputationAddType, reputationNum);
		}
		
		//是否感谢对方
		if (thankAllyType != ThankAllyType.NONE) {
			//createThankAlly(actorId, notifyVO.fromActorId, thankAllyType);
		}
	
		//添加奖励的物品
		for (Entry<Integer, Integer> entry : giveGoodsMaps.entrySet()) {
			goodsFacade.addGoodsVO(actorId, goodsAddType, entry.getKey(), entry.getValue());
		}
		
		//添加奖励的装备
		for (Entry<Integer, Integer> entry : giveEquipMaps.entrySet()) {
			for (int i = 0; i < entry.getValue(); i++) {
				equipFacade.addEquip(actorId, EquipAddType.VIP_GIVE_EQUIP, entry.getKey());
			}
		}
		
		return Result.valueOf();
	}

	@Override
	public Result notifyAlly(long actorId, long nId) {
		Notify notify = notifyDao.get(actorId);
		AbstractNotifyVO notifyVO = getNotifyVO(notify, nId);
		if (notifyVO == null) {
			return Result.valueOf(NOTIFICATION_UNEXISTS);
		}
		if (NotifyType.get(notifyVO.type) != NotifyType.SNATCH) {
			return Result.valueOf(NOTIFICATION_NO_NOTICE_TYPE);
		}
		
		Collection<AllyVO> allyCol = allyFacade.getAlly(actorId);
		if (allyCol.size() < 1) {
			return Result.valueOf(NOTIFICATION_ALLY_NOT_FOUND);
		}
		
		SnatchNotifyVO vo = (SnatchNotifyVO) notifyVO;
		if (vo.isNoticeAlly == BooleanType.TRUE) {
			return Result.valueOf(NOTIFICATION_ALREADY_NOTICE);
		}
		vo.isNoticeAlly = BooleanType.TRUE;
		
		//更新
		notifyDao.update(notify);
		
		boolean isNotice = noticeAll(actorId, vo);
		if (isNotice == false) {
			return Result.valueOf(NOTIFICATION_ALLY_NOT_FOUND);
		}
		remove(actorId, nId);
		return Result.valueOf();
	}
	
	private boolean noticeAll(long actorId, SnatchNotifyVO fromVO) {
		Collection<AllyVO> allyCol = allyFacade.getAlly(actorId);
		if (allyCol.size() < 1) {
			return false;
		}

		Actor actor = actorFacade.getActor(actorId);
		long targetActorId = 0;
		for (AllyVO allyVO : allyCol) {
			// 若抢夺双方是盟友，则点通知盟友的时候不产生
			if (fromVO.fromActorId == allyVO.actorId || fromVO.toActorId == allyVO.actorId) {
				continue;
			}

			if (fromVO.ownerActorId == fromVO.fromActorId) {
				targetActorId = fromVO.toActorId;
			} else {
				targetActorId = fromVO.fromActorId;
			}

			BattleResultType battleResult = BattleResultType.getByCode(fromVO.battleResult);
			SnatchAllyNotifyVO vo = new SnatchAllyNotifyVO(fromVO.attackType, targetActorId,fromVO.goodsId, fromVO.goodsNum);
			vo.setAbstractVO(NotifyType.SNATCH_ALLY, allyVO.actorId, actor.level, actorId, allyVO.actorId, battleResult);
			createNotify(vo,true);
			remove(actorId, fromVO.nId);
		}
		return true;
	}

	@Override
	public Result setReaded(long toActorId,List<Long> nIds) {
		boolean updateFlag = false;
		Notify notify = notifyDao.get(toActorId);
		List<AbstractNotifyVO> sendList = notify.getSendNotifyList();
		synchronized (sendList) {
			for (AbstractNotifyVO vo : sendList) {
				if (nIds.contains(vo.nId) && vo.isReaded == 0) {
					vo.isReaded = 1;
					updateFlag = true;
				}
			}
		}
		List<AbstractNotifyVO> receiveList = notify.getReceiveNotifyList();
		synchronized (receiveList) {
			for (AbstractNotifyVO vo : receiveList) {
				if (nIds.contains(vo.nId) && vo.isReaded == 0) {
					vo.isReaded = 1;
					updateFlag = true;
				}
			}
		}
		
		if(updateFlag) {
			notifyDao.update(notify);	
		}
		
		return Result.valueOf();
	}

	@Override
	public Result remove(long toActorId, List<Long> nIds) {
		Notify notify = notifyDao.get(toActorId);
		if (notify.removeNotify(nIds)) {
			NotifyPushHelper.pushRemoveNotify(toActorId, nIds);
		}
		notifyDao.update(notify);
		try {
			for (Long nId : nIds) {
				fightVideoDao.remove(toActorId, nId,FightVideoRemoveType.TYPE2);
			}
		} catch (Exception ex) {
		}
		
		return Result.valueOf();
	}

	@Override
	public Result remove(long toActorId, long nId) {
		List<Long> ids = new ArrayList<>();
		ids.add(nId);
		return remove(toActorId, ids);

		// Notification removed = get(actorId, nId);
		// if (removed != null) {
		// List<Notification> notifyList = getList(actorId);
		// try {
		// synchronized (notifyList) {
		// notifyList.remove(removed);
		// }
		// jdbc.delete(removed);
		// } catch (Exception e) {
		// LOGGER.error("remove a Notify", e);
		// } finally {
		// fightVideoDao.remove(nId);
		// }
		// }
		// return removed;
	}

	@Override
	public boolean ableSnatch(long actorId, long targetActorId, long nId) {
		AbstractNotifyVO notifyVO = notifyDao.getNotifyVO(actorId, nId);
		if (notifyVO == null) {
			return false;
		}

		if (notifyVO.type == NotifyType.SNATCH.getCode()) {
			if (notifyVO.toActorId == targetActorId || notifyVO.fromActorId == targetActorId) {
				return true;
			}
			return false;
		} else if (notifyVO.type == NotifyType.SNATCH_ALLY.getCode()) {
			SnatchAllyNotifyVO snatchAllyVo = (SnatchAllyNotifyVO) notifyVO;
			return targetActorId == snatchAllyVo.targetActorId;
		}
		return false;
	}

	@Override
	public void createSnatch(long actorId, int actorLevel, SnatchEnemyType enemyType, long targetActorId, String targetActorName,
			int targetActorLevel, boolean isWin, int score, int goodsId, int goodsNum, int snatchedGoodsId,
			int snatchedGoodsNum, byte[] fightVideo) {

		//如果是抢夺金币则需要过滤 金币为0的消息
		if(goodsNum < 1) {
			return;
		}
		
		
		//是否需要创建抢夺者的发送消息
		//判断对方等级是否在范围内，如果不在则不需要创建一条发出的消息
		boolean needCreateSenderNotify = SnatchService.snatchTargetInLevelLimitScope(actorLevel, enemyType, targetActorLevel); 
		
		// 发送给抢夺者的信息
		if (needCreateSenderNotify && enemyType == SnatchEnemyType.ACTOR && isWin == false) {
			
			BattleResultType fromResultType = BattleResultType.getType(isWin);
			SnatchNotifyVO vo = new SnatchNotifyVO(AttackType.ATTACKING.getCode(), goodsId, goodsNum, snatchedGoodsId,
					snatchedGoodsNum, BooleanType.FALSE);
			vo.setAbstractVO(NotifyType.SNATCH, actorId, targetActorLevel, actorId, targetActorId, fromResultType);
			createNotify(vo,true);
		}
		
		//被抢夺者如果是真实玩家，才发送消息给TA
		if (enemyType == SnatchEnemyType.ACTOR) {			
			// 发送给被抢夺者的信息
			BattleResultType toResultType = BattleResultType.getReverseResult(isWin);
			Actor actor = actorFacade.getActor(actorId);
			
			SnatchNotifyVO targetVo = new SnatchNotifyVO(AttackType.ATTACKED.getCode(), goodsId, goodsNum, snatchedGoodsId,
					snatchedGoodsNum, BooleanType.FALSE);
			targetVo.setAbstractVO(NotifyType.SNATCH, targetActorId, actor.level, actorId, targetActorId, toResultType);
			createNotify(targetVo,true);
			fightVideoDao.create(targetVo.ownerActorId, targetVo.nId, fightVideo);
		}
	}

	@Override
	public void createAllyFight(long actorId, long targetActorId, boolean isWin, Map<Long, Integer> moraleReward, int successNum, int failNum,
			byte[] fightVideo) {
		
		int morale = moraleReward != null && moraleReward.containsKey(actorId) ? moraleReward.get(actorId) : 0;

//		BattleResultType fromResultType = BattleResultType.getType(isWin);
//		Actor targetActor = actorFacade.getActor(targetActorId);
//		
//		AllyFightNotifyVO vo = new AllyFightNotifyVO(AttackType.ATTACKING.getCode(), morale, successNum, failNum);
//		int firstHeroId = lineupFacade.getFirstHero(actorId);
//		vo.setAbstractVO(NotifyType.ALLY_FIGHT, actorId, targetActor.level, actorId,firstHeroId, targetActorId, fromResultType);
//		// 发送给进行切磋者
//		createNotify(vo);
		
		morale = moraleReward != null && moraleReward.containsKey(targetActorId) ? moraleReward.get(targetActorId) : 0;
		Actor actor = actorFacade.getActor(actorId);
		BattleResultType toResultType = BattleResultType.getReverseResult(isWin);
		
		AllyFightNotifyVO toVo = new AllyFightNotifyVO(AttackType.ATTACKED.getCode(), morale, failNum, successNum);
		toVo.setAbstractVO(NotifyType.ALLY_FIGHT, targetActorId, actor.level, actorId, targetActorId, toResultType);		
		// 发送给被切磋者
		createNotify(toVo,true);
		fightVideoDao.create(toVo.ownerActorId, toVo.nId, fightVideo);
	}

	@Override
	public void createTrial(long actorId, long allyActorId, int battleId, boolean isWin, int allyReputation, Map<Integer, Integer> rewardGoods,
			byte[] fightVideo) {
		if (allyActorId < 1) {
			return;
		}

		BattleResultType type = BattleResultType.getType(isWin);
		Actor actor = actorFacade.getActor(actorId);
		TrialNotifyVO vo = new TrialNotifyVO(battleId, allyReputation, rewardGoods, BooleanType.FALSE.getCode());
		vo.setAbstractVO(NotifyType.TRIAL, allyActorId, actor.level, actorId, allyActorId, type);

		// 发送给受邀请者
		createNotify(vo,true);
		fightVideoDao.create(vo.ownerActorId, vo.nId, fightVideo);
	}

	@Override
	public void createStory(long actorId, long allyActorId, int battleId, boolean isWin, int allyReputation, Map<Integer, Integer> rewardGoods,boolean isReward) {
		BattleResultType type = BattleResultType.getType(isWin);
		Actor actor = actorFacade.getActor(actorId);
		StoryNotifyVO vo = null;
		vo = new StoryNotifyVO(battleId, allyReputation, rewardGoods, BooleanType.FALSE.getCode(),isReward);
		vo.setAbstractVO(NotifyType.STORY, allyActorId, actor.level, actorId, allyActorId, type);

		// 发送给受邀请者
		createNotify(vo,true);
	}

	@Override
	public void createAddAlly(long actorId, long allyActorId) {
		BattleResultType type = BattleResultType.NONE;

//		// 发送给添加盟友者		
//		Actor allyActor = actorFacade.getActor(allyActorId);
//		AllyNotifyVO vo = new AllyNotifyVO(AttackType.ATTACKING.getCode());
//		int firstHeroId = lineupFacade.getFirstHero(actorId);
//		vo.setAbstractVO(NotifyType.ADD_ALLY, actorId, allyActor.level, actorId,firstHeroId, allyActorId, type);
//		createNotify(vo);
		
		// 发送给被添加者
		Actor actor = actorFacade.getActor(actorId);
		AllyNotifyVO targetVo = new AllyNotifyVO(AttackType.ATTACKED.getCode());
		targetVo.setAbstractVO(NotifyType.ADD_ALLY, allyActorId, actor.level, actorId, allyActorId, type);
		createNotify(targetVo,false);
	}

	@Override
	public void createRemoveAlly(long actorId, long allyActorId) {
		BattleResultType type = BattleResultType.NONE;

//		// 发送给删除盟友者
//		Actor allyActor = actorFacade.getActor(allyActorId);
//		AllyNotifyVO vo = new AllyNotifyVO(AttackType.ATTACKING.getCode());
//		int firstHeroId = lineupFacade.getFirstHero(actorId);
//		vo.setAbstractVO(NotifyType.REMOVE_ALLY, actorId, allyActor.level, actorId,firstHeroId, allyActorId, type);
//		createNotify(vo);
		
		
		// 发送给被删除者
		Actor actor = actorFacade.getActor(actorId);
		AllyNotifyVO targetVo = new AllyNotifyVO(AttackType.ATTACKED.getCode());
		targetVo.setAbstractVO(NotifyType.REMOVE_ALLY, allyActorId, actor.level, actorId, allyActorId, type);
		createNotify(targetVo,false);
	}

	@Override
	public void createPowerFight(boolean isWin, boolean isCaptureRankSuccess, long actorId, int rank, long targetActorId, int targetRank,
			byte[] fightVideo) {

		// 发送给挑战者
//		BattleResultType fromResultType = BattleResultType.getType(isWin);
//		Actor targetActor = actorFacade.getActor(targetActorId);
//		PowerNotifyVO vo = new PowerNotifyVO(AttackType.ATTACKING.getCode(), isCaptureRankSuccess ? 1 : 0, rank, targetRank);
//		int firstHeroId = lineupFacade.getFirstHero(actorId);
//		vo.setAbstractVO(NotifyType.POWER_RANK_CHALLENGE, actorId, targetActor.level, actorId,firstHeroId, targetActorId, fromResultType);
//		createNotify(vo);
		

		// 发送给被挑战者
		BattleResultType toResultType = BattleResultType.getReverseResult(isWin);
		Actor actor = actorFacade.getActor(actorId);
		PowerNotifyVO targetVo = new PowerNotifyVO(AttackType.ATTACKED.getCode(), isCaptureRankSuccess ? 1 : 0, rank, targetRank);
		targetVo.setAbstractVO(NotifyType.POWER_RANK_CHALLENGE, targetActorId, actor.level, actorId, targetActorId, toResultType);
		createNotify(targetVo,true);
		fightVideoDao.create(targetVo.ownerActorId, targetVo.nId, fightVideo);
	}

//	@Override
//	public void createPowerReward(long actorId, int rank, int heroSoulId, int heroSoulNum, Map<Integer, Integer> goods) {
//		if (heroSoulNum < 1) {
//			heroSoulId = 0;
//		}
//		Actor actor = actorFacade.getActor(actorId);
//		PowerRewardNotifyVO vo = new PowerRewardNotifyVO(rank, heroSoulId, heroSoulNum, goods);
//		vo.setAbstractVO(NotifyType.POWER_REWARD, actorId, actor.level, 0, actorId, BattleResultType.WIN);
//		createNotify(vo);
//	}

	@Override
	public void createHole(long actorId, long toActorId, long id, int holeId) {
		Actor actor = actorFacade.getActor(actorId);
		HoleInviteNotifyVO vo = new HoleInviteNotifyVO(id,holeId);
		vo.setAbstractVO(NotifyType.HOLE_INVITE_ALLY, toActorId, actor.level, actorId, toActorId, BattleResultType.NONE);
		createNotify(vo,true);
	}

//	@Override
//	public void createThankAlly(long actorId, long toActorId, ThankAllyType type) {
//		Actor actor = actorFacade.getActor(actorId);
//		ThankAllyNotifyVO vo = new ThankAllyNotifyVO(type.getId());
//		int firstHeroId = lineupFacade.getFirstHero(actorId);
//		vo.setAbstractVO(NotifyType.THANK_ALLY, toActorId, actor.level, actorId,firstHeroId, toActorId, BattleResultType.NONE);
//		createNotify(vo);
//	}

//	@Override
//	public void createGiveGift(long actorId, long toActorId) {
//		Actor actor = actorFacade.getActor(actorId);
//		GiveGiftNotifyVO vo = new GiveGiftNotifyVO();
//		int firstHeroId = lineupFacade.getFirstHero(actorId);
//		vo.setAbstractVO(NotifyType.GIVE_GIFT, toActorId, actor.level, actorId,firstHeroId, toActorId, BattleResultType.NONE);
//		createNotify(vo);
//	}

	@Override
	public void createSnatchRevenge(long actorId, long notifyId, int goodsId, int goodsNum, WinLevel winLevel) {
//		AbstractNotifyVO notifyVO = notifyDao.getNotifyVO(actorId, notifyId);
//		if (notifyVO == null) {
//			return;
//		}
//		
//		NotifyType notifyType = NotifyType.get(notifyVO.type);
//		if (notifyType == NotifyType.SNATCH_ALLY) {
//			
//			SnatchAllyNotifyVO snatchAllyNotifyVo = (SnatchAllyNotifyVO) notifyVO;
//			Actor actor = actorFacade.getActor(actorId);
//			BattleResultType battleResultType = winLevel.isWin() ? BattleResultType.WIN : BattleResultType.FAIL;
//			long toActorId = notifyVO.fromActorId;
//			
//			SnatchRevengeNotifyVO vo = new SnatchRevengeNotifyVO(snatchType.getType(), goodsId, goodsNum, snatchAllyNotifyVo.targetActorId);
//			int firstHeroId = lineupFacade.getFirstHero(actorId);
//			vo.setAbstractVO(NotifyType.SNATCH_REVENGE, toActorId, actor.level,actorId,firstHeroId, toActorId, battleResultType);
//			createNotify(vo);
//		}
	}

	@Override
	public void createGiveEquip(long actorId, long toActorId, int equipId, int equipNum) {
		Actor actor = actorFacade.getActor(actorId);
		GiveEquipNotifyVO vo = new GiveEquipNotifyVO(equipId, equipNum, (byte) 0);
		vo.setAbstractVO(NotifyType.VIP_GIVE_EQUIP, toActorId, actor.level, actorId, toActorId, BattleResultType.NONE);
		createNotify(vo,true);
	}
	
	private void createNotify(AbstractNotifyVO notifyVO, boolean isSave) {
		long actorId = notifyVO.ownerActorId;
		int maxNum = NotifyRule.NOTIFICATION_MAX_NUM_LIMIT;
		
		try {
			List<Long> removeIds = new ArrayList<>();
			Notify notify = notifyDao.get(actorId);
			notifyVO.nId = notify.incrementId(); // 添加自增加id

			List<AbstractNotifyVO> createList = notify.getCreateList(notifyVO);
			synchronized (createList) {
				// 删除多余的旧消息
				if(maxNum > 1) {
					for (int i = maxNum; i <= createList.size(); i++) {
						AbstractNotifyVO removeVO = createList.remove(i - 1);
						if (removeVO != null) {
							removeIds.add(removeVO.nId);
						}
					}					
				}
				// 添加新消息
				createList.add(0, notifyVO);
			}
			if(isSave){
				notifyDao.update(notify);
			}
			NotifyPushHelper.pushRemoveNotify(actorId, removeIds);
			notifyVO.iconVO = iconFacade.getIconVO(notifyVO.fromActorId);
			NotifyPushHelper.pushNotify(notifyVO.ownerActorId, notifyVO);
		} catch(Exception ex) {
			LOGGER.error("",ex);
		}
	}

	@Override
	public void onLogin(long actorId) {
		Notify notify = notifyDao.get(actorId);
		List<Long> list = new ArrayList<>();
		int now = TimeUtils.getNow();
		for(AbstractNotifyVO notifyVO:notify.getReceiveNotifyList()){
			getRemoveNotify(list, now, notifyVO);
		}
		for(AbstractNotifyVO notifyVO:notify.getSendNotifyList()){
			getRemoveNotify(list, now, notifyVO);
		}
		remove(actorId, list);
	}

	private void getRemoveNotify(List<Long> list, int now,AbstractNotifyVO notifyVO) {
		NotifyType type = NotifyType.get(notifyVO.type); 
		switch(type){
		case ADD_ALLY:
			if(TimeUtils.getBetweenDay(now,notifyVO.sendTime) > 7){
				list.add(notifyVO.nId);
			}
			break;
		case ALLY_FIGHT:
			if(TimeUtils.getBetweenDay(now, notifyVO.sendTime) > 3){
				list.add(notifyVO.nId);
			}
			break;
//		case GIVE_GIFT:
//			if(TimeUtils.getBetweenDay(now, notifyVO.sendTime) > 1){
//				list.add(notifyVO.nId);
//			}
//			break;
		case HOLE_INVITE_ALLY:
			if(TimeUtils.getBetweenDay(now, notifyVO.sendTime) > 1){
				list.add(notifyVO.nId);
			}
			break;
		case POWER_RANK_CHALLENGE:
			if(TimeUtils.getBetweenDay(now, notifyVO.sendTime) > 7){
				list.add(notifyVO.nId);
			}
			break;
		case REMOVE_ALLY:
			if(TimeUtils.getBetweenDay(now, notifyVO.sendTime) > 7){
				list.add(notifyVO.nId);
			}
			break;
		case SNATCH:
			if(TimeUtils.getBetweenDay(now, notifyVO.sendTime) > 7){
				list.add(notifyVO.nId);
			}
			break;
		case SNATCH_ALLY:
			if(TimeUtils.getBetweenDay(now, notifyVO.sendTime) > 3){
				list.add(notifyVO.nId);
			}
			break;
//		case SNATCH_REVENGE:
//			if(TimeUtils.getBetweenDay(now, notifyVO.sendTime) > 3){
//				list.add(notifyVO.nId);
//			}
//			break;
		case STORY:
			if(TimeUtils.getBetweenDay(now, notifyVO.sendTime) > 30){
				list.add(notifyVO.nId);
			}
			break;
//		case THANK_ALLY:
//			if(TimeUtils.getBetweenDay(now, notifyVO.sendTime) > 1){
//				list.add(notifyVO.nId);
//			}
//			break;
		case TRIAL:
			if(TimeUtils.getBetweenDay(now, notifyVO.sendTime) > 30){
				list.add(notifyVO.nId);
			}
			break;
		case VIP_GIVE_EQUIP:
			if(TimeUtils.getBetweenDay(now, notifyVO.sendTime) > 7){
				list.add(notifyVO.nId);
			}
			break;
		case NONE:
			break;
		default:
			break;
		}
	}
	
	private AbstractNotifyVO getNotifyVO(Notify notify,long nId) {
		List<AbstractNotifyVO> sendList = notify.getSendNotifyList();
		for (AbstractNotifyVO vo : sendList) {
			if (vo.nId == nId) {
				return vo;
			}
		}
		
		List<AbstractNotifyVO> receiveList = notify.getReceiveNotifyList();
		for (AbstractNotifyVO vo : receiveList) {
			if (vo.nId == nId) {
				return vo;
			}
		}
		return null;
	}

}
