package com.jtang.gameserver.admin.facade.impl;

import static com.jtang.core.protocol.StatusCode.DATA_VALUE_ERROR;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.ACTOR_NOT_EXIST;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.ADD_GOLD_ARG_ERROR;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.DECREASE_GOLD_ERROR;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.DELETE_ACTIVE_FILE;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.GIVE_REPUTATION_ERROR;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.GIVE_VIPLEVEL_ERROR;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.GOLD_ADD_DB_ERROR;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.GOLD_NOT_ENOUGH;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.DataType;
import com.jtang.core.event.EventBus;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.admin.facade.ActorMaintianFacade;
import com.jtang.gameserver.component.event.VipLevelChangeEvent;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.adventures.achievement.facade.AchieveFacade;
import com.jtang.gameserver.module.app.dao.AppRecordDao;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.recruit.facade.RecruitFacade;
import com.jtang.gameserver.module.user.dao.ActorDao;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.type.ActorAttributeKey;
import com.jtang.gameserver.module.user.type.GoldAddType;
import com.jtang.gameserver.module.user.type.GoldDecreaseType;
import com.jtang.gameserver.module.user.type.ReputationAddType;

@Component
public class ActorMaintianFacadeImpl implements ActorMaintianFacade, ApplicationListener<ContextRefreshedEvent> {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	private Map<ActorAttributeKey, String> actorKey_name = new HashMap<ActorAttributeKey, String>();
	@Autowired
	private ActorDao actorDao;
	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private VipFacade vipFacade;
	@Autowired
	private GoodsFacade goodsFacade;
	@Autowired
	private RecruitFacade recruitFacade;
	@Autowired
	private AchieveFacade achieveFacade;
	
	@Autowired
	private EventBus eventBus;
	
	@Autowired
	private AppRecordDao appRecordDao;

	@Override
	public TResult<List<ActorAttributeKey>> modify(long actorId, Map<ActorAttributeKey, String> modifyFields) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return TResult.valueOf(ACTOR_NOT_EXIST);
		}
		List<ActorAttributeKey> list = new ArrayList<>();
		for (Entry<ActorAttributeKey, String> entry : modifyFields.entrySet()) {
			String modifyField = entry.getValue();
			String fieldName = actorKey_name.get(entry.getKey());
			Object obj = parseFieldValue(entry.getKey().getType(), modifyField);
			if (fieldName != null) {
				try {
					Field field = actor.getClass().getDeclaredField(fieldName);
					field.setAccessible(true);
					field.set(actor, obj);
				} catch (Exception e) {
					LOGGER.error("修改实体值类型错误", e);
					list.add(entry.getKey());
				}
			} else {
				LOGGER.error(String.format("不支持修改属性key:[%s]", entry.getKey().getCode()));
				list.add(entry.getKey());
			}
		}
		actorDao.updateActor(actor);

		return TResult.sucess(list);

	}

	private Object parseFieldValue(DataType fieldValueType, String value) {
		Object object = null;
		switch (fieldValueType) {
		case STRING:
			object = value;
			break;
		case INT:
			object = Integer.valueOf(value);
			break;
		case LONG:
			object = Long.valueOf(value);
			break;

		default:
			object = value;
			break;
		}

		return object;
	}
	
	@Override
	public Result addReputation(long actorId, int reputationValue) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null){
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		
		if (reputationValue <= 0) {
			 Result.valueOf(GIVE_REPUTATION_ERROR);
		}
		
		actorFacade.addReputation(actorId, ReputationAddType.MAINTAIN, reputationValue);
		return Result.valueOf();
	}

	@Override
	public Result addGold(long actorId, int giveNum) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}

		if (giveNum < 1) {
			LOGGER.error(String.format("加金币数值错误，giveNum:[%s]", giveNum));
			return Result.valueOf(ADD_GOLD_ARG_ERROR);
		}
		boolean result = actorFacade.addGold(actorId, GoldAddType.MAINTAIN, giveNum);
		if (result) {
			return Result.valueOf();
		} else {
			return Result.valueOf(GOLD_ADD_DB_ERROR);
		}
	}

	@Override
	public Result modifyVipLevel(long actorId, int level) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		if (level < 0) {
			return Result.valueOf(GIVE_VIPLEVEL_ERROR);
		}

		int selfLevel = vipFacade.getVipLevel(actorId);
		if (selfLevel < level) {
			vipFacade.updateVipLevel(actorId, level);
			vipFacade.sendRewardByVipLevel(actorId, selfLevel, level); // 送vip仅送vip等级
																		// 不发放响应奖励
			eventBus.post(new VipLevelChangeEvent(actorId, level));
		} else {
			vipFacade.updateVipLevel(actorId, level);
		}
		return Result.valueOf();
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {

		actorKey_name.put(ActorAttributeKey.LEVEL, "level");
		actorKey_name.put(ActorAttributeKey.GOLD, "gold");
		actorKey_name.put(ActorAttributeKey.ENERGY, "energy");
		actorKey_name.put(ActorAttributeKey.MAXENERGY, "maxEnergy");
		actorKey_name.put(ActorAttributeKey.VIT, "vit");
		actorKey_name.put(ActorAttributeKey.MAXVIT, "maxVit");
		actorKey_name.put(ActorAttributeKey.MORALE, "morale");
	}

	@Override
	public Result decreaseGold(long actorId, int decreaseNum) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		
		if (decreaseNum < 1) {
			return Result.valueOf(DECREASE_GOLD_ERROR);
		}
		
		boolean result = actorFacade.decreaseGold(actorId, GoldDecreaseType.MAINTAIN, decreaseNum);
		if (result) {
			return Result.valueOf();
		} else {
			return Result.valueOf(GOLD_NOT_ENOUGH);
		}
	}

	@Override
	public Result deleteActorActive(long actorId,long appId) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		boolean isSuccess=appRecordDao.resetRecord(actorId, appId);
		if(isSuccess){
			return Result.valueOf();
		}
		return Result.valueOf(DELETE_ACTIVE_FILE);
	}

	@Override
	public Result changeActorUid(long actorId, int newPlatformId, String newUid, int newChannelId) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}

		if (newPlatformId < 1 || StringUtils.isBlank(newUid) || newChannelId < 1) {
			return Result.valueOf(DATA_VALUE_ERROR);
		}

		actor.platformType = newPlatformId;
		actor.uid = newUid;
		actor.channelId = newChannelId;
		actorDao.dbUpdate(actor);
		

		return Result.valueOf();
	}

}
