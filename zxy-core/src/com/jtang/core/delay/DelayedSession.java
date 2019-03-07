package com.jtang.core.delay;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.session.IoSession;

/**
 * 延迟关闭session对象
 * @author ludd
 *
 */
public class DelayedSession implements Delayed {

	/**
	 * 
	 */
	private IoSession session;
	/**
	 * 开始时间（毫秒）
	 */
	private long start;
	/**
	 * 延迟时间（毫秒）
	 */
	private long delay;
	
	/**
	 * 构造方法
	 * @param session 
	 * @param start 开始时间
	 * @param delay 延迟时间（秒）
	 */
	public DelayedSession(IoSession session, Date start, int delay) {
		super();
		this.session = session;
		this.start = start.getTime();
		this.delay = delay * 1000L;
	}

	@Override
	public int compareTo(Delayed o) {
		if (o.getDelay(TimeUnit.MILLISECONDS) < this.getDelay(TimeUnit.MILLISECONDS)){
			return 1;
		} else if (o.getDelay(TimeUnit.MILLISECONDS) > this.getDelay(TimeUnit.MILLISECONDS)){
			return -1;
		}
		return 0;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return start + delay - System.currentTimeMillis();
	}

	public IoSession getSession() {
		return session;
	}
	
	

}
