package com.jtang.gameserver.module.vampiir.constant;

import com.jtang.core.rhino.FormulaHelper;
import com.jtang.gameserver.dataconfig.service.GlobalService;

public class VampiirRule {
	
	/**
	 * 每次吸灵最多可吸收的仙人数
	 */
	public static int VAMPIIR_MAX_SELECTED_HEROS;
	
	/**
	 * 吸灵时消耗金币的表达式
	 */
	public static String VAMPIIR_CONSUME_GOLDS_EXPR;
	
	/**
	 * 吸灵室使用物品最多物品id数
	 */
	public static int VAMPIIR_MAX_USE_GOODS;

	static {
		VAMPIIR_MAX_SELECTED_HEROS = GlobalService.getInt("VAMPIIR_MAX_SELECTED_HEROS");
		VAMPIIR_CONSUME_GOLDS_EXPR = GlobalService.get("VAMPIIR_CONSUME_GOLDS_EXPR");
		VAMPIIR_MAX_USE_GOODS = GlobalService.getInt("VAMPIIR_MAX_USE_GOODS");

		// 预解析公式
		FormulaHelper.execute(VAMPIIR_CONSUME_GOLDS_EXPR, new Number[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
	}
	
	public static int getGolds(int level, int addExp, int star) {
		return  FormulaHelper.executeCeilInt(VampiirRule.VAMPIIR_CONSUME_GOLDS_EXPR, level, addExp, star);
	}
	
}
