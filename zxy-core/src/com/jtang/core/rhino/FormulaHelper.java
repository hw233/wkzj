package com.jtang.core.rhino;

import org.springframework.stereotype.Component;

import com.jtang.core.utility.RhinoUtils;

/**
 * 公式帮助类
 * @author 0x737263
 *
 */
@Component
public class FormulaHelper {

	private static final String CACHE_KEY_HEAD = "formula_";
	
	/**
	 * 执行公式
	 * @param formulaId		公式Id
	 * @param args			公式参数
	 * @return
	 */
	public static Number execute(String expression, Number... args) {
		Object value = RhinoUtils.execute(expression, CACHE_KEY_HEAD + expression, args);
		Number result = (value == null) ? Integer.valueOf(0) : (Number) value;
		
		return result;
	}
	
	/**
	 * 执行公式(float)
	 * @param formulaId		公式Id
	 * @param args			公式参数
	 * @return 返回int
	 */
	public static float executeFloat(String expression, Number... args) {
		return execute(expression, args).floatValue();
	}
	
	/**
	 * 执行公式(确定无小数的才使用）
	 * @param formulaId		公式Id
	 * @param args			公式参数
	 * @return 返回int
	 */
	public static int executeInt(String expression, Number... args) {
		return execute(expression, args).intValue();
	}
	
	/**
	 * 向上取证执行公式
	 * @param expression
	 * @param args
	 * @return
	 */
	public static int executeCeilInt(String expression, Number... args) {
		Number number = execute(expression, args);
		Double d = number.doubleValue();
		Double result = Math.ceil(d);
		return result.intValue();
	}
	
	/**
	 * 向下取证执行公式
	 * @param expression
	 * @param args
	 * @return
	 */
	public static int executeFloorInt(String expression, Number... args) {
		Number number = execute(expression, args);
		Double d = number.doubleValue();
		Double result = Math.floor(d);
		return result.intValue();
	}
	
	/**
	 * 四舍五入执行公式
	 * @param expression
	 * @param args
	 * @return
	 */
	public static int executeRoundInt(String expression, Number... args) {
		Number number = execute(expression, args);
		Float f = number.floatValue();
		return Math.round(f);
	}
	/**
	 * 四舍五入执行公式
	 * @param expression
	 * @param args
	 * @return
	 */
	public static long executeRoundLong(String expression, Number... args) {
		Number number = execute(expression, args);
		Double d = number.doubleValue();
		return Math.round(d);
	}
	

	
	/**
	 * 执行有字符拼接的js表达式
	 * @param expression
	 * @param args
	 * @return
	 */
	public static String excuteString(String expression, Number... args) {
		Object value = RhinoUtils.execute(expression, CACHE_KEY_HEAD + expression, args);
		String result = (value == null) ? "" :  value.toString();

		return result;
	}
	

}
