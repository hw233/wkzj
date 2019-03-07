package com.jtang.gameserver.module.battle.type;

/**
 * 用于返回战报时, 标明当前的战报是被动影响(比如被暴击) 还是主动触发(比如躲闪)
 * @author vinceruan
 *
 */
public enum EffectReportType {
	SKILL,	//主动释放技能
	EFFECT, //被动受到技能影响
	BUFFER  //buffer跳动
}
