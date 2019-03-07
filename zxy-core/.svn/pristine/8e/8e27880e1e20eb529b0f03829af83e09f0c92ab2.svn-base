//package com.jtang.core.rhino;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.stereotype.Component;
//
//import com.jtang.core.utility.RhinoUtils;
//
///**
// * 脚本执行帮助类
// * {@code ScriptConfig}
// * @author 0x737263
// * 
// */
//@Component
//public class ScriptHelper {
//	/**
//	 * 运算结果缓存集合
//	 */
//	private static Map<String, Number> EXECUTE_RESULT_MAPS = new HashMap<String, Number>(5000);
//	
//	private static final String CACHE_KEY_HEAD = "script_";
//
//	/**
//	 * 执行脚本
//	 * 
//	 * @param id	脚本id
//	 * @param args	参数 参数名为x1,x2,x3...
//	 * @return
//	 */
//	public static Number execute(int id, Number... args) {
//		String cacheKey = getCacheKey(id, args);
//
//		Number result = EXECUTE_RESULT_MAPS.get(cacheKey);
//		if (result != null) {
//			return result;
//		}
//
//		Object value = RhinoUtils.execute(getExpression(id), cacheKey, args);
//
//		result = (value == null) ? Integer.valueOf(0) : (Number) value;
//		if (EXECUTE_RESULT_MAPS.containsKey(cacheKey) == false) {
//			EXECUTE_RESULT_MAPS.put(cacheKey, result);
//		}
//
//		return result;
//	}
//
//	public static int executeInt(int id, Number... args) {
//		return execute(id, args).intValue();
//	}
//
//	/**
//	 * 获取脚本内容
//	 * @param id
//	 * @return
//	 */
//	private static String getExpression(int id) {
//		return ScriptService.getScript(id);
//	}
//
//	/**
//	 * 生成缓存key
//	 * 
//	 * @param id
//	 * @param args
//	 * @return
//	 */
//	private static String getCacheKey(int id, Number... args) {
//		return CACHE_KEY_HEAD + String.valueOf(id) + Arrays.toString(args);
//	}
//
//}
