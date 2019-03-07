package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

/**
 * 装备、碎片提炼黑名单置表
 * @author hezh
 *
 */
@DataFile(fileName = "convertBlackConfig")
public class ConvertBlackConfig implements ModelAdapter{
	
	/** 类型	1-装备碎片；2-装备*/
	private int type;

	/** 装备、碎片对应的基础配置ID*/
	private int id;
	
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
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
}
