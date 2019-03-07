package com.jtang.gameserver.module.applog.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.AppLog;
import com.jtang.gameserver.module.applog.dao.LogDao;

@Component
public class LogDaoImpl implements LogDao {

	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	@Override
	public void save(AppLog log) {
//		jdbc.save(log);
		dbQueue.insertQueue(log);
	}

}
