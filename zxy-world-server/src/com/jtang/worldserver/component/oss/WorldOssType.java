package com.jtang.worldserver.component.oss;

/**
 * 世界服的oss类型
 * @author 0x737263
 *
 */
public enum WorldOssType {

	/**
	 * 贡献点日志
	 */
	EXCHANGE_POINT("exchangePoint");
	
	
	/**
	 * 日志名称
	 */
	private String name = "";
	
	WorldOssType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public static String[] getEnumArray() {
		WorldOssType[] ossTypeArray = WorldOssType.values();
		String[] array = new String[ossTypeArray.length];
		for (int i = 0; i < ossTypeArray.length; i++) {
			array[i] = ossTypeArray[i].getName();
		}
		return array;
	}
}
