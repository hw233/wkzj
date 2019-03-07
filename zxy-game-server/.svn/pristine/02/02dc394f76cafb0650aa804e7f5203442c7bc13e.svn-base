package com.jtang.gameserver.module.battle.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtang.gameserver.dataconfig.model.MapConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.battle.model.Tile;
import com.jtang.gameserver.module.battle.type.Camp;
import com.jtang.gameserver.module.buffer.helper.BufferHelper;
import com.jtang.gameserver.module.lineup.constant.LineupRule;

public class BattleHelper {
	private final static Logger LOGGER = LoggerFactory.getLogger(BattleHelper.class);
	static List<Fighter> EMPTY_LIST = Collections.unmodifiableList(new ArrayList<Fighter>());

	/**
	 * 根据仙人在阵型中的位置返回在战场中的坐标
	 * @param gridIndex
	 * @param camp
	 * @param map
	 * @return
	 */
	public static Tile getTileInMap(int gridIndex, Camp camp, MapConfig map) {
		if (map.getGridCol() == LineupRule.COL_COUNT) {
			//1vs1 的战斗地图
			return getTileInSmallMap(gridIndex, camp);
		} else if (map.getGridCol() == LineupRule.COL_COUNT * 2) {
			//2vs2的战斗地图
			return getTileInBigMap(gridIndex, camp);
		}
		LOGGER.error(String.format("地图与阵形不匹配, gridCol:[%s]", map.getGridCol()));
		return null;
	}
	
	public static Tile getTileInSmallMap(int gridIndex, Camp camp) {
		if (camp == Camp.ABOVE) {
			switch (gridIndex) {
			case 1:
				return new Tile(2,5);
			case 2:		
				return new Tile(1,5);
			case 3:
				return new Tile(0,5);				
			case 4:
				return new Tile(2,6);
			case 5:
				return new Tile(1,6);
			case 6:
				return new Tile(0,6);
			case 7:
				return new Tile(2,7);
			case 8:
				return new Tile(1,7);				
			case 9:
				return new Tile(0,7);				
			default:
				break;
			}
			
		} else if (camp == Camp.BELOW) {
			
			switch (gridIndex) {
			case 1:
				return new Tile(0,2);				
			case 2:		
				return new Tile(1,2);				
			case 3:
				return new Tile(2,2);				
			case 4:
				return new Tile(0,1);				
			case 5:
				return new Tile(1,1);				
			case 6:
				return new Tile(2,1);				
			case 7:
				return new Tile(0,0);				
			case 8:
				return new Tile(1,0);				
			case 9:
				return new Tile(2,0);				
			default:
				break;
			}
		}
		return null;
	}
	
	public static Tile getTileInBigMap(int gridIndex, Camp camp) {
		if (camp == Camp.ABOVE) {
			switch (gridIndex) {
			case 1:
				return new Tile(5,5);
			case 2:		
				return new Tile(4,5);
			case 3:
				return new Tile(3,5);
			case 4:
				return new Tile(5,6);
			case 5:
				return new Tile(4,6);
			case 6:
				return new Tile(3,6);
			case 7:
				return new Tile(5,7);
			case 8:
				return new Tile(4,7);
			case 9:
				return new Tile(3,7);
			case 10:
				return new Tile(2,5);
			case 11:
				return new Tile(1,5);
			case 12:
				return new Tile(0,5);
			case 13:
				return new Tile(2,6);
			case 14:
				return new Tile(1,6);
			case 15:
				return new Tile(0,6);
			case 16:
				return new Tile(2,7);
			case 17:
				return new Tile(1,7);
			case 18:
				return new Tile(0,7);
			default:
				break;
			}
			
		} else if (camp == Camp.BELOW) {
			if (gridIndex < 10) {
				return getTileInSmallMap(gridIndex, camp);
			}
			switch (gridIndex) {
			case 10:
				return new Tile(3,2);
			case 11:
				return new Tile(4,2);
			case 12:
				return new Tile(5,2);
			case 13:
				return new Tile(3,1);
			case 14:
				return new Tile(4,1);
			case 15:
				return new Tile(5,1);
			case 16:
				return new Tile(3,0);
			case 17:
				return new Tile(4,0);
			case 18:
				return new Tile(5,0);
			default:
				break;
			}
		}
		return null;
	}
	
	/**
	 * 获取敌人列表
	 * @param fighter
	 * @param context
	 * @return
	 */
	public static List<Fighter> getEnemies(Fighter fighter, Context context) {
		//如果处于混乱状态,则返回自己的队友(不包括自己),否则返回敌人列表
		if (BufferHelper.isInFighting(fighter.getBuffers()) ||BufferHelper.isInFightingHertChange(fighter.getBuffers())) {
			List<Fighter> list = new ArrayList<>();
			list.addAll(context.getTeamListByCamp(fighter.getCamp()));
			Iterator<Fighter> iter = list.iterator();
			while(iter.hasNext()) {
				if (iter.next().equals(fighter)) {
					iter.remove();
					break;
				}
			}
//			Collections.sort(list);
			return list;
		} else {
			List<Fighter> list = new ArrayList<>(context.getTeamListByCamp(fighter.getCamp().getEnermyCamp()));
//			Collections.sort(list);
			return list;
		}
	}
	
	/**
	 * 获取队友列表 
	 * @param fighter
	 * @param context
	 * @return
	 */
	public static List<Fighter> getFriends(Fighter fighter, Context context){
		//如果处于内讧状态,则返回空列表,否则正常返回队员列表.
		if (BufferHelper.isInFighting(fighter.getBuffers()) || BufferHelper.isInFightingHertChange(fighter.getBuffers())) {
			return EMPTY_LIST;
		} else {
			List<Fighter> list = new ArrayList<>(context.getTeamListByCamp(fighter.getCamp()));
			Collections.sort(list);
			return list;
		}
	}
	
	/**
	 * 找出两个点之间(不包括端点)的所有攻击者
	 * @param from
	 * @param to
	 * @param list
	 * @return
	 */
	public static List<Fighter> getFighterInLine(Tile from, Tile to, List<Fighter> list) {
		List<Fighter> resultList = new ArrayList<>();
		for (Fighter f : list) {
			if (f.getCamp().isBehind(from, f.getTile()) && f.getCamp().isBehind(f.getTile(), to) && f.getTile().getX() == from.getX()) {
				resultList.add(f);
			}
		}
		return resultList;
	}
	
	public static void main(String args[]) {
		System.out.println(Camp.ABOVE.toString());
//		System.out.println(getTileInBattlefield(1, Camp.BELOW));
		System.out.println(Integer.MAX_VALUE);
	}
}
