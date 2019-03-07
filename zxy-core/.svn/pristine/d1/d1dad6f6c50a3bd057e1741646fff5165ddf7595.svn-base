package com.jiatang.common.model;

/**
 * 装备类型
 * @author 0x737263
 *
 */
public enum EquipType {
	
	/**
	 * 1：武器 
	 */
	WEAPON(1),
	
	/**
	 * 2：防具
	 */
	ARMOR(2),

	/**
	 * 3饰品
	 */
	ORNAMENTS(3);

	private int id;

	private EquipType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public static EquipType getType(int id) {
		 for(EquipType type : EquipType.values()) {
			 if(type.getId() == id) {
				 return type;
			 }
		 }
		return null;
	}
	
}
