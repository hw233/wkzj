package com.jtang.worldserver.module.crossbattle.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 所有玩家的阵型报名到worldserver后。通过该类重新分配新的精灵唯一id
 * (用于处理跨服重复精灵id的问题)
 * @author 0x737263
 *
 */
public class SpriteIdUtil {
	/**
	 * 每个精灵在生命周期内都会有一个临时的uid.当对象构造的时候就会分配一个。
	 */
	private static final AtomicLong AUTO_UID = new AtomicLong(1);
	
	/**
	 * 生成新的精灵id
	 * @param num
	 * @return
	 */
	public static List<Long> getSpriteId(int num) {
		List<Long> list = new ArrayList<>();
		for (int i = 0; i < num; i++) {
			long id = AUTO_UID.incrementAndGet();
			list.add(id);
		}
		return list;
	}
}
