package com.jtang.gameserver.admin.facade;

import java.util.List;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;

public interface MaintainFacade {
	
	
	/**
	 * 倒计时踢出所有玩家角色
	 * @param time
	 */
	public void delayKickActor(int time);
	
	/**
	 * 开关服务器
	 * @param flag 0.正常状态（所有人可访问） 1.维护状态（允许ip列表访问），2,关闭状态（所有人不可访问）
	 * @param time 如果关闭服务器 该字段表示倒计时踢出玩家
	 * @return
	 */
	public Result changeServerState(byte flag, int time);
	
//	/**
//	 * 发公告
//	 * @param message
//	 * @return
//	 */
//	public Result sendNotice(String message);
	
	/**
	 * 轮询公告
	 * @param message 公告内容
	 * @param pollingNum  轮询次数
	 * @param delayTime 间隔时间（秒）
	 * @return
	 */
	public Result sendNotice(String message, int pollingNum, int delayTime, List<Integer> channelIds);

	/**
	 * 获取当前在线玩家数量
	 * @return
	 */
	public int getOnlinePlayerNum();

	/**
	 * 获取历史最小在线
	 * @return
	 */
	public int getHistorMinPlayerNum();

	/**
	 * 获取历史最大在线
	 * @return
	 */
	public int getHistorMaxPlayerNum();
	
	/**
	 * 关闭服务器
	 * <pre>
	 * 1：改变服务器状态，不可登录
	 * 2：保存oss
	 * 3：关闭spring
	 * 4：保存db队列
	 * 5：退出虚拟机
	 * </pre>
	 * 5秒后将关闭虚拟机
	 * @return
	 */
	public Result shutdownServer();
	
	/**
	 * 踢出玩家
	 * @param actorId
	 * @return
	 */
	public Result kickPlayr(long actorId);
	
	/**
	 * 刷新配置文件
	 * @param fileName 文件名
	 * @param data 文件内容
	 * @return
	 */
	public Result flushDataConfig(String fileName, String data);
	
	
	TResult<String> entity2JSON(String tableName, long actorId);

	/**
	 * 增加uid
	 */
	public Result addUid(String uid);

	/**
	 * 清空uid
	 * @return
	 */
	public Result cleanUid();

	/**
	 * 清理运行时数据库缓存
	 * @return
	 */
	Result clearDBEntityCache(long actorId);
}
