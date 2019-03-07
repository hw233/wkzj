//
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.stereotype.Component;
//
//import com.jtang.core.dataconfig.ServiceAdapter;
//
//@Component
//public class StoryService1 extends ServiceAdapter {
//	
//	/**
//	 * 战场配置,格式是Map<BatttleId, BattleConfig>
//	 */
//	static Map<Integer, BattleConfig1> BATTLE_CONFIG_MAP = new HashMap<Integer, BattleConfig1>();
//	
//	
//	@Override
//	public void clear() {
//		BATTLE_CONFIG_MAP.clear();
//	}
//	
//	@Override
//	public void initialize() {
//		List<BattleConfig1> battleList = dataConfig.listAll(this, BattleConfig1.class);
//		
//		//将所有的战场配置缓存
//		for (BattleConfig1 conf : battleList) {
//			BATTLE_CONFIG_MAP.put(conf.battleId, conf);
//		}
//		
//		
//	}
//	
//	/**
//	 * 根据战场id获取战场配置
//	 * @param battleId
//	 * @return
//	 */
//	public static BattleConfig1 get(int battleId) {
//		return BATTLE_CONFIG_MAP.get(battleId);
//	}
//	
//	public static Map<Integer, BattleConfig1> getBATTLE_CONFIG_MAP() {
//		return BATTLE_CONFIG_MAP;
//	}
//	
//	
//}
