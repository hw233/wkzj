package com.jtang.gameserver.module.trialcave.handler;
/**
 * 试炼洞命令码
 * @author lig
 *
 */
public interface TrialCaveCmd {
	
	/**
	 * 获取试炼洞信息
	 * <pre>
	 * 请求:{@code Request}
	 * 响应:{@code TrialCaveInfoResponse}
	 * </pre> 
	 */
	byte TRIAL_CAVE_INFO = 1;
	
	/**
	 * 开始试炼战斗
	 * 返回成功时客户端需要将已试炼次数+1
	 * <pre>
	 * 	请求:{@code TrialBattleRequest}
	 *  响应:{@code TrialBattleResultResponse}
	 * </pre>
	 */
	byte TRIAL_BATTLE = 2;
	
	/**
	 * 重置试炼购买次数。
	 * 推送今天已经试炼次数(用于每天试炼次数重置时推送次数更新给在线用户)
	 * <pre>
	 * 推送:{@code Response}
	 * </pre>
	 */
	byte RESET_TRIALED_COUNT = 3;

	/**
	 * 请求重置试炼。
	 * 请求:{@code Request}
	 * 响应:{@code TrialCaveResetResponse}
	 * 推送:{@code TrialCaveResetResponse}
	 */
	byte TRIAL_CAVE_RESET = 4;

}
