package com.jtang.gameserver.dbproxy;

import org.springframework.beans.factory.annotation.Autowired;

import com.jtang.core.db.BaseDBQueueImpl;
import com.jtang.core.db.Entity;
import com.jtang.core.db.EntityInfo;
import com.jtang.core.db.EntityListener;
public class GameDbQueueImpl extends BaseDBQueueImpl {
	
	@Autowired
	private IdTableJdbc idTableJdbc;
	@Override
	protected Runnable createInsertTask(final Entity<?> entity) {
		return new Runnable() {
			@Override
			public void run() {
				@SuppressWarnings("unchecked")
				Entity<Long> en = (Entity<Long>) entity;
				try {
					// TODO 这个位置可以使用上面定义的重试次数.
					idTableJdbc.save(en);
				} catch (Exception e) {
					EntityInfo info = EntityListener.getEntityInfo(en.getClass());
					LOGGER.error(String.format("save db error. pk:[%s], tableName:[%s], entity drop.", en.getPkId(), info.tableName), e);
				}
			}
		};
	}
}
