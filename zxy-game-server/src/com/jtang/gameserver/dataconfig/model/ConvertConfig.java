package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

/**
 * 装备、碎片提炼配置表
 * @author hezh
 *
 */
@DataFile(fileName = "convertConfig")
public class ConvertConfig implements ModelAdapter{
	
	/** 类型	1-装备碎片；2-装备*/
	private int type;

	/** 星级（品质）*/
	private int star;
	
	/** 分解获得物品 类型（0-物品；1-装备；2-仙人魂魄）_ID_数量。多个用"|"间隔*/
	private String rewardGoods;

	/** 消耗 物品ID_数量。多个用"|"间隔（没有不填）*/
	private String consume;
	
	@Override
	public void initialize() {
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	
	/**
	 * @return the star
	 */
	public int getStar() {
		return star;
	}

	/**
	 * @param star the star to set
	 */
	public void setStar(int star) {
		this.star = star;
	}

	/**
	 * @return the rewardGoods
	 */
	public String getRewardGoods() {
		return rewardGoods;
	}

	/**
	 * @param rewardGoods the rewardGoods to set
	 */
	public void setRewardGoods(String rewardGoods) {
		this.rewardGoods = rewardGoods;
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
