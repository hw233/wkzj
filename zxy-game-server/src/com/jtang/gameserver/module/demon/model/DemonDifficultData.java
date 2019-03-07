package com.jtang.gameserver.module.demon.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jtang.gameserver.component.model.MonsterVO;
import com.jtang.gameserver.module.demon.type.DemonCamp;

public class DemonDifficultData {
	/**
	 * key:位置
	 * value:boss 数据
	 */
	private Map<Integer, MonsterVO> monsterVO;
	
	/**
	 * 集众降魔数据
	 */
	private List<DemonModel> demons = new ArrayList<>();
	
	/**
	 * 难度
	 */
	private int difficult;
	
	/**
	 * 怪物气势
	 */
	private int monsterMorale;
	
	/**
	 * 角色消耗点券记录
	 * key:角色id
	 * value：累计消耗点券数
	 */
	private Map<Long, Integer> useTicketMap = new ConcurrentHashMap<>();
	
	/**
	 * 第一个有人的阵营
	 */
	private DemonCamp firstCamp = DemonCamp.BLUE_CAMP;

	public DemonDifficultData(int defficult, List<DemonModel> demons) {
		super();
		this.difficult = defficult;
		this.demons = demons;
	}
	
	public List<DemonModel> getDemons() {
		return demons;
	}
	
	public Map<Integer, MonsterVO> getMonsterVO() {
		return monsterVO;
	}
	
	public int getMonsterHurt() {
		int hurt = 0;
		for (MonsterVO monster : monsterVO.values()) {
			hurt += (monster.getMaxHp() - monster.getHp());
		}
		return hurt;
	}
	
	public void setMonsterVO(Map<Integer, MonsterVO> monsterVO) {
		this.monsterVO = monsterVO;
	}
	
	public Collection<MonsterVO> getBoss(){
		return this.monsterVO.values();
	}
	/**
	 * 获取阵营人数
	 * @param demonCamp
	 * @return
	 */
	public int getCampSize(int demonCamp) {
		int num = 0;
		for (DemonModel demonModel : demons) {
			if (demonCamp == demonModel.getCamp()) {
				num += 1;
			}
		}
		return num;
	}
	/**
	 * 是否第一个加入
	 * @return
	 */
	public boolean isFirstJoin() {
		if (this.getCampSize(DemonCamp.BLUE_CAMP.getCode()) == 0 && this.getCampSize(DemonCamp.RED_CAMP.getCode()) == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取红蓝两方人数差
	 * 
	 * @return 大于0表示传入阵营人数小于对方正营数值，小于0表示传入阵营人数大于对方正营数值
	 */
	public int getCampDiffrentSize(int camp) {
		int blueSize = getCampSize(DemonCamp.BLUE_CAMP.getCode());
		int redSize = getCampSize(DemonCamp.RED_CAMP.getCode());
		DemonCamp demonCamp = DemonCamp.getByCode(camp);
		if (demonCamp.equals(DemonCamp.BLUE_CAMP)) {
			return redSize - blueSize;
		} else if (demonCamp.equals(DemonCamp.RED_CAMP)) {
			return blueSize - redSize;
		} else {
			return 0;
		}
	}
	
	public int getDifficult() {
		return difficult;
	}
	
	public void setMonsterMorale(int monsterMorale) {
		this.monsterMorale = monsterMorale;
	}
	
	public int getMonsterMorale() {
		return monsterMorale;
	}
	
	/**
	 * 功勋排序（不包括为加入阵营角色）
	 * key：角色id
	 * value:名次
	 */
	public Map<Long, Integer> sortFeats() {
		List<DemonModel> joinList = getJoinList();
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		for (int i = 0; i < joinList.size(); i++) {
			int rank = i + 1;
			DemonModel d = joinList.get(i);
			long actorId = d.getActorId();
			map.put(actorId, rank);
		}
		return map;
	}
	
	/**
	 * 获取功勋排名角色（id）（已加入）
	 * @return
	 */
	public List<Long> getFeatsRankList() {
		List<DemonModel> joinList = getJoinList();
		List<Long> featsRankList = new ArrayList<>();
		for (DemonModel demonModel : joinList) {
			featsRankList.add(demonModel.getActorId());
		}
		return featsRankList;
	}
	
	/**
	 * 获取排序后的加入数据
	 * @return
	 */
	private List<DemonModel> getJoinList() {
		List<DemonModel> joinList = new ArrayList<>();
		for (DemonModel demon : demons) {
			if (demon.getCamp() != 0) {
				joinList.add(demon);
			}
		}
		Collections.sort(joinList);
		return joinList;
	}
	
	/**
	 * 获取阵营功勋值第一名
	 * @return
	 */
	public DemonModel getWinFristFeats() {
		List<DemonModel> win = winCamp();
		if (win.size() == 0) {
			return null;
		}
		Collections.sort(win);
		return win.get(0);
	}
	/**
	 *  检测怪物是否全部挂了
	 * @return
	 */
	public boolean monsterAllDead() {
		for (MonsterVO m : monsterVO.values()) {
			if (m.getHp() != 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 获胜阵营
	 * 如果功勋值相等并且不等于0，随机 一个正营列表
	 * 如果功勋值都等于0，返回空列表
	 * @return
	 */
	public List<DemonModel> winCamp() {
		List<DemonModel> blue = new ArrayList<>();
		List<DemonModel> red = new ArrayList<>();
		long blueFeats = 0;
		long redFeats = 0;
		for (DemonModel demonModel : demons) {
			if (demonModel.getCamp() == DemonCamp.RED_CAMP.getCode()) {
				red.add(demonModel);
				redFeats += demonModel.feats;
			} else if (demonModel.getCamp() == DemonCamp.BLUE_CAMP.getCode()) {
				blue.add(demonModel);
				blueFeats += demonModel.feats;
			}
		}
		
		if (blueFeats == 0L && redFeats == 0L) {
			return new ArrayList<>();
		}
		
		if (blueFeats > redFeats) {
			return blue;
		} else if (blueFeats == redFeats){
			if (firstCamp.equals(DemonCamp.BLUE_CAMP)) {
				return blue;
			} else {
				return red;
			}
//			return Math.random() > 0.5 ? blue : red;
		} else {
			return red;
		}
	}
	
	/**
	 * 记录点券使用
	 * @param actorId
	 * @param ticketsNum
	 */
	public void recordUseTicket(long actorId, int ticketsNum) {
		if (ticketsNum < 1) {
			return;
		}

		int value = 0;
		if (useTicketMap.containsKey(actorId)) {
			value = useTicketMap.get(actorId);
			value += ticketsNum;
		} else {
			value = ticketsNum;
		}
		useTicketMap.put(actorId, value);
	}

	/**
	 * 获取角色使用点券数
	 * @param actorId
	 * @return
	 */
	public int getUseTicketNum(long actorId) {
		if (useTicketMap.containsKey(actorId)) {
			return useTicketMap.get(actorId);
		}
		return 0;
	}
	
	public void setFirstCamp(DemonCamp firstCamp) {
		this.firstCamp = firstCamp;
	}
	
	public List<DemonModel> getDemonModels(int demonCamp) {
		List<DemonModel> list = new ArrayList<DemonModel>();
		for (DemonModel demonModel : this.demons) {
			if (demonModel.getCamp() == demonCamp) {
				list.add(demonModel);
			}
		}
		return list;
	}
	
}
