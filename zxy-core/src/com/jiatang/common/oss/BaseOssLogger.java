package com.jiatang.common.oss;

import java.util.Enumeration;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * oss日志基类
 * @author 0x737263
 *
 */
public abstract class BaseOssLogger {
	protected static Logger LOGGER = LoggerFactory.getLogger(BaseOssLogger.class);
	
	protected static String COLUMN_SPLIT = ",";
	protected static String ROW_SPLIT = "\r\n";
	
	protected void write(String ossName, String message) {
		Logger log = LoggerFactory.getLogger(ossName);
		if (log.isInfoEnabled() && message != null) {
			log.info(message);
		}
	}
	
	protected void reflushLogger(String[] ossNameArray) {
		for (String ossName : ossNameArray) {
			try {
				org.apache.log4j.Logger log = LogManager.getLogger(ossName);
				if (log == null) {
					continue;
				}

				Enumeration<?> allAppenders = log.getAllAppenders();
				if (allAppenders == null) {
					continue;
				}
				while (allAppenders.hasMoreElements()) {
					Appender appender = (Appender) allAppenders.nextElement();
					if (appender instanceof FileAppender) {
						FileAppender fileAppender = (FileAppender) appender;
						fileAppender.setBufferedIO(false);
						fileAppender.setImmediateFlush(true);
						log.info("");
					}
				}
			} catch (Exception e) {
				LOGGER.error("{}", e);
			}
		}
	}

}
