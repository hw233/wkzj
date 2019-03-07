package com.jtang.gameserver.module.story.helper;

import java.util.List;

import com.jtang.gameserver.dataconfig.model.BattleConfig;
import com.jtang.gameserver.dataconfig.service.StoryService;
import com.jtang.gameserver.module.battle.constant.WinLevel;
import com.jtang.gameserver.module.story.model.StoryVO;
import com.jtang.gameserver.module.story.type.Star;

public class StoryHelper {
	/**
	 * 根据战场的结果计算故事的星级
	 * @return
	 */
	public static byte computeStoryStar(StoryVO st) {
		int storyId = st.storyId;
		
		List<BattleConfig> mainBattleList = StoryService.getMainLineBattle(storyId);
//		int totalBattleNum = mainBattleList.size();
//		
//		//完胜的主线战场数
//		int fullVicBattleNum = 0;
		if(st.getBattleMap().size() >= mainBattleList.size()){
			byte maxStar = Star.THREE_STAR;
			for (BattleConfig bat : mainBattleList) {
				if (st.getBattleStar(bat.getBattleId()) < maxStar) {
					maxStar = st.getBattleStar(bat.getBattleId());
				}
			}
			return maxStar;
		}else{
			return 0;
		}
			
		
//		//没有完胜
//		if (fullVicBattleNum == 0) {
//			return Star.ZERO_STAR;
//		}
//				
//		//全部都是完胜
//		if (totalBattleNum == fullVicBattleNum) {
//			return Star.THREE_STAR;
//		}		
//		
//		int div = totalBattleNum/fullVicBattleNum;
//		int mod = totalBattleNum%fullVicBattleNum;
//		
//		//有一半以上是完胜
//		if (div == 1 || (div == 2 && mod == 0)) {
//			return Star.TWO_STAR;
//		}
//		
//		//有一个以上但未到一半完胜
//		return Star.ONE_STAR;
	}
	
	/**
	 * 根据胜利的等级计算战场的星级
	 * @param level
	 * @return
	 */
	public static byte computeBattleStar(WinLevel level) {
		switch (level) {
		case SMALL_WIN:
			return Star.ONE_STAR;
		case WIN:
			return Star.TWO_STAR;
		case BIG_WIN:
			return Star.THREE_STAR;
		default:
			return Star.ZERO_STAR;
		}
	}
	
	public static boolean isBattleWin(Byte battleStar) {
		if (battleStar == null) return false;
		return battleStar > Star.ZERO_STAR;
	}
}
