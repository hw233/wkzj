package com.jtang.gameserver.module.snatch.result;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * 抢夺结果上下文
 * @author 0x737263
 *
 */
@Component
public class SnatchResultContext {
	
	private static Map<Byte, SnatchResult> SNATCH_RESULT_MAPS = new HashMap<>();
	
	/**
	 * 获取抢夺结果执行类
	 * @param snatchType
	 * @return
	 */
	public SnatchResult get() {
		return SNATCH_RESULT_MAPS.get(0);
	}
	
	/**
	 * 添加解析器
	 * @param snatchType
	 * @param snatchResult
	 */
	public void put(SnatchResult snatchResult) {
		SNATCH_RESULT_MAPS.put((byte)0, snatchResult);
	}
	
}
