package com.jtang.gameserver.module.notify.model.impl;

import java.util.List;

import com.jtang.gameserver.module.notify.model.AbstractNotifyVO;

/**
 * 添加或删除盟友的系统通知
 * @author pengzy
 *
 */
public class AllyNotifyVO extends AbstractNotifyVO {
	private static final long serialVersionUID = -8858210098822624207L;
	
	/**
	 * 信息类型为 6.加为盟友时1-添加；2-被添加
	 * 信息类型为 7.删除盟友时1-删除；2-被删除
	 */
	public byte actionType;
	
	public AllyNotifyVO() {
		
	}
	
	public AllyNotifyVO(byte actionType) {
		this.actionType = actionType;
	}


	@Override
	protected void subClazzWrite() {
		writeByte(actionType);
	}

	@Override
	protected void subClazzString2VO(String[] items) {
		this.actionType = Byte.valueOf(items[0]);		
	}

	@Override
	protected void subClazzParse2String(List<String> attributes) {
		attributes.add(String.valueOf(actionType));		
	}
	
}
