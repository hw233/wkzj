package com.jtang.gameserver.admin.handler.request;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 系统公告
 * @author ludd
 *
 */
public class SystemNoticeRequest extends IoBufferSerializer {

	/**
	 * 公告内容
	 */
	public String message;
	
	/**
	 * 轮询次数
	 */
	public int pollingNum;
	
	/**
	 * 间隔时间
	 */
	public int delayTime;
	
	/**
	 * 渠道id列表， 如果没有渠道id，默认全部发送
	 */
	public List<Integer> channelIds;
	
	
	public SystemNoticeRequest(byte[] bytes) {
		super(bytes);
	}


	@Override
	public void read() {
		message = readString();
		pollingNum = readInt();
		delayTime = readInt();
		channelIds = readIntegerList();
	}

}
