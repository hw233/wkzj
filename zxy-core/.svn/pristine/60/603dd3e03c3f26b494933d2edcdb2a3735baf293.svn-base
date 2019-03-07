package com.jtang.core.db;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.PackageScanner;

/**
 * 实体反射初始化
 * @author 0x737263
 *
 */
public class EntityListener {
	
	private static final Logger LOGGER = Logger.getLogger(EntityListener.class);

	private static Map<Class<?>, EntityInfo> entityInfos = new HashMap<Class<?>, EntityInfo>();
	
	public static void entityScan(String packageScan) {
		// 获取所有继承于Entity类的对象列表。
		// 扫描所有Entity对象。获取 表名，字段名,
		Collection<Class<Object>> collection = PackageScanner.scanPackages(packageScan);
		for (Class<Object> clz : collection) {
			if (Entity.class.isAssignableFrom(clz)) {
				EntityInfo ei = new EntityInfo();
				ei.className = clz.getCanonicalName();
				TableName tname = clz.getAnnotation(TableName.class);
				if (null != tname) {
					ei.tableName = tname.name();
					ei.tableType = tname.type();
				} else {
					LOGGER.error(clz.getCanonicalName() + "未定义表名");
					continue;
				}

				// Entity反射属性列表
				Field[] fields = clz.getDeclaredFields();
				ArrayList<String> arrayList = new ArrayList<>();
				for (Field field : fields) {

					Column column = field.getAnnotation(Column.class);
					if (null != column) {

						String dbColumName = (!column.alias().equals("")) ? column.alias() : field.getName();
						if (column.pk()) {
							ei.pkName = dbColumName;
							if (null != ei.pkName && "".equals(ei.pkName)) {
								throw new RuntimeException(ei.className + "重复主键");
							}
						}
						arrayList.add(dbColumName);
					}
				}
				String[] tableNames = new String[arrayList.size()];
				ei.columnName = arrayList.toArray(tableNames);

				if (ei.pkName == null || ei.pkName.equals("")) {
					LOGGER.error(ei.className + "实体缺少主键");
				}

				// 实例化Entity
				try {
					ei.entity = (Entity<?>) clz.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
				entityInfos.put(clz, ei);
			}
		}

	}
	
	/**
	 * 获取EntityInfo
	 * @param clazz		
	 * @return
	 */
	public static <T extends Entity<?>> EntityInfo getEntityInfo(Class<T> clazz) {
		return entityInfos.get(clazz);
	}

	public static Map<Class<?>, EntityInfo> getEntityInfos() {
		return entityInfos;
	}
}
