package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.rhino.FormulaHelper;

/**
 * 技能效果配置
 * <pre>
 *
 *  desc:			描述
 *  id:				效果id
 *  rate: 			效果起作用的概率,基数为1000, 如概率为80%，则填入800
 *  maxRelease: 	每场战斗最多可以成功释放该技能多少次,0表示无限制
 *  affectBoss : 	技能是否对boss有效(1表示有效，0表示无效)
 *  
 *  processType: 	什么时候处理效果:  
 *  						   	1.开战时 		2.选择攻击类型时(普通攻击还是技能攻击) 	3.普通攻击时触发 
 *  						   	4.攻击后触发  	5.被击  						6. 死亡 
 *  						   	7.双方回合结束  	8.战斗结束时 					9.击倒敌人时 
 *  						   	10.回合开始时	11.被攻击前触发
 *  
 *  targetType: 	作用目标分类:
 *  							1.自己				2.敌方目标，		3.施法者所在的整列敌人 
 *  							4.友方所有				5.敌方所有 			6.射程内所有敌人 
 *  							7.随机一个友方受伤仙人 		8.目标身后整排		9.所在整排友方仙人 
 *  							10.射程内所有友方 		11.目标整列敌人 		12.目标及身后一列敌人 
 *  							13.前后左右友方 			14.目标同排敌人		15. 随机射程范围一个友方及同排友方 
 *  							16.身后友方 			17.射程内随机3个敌人	18.敌方男性仙人 
 *  							19.目标身后的敌人(1个)
 *  
 *  parserId:	  	解释器的id.参考:InbattleParserKey.java
 *  effectExpr: 	表达式(可用的参数参考:InbattleParserKey.java)
 *  effectCycle: 	buffer的跳动时机: 1.立刻作用	2.每轮开始时 	3.每轮结束时    4.战斗结束时    5.死亡时跳动   6.击倒敌人时    7.射程不够时
 *  effectEndType: 	buffer的结束时机: 1.跳动一次      2.当前轮结束          4.下一轮结束    8.死亡时结束    16.战斗结束    32.施法者死亡时结束
 * </pre>
 * @author 0x737263
 *
 */
@DataFile(fileName = "inbattleEffectConfig")
public class InbattleEffectConfig implements ModelAdapter {
	
	/**
	 * 效果Id
	 */
	private int effectId;
		
	/**
	 * 技能触发机率
	 * <pre>
	 * 效果起作用的概率,基数为1000, 如概率为80%，则填入800
	 * </pre>
	 */
	private int rate=1000;
	
	/**
	 * 效果公式. 可用的参数参考:{@code InbattleParserKey}
	 */
	private String effectExpr;
	
	/**
	 * 技能效果目标
	 * <pre>
	 * 作用目标分类(1.自己，2.敌方目标，3.施法者所在的整列敌人, 4.友方所有，5.敌方所有 6.射程内所有敌人 7,随机一个友方受伤仙人 
	 * 				8,目标身后整排 9,所在整排友方仙人 10.射程内所有友方 11.目标整列敌人 12.目标及身后一列敌人 13.前后左右友方 14.目标同排敌人
	 *				15. 随机射程范围一个友方及同排友方 16.身后友方 17.射程内随机3个敌人 18.敌方男性仙人 19.目标身后的敌人(1个)
	 * </pre>
	 */
	private int targetType;
	/**
	 * 目标数量
	 */
	private int targetNum;
	
	/**
	 * 产生的buffer周期性跳动的时机
	 * <pre>
	 * buffer的跳动时机, 1.立刻作用，2.每轮开始时 3.每轮结束时 4.战斗结束时 5死亡时跳动 6.击倒敌人时 7.射程不够时
	 * </pre>
	 */
	private int effectCycle;
	
	/**
	 * buffer的失效时机
	 * <pre>
	 * buffer的结束时机: 1.跳动一次，2.当前轮结束，4.下一轮结束 8.死亡时结束，16.战斗结束 32.施法者死亡时结束
	 * </pre>
	 */
	private int effectEndType;
	
	/**
	 * 该效果对应的技能是在什么时候释放的.
	 * <pre>
	 *  processType: 	什么时候处理效果:  
	 *  				1.开战时 		2.选择攻击类型时(普通攻击还是技能攻击) 	3.普通攻击时触发 
	 *  				4.攻击后触发  	5.被击  						6. 死亡 
	 *  				7.双方回合结束  	8.战斗结束时 					9.击倒敌人时 
	 *  				10.回合开始时	11.被攻击前触发
	 * </pre>
	 */
	private int processType;
	
	/**
	 * 每场战斗最多可成功释放的次数
	 * <pre>
	 * 每场战斗最多可以成功释放该技能多少次
	 * </pre>
	 */
	private int maxRelease = -1;
	
	/**
	 * 是否对boss有效
	 * <pre>
	 *  技能是否对boss有效(1表示有效，0表示无效)
	 * </pre>
	 */
	private boolean affectBoss = true;
	
	/**
	 * 解析器id {@code InbattleParserKey}
	 */
	private int parserId;
	
	public int immuityEffectId;
	
	/**
	 * 是否有特效
	 * 0：无 1：有
	 */
	public int isEffect;
	
	/**
	 * 所属技能Id
	 */
	@FieldIgnore
	private int skillId;
	
	@FieldIgnore
	private String skillName;
	
	@Override
	public void initialize() {
		//预加载一次公式 
		Number[] args = new Number[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		FormulaHelper.execute(effectExpr, args);
	}

	public int getEffectId() {
		return effectId;
	}

	public int getSkillId() {
		return skillId;
	}

	public String getEffectExpr() {
		return effectExpr;
	}

	public int getEffectCycle() {
		return effectCycle;
	}

	public int getEffectEndType() {
		return effectEndType;
	}

	public int getTargetType() {
		return targetType;
	}

	public int getProcessType() {
		return processType;
	}

	public String getSkillName() {
		return skillName;
	}
	
	public int getMaxRelease() {
		return maxRelease;
	}

	public boolean isAffectBoss() {
		return affectBoss;
	}

	public int getRate() {
		return rate;
	}
	
	public int getTargetNum() {
		return targetNum;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}
	
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
	
	@Override
	public int hashCode() {
		return Integer.valueOf(this.getEffectId()).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof InbattleEffectConfig)) {
			return false;
		}
		return this.getEffectId() == ((InbattleEffectConfig)obj).getEffectId();
	}

	public int getParserId() {
		return parserId;
	}
}
