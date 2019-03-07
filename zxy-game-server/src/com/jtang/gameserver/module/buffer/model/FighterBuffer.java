package com.jtang.gameserver.module.buffer.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.buffer.type.BufferEndType;
import com.jtang.gameserver.module.buffer.type.BufferType;
import com.jtang.gameserver.module.buffer.type.CycleType;


/**
 * 战斗中产生的临时buffer
 * @author vinceruan
 */
public class FighterBuffer {
	static Logger logger = LoggerFactory.getLogger(FighterBuffer.class);

	/** ID */
	private int id;
	
	/** 效果ID */
	private int effectId;
	
	/** 跳动类型  */
	private int cycleType;

	/** 结束时机 */
	private int endType;
	
	/** 已跳动次数 */
	private int effectTimes;
	
	/** 累计加成的值 */
	private int accumulateBufferVal = 0;
	
	/** 每次跳动增减的值(负数代表减少) */
	private int addVal;
	
	/** buffer影响到的属性id */
	private AttackerAttributeKey attr;
	
	/**
	 * buffer的拥有者
	 */
	private Fighter owner;
	
	/**
	 * buffer的释放者
	 */
	private Fighter caster;
	
	/**
	 * 是否超时
	 */
	private boolean timeout = true;
	
	/**
	 * 技能名称
	 */
	private String skillName;
	
	/**
	 * 已经持续了多少轮
	 */
	private int continuedRound = 0;
	
	/**
	 * 是否需要把buffer同步到客户端
	 */
	private boolean syn2Client = false;
	
	/**
	 * buffer的类型
	 */
	private BufferType type;
	
	
	/**
	 *  是否带效果
	 */
	private boolean isEffect;
	/**
	 * buffer跳动
	 * @param cycleType
	 * @return
	 */
	public boolean heartBeat(int cycleType) {
		if (timeout) {
			return false;
		}
		
		boolean res = false;
		if (this.cycleType == cycleType) {
			this.accumulateBufferVal += this.addVal;
			res = this.heartBeatInternal();
			this.effectTimes ++;			
		}
		
		//更新buffer的状态
		updateBufferState(cycleType);
		return res;
	}
	
	/**
	 * 留给子类自定义实现
	 * @return
	 */
	protected boolean heartBeatInternal() {
		logger.debug(String.format("buffer:%s 心跳",skillName));
		return true;
	}

	
	/**
	 * 更新buffer状态
	 * @param isRoundEnd
	 * @param isFightEnd
	 */
	private void updateBufferState(int cycleType) {
		boolean isRoundEnd = cycleType == CycleType.END_ROUND;
		boolean isFightEnd = cycleType == CycleType.END_FIGHT;
		
		if(timeout) {
			return; //如果已经超时,直接返回,以防下面的计算让buffer死而复生.
		}
		
		/** 跳动一次后失效 */
		if ((BufferEndType.EFFECT_ONCE & endType) == BufferEndType.EFFECT_ONCE && this.effectTimes >= 1) {
			logger.debug(String.format("[%s]buffer:%s已经失效", owner.getName(), skillName));
			timeout = true;
			return;
		} 
		
		if (isRoundEnd) {
			continuedRound ++;
		}
		
		/** 持续一轮 */
		if ((BufferEndType.ROUND_END & endType) == BufferEndType.ROUND_END && this.continuedRound >= 1) {
			logger.debug(String.format("[%s]buffer:%s已经失效", owner.getName(), skillName));
			timeout = true;
			return;
		}
		
		/** 持续两轮 */
		if ((BufferEndType.NEXT_ROUND_END & endType)== BufferEndType.NEXT_ROUND_END && this.continuedRound >= 2) {
			logger.debug(String.format("[%s]buffer:%s已经失效", owner.getName(), skillName));
			timeout = true;
			return;
		}
		
		/** 目标死亡时失效 */
		if ((BufferEndType.TARGET_DIE & endType) == BufferEndType.TARGET_DIE && this.owner.isDead()) {
			logger.debug(String.format("[%s]buffer:%s已经失效", owner.getName(), skillName));
			timeout = true;
			return;
		}
		
		/** 战斗结束时失效 */
		if ((BufferEndType.FIGHT_END & endType) == BufferEndType.FIGHT_END && isFightEnd) {
			logger.debug(String.format("[%s]buffer:%s已经失效", owner.getName(), skillName));
			timeout = true;
			return;
		}
		
		/** 施法者死亡时失效 */
		if ((BufferEndType.CASTER_DIE & endType) == BufferEndType.CASTER_DIE && this.caster.isDead()) {
			logger.debug(String.format("[%s]buffer:%s已经失效", owner.getName(), skillName));
			timeout = true;
			return;
		}
	}
	
	/**
	 * 提供此方法是因为,有些buffer的过期是由外部触发
	 * @param timeout
	 */
	public void setTimeout(boolean timeout) {
		this.timeout = timeout;
	}
	
//	public static FighterBuffer valueOf(int id, int addVal, AttackerAttributeKey attr, Fighter caster, Fighter owner, int cycleType, int endType, int effectId, boolean syn2Client, String skillName, BufferType type) {
//		FighterBuffer buffer = new FighterBuffer(id, addVal, attr, caster, owner, cycleType, endType, effectId, syn2Client, skillName, type);		
//		return buffer;
//	}
//	public static FighterBuffer valueOf(int id, int addVal, AttackerAttributeKey attr, Fighter caster, Fighter owner, int cycleType, int endType, int effectId, boolean syn2Client, String skillName, BufferType type, int rate) {
//		FighterBuffer buffer = new FighterBuffer(id, addVal, attr, caster, owner, cycleType, endType, effectId, syn2Client, skillName, type, rate);		
//		return buffer;
//	}
//	
//	public FighterBuffer(int id, int addVal, AttackerAttributeKey attr, Fighter caster,  Fighter owner, InbattleEffectConfig effect, boolean syn2Client, String skillName, BufferType type) {
//		this.addVal = addVal;
//		this.attr = attr;
//		this.caster = caster;
//		this.cycleType = effect.getEffectCycle();
//		this.endType = effect.getEffectEndType();
//		this.owner = owner;
//		this.id = id;
//		this.accumulateBufferVal = 0;
//		this.timeout = false;
//		this.skillName = skillName;
//		this.effectId = effect.getEffectId();
//		this.syn2Client = syn2Client;
//		this.type = type;
//	}
	public FighterBuffer(int id, int addVal, AttackerAttributeKey attr, Fighter caster,  Fighter owner,InbattleEffectConfig effect, boolean syn2Client, BufferType type) {
		this.addVal = addVal;
		this.attr = attr;
		this.caster = caster;
		this.cycleType = effect.getEffectCycle();
		this.endType = effect.getEffectEndType();
		this.owner = owner;
		this.id = id;
		this.accumulateBufferVal = 0;
		this.timeout = false;
		this.skillName = "buffer触发技能生效:"+effect.getSkillName();
		this.effectId = effect.getEffectId();
		this.syn2Client = syn2Client;
		this.type = type;
		this.isEffect = effect.isEffect == 1 ? true :false ;
	}
	/**
	 * 用于特殊buff（如:如果bufferA是需要等到施法者死亡才解除, 则往施法者身上同时加上一个死亡才跳动的bufferB, 在施法者死亡时触发, 解除受害者身上的bufferA）
	 * @param generateBufferId
	 * @param addVal
	 * @param attr
	 * @param caster
	 * @param owner
	 * @param cycleType
	 * @param endType
	 * @param effectId
	 * @param syn2Client
	 * @param skillName
	 * @param statusBuffer
	 */
	public FighterBuffer(int generateBufferId, int addVal, AttackerAttributeKey attr, Fighter caster, Fighter owner, int cycleType, int endType,
			int effectId, boolean syn2Client, String skillName, BufferType statusBuffer) {
		this.addVal = addVal;
		this.attr = attr;
		this.caster = caster;
		this.cycleType = cycleType;
		this.endType = endType;
		this.owner = owner;
		this.id = generateBufferId;
		this.accumulateBufferVal = 0;
		this.timeout = false;
		this.skillName = skillName;
		this.effectId = effectId;
		this.syn2Client = syn2Client;
		this.type = statusBuffer;
	}

	public int getId() {
		return id;
	}
	
	public boolean isTimeout() {
		return timeout;
	}
	
	public boolean isSyn2Client() {
		return syn2Client;
	}

	public int getCycleType() {
		return cycleType;
	}

	public int getEndType() {
		return endType;
	}

	public int getEffectTimes() {
		return effectTimes;
	}

	public int getAccumulateBufferVal() {
		return accumulateBufferVal;
	}

	public void addBufferVal(int val) {
		this.accumulateBufferVal += val;
	}

	public AttackerAttributeKey getAttr() {
		return attr;
	}

	public int getAddVal() {
		return addVal;
	}

	public Fighter getOwner() {
		return owner;
	}

	public String getSkillName() {
		return skillName;
	}

	public Fighter getCaster() {
		return caster;
	}

	public BufferType getType() {
		return type;
	}

	public int getEffectId() {
		return effectId;
	}
	
	public void setAddVal(int addVal) {
		this.addVal = addVal;
	}
	
	public boolean isEffect() {
		return isEffect;
	}
}

