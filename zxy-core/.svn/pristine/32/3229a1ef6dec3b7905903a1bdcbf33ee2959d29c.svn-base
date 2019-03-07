package com.jtang.core.db;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.jdbc.core.RowMapper;


/**
 * 所有实体继承于此,实现已下方法
 * @author 0x737263
 *
 * @param <PK>pk类型
 */
public abstract class Entity<PK> implements RowMapper<Entity<PK>>, Serializable {
	private static final long serialVersionUID = -7812007366065808305L;

	/**
	 * 获取主键值
	 * @return
	 */
	public abstract PK getPkId();
	
	/**
	 * 设置主键值
	 * @param id
	 */
	public abstract void setPkId(PK pk);
	
	/**
	 * 空字符串常量
	 */
	protected static final String EMPTY_STRING = "";
	
	@Override
	public Entity<PK> mapRow(ResultSet rs, int rowNum) throws SQLException {
		Entity<PK> entity = readData(rs, rowNum);
		entity.hasReadEvent(); // 读取后执行该方法做一些初始化
		disposeBlob();
		return entity;
	}
	
	/**
	 * 获实体的字段内容
	 * @param containsPk
	 * @return
	 */
	public ArrayList<Object> getRowValue(boolean containsPk) {
		this.beforeWritingEvent();
		ArrayList<Object> list =  writeData(containsPk);
		list.addAll(list);
		disposeBlob();
		return list;
	}
	
	/**
	 * 从db读取每一行记录
	 * @param rs
	 * @param rowNum
	 * @return
	 */
	protected abstract Entity<PK> readData(ResultSet rs, int rowNum) throws SQLException;

	/**
	 * 数据读取后的事件处理(可以处理blob字符串转对象)
	 */
	protected abstract void hasReadEvent();
	
	/**
	 * 获取所有字段的值
	 * @return
	 */
	protected abstract ArrayList<Object> writeData(Boolean containsPK);
	
	/**
	 * 数据保存进db之前的事件(可以处理对象转blob字符串)
	 */
	protected abstract void beforeWritingEvent();
	
	/**
	 * 清理blob调用
	 */
	protected abstract void disposeBlob();
	
}
