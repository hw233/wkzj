package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

/**
 * 物品配置
 * @author 0x737263
 *
 */
@DataFile(fileName = "goodsConfig")
public class GoodsConfig implements ModelAdapter {

	/**
	 * 物品配置id，唯一
	 */
	private int goodsId;
	
	/**
	 * 物品名称
	 */
	private String name;
	
	/**
	 * 星级(1-6)
	 */
	private int star;
	
	/**
	 * 物品分类
	 */
	private int goodsType;
	
	/**
	 * 物品子分类
	 */
	private int goodsSubType;
	
	/**
	 * 购买类型  1.金币  2.点券
	 */
	private int buyType;
	
	/**
	 * 购买价格
	 */
	private int buyPrice;
	
	/**
	 * 出售类型 1.金币 2.点券
	 */
	private int sellType;
	
	/**
	 * 出售价格
	 */
	private int sellPrice;
	
	/**
	 * 解析器id
	 */
	public int parserId;
	
	/**
	 * 解析表达式
	 */
	public String effectValue;
	
	/**
	 * 依赖物品id
	 */
	public String depends;
	
	/**
	 * 物品使用等级
	 */
	public int useLevel;
	
	@Override
	public void initialize() {
	}

	public int getGoodsId() {
		return goodsId;
	}

	public String getName() {
		return name;
	}

	public int getStar() {
		return star;
	}

	public int getGoodsType() {
		return goodsType;
	}
	
	public int getGoodsSubType() {
		return goodsSubType;
	}

	public int getBuyType() {
		return buyType;
	}

	public int getBuyPrice() {
		return buyPrice;
	}

	public int getSellType() {
		return sellType;
	}

	public int getSellPrice() {
		return sellPrice;
	}

	
}
