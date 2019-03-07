package com.jtang.core.event;

/**
 * 事件基类
 * @author vinceruan
 *
 */
public class Event {
	/**
	 * 事件的key {@code EventKey}
	 */
	public String name;
	
	public Event(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	@SuppressWarnings("unchecked")
	public <T> T convert() {
		return (T)this;
	}
}