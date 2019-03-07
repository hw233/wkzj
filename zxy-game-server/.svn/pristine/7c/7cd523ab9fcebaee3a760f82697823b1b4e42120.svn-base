package com.jtang.gameserver.dataconfig.model;


import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

/**
 * 仙人升级配置
 * @author 0x737263
 *
 */
@DataFile(fileName = "heroUpgradeConfig")
public class HeroUpgradeConfig implements ModelAdapter {

	/**
	 * 仙人星级
	 */
	private int star;
	/**
	 * 仙人等级
	 */
	private int level;
	/**
	 * 本级需要经验
	 */
	private int needExp;
	/**
	 * 升级时增加英雄攻击数值
	 */
	private int upgradeAttack;
	/**
	 * 升级时增加英雄防御数值
	 */
	private int upgradeDefense;
	/**
	 * 升级时增加英雄生命数值
	 */
	private int upgradeHp;
	
	/**
	 * 升级时增加的潜修次数
	 */
	private int upgradeDelve;

	@Override
	public void initialize() {	
	}

	public int getStar() {
		return star;
	}

	public int getLevel() {
		return level;
	}

	public int getNeedExp() {
		return needExp;
	}

	public int getUpgradeAttack(float heroUpgradeAtk) {
		return Math.round(heroUpgradeAtk * upgradeAttack);
	}

	public int getUpgradeDefense(float heroUpgradeDefense) {
		return Math.round(heroUpgradeDefense * upgradeDefense);
	}

	public int getUpgradeHp(float heroUpgradeHp) {
		return Math.round(heroUpgradeHp * upgradeHp);
	}

	public int getUpgradeDelve() {
		return upgradeDelve;
	}
}
