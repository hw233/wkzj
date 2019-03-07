package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
/**
 * 吸灵室使用物品配置
 * @author ludd
 *
 */
@DataFile(fileName = "heroUseGoodsConfig")
public class HeroUseGoodsConfig implements ModelAdapter {

	/**
	 * 物品id
	 */
	private int goodsId;
	/**
	 * 获得经验
	 */
	private int addExp;
	@Override
	public void initialize() {
		
	}
	
	public int getAddExp() {
		return addExp;
	}
	
	public int getGoodsId() {
		return goodsId;
	}
	
	
	

}
