package com.jtang.core.db;

import com.jtang.core.db.statement.DeleteStatement;
import com.jtang.core.db.statement.InsertUpdateStatement;
import com.jtang.core.db.statement.ReplaceIntoStatement;
import com.jtang.core.db.statement.SelectStatement;


/**
 * 实体信息类,用于记录反射后的一些常用信息
 * @author 0x737263
 *
 */
public class EntityInfo {

	/**
	 * 类名
	 */
	public String className;
	
	/**
	 * 表名
	 */
	public String tableName;
	
	/**
	 * 表类型
	 */
	public DBQueueType tableType;
	
	/**
	 * 主键名
	 */
	public String pkName;
	
	/**
	 * 字段名
	 */
	public String[] columnName;
	
	private DeleteStatement deleteStatement = new DeleteStatement();
	private SelectStatement selectStatement = new SelectStatement();
	@SuppressWarnings("unused")
	private ReplaceIntoStatement replaceIntoStatement = new ReplaceIntoStatement();
	private InsertUpdateStatement insertUpdateStatement = new InsertUpdateStatement();
	
	/**
	 * 实体实例对象(反射时的)
	 */
	public Entity<?> entity;
	
	/**
	 * 无条件查询
	 * @return
	 */
	public String getSelectSql() {
		return selectStatement.toSqlString(pkName, tableName, columnName);
	}
	
	/**
	 * 条件查询
	 * @param condition 条件
	 * @return
	 */
	public String getSelectSql(String... condition) {
		return selectStatement.toSqlString(pkName, tableName, columnName, condition);
	}
	
	/**
	 * 条件查询目标字段值
	 * @param columName 目标字段
	 * @param condition 条件字段
	 * @return
	 */
	public String getSelectSql(String columName, String[] condition) {
		return selectStatement.toSqlString(pkName, tableName, columnName, columName, condition);
	}

	public String getSelectSql(int limitBegin, int limitEnd, String... condition) {
		return selectStatement.toSqlString(pkName, tableName, columnName, limitBegin, limitEnd, condition);
	}

	public String getSelectSql(String targetColum, int limitBegin, int limitEnd, String[] condition) {
		return selectStatement.toSqlString(pkName, tableName, columnName, targetColum, limitBegin, limitEnd, condition);
	}
	
	/**
	 * 条件更新
	 * @param condition 条件字段
	 * @return
	 */
	public String getReplaceIntoSql() {
		return insertUpdateStatement.toSqlString(pkName, tableName, columnName);
//		return replaceIntoStatement.toSqlString(pkName, tableName, columnName);
	}
	
	
//	/**
//	 * 条件更新目标字段
//	 * @param target 目标字段
//	 * @param condition 条件字段
//	 * @return
//	 */
//	public String getUpdateSql(String[] target, String... condition) {
//		return updateStatement.toSqlString(pkName, tableName, columnName, target, condition);
//	}
	
	/**
	 * 条件删除
	 * @param condition 条件字段
	 * @return
	 */
	public String getDeleteSql(String... condition) {
		return deleteStatement.toSqlString(pkName, tableName, condition);
	}

//	public String getInsertSql() {
//		return replaceIntoStatement.toSqlString(pkName, tableName, columnName);
//	}
	
}
