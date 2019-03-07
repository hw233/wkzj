package com.jtang.gameserver.module.adventures.favor.type;

public enum FavorParserType {
	/**
	 * 解析器1
	 */
	TYPE1(1),
	/**
	 * 解析器2
	 */
	TYPE2(2),
	/**
	 * 解析器3
	 */
	TYPE3(3);
	private final int type;
	private FavorParserType(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
	}
	
	public static FavorParserType getByType(int type){
		for (FavorParserType favorParserType : FavorParserType.values()) {
			if (favorParserType.getType() == type){
				return favorParserType;
			}
		}
		return null;
	}
	
}
