package com.jtang.worldserver.module.crossbattle.model;

import com.jiatang.common.crossbattle.model.ActorCrossData;

/**
 * 跨服数据包装
 * @author ludd
 *
 */
public class CrossModel {
//	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	/**
	 * 服务器id
	 */
	private int serverId;
	/**
	 * 角色跨服数据
	 */
	private ActorCrossData actorCrossVO;
	
	/**
	 * 对方服务器id
	 */
	private int otherServerId;


	public CrossModel(int serverId, ActorCrossData actorCrossVO,
			int otherServerId) {
		super();
		this.serverId = serverId;
		this.actorCrossVO = actorCrossVO;
		this.otherServerId = otherServerId;
	}

	public int getServerId() {
		return serverId;
	}

	public ActorCrossData getActorCrossData() {
		return actorCrossVO;
	}

//	public boolean revive() {
//		CrossBattleConfig cfg = CrossBattleService.get();
//		if (actorCrossVO.getDeadTime() > 0 && cfg.getReliveTime() <= TimeUtils.getNow() - actorCrossVO.getDeadTime()) {
//			actorCrossVO.revive();
//			if (LOGGER.isDebugEnabled()) {
//				LOGGER.debug(String.format("actorId:[%s] 复活, 当前hp:[%s]", actorCrossVO.actorId, actorCrossVO.hp));
//			}
//			return true;
//		}
//		return false;
//	}
	
	public int getOtherServerId() {
		return otherServerId;
	}
	
}
