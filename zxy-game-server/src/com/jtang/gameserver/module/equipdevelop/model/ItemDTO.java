package com.jtang.gameserver.module.equipdevelop.model;

/**
 * 物品传输对象
 * @author hezh
 *
 */
public class ItemDTO {

	/** 类型 0-物品；1-装备；2-仙人魂魄*/
	private int type;
	
	/** ID*/
	private int id;
	
	/** 数量*/
	private int num;

	public ItemDTO(int type,int id,int num){
		this.type = type;
		this.id = id;
		this.num = num;
	}
	public ItemDTO(String type,String id,String num){
		this.type = Integer.parseInt(type);
		this.id = Integer.parseInt(id);
		this.num = Integer.parseInt(num);
	}
	
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the num
	 */
	public int getNum() {
		return num;
	}
	
}
