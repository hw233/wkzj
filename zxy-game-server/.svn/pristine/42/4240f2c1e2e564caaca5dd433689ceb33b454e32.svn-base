package com.jtang.gameserver.module.dailytask.facade.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.ExprRewardObject;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.TResult;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.ActorUpgradeConfig;
import com.jtang.gameserver.dataconfig.model.DailyTaskRewardConfig;
import com.jtang.gameserver.dataconfig.model.DailyTasksConfig;
import com.jtang.gameserver.dataconfig.service.ActorService;
import com.jtang.gameserver.dataconfig.service.DailyTaskService;
import com.jtang.gameserver.dataconfig.service.TrialCaveService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.DailyTask;
import com.jtang.gameserver.dbproxy.entity.TrialCave;
import com.jtang.gameserver.module.dailytask.dao.DailyTaskDao;
import com.jtang.gameserver.module.dailytask.facade.DailyTaskFacade;
import com.jtang.gameserver.module.dailytask.handler.response.DailyTaskInfoResponse;
import com.jtang.gameserver.module.dailytask.helper.DailyTaskPushHelper;
import com.jtang.gameserver.module.dailytask.model.DailyTaskVO;
import com.jtang.gameserver.module.dailytask.type.DailyRewardType;
import com.jtang.gameserver.module.dailytask.type.DailyTaskType;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.snatch.facade.SnatchFacade;
import com.jtang.gameserver.module.trialcave.facade.TrialCaveFacade;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.model.VipPrivilege;
import com.jtang.gameserver.module.user.type.GoldAddType;
import com.jtang.gameserver.module.user.type.ReputationAddType;
import com.jtang.gameserver.module.user.type.TicketAddType;
import com.jtang.gameserver.module.user.type.VITAddType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class DailyTaskFacadeImpl implements DailyTaskFacade, ActorLoginListener, ZeroListener {
	protected static final Logger LOGGER = LoggerFactory.getLogger(DailyTaskFacadeImpl.class);
	@Autowired
	private DailyTaskDao dailyTaskDao;

	@Autowired
	private EquipFacade equipFacade;

	@Autowired
	private GoodsFacade goodsFacade;

	@Autowired
	private HeroSoulFacade heroSoulFacade;

	@Autowired
	private ActorFacade actorFacade;

	@Autowired
	private VipFacade vipFacade;

	@Autowired
	private Schedule schedule;

	@Autowired
	private PlayerSession playerSession;
	
	@Autowired
	private SnatchFacade snatchFacade;

	@Autowired
	private TrialCaveFacade trialCaveFacade;
	
	@Override
	public DailyTaskInfoResponse getDailyTask(long actorId) {
		int vipLevel = vipFacade.getVipLevel(actorId);
		DailyTask task = dailyTaskDao.get(actorId, vipLevel);
		List<DailyTaskVO> list = task.getList();
		DailyTaskInfoResponse dailyTaskInfoResponse = new DailyTaskInfoResponse(list);
		return dailyTaskInfoResponse;
	}

	@Override
	public short getReward(long actorId, int taskId) {
		int vipLevel = vipFacade.getVipLevel(actorId);
		DailyTask task = dailyTaskDao.get(actorId, vipLevel);
		if (DateUtils.isToday(task.getProgressTime()) == false) {
			task.clear(vipLevel);
			DailyTaskPushHelper.pushReset(actorId);
		}
		DailyTaskVO vo = task.get(taskId);
		if (vo == null) {
			return GameStatusCodeConstant.TASK_REWARD_ALREADY_GETED;
		}
		ChainLock lock = LockUtils.getLock(vo);
		try {
			lock.lock();

			if (vo.getIsGet() == 1) {
				return GameStatusCodeConstant.TASK_REWARD_ALREADY_GETED;
			}
			DailyTasksConfig cfg = DailyTaskService.get(taskId);
			if (cfg == null) {
				return StatusCode.DATA_VALUE_ERROR;
			}
			int now = TimeUtils.getNow();
			if (cfg.getTaskParser() == DailyTaskType.SUPPLY.getCode()) { // 御赐小宴
				String[] strs = StringUtils.split(cfg.getCompleteRule(),
						Splitable.ATTRIBUTE_SPLIT);
				Date start = TimeUtils.string2TodyTime(strs[0]);
				Date end = TimeUtils.string2TodyTime(strs[1]);
				if (start != null && end != null) {
					Long startTime = start.getTime() / 1000;
					Long endTime = end.getTime() / 1000;
					if (now <= endTime.intValue() && now > startTime.intValue()) {
						handlerReward(actorId, cfg);
						task.setProgressTime(now);
						vo.setIsGet((byte) 1);
						DailyTaskPushHelper.pushTask(actorId, vo);
					} else {
						return GameStatusCodeConstant.TASK_UNFINISHED;
					}
				} else {
					return GameStatusCodeConstant.TASK_UNFINISHED;
				}
			} else if (cfg.getTaskParser() == DailyTaskType.SNATCH_NUM.getCode()) {// 抢夺次数
				String[] strs = StringUtils.split(cfg.getCompleteRule(),
						Splitable.ATTRIBUTE_SPLIT);
				Date start = TimeUtils.string2TodyTime(strs[0]);
				Date end = TimeUtils.string2TodyTime(strs[1]);
				if (start != null && end != null) {
					Long startTime = start.getTime() / 1000;
					Long endTime = end.getTime() / 1000;
					if (now <= endTime.intValue() && now > startTime.intValue()) {
						short result = handlerReward(actorId, cfg);
						if (result != 0) {
							return result;
						}
						task.setProgressTime(now);
						vo.setIsGet((byte) 1);
						DailyTaskPushHelper.pushTask(actorId, vo);
					} else {
						return GameStatusCodeConstant.TASK_UNFINISHED;
					}
				} else {
					return GameStatusCodeConstant.TASK_UNFINISHED;
				}
			} else {
				if (vo.isComplte() == false) {
					return GameStatusCodeConstant.TASK_UNFINISHED;
				}
				vo.setIsGet((byte) 1);
				handlerReward(actorId, cfg);
				DailyTaskPushHelper.pushTask(actorId, vo);

			}
			dailyTaskDao.update(task);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		return 0;
	}

	private short handlerReward(long actorId, DailyTasksConfig cfg) {
		Actor actor = actorFacade.getActor(actorId);
		List<Integer> idList = cfg.getRewardIdList();
		for (int rewardId : idList) {
			DailyTaskRewardConfig dailyTaskRewardConfig = DailyTaskService.getRewardConfig(rewardId);
			if (dailyTaskRewardConfig == null) {
				continue;
			}
			DailyRewardType type = DailyRewardType.getByCode(dailyTaskRewardConfig.getRewardType());
			switch (type) {
			case GOOS:
				List<String[]> rewardStr = StringUtils.delimiterString2Array(dailyTaskRewardConfig.getReward());
				List<RewardObject> rewardList = new ArrayList<>();
				for (String[] strings : rewardStr) {
					ExprRewardObject rewardObject = ExprRewardObject.valueOf(strings);
					rewardList.add(rewardObject.clone(actor.level));
				}
				sendReward(actorId, rewardList);
				break;
			case ENERGY:
//				actorFacade.addEnergy(actorId, EnergyAddType.TASK_AWARD, actor.maxEnergy);
				return snatchFacade.fullSnatchNum(actorId).statusCode;
			case VIT:
				actorFacade.addVIT(actorId, VITAddType.TASK_AWARD, actor.maxVit);
				break;
			case TICKET:
				vipFacade.addTicket(actorId, TicketAddType.TASK_REWARD, FormulaHelper.executeCeilInt(dailyTaskRewardConfig.getReward(), actor.level));
				break;
			case GOLD:
				actorFacade.addGold(actorId, GoldAddType.TASK_AWRD, FormulaHelper.executeCeilInt(dailyTaskRewardConfig.getReward(), actor.level));
				break;
			case VIP_TICKET:
				String str = dailyTaskRewardConfig.getReward();
				String[] items = StringUtils.split(str, Splitable.ELEMENT_SPLIT);
				Map<Integer, RewardObject> map = new HashMap<>();
				for (String item : items) {
					String[] ss = StringUtils.split(item, Splitable.ATTRIBUTE_SPLIT);
					String[] ss1 = new String[3];
					System.arraycopy(ss, 1, ss1, 0, 3);
					map.put(Integer.valueOf(ss[0]), RewardObject.valueOf(ss1));
				}
				int vipLevel = vipFacade.getVipLevel(actorId);
				RewardObject obj = map.get(vipLevel);
				if (obj != null) {
					sendReward(actorId, obj.id, obj.num, obj.rewardType);
				}
				break;
			case REPUTATION:
				ActorUpgradeConfig actorUpgradeConfig = ActorService.getUpgradeConfig(actor.level);
				long value = actorUpgradeConfig.getNeedReputation();
				actorFacade.addReputation(actorId, ReputationAddType.DAILY_TASK, FormulaHelper.executeCeilInt(dailyTaskRewardConfig.getReward(), value));
				break;

			case TRIALCAVE:
				TResult<TrialCave> result = trialCaveFacade.getTrialCaveInfo(actorId);
				if (result.isFail()) {
					return result.statusCode;
				}
				int actorVipLevel = vipFacade.getVipLevel(actorId);
				VipPrivilege vipPrivilege = vipFacade.getVipPrivilege(actorVipLevel);
				int total = TrialCaveService.getVIPTrialNumByVIPLevel(vipPrivilege, 1) + TrialCaveService.getVIPTrialNumByVIPLevel(vipPrivilege, 2);
				int lastNum = result.item.ent1trialed + result.item.ent2trialed;
				if (total - lastNum > 0) {
					return GameStatusCodeConstant.TRIAL_COUNT_CAN_NOT_RESET;
				}
				trialCaveFacade.dailyResetTrialCave(actorId);
				break;
			default:
				break;
			}
		}
		
		return 0;

	}

	/**
	 * 发放奖励
	 * 
	 * @param actorId
	 * @param id
	 * @param num
	 * @param rewardType
	 */
	private void sendReward(long actorId, int id, int num, RewardType rewardType) {
		switch (rewardType) {
		case EQUIP: {
			for (int i = 0; i < num; i++) {
				equipFacade.addEquip(actorId, EquipAddType.TASK_AWARD, id);
			}
			break;
		}
		case HEROSOUL: {
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.TASK_AWARD, id, num);
			break;
		}
		case GOODS: {
			goodsFacade.addGoodsVO(actorId, GoodsAddType.TASK_AWARD, id, num);
			break;
		}

		default:
			LOGGER.error(String.format("类型错误，type:[%s]", rewardType.getCode()));
			break;
		}
	}

	/**
	 * 发放奖励
	 * 
	 * @param actorId
	 * @param list
	 */
	private void sendReward(long actorId, List<RewardObject> list) {
		for (RewardObject rewardObject : list) {
			sendReward(actorId, rewardObject.id, rewardObject.num, rewardObject.rewardType);
		}
	}

	@Override
	public void onLogin(long actorId) {
		int vipLevel = vipFacade.getVipLevel(actorId);
		DailyTask task = dailyTaskDao.get(actorId, vipLevel);
		ChainLock lock = LockUtils.getLock(task);
		try {
			lock.lock();
			if (DateUtils.isToday(task.getProgressTime()) == false) {
				task.clear(vipLevel);
				dailyTaskDao.update(task);
			}
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		
	}

	@Override
	public void onZero() {
		Set<Long> list = playerSession.onlineActorList();
		for (long actorId : list) {
			int vipLevel = vipFacade.getVipLevel(actorId);
			DailyTask task = dailyTaskDao.get(actorId, vipLevel);
			ChainLock lock = LockUtils.getLock(task);
			try {
				lock.lock();
				task.clear(vipLevel);
				dailyTaskDao.update(task);
				DailyTaskPushHelper.pushReset(actorId);
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
	}

}
