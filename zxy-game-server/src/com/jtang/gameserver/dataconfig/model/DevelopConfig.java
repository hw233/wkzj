package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

/**
 * 装备突破配置表
 * @author hezh
 *
 */
@DataFile(fileName = "developConfig")
public class DevelopConfig implements ModelAdapter{

	/** 1-武器；2-防具；3-饰品*/
	private int equipType;
	
	/** 星级（1~6）*/
	private int equipStar;
	
	/** 突破次数*/
	private int num;
	
	/** 对应装备属性加成*/
	private int addValue;
	
	/** 增加装备精炼次数*/
	private int addRefineNum;
	
	/** 消耗 物品ID_数量。多个用"|"间隔（没有不填）*/
	private String consume;
	
	@Override
	public void initialize() {
	}

	/**
	 * @return the equipType
	 */
	public int getEquipType() {
		return equipType;
	}

	/**
	 * @param equipType the equipType to set
	 */
	public void setEquipType(int equipType) {
		this.equipType = equipType;
	}

	/**
	 * @return the equipStar
	 */
	public int getEquipStar() {
		return equipStar;
	}

	/**
	 * @param equipStar the equipStar to set
	 */
	public void setEquipStar(int equipStar) {
		this.equipStar = equipStar;
	}

	/**
	 * @return the num
	 */
	public int getNum() {
		return num;
	}

	/**
	 * @param num the num to set
	 */
	public void setNum(int num) {
		this.num = num;
	}

	/**
	 * @return the addValue
	 */
	public int getAddValue() {
		return addValue;
	}

	/**
	 * @param addValue the addValue to set
	 */
	public void setAddValue(int addValue) {
		this.addValue = addValue;
	}

	/**
	 * @return the addRefineNum
	 */
	public int getAddRefineNum() {
		return addRefineNum;
	}

	/**
	 * @param addRefineNum the addRefineNum to set
	 */
	public void setAddRefineNum(int addRefineNum) {
		this.addRefineNum = addRefineNum;
	}

	/**
	 * @return the consume
	 */
	public String getConsume() {
		return consume;
	}

	/**
	 * @param consume the consume to set
	 */
	public void setConsume(String consume) {
		this.consume = consume;
	}

	
	
}