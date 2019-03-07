package com.jtang.gameserver.module.skill.effect;

/**
 * 战斗外的效果解析器
 * @author 0x737263
 *
 */
public interface OutbattleParserKey {

	/**
	 * 加生命上限
	 * 提高目标的生命上限值(根据目标的基础生命上限值计算加成)
	 * <pre>
	 * x1*0.2
	 * x1:目标的基础生命上限
	 * </pre>
	 */
	public static final int Parser110 = 110;
	
	/**
	 * 主动技能切换
	 * <pre>
	 * 	1401
	 *  新技能id
	 * </pre>
	 */
	public static final int Parser120 = 120;

	/**
	 * 加防御力
	 * 提高目标的防御力(在目标的基础防御力的基础上面加成)
	 * <pre>
	 * x1*0.1
	 * x1:目标的基础防御值
	 * </pre>
	 */
	public static final int Parser140 = 140;
	
	/**
	 * 加攻击力
	 * 提高目标的攻击力(根据目标的基础攻击力计算加成值)
	 * <pre>
	 * x1*0.15
	 * x1:目标的基础攻击力
	 * </pre>
	 */
	public static final int Parser151 = 151;
	
	/**
	 * 加射程
	 * 提高目标的射程
	 * <pre>
	 * 1
	 * 表示射程增加1格
	 * </pre>
	 */
	public static final int Parser160 = 160;

}
