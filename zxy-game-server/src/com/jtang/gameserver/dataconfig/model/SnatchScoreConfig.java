package com.jtang.gameserver.dataconfig.model;
//package com.jtang.sm2.dataconfig.model;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.jtang.core.dataconfig.ModelAdapter;
//import com.jtang.core.dataconfig.annotation.DataFile;
//import com.jtang.core.dataconfig.annotation.FieldIgnore;
//import com.jtang.core.utility.RandomUtils;
//import com.jtang.core.utility.Splitable;
//import com.jtang.core.utility.StringUtils;
//import com.jtang.sm2.dataconfig.service.ActorService;
//import com.jtang.sm2.module.battle.constant.WinLevel;
//
///**
// * 抢夺积分配置
// * @author liujian
// *
// */
//@DataFile(name="snatchScoreConfig")
//public class SnatchScoreConfig implements ModelAdapter{
//
//	/**
//	 * 查询对手的掌教等级差：即不高于或者低于玩家掌教等级一定数值，例如填5，玩家掌教等级为10，则查询的敌人的掌教等级数值,5到15之间
//	 */
//	public int levelRange;
//	
//	/**
//	 * 排行榜总名次
//	 */
//	public int scoreTotalNumber;
//		
//	/**
//	 * 真实排名
//	 */
//	public int trueRank;
//	
//	/**
//	 * 排行榜显示个数，除开真实排名后，先填充玩家，不够填充机器人，积分按照积分差随机，格式：有关系玩家个数_大于排名总个数_小于排名总个数
//	 */
//	public String rankShowNumber;
//	
//	/**
//	 * 抢夺一次消耗的精力
//	 */
//	public int consumeEnergy;
//	
//	/**
//	 * 抢夺战斗的地图id,格式:地图id_地图id_地图id
//	 */
//	public String mapId;
//	
//	/**
//	 * 抢夺获得的积分比例,胜利时自己增加对方减少，失败时自己减少对方增加, 注意百分比都是填正数,
//	 * 格式:胜利类型_自己积分增加减少的百分比_对方增加减少的百分比|胜利类型_自己积分增加减少的百分比_对方增加减少的百分比
//	 */
//	public String scoreChangePercent;
//	
//	@FieldIgnore
//	Map<WinLevel, int[]> scoreChangePercentMap = new HashMap<WinLevel, int[]>();
//	
//	@FieldIgnore
//	private List<String> numberList = new ArrayList<>();
//	
//	@FieldIgnore
//	private List<String> mapIdList = new ArrayList<>();
//	   
//	@Override
//	public void initialize() {
//		numberList = StringUtils.delimiterString2List(rankShowNumber, Splitable.ATTRIBUTE_SPLIT);
//		mapIdList = StringUtils.delimiterString2List(mapId, Splitable.ATTRIBUTE_SPLIT);
//		List<String> list = StringUtils.delimiterString2List(scoreChangePercent, Splitable.ELEMENT_SPLIT);
//		for (String item : list) {
//			List<String> ll = StringUtils.delimiterString2List(item, Splitable.ATTRIBUTE_SPLIT);
//			WinLevel level = WinLevel.getByCode(Integer.valueOf(ll.get(0)));
//			int myChange = Integer.valueOf(ll.get(1));
//			int targetChange = Integer.valueOf(ll.get(2));
//			scoreChangePercentMap.put(level, new int[]{myChange, targetChange});
//		}
//	}
//	
//	/**
//	 * 获取地图
//	 * @return
//	 */
//	public int getMapId() {
//		int index = RandomUtils.nextIntIndex(mapIdList.size());
//		String mapId = mapIdList.get(index);
//		return Integer.valueOf(mapId);
//	}
//	
//	/**
//	 * 小于排名总个数
//	 * @return
//	 */
//	public int getLowRank(){
//		return Integer.valueOf(numberList.get(2));
//	}
//	
//	/**
//	 * 大于排名总个数
//	 * @return
//	 */
//	public int getHighRank(){
//		return Integer.valueOf(numberList.get(1));
//	}
//	
//	public int getAtkerScoreChange(int score, WinLevel level) {
//		int percent[] = scoreChangePercentMap.get(level);
//		if (percent == null) {
//			return 0;
//		}
//		return percent[0] * score / 100;
//	}
//	
//	public int getDefensorScoreChange(int score, WinLevel level) {
//		int percent[] = scoreChangePercentMap.get(level);
//		if (percent == null) {
//			return 0;
//		}
//
//		return percent[1] * score / 100;
//	}
//	
//	
//	/**
//	 * 根据当前等级获取最高等级范围
//	 * @param level
//	 * @return
//	 */
//	public int getHightLevel(int level) {
//		int result = level + this.levelRange;
//		int maxLevel = ActorService.maxLevel();
//		return result > maxLevel ? maxLevel : result;
//	}
//	
//	/**
//	 * 根据当前等级获取最低等级范围
//	 * @param level
//	 * @return
//	 */
//	public int getLowLevel(int level) {
//		int result = level - this.levelRange;
//		return result <= 0 ? 1 : result;
//	}
//	
//	/**
//	 * 根据分数估算排名
//	 * @param lastTrueSocre		排行榜最后一名真实积分
//	 * @param lastTrueRanking	排行榜最后一名真实排名
//	 * @param actorScore		当前角色的积分
//	 * @return
//	 */
//	public int estimateRank(int lastTrueSocre, int lastTrueRanking, int actorScore) {
//		if (lastTrueSocre == 0 || actorScore == 0) {
//			return this.scoreTotalNumber;
//		}
//		int rank = this.scoreTotalNumber - (actorScore * (this.scoreTotalNumber - lastTrueRanking)) / lastTrueSocre;
//		if (rank > this.scoreTotalNumber) {
//			rank = this.scoreTotalNumber;
//		}
//		return rank;
//	}
//
//}
