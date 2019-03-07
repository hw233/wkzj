package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
/**
 * 强化室配置
 * @author pengzy
 *
 */
@DataFile(fileName = "enhancedConfig")
public class EnhancedConfig implements ModelAdapter {

	/**
	 * 强化室等级
	 */
	private int level;
	/**
	 * 该强化室等级要求盟友的等级总和
	 */
	private int allyLevel;
	/**
	 * 升级需要消耗的点券
	 */
	private int upgradeTicket;
	
	/**
	 * 装备强化时,给于的属性增加百分比
	 */
	private int addValue;
	
	/**
	 * 使装备连升数级的概率
	 */
	private int probability;
	/**
	 * 装备连升的等级数
	 */
	private int upLevel;
	
	@Override
	public void initialize() {
	}
	
	public int getLevel() {
		return level;
	}
	public int getAllyLevel() {
		return allyLevel;
	}
	public int getUpgradeTicket() {
		return upgradeTicket;
	}
	public float getAddValue() {
		return addValue / 100.0f;
	}
	public int getProbability() {
		return probability;
	}
	public int getUpLevel() {
		return upLevel;
	}
	

}
