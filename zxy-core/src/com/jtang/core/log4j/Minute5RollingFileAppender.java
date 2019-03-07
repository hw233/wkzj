package com.jtang.core.log4j;

/**
 * 每5分钟滚动文件生成
 * @author 0x737263
 *
 */
public class Minute5RollingFileAppender extends MinuteRollingFileAppender {

	public Minute5RollingFileAppender() {
		super(5);
	}
}