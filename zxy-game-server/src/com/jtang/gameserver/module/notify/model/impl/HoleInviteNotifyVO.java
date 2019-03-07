package com.jtang.gameserver.module.notify.model.impl;

import java.util.List;

import com.jtang.gameserver.module.notify.model.AbstractNotifyVO;

/**
 * 邀请好友发现新的洞府通知
 * @author 0x737263
 *
 */
public class HoleInviteNotifyVO extends AbstractNotifyVO {
	private static final long serialVersionUID = -4099226344918932611L;
	/**
	 * 洞府自增id
	 */
	public long id;
	
	/**
	 * 洞府配置id
	 */
	public int holeId;
	
	
	public HoleInviteNotifyVO() {
		
	}
	
	public HoleInviteNotifyVO(long id,int holeId) {
		this.id = id;
		this.holeId = holeId;
	}
	
	
	@Override
	protected void subClazzWrite() {
		writeLong(this.id);
		writeInt(this.holeId);
	}
	
	@Override
	protected void subClazzString2VO(String[] items) {
		this.id = Long.valueOf(items[0]);
		this.holeId = Integer.valueOf(items[1]);
	}
	
	@Override
	protected void subClazzParse2String(List<String> attributes) {
		attributes.add(String.valueOf(id));
		attributes.add(String.valueOf(holeId));
	}
}
