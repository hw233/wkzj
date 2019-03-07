package com.jtang.gameserver.module.battle.facade;


import com.jtang.core.event.Receiver;
import com.jtang.core.result.Result;
import com.jtang.gameserver.module.battle.model.AttackMonsterRequest;
import com.jtang.gameserver.module.battle.model.AttackPlayerRequest;
import com.jtang.gameserver.module.battle.type.BattleType;

/**
 * 战斗接口 用于管理服务端所有玩家的战斗.
 * 
 * <pre>
 *  调用步骤:
 *  1. 调用register()方法注册特定战斗事件的结果回调
 *  2. 通过submitAtk***Request方法提交战斗请求
 *  
 *  注意事项:
 *  1. submitAtk***Request为异步方法(即该方法会立即返回,战斗请求会在新线程被处理).
 *  2. 战斗结果也是在新线程中被回调(你需要处理多线程并发修改共享数据的一致性问题)
 * <pre>
 * @author 0x737263
 * 
 */
public interface BattleFacade {
	
	/**
	 * 提交攻击怪物事件
	 * @param event
	 * @return 返回提交是否成功, 战斗结果另通过事件广播, 请通过register(...)方法注册广播事件的接收
	 */
	Result submitAtkMonsterRequest(AttackMonsterRequest event, BattleCallBack battleCallBack);
	
	/**
	 * 提交攻击玩家事件
	 * @param event
	 * @return 返回提交是否成功, 战斗结果另通过事件广播, 请通过register(...)方法注册广播事件的接收
	 */
	Result submitAtkPlayerRequest(AttackPlayerRequest event, BattleCallBack battleCallBack);
	
	/**
	 * 注册战斗结果回调, 注意:战斗结果是在新线程中回调.
	 * @param eventKey {@code EventKey}
	 * @param receiver
	 */
	void register(String eventKey, Receiver receiver);
	
	/**
	 * 获取战斗胜利次数
	 * @param actorId
	 * @param battleType
	 * @return
	 */
	int getBatteWinNum(long actorId, BattleType battleType);
	
	/**
	 * 获取战斗失败次数
	 * @param actorId
	 * @param battleType
	 * @return
	 */
	int getBatteFailNum(long actorId, BattleType battleType);
	
	/**
	 * 获取总战斗次数(失败和成功)
	 * @param actorId
	 * @param battleType
	 * @return
	 */
	int getBatteTotalNum(long actorId, BattleType battleType);
	
}