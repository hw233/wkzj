package com.jtang.gameserver.dbproxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import com.jtang.core.db.BaseJdbcTemplate;
import com.jtang.core.db.Entity;
import com.jtang.core.db.EntityInfo;
import com.jtang.core.db.EntityListener;
import com.jtang.gameserver.component.Game;
import com.jtang.gameserver.dbproxy.entity.IdTable;

/**
 * 本类操作都是基于idTable的操作。主要是为保存数据到数据库时由id表来分配主键id
 * 
 * @author ludd
 * 
 */
public class IdTableJdbc extends BaseJdbcTemplate  {

	/**
	 * id表管理其他表的自增
	 */
	private Map<String, IdTable> idTables = new ConcurrentHashMap<String, IdTable>();
	
	@Override
	@PostConstruct
	public void init() {
		super.init();
		getIdTableData();
	}

	/**
	 * 获取ID表数据
	 */
	private void getIdTableData() {
		EntityInfo info = EntityListener.getEntityInfo(IdTable.class);
		String sql = info.getSelectSql();
		List<?> list = super.query(sql, info.entity);
		for (Object entity : list) {
			IdTable idTable = (IdTable) entity;
			if (idTables.containsKey(idTable.tableName) == false && Game.getServerId() == idTable.serverId) {
				idTable.init(); // 传入 serverid算出自增初始值
				idTables.put(idTable.tableName, idTable);
			}
		}
	}

	/**
	 * 保存
	 * @param entity
	 * @return 返回主键
	 */
	public <T extends Entity<Long>> long save(T entity) {
		// TODO 缓存对象置为删除标识。
		EntityInfo info = EntityListener.getEntityInfo(entity.getClass());
		long id = getEntityId(info.tableName);
		if (id != 0L) {// ID不为0 表示需要维护自增
			entity.setPkId(id);
		}

		String sql = info.getReplaceIntoSql(); // TODO 插入无条件的。
		super.update(sql, entity.getRowValue(true).toArray());
		return id;
	}
	
	/**
	 * 批量更新
	 * @param entitys
	 * @return 返回数据库每条语句影响行数
	 */
	protected <T extends Entity<Long>> int[] save(List<T> entitys) {
		Map<String, List<T>> map = groupEntity(entitys);//分组
		
		List<Integer> resultArr = new ArrayList<>();
		for (String key : map.keySet()) {
			//获取同组的sql
			T param = map.get(key).get(0);
			EntityInfo info = EntityListener.getEntityInfo(param.getClass());
			String sql = info.getReplaceIntoSql();
			
			ArrayList<Object[]> arr = new ArrayList<>();
			for (T object : map.get(key)) {
				long pk = getEntityId(info.tableName);
				if (pk != 0L) {// ID不为0 表示需要维护自增
					object.setPkId(pk);
				}
				ArrayList<Object> values = object.getRowValue(true);
				Object[] oneObjectValue = values.toArray();
				arr.add(oneObjectValue);
			}
			
			int[] result = this.batchUpdate(sql, arr);
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
	 * 生成实体ID
	 * @param tableName
	 * @return
	 */
	private long getEntityId(String tableName) {
		// TODO 以后这里要走对列定时列新到数据库

		if (idTables.containsKey(tableName)) {
			IdTable idTable = idTables.get(tableName);
			long id = idTable.increasePK();

			ArrayList<Object> list = new ArrayList<>();
			list.add(idTable.getAtomMaxId());
			list.add(idTable.autoId);
			String sql = "UPDATE id SET maxId = ? WHERE autoId = ?";
			super.update(sql, list.toArray());
			return id;
		}

		return 0;
	}



}
