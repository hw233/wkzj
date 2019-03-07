package com.jtang.core.result;

import java.util.List;

/**
 * 分页类型的结果集
 * 
 * @author 0x737263
 * 
 */
public class PageResult<T extends Object> {

	/**
	 * 状态码
	 */
	public int statusCode;

	/**
	 * 当前页索引
	 */
	public int pageIndex;

	/**
	 * 分页大小
	 */
	public int pageSize;

	/**
	 * 总记录数
	 */
	public int totalItem;

	/**
	 * 当前分页内容
	 */
	public List<T> item;

	/**
	 * 总页数
	 */
	public int pageCount;

	public static <T> PageResult<T> valueOf(int pageIndex, int pageSize, int totalItem,List<T> items) {
		PageResult<T> pageResult = new PageResult<T>();
		pageResult.item = items;
		pageResult.pageIndex = pageIndex;
		pageResult.pageSize = pageSize;
		pageResult.totalItem = totalItem;
		pageResult.pageCount = (totalItem + pageSize - 1) / pageSize;
		return pageResult;
	}
	
	public static <T> PageResult<T> valueOf(int pageIndex, int pageSize, int totalItem){
		return valueOf(pageIndex, pageSize, totalItem,null);
	}
	
	private PageResult() {
		
	}
}
