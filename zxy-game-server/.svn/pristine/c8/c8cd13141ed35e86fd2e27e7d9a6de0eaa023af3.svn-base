package com.jtang.gameserver.module.extapp.plant.type;

public enum PlantType {
	/**
	 * 未种植
	 */
	NOT_PLANT(0),
	
	/**
	 * 成长中
	 */
	GROW(1),
	
	/**
	 * 可收获
	 */
	HARVECT(2);
	
	private final int type;
	private PlantType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public static PlantType getByType(int type) {
		for (PlantType plantType : values()) {
			if (type == plantType.getType()) {
				return plantType;
			}
		}
		return null;
	}
}
