package com.jtang.worldserver.module.base.facade.impl;

import java.io.ByteArrayInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jiatang.common.type.WorldState;
import com.jtang.core.dataconfig.DataConfig;
import com.jtang.core.dataconfig.ReloadConfig;
import com.jtang.core.db.DBQueue;
import com.jtang.core.result.Result;
import com.jtang.core.schedule.Schedule;
import com.jtang.worldserver.component.World;
import com.jtang.worldserver.component.oss.WorldOssLogger;
import com.jtang.worldserver.module.base.facade.BaseFacade;
import com.jtang.worldserver.server.session.WorldSession;

@Component
public class BaseFacadeImpl implements BaseFacade {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseFacadeImpl.class);
	
	@Autowired
	private Schedule schedule;

	@Autowired
	private DBQueue dbQueue;
	
	@Autowired
	private WorldSession worldSession;
	
	@Autowired
	private DataConfig dataConfig;

	@Autowired
	private ReloadConfig reloadConfig;
	
	@Override
	public Result shutdownServer() {
		LOGGER.info("shutdown server.....");
		World.state = WorldState.CLOSE;
		dbQueue.changeBlockTime(1000);
		WorldOssLogger.reflushLogger();
		worldSession.closeAllSession();
		schedule.addDelaySeconds(new Runnable() {
			@Override
			public void run() {
				System.exit(0);
			}
		}, 5);
		return Result.valueOf();
	}
	
	
	@Override
	public Result reloadConfig(String fileName, String data) {
		if (fileName == null || fileName.isEmpty()) {
			return Result.valueOf(GameStatusCodeConstant.DATA_CONFIG_NAME_NOT_NULL);
		}
		if (data == null || data.isEmpty()) {
			return Result.valueOf(GameStatusCodeConstant.DATA_CONFIG_NOT_NULL);
		}
		LOGGER.info("flush dataConfig:[" + fileName + "]");
		boolean result = dataConfig.checkModelAdapter(fileName, new ByteArrayInputStream(data.getBytes()));
		result = reloadConfig.flushFile(fileName, data);
		if (!result) {
			LOGGER.error("reload file check error..");
			return Result.valueOf(GameStatusCodeConstant.DATA_CONFIG_FLUSH_ERROR);
		} else {
			LOGGER.info("reload file check success..");
		}
		return Result.valueOf();
	}

}
