package com.jtang.core.utility;

import java.util.concurrent.ConcurrentHashMap;

import org.mozilla.javascript.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 基于js脚本的Rhino引擎解析器
 * @author 0x737263
 *
 */
public class RhinoUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RhinoUtils.class);
	
	private static final ThreadLocal<Scriptable> THREAD_LOCALS = new ThreadLocal<Scriptable>();

	private static final ConcurrentHashMap<String, Script> SCRIPT_CACHE = new ConcurrentHashMap<String, Script>(10000);

	/**
	 * 
	 * @return
	 */
	private static Scriptable getScripttable() {
		Scriptable scope = THREAD_LOCALS.get();
		if (scope == null) {
			ContextFactory global = ContextFactory.getGlobal();
			Context context = global.enterContext();
			scope = context.initStandardObjects();
			THREAD_LOCALS.set(scope);
		}
		return scope;
	}
	

	/**
	 * 
	 * @param expression
	 * @param ctx
	 * @return
	 */
	public static Object execute(String expression, String cacheKey, Number... args) {
		if (expression == null || expression.isEmpty()) {
			return null;
		}

		if (cacheKey == null || cacheKey.isEmpty()) {
			cacheKey = expression;
		}

		Script script = SCRIPT_CACHE.get(cacheKey);
		Context context = Context.enter();
		try {
			if (script == null) {
				script = context.compileString(javascriptFunction() + expression, "<expr>", -1, null);
				if (SCRIPT_CACHE.containsKey(cacheKey) == false) {
					SCRIPT_CACHE.put(cacheKey, script);
				}
			}

			Scriptable scripttable = context.newObject(getScripttable());

			if (args != null) {
				for (int i = 0; i < args.length; i++) {
					scripttable.put(String.valueOf("x" + (i + 1)), scripttable, args[i]);
				}
			}

			return script.exec(context, scripttable);
		} catch (Exception ex) {
			LOGGER.error(String.format("expression eval error:[%s] args:[%s]", expression, StringUtils.array2String(args)), ex);
		} finally {
			Context.exit();
		}

		return null;
	}
	
	private static String javascriptFunction() {
		StringBuilder builder = new StringBuilder();
		builder.append("function ternary(x1,x2,x3){return x1 ? x2 : x3} ");  //三目运算函数
		return builder.toString();
	}
	
}