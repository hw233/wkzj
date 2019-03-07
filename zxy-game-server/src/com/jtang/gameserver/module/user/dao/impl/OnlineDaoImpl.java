package com.jtang.gameserver.module.user.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Online;
import com.jtang.gameserver.module.user.dao.OnlineDao;

/**
 * 角色在线实现类
 * @author 0x737263
 *
 */
@Repository
public class OnlineDaoImpl implements OnlineDao {

	@Autowired
	private IdTableJdbc jdbc;

	@Override
	public Online get(long actorId) {
		return jdbc.get(Online.class, actorId);
	}
	
	@Override
	public int cleanOnline(int serverId) {
		String sql = "delete from online where serverId = ?";
		Object[] params = new Object[] { serverId };
		return jdbc.update(sql, params);
	}

	@Override
	public void add(Online online) {
		jdbc.save(online);
	}

	@Override
	public void remove(long actorId) {
		Online entity = jdbc.get(Online.class, actorId);
		if (entity != null) {
			jdbc.delete(entity);
		}
	}
}
