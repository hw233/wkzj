package com.jtang.gameserver.module.buffer.type;

public enum BufferCycleType {
	//-----------主动方buffer跳动时机-------
	BATTLE_START(1,"战斗开始"),
	ROUND_START(2, "回合开始"),
	X_ROUND(3,"第x回合"),
	BEFORE_HAND(4,"出手前"),
	ATTACK_UNREACHABLE(5, "射程不够"),
	BEFORE_MOVE(6, "移动前"),
	AFTER_MOVE(7, "移动后"),
	BEFORE_SKILL_ATTACK(8, "技能攻击前"),
	SKILL_ATTACK(9, "技能攻击"),
	AFTER_SKILL_ATTACK(10, "技能攻击后"),
	BEFORE_COMMON_ATTACK(11, "普通攻击前"),
	COMMON_ATTACK(12,"普通攻击"),
	AFTER_COMMON_ATTACK(13, "普通攻击后"),
	AFTER_ATTACK(14, "攻击后"),
	TARGET_DEAD(15,"目标死亡"),
	DEAD(16, "死亡"),
	AFTER_HAND(17,"出手后"),
	ROUND_END(18, "回合结束"),
	BATTLE_END(19,"战斗结束"),
	//------------被动方buffer跳动时机-----
	HURT(20, "受到伤害"),
	GAIN(21, "受到增益"),
	//------------------------------------
	RIGHT_NOW(100, "立即跳动");
		
		
		
		

	private final int code;
	private String desc;

	private BufferCycleType(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getDesc() {
		return desc;
	}
}
