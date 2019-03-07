package com.jtang.core.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

import com.mysql.jdbc.Statement;

/**
 * jdbc实现类，通过jdbc.xml注入
 * @author 0x737263
 *
 */
public class BaseJdbcTemplate extends JdbcTemplate {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	@Autowired
	@Qualifier("jdbc.key")
	protected String jdbcKey;

	@Autowired
	@Qualifier("jdbc.url")
	protected String url;
	
	@Autowired
	@Qualifier("jdbc.entity_scan_package")
	protected String packageScan;

	@PostConstruct
	public void init() {
		setJdbcParams();
		LOGGER.info(String.format("DataBase url:[%s]", url));
		EntityListener.entityScan(packageScan);
		checkTable();
	}
	
	private void checkTable() {
		Collection<EntityInfo> entityInfos = EntityListener.getEntityInfos().values();
		try {
			DatabaseMetaData databaseMetaData = this.getDataSource().getConnection().getMetaData();
			for (EntityInfo entityInfo : entityInfos) {
				String tableName = entityInfo.tableName;
				ResultSet rs = databaseMetaData.getTables(null, null, tableName, new String[]{"TABLE"});
				if (rs.next() == false) {
					throw new RuntimeException(tableName + "表不存在.");
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 *  设置jdbc参数
	 */
	private void setJdbcParams(){
		String[] result = null;
		// String u = null;
		// if (ciphertext) {
		// } else {
		// result = new String(Base64.decode(jdbcKey)).split("#");
		// u = new String(Base64.decode(url));
		// }

		result = jdbcKey.split("#");

		BasicDataSource ds = (BasicDataSource) getDataSource();
		ds.setUsername(result[0]);
		ds.setPassword(result[1]);
		ds.setUrl(url);
	}
	
	/**
	 * 更新
	 * @param entity
	 * @return
	 */
	public <T extends Entity<?>> int update(T entity) {
		EntityInfo info = EntityListener.getEntityInfo(entity.getClass());
		String sql = info.getReplaceIntoSql();
		List<Object> values = entity.getRowValue(true);
//		values.add(entity.getPkId());
		return super.update(sql, values.toArray());
	}

	/**
	 * 更新批量
	 * @param entity
	 * @return 返回数据库每条语句影响行数
	 */
	public <T extends Entity<?>> int[] update(Collection<T> entitys) {
		// 分組
		Map<String, List<T>> map = groupEntity(entitys);
		
		List<Integer> resultArr = new ArrayList<>();//结果
		for (String str : map.keySet()) {
			//获取同组的sql
			T param = map.get(str).get(0);
			EntityInfo info = EntityListener.getEntityInfo(param.getClass());
			String updateSql = info.getReplaceIntoSql();

			ArrayList<Object[]> arr = new ArrayList<>();
			for (T object : map.get(str)) {
				ArrayList<Object> values = object.getRowValue(true);
//				values.add(object.getPkId());
				Object[] oneObjectValue = values.toArray();
				arr.add(oneObjectValue);
			}

			int[] result = this.batchUpdate(updateSql, arr);
			for (int i : result) {
				resultArr.add(i);
			}
		}
		int[] rs = new int[resultArr.size()];
		for (int i : rs) {
			rs[i] = resultArr.get(i);
		}
		return rs;
	}

	/**
	 * 删除实体
	 * @param entity
	 * @return
	 */
	public <T extends Entity<?>> int delete(T entity) {
		EntityInfo info = EntityListener.getEntityInfo(entity.getClass());
		String sql = info.getDeleteSql(info.pkName);
		return super.update(sql, entity.getPkId());
	}
	public <T extends Entity<?>> int delete(Class<T> clazz, LinkedHashMap<String, Object> condition) {
		EntityInfo info = EntityListener.getEntityInfo(clazz);
		String[] key = new String[condition.keySet().size()];
		String sql = info.getDeleteSql(condition.keySet().toArray(key));
		return super.update(sql, condition.values().toArray());
	}

	/**
	 * 获取实体
	 * @param clazz
	 * @param pk
	 * @return
	 */
	public <T extends Entity<?>> T get(Class<T> clazz, Object pk) {
		// TODO 可做缓存
		EntityInfo info = EntityListener.getEntityInfo(clazz);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put(info.pkName, pk);

		return getFirst(clazz, map);
	}

	/**
	 * 获取首行记录
	 * @param clazz 查询实体类
	 * @param params 查询条件 key:字段名 value:查询值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Entity<?>> T getFirst(Class<T> clazz, LinkedHashMap<String, Object> condition) {
		EntityInfo info = EntityListener.getEntityInfo(clazz);
		String[] key = new String[condition.keySet().size()];
		String sql = info.getSelectSql(condition.keySet().toArray(key));

		// condition.put("limintBegin", 1);
		// condition.put("limintEnd", 2);
		List<T> result = (List<T>) super.query(sql, info.entity, condition.values().toArray());
		if (result.size() > 0) {
			return (T) result.get(0);

		} else {
			return null;
		}
	}

	/**
	 * 获取表中所有实体(基本不会用,偶尔有几条数据的表，例如Id表)
	 * @param clazz 查询实体类
	 * @return
	 */
	public <T extends Entity<?>> List<T> getList(Class<T> clazz) {
		return getList(clazz, null, 0, 0);
	}

	/**
	 * 根据条件查询
	 * @param clazz  查询实体类
	 * @param params 查询条件 key:字段名 value:查询值
	 * @return
	 */
	public <T extends Entity<?>> List<T> getList(Class<T> clazz, LinkedHashMap<String, Object> condition) {
		return getList(clazz, condition, 0, 0);
	}

	/**
	 * 分页查询
	 * @param clazz
	 * @param params
	 * @param columName
	 * @param limitBegin
	 * @param limitEnd
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Entity<?>> List<T> getList(Class<T> clazz, LinkedHashMap<String, Object> condition, int limitBegin, int limitEnd) {
		EntityInfo info = EntityListener.getEntityInfo(clazz);

		String sql;
		if (condition == null || condition.size() < 1) {
			sql = info.getSelectSql();
			return (List<T>) super.query(sql, info.entity);
		} else {
			String[] key = new String[condition.keySet().size()];
			sql = info.getSelectSql(limitBegin, limitEnd, condition.keySet().toArray(key));
			if (limitBegin > 0) {
				condition.put("limintBegin", limitBegin);
			}
			if (limitEnd > 0 && limitEnd > limitBegin) {
				condition.put("limitEnd", limitEnd);
			}
			return (List<T>) super.query(sql, condition.values().toArray(), info.entity);
		}
	}
	/**
	 * 自定义sql查询
	 * @param sql sql语句
	 * @param condition 条件值
	 * @param clazz 对应实体类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Entity<?>> List<T> getList(String sql, Object[] condition, Class<T> clazz) {
		EntityInfo info = EntityListener.getEntityInfo(clazz);

		if (condition == null || condition.length < 1) {
			sql = info.getSelectSql();
			return (List<T>) super.query(sql, info.entity);
		} else {
			return (List<T>) super.query(sql, condition, info.entity);
		}
	}

	/**
	 * 查询主键列表
	 * @param clazz 类型
	 * @param params 查询条件 key:字段名 value:查询值
	 * @return
	 */
	public <T extends Entity<?>> List<Long> getPKList(Class<T> clazz, LinkedHashMap<String, Object> condition) {
		return getPKList(clazz, condition, 0, 0);
	}

	/**
	 * 查询主键分页列表
	 * @param clazz 类型
	 * @param params 查询条件 key:字段名 value:查询值
	 * @param limit 记录条数
	 * @return
	 */
	public <T extends Entity<?>> List<Long> getPKList(Class<T> clazz, LinkedHashMap<String, Object> condition, int limitBegin, int limitEnd) {
		EntityInfo info = EntityListener.getEntityInfo(clazz);
		String[] key = new String[condition.keySet().size()];
		String sql = info.getSelectSql(info.pkName, limitBegin, limitEnd, condition.keySet().toArray(key));
		if (limitBegin > 0) {
			condition.put("limintBegin", limitBegin);
		}
		if (limitEnd > 0 && limitEnd > limitBegin) {
			condition.put("limitEnd", limitEnd);
		}

		return super.queryForList(sql, condition.values().toArray(), Long.class);
	}
	
	/**
	 * 插入返回数据库自增id
	 * @param entity
	 * @return
	 */
	public <T extends Entity<Long>> Long saveAndIncreasePK(T entity) {
		Assert.isTrue(entity.getPkId() == 0);

		EntityInfo info = EntityListener.getEntityInfo(entity.getClass());
		final String sql = info.getReplaceIntoSql();
		final ArrayList<Object> values = entity.getRowValue(true);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				for (int i = 0; i < values.size(); i++) {
					Object value = values.get(i);
					ps.setObject(i + 1, value);
				}
				return ps;
			}
		}, keyHolder);
		long id = keyHolder.getKey().longValue();
		entity.setPkId(id);
		return id;
	}
	
	/**
	 * 分组entity
	 * @param entitys
	 * @return
	 */
	protected <T extends Entity<?>> Map<String, List<T>> groupEntity(Collection<T> entitys) {
		Map<String, List<T>> map = new LinkedHashMap<String, List<T>>();
		for (T t : entitys) {
			EntityInfo info = EntityListener.getEntityInfo(t.getClass());
			String tableName = info.tableName;
			List<T> list = null;
			if (map.containsKey(tableName)) {
				list = map.get(tableName);
			} else {
				list = new ArrayList<>();
				map.put(tableName, list);
			}
			list.add(t);
		}
		return map;
	}

}
