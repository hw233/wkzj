package com.jtang.worldserver.component.oss;

import com.jiatang.common.oss.BaseOssLogger;
import com.jiatang.common.oss.OssLogType;
import com.jtang.core.utility.DateUtils;

/**
 * 游戏服oss日志记录
 * @author 0x737263
 *
 */
public class WorldOssLogger extends BaseOssLogger {
	private static byte[] lockObject = new byte[1];
	private static WorldOssLogger worldOssLogger;
	
	private WorldOssLogger() {
	}
	
	private static WorldOssLogger getInstance() {
		synchronized (lockObject) {
			if (worldOssLogger == null) {
				worldOssLogger = new WorldOssLogger();
			}
		}
		return worldOssLogger;
	}
	
	public static void reflushLogger() {
		getInstance().reflushLogger(WorldOssType.getEnumArray());
	}
	
	private void write(WorldOssType ossType, String message) {
		getInstance().write(ossType.getName(), message);
	}
	
	/**
	 * 角色贡献点增加日志
	 * @param serverId				服务器id
	 * @param actorId				角色id
	 * @param point					增加贡献点数
	 * @param totalExchangePoint	当前累计贡献点数
	 */
	public static void exchangePointAdd(int serverId, long actorId, int point, int totalExchangePoint) {
		StringBuilder sb = new StringBuilder();
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(OssLogType.ADD.getId()).append(COLUMN_SPLIT);// 1-增加，2消耗
		sb.append(point).append(COLUMN_SPLIT);
		sb.append(totalExchangePoint).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(WorldOssType.EXCHANGE_POINT, sb.toString());
	}

	/**
	 * 角色贡献点扣除日志
	 * @param serverId				服务器id
	 * @param actorId				角色id
	 * @param point					扣除贡献点数
	 * @param totalExchangePoint	当前累计贡献点数
	 */
	public static void exchangePointDecrease(int serverId, long actorId, int point, int totalExchangePoint) {
		StringBuilder sb = new StringBuilder();
		sb.append(serverId).append(COLUMN_SPLIT);
		sb.append(actorId).append(COLUMN_SPLIT);
		sb.append(OssLogType.DECREASE.getId()).append(COLUMN_SPLIT);// 1-增加，2消耗
		sb.append(point).append(COLUMN_SPLIT);
		sb.append(totalExchangePoint).append(COLUMN_SPLIT);
		sb.append(DateUtils.getNowInSecondes()).append(ROW_SPLIT);

		getInstance().write(WorldOssType.EXCHANGE_POINT, sb.toString());
	}


}
