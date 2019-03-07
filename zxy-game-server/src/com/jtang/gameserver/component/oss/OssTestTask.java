package com.jtang.gameserver.component.oss;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;





import com.jtang.core.schedule.Schedule;
import com.jtang.core.utility.DateUtils;

//@Component
public class OssTestTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(OssTestTask.class);
	
	@Autowired
	Schedule schedule;
	
	private static int index = 0;
	
	@PostConstruct
	private void init() {
		schedule.addEveryMinute(test(), 1);
		LOGGER.info("oss test task init complete!!!");
	}
	
	private Runnable test() {
		return new Runnable() {
			@Override
			public void run() {
				LOGGER.info("oss test runnable start");
				for (int i = 0; i < 10; i++) {
					index++;
					GameOssLogger.ossTest(index, 1, i * i, DateUtils.getNowInSecondes());
				}
				LOGGER.info("oss test runnable end.");
			}
		};
	}
	

}

