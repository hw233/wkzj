package com.jtang.core.utility;


/**
 * uuid生成类
 * 
 * @author ludd
 *
 */
public class UUIDUtils {
	private static int sec = TimeUtils.getNow();
	private static int uid = 0;
	/**
	 * 获取uuid
	 * @param serverId 服务器id
	 * @return
	 */
	public synchronized static long getUUID(int serverId) {
		long hashId = 0L;
		uid += 1;
		
		if (uid > 0xfffff) {
			sec +=1;
			uid = 1;
		}
		
		assert(serverId >= 0 && serverId <= 0xfff);
		assert(uid >= 0 && uid <= 0xfffff);

		hashId = ((long)sec << 32);
		
		serverId &= 0xfff;
		hashId |= (long)serverId << 20;
		
		uid &= 0xfffff;
		hashId |= (long)uid;
		return hashId ;
	}
	/**
	 * 设置uuid技术
	 * @param time
	 */
	public static void setBase(int time) {
		sec = time;
	}
	
	
}	
