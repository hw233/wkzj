//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.OutputStreamWriter;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;
//
//import com.jtang.core.context.SpringContext;
//import com.jtang.gameserver.dataconfig.model.BattleConfig;
//import com.jtang.gameserver.dataconfig.model.BattleConfig1;
//import com.jtang.gameserver.dataconfig.model.MonsterConfig;
//import com.jtang.gameserver.dataconfig.service.MonsterService;
//import com.jtang.gameserver.dataconfig.service.StoryService;
//import com.jtang.gameserver.dataconfig.service.StoryService1;
//import com.jtang.gameserver.dbproxy.IdTableJdbc;
//
//
//public class MonsterModifyUtil {
//
//	public static void main(String[] args) throws Exception {
//		IdTableJdbc jdbc = (IdTableJdbc) SpringContext.getBean(IdTableJdbc.class);
//		Map<Integer, BattleConfig1> map = StoryService1.getBATTLE_CONFIG_MAP();
//		FileOutputStream fos = new FileOutputStream(new File("d:/temp.xml"));
//		Map<Integer, Double> map1 = new HashMap<Integer, Double>();
//		Collection<MonsterConfig> all = MonsterService.all();
//		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
//		System.out.println(map.size());
//		for (BattleConfig1 cfg : map.values()) {
//			BattleConfig cfg1 = StoryService.get(cfg.battleId);
//			Map<Integer, Integer> monster = cfg1.getMonsterList();
//			for (Integer heroId : monster.values()) {
//				if (map1.containsKey(heroId)) {
//					continue;
//				}
//				map1.put(heroId, Double.valueOf(cfg.name));
//			}
//			
//		}
//		System.out.println(map1.size());
//		bw.write("<root>\n");
//		for (MonsterConfig monsterCfg : all) {
//			if (map1.containsKey(monsterCfg.getMonsterId())) {
//				Double atk = Math.ceil(monsterCfg.getAttack() * map1.get(monsterCfg.getMonsterId()));
//				Double hp = Math.ceil(monsterCfg.getHp() * map1.get(monsterCfg.getMonsterId()));
//				Double def = Math.ceil(monsterCfg.getDefense() * map1.get(monsterCfg.getMonsterId()));
//				StringBuffer sb = new StringBuffer("<item ");
//				sb.append("monsterId=\"").append(monsterCfg.getMonsterId()).append("\" ");
//				sb.append("attack=\"").append(atk.intValue()).append("\" ");
//				sb.append("hp=\"").append(hp.intValue()).append("\" ");
//				sb.append("defense=\"").append(def.intValue()).append("\" ");
//				sb.append(" /> \n");
//				bw.write(sb.toString());
//			} else {
//				StringBuffer sb = new StringBuffer("<item ");
//				sb.append("monsterId=\"").append(monsterCfg.getMonsterId()).append("\" ");
//				sb.append("attack=\"").append(monsterCfg.getAttack()).append("\" ");
//				sb.append("hp=\"").append(monsterCfg.getHp()).append("\" ");
//				sb.append("defense=\"").append(monsterCfg.getDefense()).append("\" ");
//				sb.append(" /> \n");
//				bw.write(sb.toString());
//			}
//		}
//		bw.write("</root>");
//		bw.flush();
//		bw.close();
//		
//	}
//
//}
