package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName="boxEquipConfig")
public class BoxEquipConfig implements ModelAdapter {

	/**
	 * 宝箱id
	 */
	public int boxId;
	
	/**
	 * 类型
	 */
	public int type;
	
	/**
	 * 装备类型
	 */
	public int equipType;
	
	/**
	 * 星级
	 */
	public int star;
	
	/**
	 * 奖励id
	 */
	public int id;
	
	/**
	 * 奖励数量
	 * (装备碎片才会用到)
	 */
	public int num;
	
	/**
	 * 几率
	 */
	public int proportion;
	
	@Override
	public void initialize() {
		
	}

}
