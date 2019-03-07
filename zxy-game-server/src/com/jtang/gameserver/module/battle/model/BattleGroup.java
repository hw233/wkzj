package com.jtang.gameserver.module.battle.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.gameserver.module.battle.constant.WinLevel;
import com.jtang.gameserver.module.battle.type.Camp;

/**
 * 战斗组 
 * 用于管理精灵列表或战斗的类
 * 
 * @author 0x737263
 * 
 */
public class BattleGroup {

	/**
	 * 战斗成员列表 
	 * <pre>
	 * key:出手顺序索引
	 * value:精灵对象
	 * <pre>
	 */
	private Map<Integer, Fighter> spriteList = new HashMap<Integer, Fighter>();

	/**
	 * 当前攻击者索引id
	 */
	private Integer nowAtkId = -1;
	
	/**
	 * 战斗组所属的阵营
	 */
	private Camp camp;
	
	/***
	 * 战斗结果类型
	 */
	private WinLevel winLevel;
	
	/**
	 * 气势值
	 */
	private int morale;
	
	/**
	 * Constructor.
	 * @param list
	 * @param atkNum 攻击次数.
	 */
	public BattleGroup(int atkNum,List<Fighter> list, Camp camp, int morale) {
		this.camp = camp;
		for(Fighter s : list) {
			spriteList.put(spriteList.size(), s);
		}
		
		//补充空余的精灵.为了方便跳过出手.
		if(list.size() < atkNum) {
			for(int i=list.size();i < atkNum;i++) {
				spriteList.put(i,null);
			}
		}
		
		this.morale = morale;
	}

	/**
	 * 获取可战斗的成员列表
	 * @return
	 */
	public List<Fighter> getList() {
		List<Fighter> list = new ArrayList<Fighter>();
		
		for(Fighter s : spriteList.values()) {
			if(s != null && s.isDead() == false) {
				list.add(s);
			}
		}
		return list;
	}
	
	/**
	 * 获取当前队列亡成员列表
	 * @return
	 */
	public List<Fighter> getDeadList() {
		List<Fighter> list = new ArrayList<Fighter>();
		
		for(Fighter s : spriteList.values()) {
			if(s != null && s.isDead()) {
				list.add(s);
			}
		}
		return list;
	}

	/**
	 * 跳到下一个可以出手攻击的成员
	 * @return
	 */
	public Fighter readyAtker() {
		if (nowAtkId == spriteList.size() - 1) {
			nowAtkId = 0;
		} else {
			nowAtkId++;
		}
		return spriteList.get(nowAtkId);
	}
	/**
	 * 是玩家还是怪
	 * @return true玩家
	 */
	public boolean isActor() {
		for(Fighter s : spriteList.values()) {
			if(s != null && s.actorId > 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否一轮结束
	 * @return
	 */
	public boolean isRoundFinish() {
		return nowAtkId == spriteList.size()-1;
	}
	
	/**
	 * 是否一轮开始
	 * @return
	 */
	public boolean isRoundStart() {
		return nowAtkId == 0;
	}

	/**
	 * 获取 气势(团队战斗力总和)
	 * @return
	 */
	public Integer getMorale() {
		return this.morale;
	}

	/**
	 * 是否战斗失败
	 * @return
	 */
	public Boolean isLose() {
		for (Fighter s : spriteList.values()) {
			if (s != null && s.isDead() == false && s.getCamp() == camp) {
				return false;
			}
		}
		return true;
	}
	
	public int getAtkNum() {
		return this.spriteList.size();
	}

	public Camp getCamp() {
		return camp;
	}

	public void setCamp(Camp camp) {
		this.camp = camp;
	}

	public WinLevel getWinLevel() {
		return winLevel;
	}

	public void setWinLevel(WinLevel winLevel) {
		this.winLevel = winLevel;
	}
}
