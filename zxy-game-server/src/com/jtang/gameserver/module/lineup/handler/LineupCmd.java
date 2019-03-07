package com.jtang.gameserver.module.lineup.handler;


/**
 * 阵型模块命令
 * @author vinceruan
 *
 */
public interface LineupCmd {
	/**
	 * 获取阵型信息
	 * <pre>
	 * 请求: null
	 * 响应:{@code LineupInfoResponse}
	 * </pre>
	 */
	byte LINEUP_INFO = 1;
	
	/**
	 * 指派仙人上阵
	 * <pre>
	 * 请求：{@code AssignHeroRequest}
	 * 响应:{@code Response}
	 * 同步接口:
	 * {@code UpdateHeroResponse}
	 * </pre>
	 */
	byte ASSIGN_HERO = 2;
	
	/**
	 * 将仙人脱离阵型
	 * <pre>
	 * 请求:{@code UnAssignHeroRequest}
	 * 响应:{@code Response}
	 * 同步接口:
	 * {@code UpdateHeroResponse}
	 * </pre>
	 */
	byte UNASSIGN_HERO = 3;
	
	/**
	 * 装备放入到阵型的凹凿
	 * <pre>
	 * 请求:{@code AssignEquipRequest}
	 * 响应:{@code Response}
	 * 同步接口:
	 * {@code UpdateHeroResponse}
	 * </pre>
	 */
	byte ASSIGN_EQUIP = 4;
	
	/**
	 * 将装备从阵型中拿下
	 * <pre>
	 * 请求:{@code UnAssignEquipRequest}
	 * 响应:{@code Response}
	 * 同步接口:
	 * {@code UpdateHeroResponse}
	 * </pre>
	 */
	byte UNASSIGN_EQUIP = 5;
	
	/**
	 * 调整阵型:将仙人从一个格子移到另外一个格子
	 * <pre>
	 * 请求:{@code ChangeHeroGridRequest}
	 * 响应:{@code Response}
	 * 同步接口
	 * {@code UpdateHeroResponse}
	 * </pre>
	 */
	byte CHANGE_HERO_GRID = 6;
	
	/**
	 * 调整阵型:将2个仙人的位置对调
	 * <pre>
	 * 请求:{@code ExchangeHeroGridRequest}
	 * 响应:{@code Response}
	 * 同步接口:
	 * {@code UpdateHeroResponse}
	 * </pre>
	 */
	byte EXCHANGE_HERO_GRID = 7;
	
	/**
	 * 解锁阵型
	 * <pre>
	 * 请求:{@code Request}
	 * 响应:{@code Response}
	 * 推送:见LineupCmd.PUSH_LINEUP_UNLOCK
	 * </pre>
	 */
	byte UNLOCK_LINEUP = 8;
	
	/**
	 * 推送格子解锁信息
	 * <pre>
	 * 请求：null
	 * 响应:{@code PushLineupUnlockResponse}
	 * </pre>
	 */
	byte PUSH_LINEUP_UNLOCK = 9;
	
	/**
	 * 请求查看阵形
	 * 请求:{@code ViewLineupRequest}
	 * 响应:{@code ViewLineupResponse}
	 */
	byte VIEW_LINEUP = 10;
	
	/**
	 * 请求更换阵型
	 * 请求:{@code ChangeLinequpRequest}
	 * 响应:{@code ViewLineupResponse}
	 */
	byte CHANGE_LINEUP = 11;
}
