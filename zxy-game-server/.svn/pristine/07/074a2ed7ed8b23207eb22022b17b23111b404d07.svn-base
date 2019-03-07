package com.jtang.gameserver.module.buffer.helper;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.core.utility.CollectionUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.dataconfig.service.SkillService;
import com.jtang.gameserver.module.battle.helper.FightProcessor;
import com.jtang.gameserver.module.battle.model.AttributeChange;
import com.jtang.gameserver.module.battle.model.BufferAction;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.DeadAction;
import com.jtang.gameserver.module.battle.model.DisapperAction;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.battle.type.Camp;
import com.jtang.gameserver.module.buffer.model.FighterBuffer;
import com.jtang.gameserver.module.buffer.type.BufferType;
import com.jtang.gameserver.module.buffer.type.CycleType;
import com.jtang.gameserver.module.skill.type.ProcessType;

/**
 * Buffer帮助类
 * 
 * @author vinceruan
 */
public class BufferHelper {
	protected static final Logger LOGGER = LoggerFactory.getLogger(BufferHelper.class);
	
	/**
	 * 角色是否在定身状态.
	 * 
	 * @param	buffers			角色的BUFFER列表
	 * @return 定身状态值
	 */
	public static int isUnitInImmobilize(Map<AttackerAttributeKey, List<FighterBuffer>> buffers) {
		
		if (buffers == null) {
			return 0; 
		}
		
		List<FighterBuffer> bufferList = buffers.get(AttackerAttributeKey.IMMOBILIZE);
		if(bufferList == null) {
			return 0;
		}
		
		for (FighterBuffer buffer : bufferList) {
			if (!buffer.isTimeout()) {
				return buffer.getAddVal();
			}
		}
		return 0;
//		return isHasAttrBuff(AttackerAttributeKey.IMMOBILIZE, buffers);
	}
	
	/**
	 * 是否处于内讧状态
	 * @param buffers
	 * @return
	 */
	public static boolean isInFighting(Map<AttackerAttributeKey, List<FighterBuffer>> buffers) {
		return isHasAttrBuff(AttackerAttributeKey.IN_FIGHTING, buffers);
	}
	/**
	 * 是否处于减少伤害的内讧状态
	 * @param buffers
	 * @return
	 */
	public static boolean isInFightingHertChange(Map<AttackerAttributeKey, List<FighterBuffer>> buffers) {
		return isHasAttrBuff(AttackerAttributeKey.IN_FIGHTING_HEAT_CHANGE, buffers);
	}
	
	/**
	 * 获取增加闪避的buffer
	 * @param buffers
	 * @return
	 */
	public static FighterBuffer getDodgeBuffer(Map<AttackerAttributeKey, List<FighterBuffer>> buffers) {
		if (buffers == null) {
			return null; 
		}
		
		List<FighterBuffer> bufferList = buffers.get(AttackerAttributeKey.DODGE);
		if(bufferList == null) {
			return null;
		}
		
		for (FighterBuffer buffer : bufferList) {
			if (!buffer.isTimeout()) {
				return buffer;
			}
		}
		return null;
	}
	/**
	 * 获取增加免疫的buffer
	 * @param buffers
	 * @return
	 */
	public static FighterBuffer getImmunityBuffer(Map<AttackerAttributeKey, List<FighterBuffer>> buffers) {
		if (buffers == null) {
			return null; 
		}
		
		List<FighterBuffer> bufferList = buffers.get(AttackerAttributeKey.IMMUNITY);
		if(bufferList == null) {
			return null;
		}
		
		for (FighterBuffer buffer : bufferList) {
			if (!buffer.isTimeout()) {
				return buffer;
			}
		}
		return null;
	}
	
	/**
	 * 尝试闪避
	 * @param fighter
	 * @return
	 */
	public static boolean tryDodge(Fighter fighter) {
		FighterBuffer buffer = getDodgeBuffer(fighter.getBuffers());
		if (buffer == null) {
			return false;
		}
		if (!buffer.isTimeout()) {
			buffer.heartBeat(CycleType.RIGHT_NOW);
			return true;
//			if (RandomUtils.is1000Hit(buffer.getRate())){
//			}
		}
		return false;
	}
	/**
	 * 尝试闪避
	 * @param fighter
	 * @return
	 */
	public static boolean tryImmunity(Fighter fighter, int immunityEffectId) {
		FighterBuffer buffer = getImmunityBuffer(fighter.getBuffers());
		if (buffer == null) {
			return false;
		}
		if (!buffer.isTimeout()) {
			buffer.heartBeat(CycleType.RIGHT_NOW);
			InbattleEffectConfig cfg = SkillService.getInbattleEffect(buffer.getEffectId());
			List<Integer> list = StringUtils.delimiterString2IntList(cfg.getEffectExpr(), Splitable.ELEMENT_SPLIT);
			if (list.contains(immunityEffectId)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断是否存在影响指定属性的buffer
	 * @param key
	 * @param buffers
	 * @return
	 */
	public static boolean isHasAttrBuff(AttackerAttributeKey key, Map<AttackerAttributeKey, List<FighterBuffer>> buffers) {
		if (buffers == null) {
			return false; 
		}
		
		List<FighterBuffer> bufferList = buffers.get(key);
		if(bufferList == null) {
			return false;
		}
		
		for (FighterBuffer buffer : bufferList) {
			if (!buffer.isTimeout()) {
				return true;
			}
		}
		return false;
	}
	
	
	
	/**
	 * 处理buffer心跳
	 * @param fighters
	 * @param cycleType
	 */
	public static void triggerBuffers(List<Fighter> fighters, int cycleType, Context context) {		
		for (Fighter fighter : fighters) {
			triggerBuffers(fighter, cycleType, context);		
		}
	}
	
	/**
	 * 处理buffer心跳
	 * @param fighters
	 * @param cycleType
	 */
	public static void triggerBuffers(Context context, int cycleType) {		
		triggerBuffers(context.getTeamListByCamp(Camp.ABOVE), cycleType, context);
		triggerBuffers(context.getTeamListByCamp(Camp.BELOW), cycleType, context);
	}

	/**
	 * 处理buffer心跳
	 * 
	 * @param fighter
	 * @param cycleType
	 *            {@code CycleType}
	 */
	public static void triggerBuffers(Fighter fighter, int cycleType, Context context) {
		Map<AttackerAttributeKey, List<FighterBuffer>> buffers = fighter.getBuffers();
		if (buffers == null) {
			return;
		}

		for (List<FighterBuffer> list : buffers.values()) {
			if (CollectionUtils.isEmpty(list)) {
				continue;
			}
			Iterator<FighterBuffer> bufferIter = list.iterator();
			while (bufferIter.hasNext()) {
				FighterBuffer buffer = bufferIter.next();
				boolean res = buffer.heartBeat(cycleType);
				if (res && buffer.isSyn2Client()) {
					// 通知给客户端
					AttributeChange change = null;
					if (buffer.getType() == BufferType.ATTR_BUFFER) {
						change = AttributeChange.valueOf(buffer.getAttr(), fighter.getAttrVal(buffer.getAttr()));
					}
					BufferAction report = BufferAction.bufferHeartBeat(fighter.getFighterId(), buffer.getEffectId(), change);
					context.fightRecorder.addAction(report);
					if (fighter.isDead()) {
						FightProcessor.processSkillEffects(fighter, context, ProcessType.DIE);
						DeadAction deadAction = new DeadAction(fighter.getFighterId());
						DisapperAction disapperAction = new DisapperAction(fighter.getFighterId());
						deadAction.setDisapperAction(disapperAction);
						context.fightRecorder.addAction(deadAction);
						context.fightRecorder.addAction(disapperAction);
						FightProcessor.processSkillEffects(fighter, context, ProcessType.AFTER_DEAD);
						
					}
				}
				if (!buffer.isTimeout()) {
					continue;
				}
				// 移除过期的buffer
				bufferIter.remove();
				if (buffer.isSyn2Client()) {
					// 通知给客户端
					LOGGER.debug(String.format("[%s]解除buff:[%s]", fighter.getName(), buffer.getType()));
					AttributeChange change = null;
					boolean fighterDead = fighter.isDead();
					if (buffer.getType() == BufferType.ATTR_BUFFER) {
						change = AttributeChange.valueOf(buffer.getAttr(), fighter.getAttrVal(buffer.getAttr()));
						if (fighterDead == false) { //死亡后不通知客户端
							BufferAction report = BufferAction.removeBuffer(fighter.getFighterId(), buffer.getEffectId(), change);
							context.fightRecorder.addAction(report);
						} else {
							if (buffer.isEffect() == true) {
								BufferAction report = BufferAction.removeBuffer(fighter.getFighterId(), buffer.getEffectId(), change);
								context.fightRecorder.addAction(report);
							}
						}
					} else if (buffer.getType() == BufferType.STATUS_BUFFER) {
						if (buffer.getAttr() == AttackerAttributeKey.IMMOBILIZE) {
							change = AttributeChange.valueOf(buffer.getAttr(), fighter.getAttrVal(buffer.getAttr()));
						}
						BufferAction report = BufferAction.removeBuffer(fighter.getFighterId(), buffer.getEffectId(), change);
						context.fightRecorder.addAction(report);
					}
				}
			}
			
		}
		
		if (fighter.isDead()) {
			if (cycleType != CycleType.DIE) {
				BufferHelper.triggerBuffers(fighter, CycleType.DIE, context);
			}
		}
	}
	
	

	
	public static FighterBuffer getHertChangeBuffer(Map<AttackerAttributeKey, List<FighterBuffer>> buffers) {
		if (buffers == null) {
			return null; 
		}
		
		List<FighterBuffer> bufferList = buffers.get(AttackerAttributeKey.HERT_CHANGE);
		if(bufferList == null) {
			return null;
		}
		
		for (FighterBuffer buffer : bufferList) {
			if (!buffer.isTimeout()) {
				return buffer;
			}
		}
		return null;
	}
	public static FighterBuffer getInfightHertChangeBuffer(Map<AttackerAttributeKey, List<FighterBuffer>> buffers) {
		if (buffers == null) {
			return null; 
		}
		
		List<FighterBuffer> bufferList = buffers.get(AttackerAttributeKey.IN_FIGHTING_HEAT_CHANGE);
		if(bufferList == null) {
			return null;
		}
		
		for (FighterBuffer buffer : bufferList) {
			if (!buffer.isTimeout()) {
				return buffer;
			}
		}
		return null;
	}
}
