package com.jtang.gameserver.module.snatch.helper;

import static com.jtang.gameserver.module.snatch.type.SnatchEnemyType.ACTOR;
import static com.jtang.gameserver.module.snatch.type.SnatchEnemyType.ROBOT;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.HeroVO;
import com.jiatang.common.model.LineupFightModel;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.dataconfig.model.SnatchAchieveConfig;
import com.jtang.gameserver.dataconfig.service.SnatchRobotService;
import com.jtang.gameserver.dataconfig.service.SnatchService;
import com.jtang.gameserver.module.lineup.helper.LineupHelper;
import com.jtang.gameserver.module.snatch.facade.SnatchRobotFacade;
import com.jtang.gameserver.module.snatch.model.SnatchEnemyVO;
import com.jtang.gameserver.module.snatch.type.SnatchEnemyType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;

/**
 * 抢夺帮助类
 * @author 0x737263
 *
 */
@Component
public class SnatchHelper {
	
	private static ObjectReference<SnatchHelper> REF = new ObjectReference<>();
	
	@Autowired
	ActorFacade actorFacade;
	@Autowired
	SnatchRobotFacade snatchRobotFacade;
	
	@PostConstruct
	protected void init() {
		REF.set(this);
	}
	
	private static SnatchHelper getInstance() {
		return REF.get();
	}
	
	/**
	 * 获 取目标角色的气势值
	 * @param targetActorId
	 * @param type
	 * @return
	 */
	public static int getTargetMorale(long targetActorId, SnatchEnemyType type) {
		if (type == SnatchEnemyType.ACTOR) {
			int targetMorale = getInstance().actorFacade.getActor(targetActorId).morale;
			return targetMorale;
		} else {
			SnatchEnemyVO s = getInstance().snatchRobotFacade.getSnatchEnemy(targetActorId);
			return SnatchRobotService.randomMorale(s.actorLevel);
		}
	}
	
	/**
	 * 获取阵型
	 * @param allyId
	 * @param actorId
	 * @param enemyType
	 */
	public static LineupFightModel getTargetLineUp(long actorId, SnatchEnemyType enemyType) {
		if (enemyType == ROBOT) {
			Map<Integer, HeroVO> heros = getInstance().snatchRobotFacade.getRobotLineup(actorId);
			LineupFightModel model = new LineupFightModel();
			model.setHeros(heros);
			return model;
		}

		return LineupHelper.getLineupFight(actorId);
	}
	
	/**
	 * 获取目标的等级
	 * @param actorId
	 * @param type
	 * @return
	 */
	public static int getTargetLevel(long actorId, SnatchEnemyType type) {
		if (type == ACTOR) {
			return getInstance().actorFacade.getActor(actorId).level;
		} else {
			return getInstance().snatchRobotFacade.getSnatchEnemy(actorId).actorLevel;
		}
	}
	
	/**
	 * 获取目标角色名
	 * @param actorId
	 * @return
	 */
	public static String getTargetActorName(long actorId) {
		String name = ActorHelper.getActorName(actorId);
		if (name.isEmpty() || name == "") {
			name = getInstance().snatchRobotFacade.getSnatchEnemy(actorId).actorName;
		}
		return name;
	}
	
	/**
	 * 获取初始化成就
	 */
	public static Map<Integer,Integer> getInitAchieve(){
		List<SnatchAchieveConfig> list = SnatchService.getFirstAchieve();
		Map<Integer,Integer> map = new HashMap<>();
		for(SnatchAchieveConfig config:list){
			map.put(config.achieveId, 0);
		}
		return map;
	}
	
}
