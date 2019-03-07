package com.jtang.gameserver.module.skill.type;

/**
 *
 *注意：每个人出手分两个环节:
 *  >普攻环节, 仙人有可能用技能打(即"普功时..."这种描述的技能)或平砍(即非技能类的攻击), 但只能两者选其一,优先是技能打,但是如果概率没命中或者找不到目标导致无法释放的话，就平砍
 *  >技能环节, 即处理那些"[普攻后...]"这些描述的技能.    
 *  
 * processType的可能取值如下:
 * 1.开战时(就是进入战场后到还没开始任意一个回合前的这段时间)
 * 2. 战斗结束时(就是回合结束后到奖励结算前的这段时间)
 * 3. 普攻环节开始时(那些普攻环节的技能要在这个时机被处理)
 * 4. 技能环节开始时(那些技能环节的技能都要在这个时机被处理)
 * 6.平砍后(如果需要配置一个只在普攻时才吸血的技能，则用这个)
 * 7.被平砍前 (如果需要配置一个被别人普攻时才闪避的技能，则用这个)
 * 8.被平砍后(如果需要配置一个被别人普攻时才反击的技能，则用这个)
 * 10.技能攻击后 (包括普攻环节和技能环节的技能攻击, 例子:吸血)
 * 11.被技能攻击前(包括普攻环节和技能环节的技能攻击, 例子:闪避)
 * 12.被技能攻击后 (包括普攻环节和技能环节的技能攻击, 例子:反击)
 * 14.攻击后 (包括平砍和技能攻击, 例子:吸血)
 * 15.被攻击前(包括平砍和技能攻击, 例子:闪避)
 * 16.被攻击后(包括平砍和技能攻击, 例子:反击)
 * 17.击杀敌人时
 * 18.死亡时
 * 19.回合开始时
 * 20.回合结束时
 * 21.普通攻击
 * @author vinceruan
 *
 */
public enum ProcessType {
	
	  BEGIN_FIGHT(1,"开战时"),
	  
	  END_FIGHT(2, "战斗结束时"),
	  
	  BEGIN_STAGE_1(3, "普攻环节开始时"),
	  
	  BEGIN_STAGE_2(4, "技能环节开始时"),
	  
//	  BEGIN_COMMON_ATK(5, "平砍前"),
	  
	  END_COMMON_ATK(6, "平砍后"),
	  
	  BEFORE_BE_COMM_ATKED(7, "被平砍前"),
	  
	  AFTER_BE_COMM_ATKED(8, "被平砍后"),
	  
//	  BEGIN_SKILL_ATK(9, "技能攻击前"),
	  
//	  END_SKILL_ATK(10, "技能攻击后"),
	  
	  BEFORE_BE_SKILL_ATKED(11, "被技能攻击前"),
	  
	  AFTER_BE_SKILL_ATKED(12, "被技能攻击后"),
	  
//	  BEGIN_ATK(13, "攻击前"),
	  
	  END_ATK(14, "攻击后"),
	  
	  BEFORE_BE_ATKED(15, "被攻击前"),
	  
	  AFTER_BE_ATKED(16, "被攻击后"),
	  
	  KILL_ENEMY(17, "击杀敌人时"),
	  
	  DIE(18, "死亡时"),
	  
	  START_ROUND(19, "回合开始时"),
	  
	  END_ROUND(20, "回合结束时"),
	  
	  COMMON_ATK(21, "普通攻击"),
	  
	  //特殊类型,用于增加普通攻击次数,每次普攻前触发
	  ADD_COMMON_ATK_TIMES(22, "增加普通攻击次数"),
	  
	  EVENT(23, "外部事件触发"),
	  
	  HERT(24, "受到伤害触发"),
	  /**
	   * 用于计算移动次数
	   */
	  BEFORE_MOVE(25, "移动前触发"),
	  /**
	   * 受到治疗触发技能
	   */
	  ADD_HP(26, "收到治疗时触发"), 
	  /**
	   * 最大回合时触发
	   */
	  MAX_ROUND(27, "最大回合结束时触发"), 
	  /**
	   * 倒地之后触发
	   */
	  AFTER_DEAD(28,"倒地之后触发");
	  
	  private int code;
	  private String desc;
	  
	  private ProcessType(int code, String desc) {
		  this.code = code;
		  this.desc = desc;
	  }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
