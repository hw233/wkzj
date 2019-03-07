package com.jtang.gameserver.module.skill.effect.inbattle;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.dataconfig.service.SkillService;
import com.jtang.gameserver.module.battle.constant.BattleRule;
import com.jtang.gameserver.module.battle.helper.FightProcessor;
import com.jtang.gameserver.module.battle.model.Action;
import com.jtang.gameserver.module.battle.model.AttributeChange;
import com.jtang.gameserver.module.battle.model.BattleMap;
import com.jtang.gameserver.module.battle.model.BufferAction;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.DeadAction;
import com.jtang.gameserver.module.battle.model.DisapperAction;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.battle.model.SequenceAction;
import com.jtang.gameserver.module.battle.model.SkillTarget;
import com.jtang.gameserver.module.battle.model.SpawnAction;
import com.jtang.gameserver.module.battle.model.Tile;
import com.jtang.gameserver.module.battle.type.Camp;
import com.jtang.gameserver.module.buffer.helper.BufferHelper;
import com.jtang.gameserver.module.buffer.model.AttackBuffer;
import com.jtang.gameserver.module.buffer.model.DeffendsBuffer;
import com.jtang.gameserver.module.buffer.model.FighterBuffer;
import com.jtang.gameserver.module.buffer.model.HpMaxBuffer;
import com.jtang.gameserver.module.buffer.type.BufferEndType;
import com.jtang.gameserver.module.buffer.type.BufferType;
import com.jtang.gameserver.module.buffer.type.CycleType;
import com.jtang.gameserver.module.skill.model.SkillReport;
import com.jtang.gameserver.module.skill.model.TargetReport;
import com.jtang.gameserver.module.skill.type.ProcessType;

/**
 * 抽象的技能解析器
 * 
 * @author vinceruan
 */
public abstract class AbstractInBattleEffectParser implements InBattleEffectParser {
	
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
	@Autowired
	private InBattleSkillEffectContext context;

	@PostConstruct
	void init() {
		context.putParser(getParserId(), this);
	}
	
	/**
	 * 添加属性变更数据包
	 * @param hero
	 * @param report
	 * @param attr
	 */
	protected void addAttributeChange(Fighter hero, TargetReport report, AttackerAttributeKey attr) {
		report.addAttrChange(AttributeChange.valueOf(attr, hero.getAttrVal(attr)));
	}
	
	/**
	 * 添加buffer
	 * @param caster 施法者
	 * @param target 目标
	 * @param report 战报记录
	 * @param attr	 buffer作用的属性
	 * @param effect	效果配置
	 * @param context	上下文环境
	 * @param changeVal	buffer每次跳动改变的属性值
	 * @param syn2Client	是否应该同步到客户端
	 * @param type			buffer的类型
	 */
	protected void addBuffer(Fighter caster, Fighter target, TargetReport report, AttackerAttributeKey attr, InbattleEffectConfig effect, Context context, int changeVal, boolean syn2Client, BufferType type) {
		FighterBuffer buffer = this.newBuffer(context.generateBufferId(), caster, target, attr, effect, changeVal, syn2Client, type);
		
		this.addBuffer(buffer, target, effect, context, report); 
	}
	
	/**
	 * 通用方法,给角色附加一个buffer,并且添加战报
	 * @param buffer 
	 * @param owner buffer的拥有者
	 * @param effect 技能配置
	 * @param context 上下文环境
	 * @return boolean buffer有没有立刻生效
	 */
	protected void addBuffer(final FighterBuffer buffer, Fighter owner, InbattleEffectConfig effect, Context context, TargetReport report) {		
		boolean res = buffer.heartBeat(CycleType.RIGHT_NOW);
		
		if (!buffer.isTimeout()) {
			owner.addBuffer(buffer);
		}
		
		//如果bufferA是需要等到施法者死亡才解除, 则往施法者身上同时加上一个死亡才跳动的bufferB, 在施法者死亡时触发, 解除受害者身上的bufferA
		if ((buffer.getEndType() & BufferEndType.CASTER_DIE) == BufferEndType.CASTER_DIE) {
			FighterBuffer casterBuffer = new FighterBuffer(context.generateBufferId(), -1, AttackerAttributeKey.NONE, buffer.getCaster(), buffer.getCaster(), CycleType.DIE, BufferEndType.EFFECT_ONCE, effect.getEffectId(), false, "解除受害者身上的魔法:"+effect.getSkillName(), BufferType.STATUS_BUFFER) {
				@Override
				protected boolean heartBeatInternal() {
					buffer.setTimeout(true);
					return super.heartBeatInternal();
				}
			};
			buffer.getCaster().addBuffer(casterBuffer);
		}
		
		AttackerAttributeKey attr = buffer.getAttr();
		
		if (res && buffer.getType() == BufferType.ATTR_BUFFER) {
			report.addAttrChange(AttributeChange.valueOf(attr, owner.getAttrVal(attr)));
		}
		
		if (!buffer.isTimeout() && buffer.isSyn2Client()) {
			BufferAction bufRep = BufferAction.addBuffer(owner.getFighterId(), buffer.getEffectId(),  AttributeChange.valueOf(attr, owner.getAttrVal(attr)));
			report.actions.add(bufRep);
		}
	}
	
	/**
	 * 新建一个buffer
	 * @param caster
	 * @param target
	 * @param attr
	 * @param effect
	 * @param changeVal
	 * @return
	 */
	protected FighterBuffer newBuffer(int id, Fighter caster, Fighter target, AttackerAttributeKey attr, InbattleEffectConfig effect, int changeVal, boolean syn2Client, BufferType type) {
		FighterBuffer buffer = new FighterBuffer(id, changeVal, attr, caster, target, effect, syn2Client, type);	
		return buffer;
	}
	
	/**
	 * 增加生命上限
	 * @param attacker
	 * @param report
	 * @param effect
	 * @param context
	 * @param addHpMax
	 */
	protected void incrHpMax(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context, int addHpMax) {
//		//增加生命上限
//		FighterBuffer buffer = newBuffer(context.generateBufferId(), attacker, attacker, AttackerAttributeKey.HP_MAX, effect, addHpMax, true, BufferType.ATTR_BUFFER);
//		attacker.addBuffer(buffer);
//		
//		//满血
//		int addHp = attacker.getHpMax() - attacker.getHp();
//		if (addHp > 0) {
//			attacker.addHp(addHp);
//		}
//		
//		addAttributeChange(attacker, report, AttackerAttributeKey.HP_MAX);
//		addAttributeChange(attacker, report, AttackerAttributeKey.HP);
//		BufferAction buf = BufferAction.addBuffer(buffer.getOwner().getFighterId(), buffer.getEffectId(),  AttributeChange.valueOf(AttackerAttributeKey.HP_MAX, attacker.getAttrVal(AttackerAttributeKey.HP_MAX)));
//		report.actions.add(buf);
		
		FighterBuffer buffer = new HpMaxBuffer(context.generateBufferId(), addHpMax, caster, target, effect);
		this.addBuffer(buffer, target, effect, context, report);
		target.addHp(addHpMax);
		addAttributeChange(target, report, AttackerAttributeKey.HP);
	}
	
	/**
	 * 增加血量
	 * @param attacker
	 * @param target
	 * @param targetReport
	 * @param effect
	 * @param context
	 * @param addHp
	 */
	protected void incrHp(Fighter attacker, Fighter target, TargetReport targetReport, InbattleEffectConfig effect, Context context, int addHp) {
		target.addHp(addHp);
		SkillTarget action = new SkillTarget(target.getFighterId(), AttributeChange.valueOf(AttackerAttributeKey.HP, target.getAttrVal(AttackerAttributeKey.HP)));
		targetReport.targetAttr = action;
	}
		
	/**
	 * 保证伤害值是有效的(即至少产生一点伤害值)
	 * @param hurt
	 * @return
	 */
	protected int ensureValidHurt(int hurt) {
		if (hurt < 1) {
			hurt = 1;
		}
		return hurt;
	}
	
	/**
	 * @param addAtk
	 * @return
	 */
	protected int ensureAddValidAtk(int addAtk) {
		if (addAtk < 1) {
			addAtk = 1;
		}
		return addAtk;
	}
	
	/**
	 * @param addDef
	 * @return
	 */
	protected int ensureAddValidDef(int addDef) {
		if (addDef < 1) {
			addDef = 1;
		}
		return addDef;
	}
	
	/**
	 * 增加血量
	 * 增加值是根据表达式和两个参数计算出来的
	 * @param caster
	 * @param arg1
	 * @param arg2
	 * @param target
	 * @param report
	 * @param effect
	 * @param context
	 * @return
	 */
	protected boolean addHpComputedByTwoArg(Fighter caster, int arg1, int arg2, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		int addHp = FormulaHelper.executeCeilInt(effect.getEffectExpr(), arg1, arg2);
		return addHp(caster, target, report, effect, context, addHp);
	}
	
	/**
	 * 增加血量
	 * 增加值是根据表达式和一个参数计算出来的
	 * @param caster
	 * @param target
	 * @param report
	 * @param effect
	 * @param context
	 * @param arg
	 * @return
	 */
	protected boolean addHpComputedByOneArg(Fighter caster, Fighter target,	TargetReport report,InbattleEffectConfig effect, Context context, int arg) {
		int addHp = FormulaHelper.executeCeilInt(effect.getEffectExpr(), arg);
		return addHp(caster, target, report, effect, context, addHp);
	}

	/**
	 * 增加血量
	 * @param caster
	 * @param target
	 * @param report
	 * @param effect
	 * @param context
	 * @param addHp
	 * @return
	 */
	protected boolean addHp(Fighter caster, Fighter target, TargetReport report,
			InbattleEffectConfig effect, Context context, int addHp) {
		if (target.isDead()) {
			report.valid = false;
			return false;
		}
		
		if (addHp < 1) {
			addHp = 1;
		}
		
		incrHp(caster, target, report, effect, context, addHp);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}]释放技能[{}],对目标[{}]回复血量值：[{}]", caster.getName(), effect.getSkillName(), target.getName(), addHp);
		}
		//记录加血的仙人
		context.addFighterHpAdded(target);
		return true;
	}
	
	/**
	 * 增加生命上限
	 * @param attacker
	 * @param target
	 * @param report
	 * @param effect
	 * @param context
	 * @param arg
	 * @return
	 */
	protected boolean addHpMaxComputedByOneArg(Fighter attacker, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context,
			int arg) {
		if (target.isDead()) {
			report.valid = false;
			return false;
		}
		
		int addHpMax = FormulaHelper.executeCeilInt(effect.getEffectExpr(), arg);
		
		if(addHpMax < 1) {
			addHpMax = 1;
		}
		
		this.incrHpMax(attacker, target, report, effect, context, addHpMax);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}]释放技能[{}],对[{}]增加生命上限值:[{}]", attacker.getName(), effect.getSkillName(), target.getName(), addHpMax);
		}
		return true;
	}
	
	/**
	 * 增加防御力
	 * @param attacker
	 * @param target
	 * @param report
	 * @param defense
	 * @param effect
	 * @param context
	 * @return
	 */
	protected boolean addDefComputedByOneArg(Fighter attacker, Fighter target,TargetReport report, int defense, InbattleEffectConfig effect, Context context) {
		if (target.isDead()) {
			report.valid = false;
			return false;
		}
		int addDefense = FormulaHelper.executeCeilInt(effect.getEffectExpr(), defense);
		
		if (addDefense < 1) {
			addDefense = 1;
		}
		
		addBuffer(attacker, target, report, AttackerAttributeKey.DEFENSE, effect, context, addDefense, true, BufferType.ATTR_BUFFER);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}]释放技能[{}],对[{}]增加了防御力:[{}]", attacker.getName(), effect.getSkillName(), target.getName(), addDefense);
		}
		return true;
	}
	
	/**
	 * 改变防御力
	 * @param attacker
	 * @param target
	 * @param report
	 * @param addDefense
	 * @param effect
	 * @param context
	 * @return
	 */
	protected boolean addDeffendsBuffer(Fighter caster, Fighter target,TargetReport report, int value, InbattleEffectConfig effect, Context context) {
//		if (target.isDead()) {
//			report.valid = false;
//			return false;
//		}
//		
//		if (addDefense < 1) {
//			addDefense = 1;
//		}
//		
//		addBuffer(attacker, target, report, AttackerAttributeKey.DEFENSE, effect, context, addDefense, true, BufferType.ATTR_BUFFER);
//		if (LOGGER.isDebugEnabled()) {
//			LOGGER.debug("[{}]释放技能[{}],对[{}]增加了防御力:[{}]", attacker.getName(), effect.getSkillName(), target.getName(), addDefense);
//		}
		if (value + target.getDefense() < 0) {
			value = -Math.abs(target.getDefense());
		}
		DeffendsBuffer fighterBuffer = new DeffendsBuffer(context.generateBufferId(), value, caster, target, effect);
		this.addBuffer(fighterBuffer, target, effect, context, report);
		return true;
	}

	/**
	 * 增加攻击力
	 * @param caster
	 * @param target
	 * @param report
	 * @param arg
	 * @param effect
	 * @param context
	 * @return
	 */
	protected boolean addAtkComputedByOneArg(Fighter caster, Fighter target, TargetReport report, int arg, InbattleEffectConfig effect, Context context) {
		if (target.isDead()) {
			report.valid = false;
			return false;
		}
		
		int addAtk = FormulaHelper.executeCeilInt(effect.getEffectExpr(), arg);
		
		addAtk = ensureAddValidAtk(addAtk);		
		
		addBuffer(caster, target, report, AttackerAttributeKey.ATK, effect, context, addAtk, true, BufferType.ATTR_BUFFER);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}]释放技能[{}],对[{}]增加了攻击力值:[{}]", caster.getName(), effect.getSkillName(), target.getName(), addAtk);
		}
				
		return true;
	}
	/**
	 * 改变攻击力
	 * @param caster
	 * @param target
	 * @param report
	 * @param arg
	 * @param effect
	 * @param context
	 * @return
	 */
	protected boolean addAtkBuffer(Fighter caster, Fighter target, TargetReport report, int value, InbattleEffectConfig effect, Context context) {

		if (value + target.getAtk() < 0) {
			value = -Math.abs(target.getAtk());
		}
		AttackBuffer fighterBuffer = new AttackBuffer(context.generateBufferId(), value, caster, target, effect);
		this.addBuffer(fighterBuffer, target, effect, context, report);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}]释放技能[{}],对[{}]改变了攻击力值:[{}]", caster.getName(), effect.getSkillName(), target.getName(), value);
		}
		return true;
	}
	
	/**
	 * 减少攻击力
	 * @param caster
	 * @param target
	 * @param report
	 * @param arg
	 * @param effect
	 * @param context
	 * @return
	 */
	protected boolean decrAtkComputedByOneArg(Fighter caster, Fighter target, TargetReport report, int arg, InbattleEffectConfig effect, Context context) {
		if (target.isDead()) {
			report.valid = false;
			return false;
		}
		int decrAtk = FormulaHelper.executeCeilInt(effect.getEffectExpr(), arg);
		
		decrAtk = -Math.abs(decrAtk);
		if (decrAtk == 0) {
			decrAtk = -1;
		}
		
		if (decrAtk + target.getAtk() < 0) {
			decrAtk = -Math.abs(target.getAtk());
		}
				
		this.addBuffer(caster, target, report, AttackerAttributeKey.ATK, effect, context, decrAtk, true, BufferType.ATTR_BUFFER);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}]释放技能[{}],对[{}]减少了攻击力值:[{}]", caster.getName(), effect.getSkillName(), target.getName(), decrAtk);
		}
		
		return true;
	}
	
	/**
	 * 减少防御力
	 * @param caster
	 * @param target
	 * @param report
	 * @param arg
	 * @param effect
	 * @param context
	 * @return
	 */
	protected boolean decrDefComputedByOneArg(Fighter caster, Fighter target, TargetReport report, int arg, InbattleEffectConfig effect, Context context) {
		if (target.isDead()) {
			report.valid = false;
			return false;
		}
		int decrDef = FormulaHelper.executeCeilInt(effect.getEffectExpr(), arg);
		
		decrDef = -Math.abs(decrDef);
		if (decrDef == 0) {
			decrDef = -1;
		}
		
		if (decrDef + target.getDefense() < 0) {
			decrDef = -Math.abs(target.getDefense());
		}
				
		this.addBuffer(caster, target, report, AttackerAttributeKey.DEFENSE, effect, context, decrDef, true, BufferType.ATTR_BUFFER);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}]释放技能[{}],对[{}]减少了防御值:[{}]", caster.getName(), effect.getSkillName(), target.getName(), decrDef);
		}
		
		return true;
	}
	
	/**
	 * 加伤害
	 * @param caster
	 * @param target
	 * @param report
	 * @param arg
	 * @param effect
	 * @param context
	 * @return
	 */
	protected boolean addHurtComputedByOneArg(Fighter caster, Fighter target, TargetReport report, int arg, InbattleEffectConfig effect, Context context) {
		int hurt = FormulaHelper.executeCeilInt(effect.getEffectExpr(), arg);
		return addHurt(caster, target, report, effect, context, hurt);
	}
	
	/**
	 * 加伤害
	 * @param caster
	 * @param target
	 * @param report
	 * @param effect
	 * @param context
	 * @param atk
	 * @param defense
	 * @return
	 */
	protected boolean addHurtComputedByTwoArg(Fighter caster, Fighter target, TargetReport report,
			InbattleEffectConfig effect, Context context, int atk, int defense) {
		//计算伤害值
		int hurt = FormulaHelper.executeCeilInt(effect.getEffectExpr(), atk, defense, BattleRule.BATTLE_DEF_FACTOR);
		return addHurt(caster, target, report, effect, context, hurt);
	}
	
	/**
	 * 加伤害
	 * @param caster
	 * @param target
	 * @param report
	 * @param effect
	 * @param context
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @return
	 */
	protected boolean addHurtComputedByThreeArg(Fighter caster, Fighter target, TargetReport report,
			InbattleEffectConfig effect, Context context, int arg1, int arg2,
			int arg3) {
		//计算伤害值
		int hurt = FormulaHelper.executeCeilInt(effect.getEffectExpr(), arg1, arg2, BattleRule.BATTLE_DEF_FACTOR, arg3);
				
		return addHurt(caster, target, report, effect, context, hurt);
	}

	/**
	 * 加伤害
	 * @param caster
	 * @param target
	 * @param report
	 * @param effect
	 * @param context
	 * @param hurt
	 * @return
	 */
	protected boolean addHurt(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context, int hurt) {
		if (target.isDead()) {
			report.valid = false;
			return false;
		}
		
		processSkillBeforeBeAtked(target, context);
		boolean im = tryImmunity(caster, target, effect, context);
		boolean dodge = FightProcessor.getInstance().tryDodge(target, context);
//		//尝试闪避
//		boolean isDodge = FightProcessor.getInstance().tryDodge(target, context);
		if(im || dodge) {
			return true;
		}
		
		addActualHurt(caster, target, report, effect, context, hurt);
		
		return true;
	}
	
	/**
	 * 加伤害
	 * @param caster
	 * @param target
	 * @param report
	 * @param effect
	 * @param context
	 * @param hurt
	 */
	protected void addActualHurt(Fighter caster, final Fighter target, final TargetReport report, InbattleEffectConfig effect, final Context context, int hurt) {
		//伤害增加buf处理
		FighterBuffer hertChangeBuf = BufferHelper.getHertChangeBuffer(target.getBuffers());
		if (hertChangeBuf != null && hertChangeBuf.isTimeout() == false) {
			hurt -= hertChangeBuf.getAddVal();
		}
		
		if (BufferHelper.isInFightingHertChange(caster.getBuffers())) {
			FighterBuffer buf = BufferHelper.getInfightHertChangeBuffer(caster.getBuffers());
			InbattleEffectConfig inbattleEffectConfig = SkillService.getInbattleEffect(buf.getEffectId());
			Double d = Math.ceil(Integer.valueOf(inbattleEffectConfig.getEffectExpr()) / 1000.0 * hurt);
			hurt = d.intValue();
		}
		
		//保证伤害至少为1点
		hurt = ensureValidHurt(hurt);		
		FighterBuffer buffer = new FighterBuffer(context.generateBufferId(), hurt, AttackerAttributeKey.HP, caster, target, effect, true, BufferType.ATTR_BUFFER) {
			@Override
			protected boolean heartBeatInternal() {
				//减血
				getOwner().addHurt(Math.abs(getAddVal()));				
				//统计阵营的总伤害值(用于对峙时胜败判定)
				context.addAtkHur(getCaster().getCamp(), getAddVal());
				return true;
			}
		};
		addBuffer(buffer, target, effect, context, report);
		
		//记录受影响者(用于反击)
		context.addFighterBeAtcked(target);	
		
		if (effect.getEffectCycle() == CycleType.RIGHT_NOW) {
			if (context.getProcessType() == ProcessType.COMMON_ATK) {
				context.setCommonAtkHurt(hurt);
			} else {
				context.setSkillAtkHurt(hurt);
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}]释放技能[{}],对[{}]造成伤害值:[{}]", caster.getName(), effect.getSkillName(), target.getName(), hurt);
		}
	}
	
	/**
	 * 处理被攻击时的技能触发
	 * @param target
	 * @param context
	 */
	protected void processSkillBeforeBeAtked(Fighter target, Context context) {
		//处理敌人被攻击前时触发的技能.
		FightProcessor.processSkillEffects(target, context, ProcessType.BEFORE_BE_ATKED);
		
		if (context.getProcessType() == ProcessType.COMMON_ATK) {
			//普攻攻击前触发
			FightProcessor.processSkillEffects(target, context, ProcessType.BEFORE_BE_COMM_ATKED);
		} else {
			//技能攻击前触发
			FightProcessor.processSkillEffects(target, context, ProcessType.BEFORE_BE_SKILL_ATKED);
		}
	}

	@Override
	public boolean parser(Fighter caster, List<Fighter> targets, InbattleEffectConfig effect, Context context) {
		if (targets.isEmpty()) {
			return false;
		}
		
		//保存一次技能达到N个人身上的数据包
		byte distance = 0;
		if (effect.getProcessType() == ProcessType.COMMON_ATK.getCode()) {
			distance = getDistance(caster, targets.get(0), context);
		}
		SkillReport skillRpt = new SkillReport(caster.getFighterId(), effect.getSkillId(), distance);
		boolean result = false;
		for (Fighter target : targets) {
			TargetReport report = new TargetReport(target.getFighterId());
			
				
				/**第四步: 将效果应用到目标身上 */
			boolean result1 = castSkill(caster, target, report, effect, context);
			
			result = result || result1;
				
			
			//由于某些原因导致技能无法释放到该目标身上(比如一个伤害技能打到一个死人身上),则该数据包无效
			//或者该数据包不需要下发,则也把valid设置为false,比如一个战斗结束时加经验的技能，或者是一个虚拟出来的技能(比如Parser1240)
			if (report.isValid()) {
				skillRpt.targets.add(report);
			}
			if (context.tempTargetReports.size() > 0) {
				skillRpt.targets.addAll(context.tempTargetReports);
			}
			context.tempTargetReports.clear();
			if (result1 && target.isDead()) {
				DeadAction deadAction = new DeadAction(target.getFighterId());
				report.actions.add(deadAction);
				DisapperAction disapperAction = new DisapperAction(target.getFighterId());
				report.actions.add(disapperAction);
				deadAction.setDisapperAction(disapperAction);
			}
			
		}
		
		addSkillReport(effect, skillRpt, context);
		return result;
	}
	
	protected boolean tryImmunity(Fighter caster, Fighter target, InbattleEffectConfig effect, Context context) {
		
		return FightProcessor.getInstance().tryImmunity(target, context, effect.getEffectId());
//		report.actions.add(new ImmunityAction(target.getFighterId()));
//		return true;
	}

	protected void addSkillReport(InbattleEffectConfig effect, SkillReport report, Context context) {
		if (report.isValid() == false) {
			return;
		}

		List<SkillTarget> list = new ArrayList<>();
		List<Action> actions = new ArrayList<>();
		for (TargetReport tr : report.targets) {
			list.add(tr.targetAttr);
//			actions.addAll(tr.actions);
			SequenceAction sequenceAction = new SequenceAction();
			sequenceAction.actions = tr.actions;
			actions.add(sequenceAction);
		}
		SpawnAction spawnAction  = new SpawnAction();
		spawnAction.actions = actions;
		List<Action> actionList = new ArrayList<>();
		actionList.add(spawnAction);
		//记录一个技能释放
		context.fightRecorder.startRecordSkillAtk(report.casters, report.skillId, list, actionList, report.distance, actions);
		
//		//记录N个人同时被技能击中
//		context.fightRecorder.startSpwanAction();
//		for (TargetReport atk : report.targets) {
//			if (atk.actions.size() > 1) {
//				//记录每个人被击中后的一系列顺序动作：加buffer等
//				context.fightRecorder.startSequenceAction();
//				context.fightRecorder.addActions(atk.actions);
//				context.fightRecorder.stopSequenceAction();
//			} else {
//				context.fightRecorder.addActions(atk.actions);
//			}
//		}
//		context.fightRecorder.stopSpwanAction();
	}
	
	/**
	 * 获取前方队友
	 * @param fighter
	 * @param targets
	 * @return
	 */
	protected List<Fighter> getAheadFighter(Fighter fighter, Context context, boolean filterDead) {
		Camp camp = fighter.getCamp();
		List<Fighter> targets = context.getTeamListByCamp(camp);
		List<Fighter> list = new ArrayList<>();
		for (Fighter f : targets) {
			if (fighter.getFighterId() == f.getFighterId()){
				continue;
			}
			if (f.isDead() && filterDead) {
				continue;
			}
			if (f.getTile().getX() == fighter.getTile().getX() && camp.isAhead(f.getTile(), fighter.getTile())) {
				list.add(f);
			}
		}
		return list;
		
	}
	/**
	 * 获取后方队友
	 * @param fighter
	 * @param targets
	 * @return
	 */
	protected List<Fighter> getBehindFighter(Fighter fighter, Context context, boolean filterDead) {
		Camp camp = fighter.getCamp();
		List<Fighter> targets = context.getTeamListByCamp(camp);
		List<Fighter> list = new ArrayList<>();
		for (Fighter f : targets) {
			if (fighter.getFighterId() == f.getFighterId()){
				continue;
			}
			if (f.isDead() && filterDead) {
				continue;
			}
			if (f.getTile().getX() == fighter.getTile().getX() && camp.isBehind(f.getTile(), fighter.getTile())) {
				list.add(f);
			}
		}
		return list;
		
	}
	
	protected boolean castSkill(Fighter caster, Fighter target,TargetReport report, InbattleEffectConfig effect, Context context) {
		throw new RuntimeException("子类需要实现该方法才能被调用");
	}
	
	protected abstract int getParserId();
	
	/**
	 * 获取攻击者方向
	 * @param fighters
	 * @param target
	 * @param context
	 * @return
	 */
	protected List<Byte> getDirection(List<Fighter> fighters, Fighter target, Context context){
		List<Byte> result = new ArrayList<>();
		for (Fighter fighter : fighters) {
			Byte direction = getDirection(fighter, target, context);
			result.add(direction);
		}
		return result;
		
	}
	/**
	 * 获取距离
	 * @param fighter
	 * @param target
	 * @param context
	 * @return 2：远程 1：近程
	 */
	protected byte getDistance(Fighter fighter, Fighter target, Context context) {
		if (context.getTargetEnermy() != null){
			target = context.getTargetEnermy();
		}
		Tile targetTile = target.getTile();
		BattleMap map = context.getBattleMap();
		Tile tile = fighter.getTile();
		byte result = map.isFourDirTile(tile, targetTile);
		return result;
	}
	protected byte getDirection(Fighter fighter, Fighter target, Context context){
		if (context.getTargetEnermy() != null){
			target = context.getTargetEnermy();
		}
		Tile targetTile = target.getTile();
		BattleMap map = context.getBattleMap();
		Tile tile = fighter.getTile();
		byte direction = map.getDirections(tile, targetTile);
		return direction;
//		if (fighter.direction == (byte)1) {
//			return direction;
//		} else {
//			if (direction == 1) {
//				return 0;
//			} else {
//				return 1;
//			}
//		}
//		if(fighter.direction != target.direction) {//反向
//			if(direction == (byte)0) {
//				if (fighter.direction == (byte)0) {
//					fighter.direction = 1;
//				} else {
//					fighter.direction = 0;
//				}
//			}
//		} else {
//			if(direction == (byte)1) {
//				if (fighter.direction == (byte)0) {
//					fighter.direction = 1;
//				} else {
//					fighter.direction = 0;
//				}
//			}
//		}
		
		
	}
}
