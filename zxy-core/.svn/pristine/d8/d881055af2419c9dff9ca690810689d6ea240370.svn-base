package com.jtang.core.utility;

/**
 * 各种分隔符
 * @author 0x737263
 *
 */
public abstract interface Splitable {
	
	/**
	 * 分隔符说明
	 * 第一层:  |				1001|1002
	 * 第二层:  _				1001_1002|1003_1004
	 * 第三层:  ,				1001_1002_1003,1001_1002_1003|1001_1002_1003,1001_1002_1003
	 * 第四层:  :				1001_1002_k1:v1,k2,v2|1001_1002_k1:v1,k2,v2
	 * 
	 */

	/**
	 * ":"
	 */
	public static final String DELIMITER_ARGS = ":";
	
	/**
	 * ","
	 */
	public static final String BETWEEN_ITEMS = ",";
	
	/**
	 * "\\|"
	 */
	public static final String ELEMENT_SPLIT = "\\|";
	
	/**
	 * "|" 调用String.split时不能用ELEMENT_DELIMITER, 用ELEMENT_SPLIT即可
	 */
	public static final String ELEMENT_DELIMITER = "|";
	
	/**
	 * "_"
	 */
	public static final String ATTRIBUTE_SPLIT = "_";
	
	/**
	 * "["
	 */
	public static final String LEFT_PARENTH_SPLIT = "[";
	
	/**
	 * "]"
	 */
	public static final String RIGHT_PARENTH_SPLIT = "]";
	
	/**
	 * "_["
	 */
	public static final String LEFT_ELEMENT_SPLIT = "_[";
	
	/**
	 * "#"
	 */
	public static final String ATTRIBUTE_SPLITE_1 = "#";
}