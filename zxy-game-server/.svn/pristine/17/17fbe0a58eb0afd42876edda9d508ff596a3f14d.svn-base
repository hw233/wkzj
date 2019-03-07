package com.jtang.gameserver.module.notice.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.notice.model.NoticeVO;

/**
 * 广播消息的时候推送的数据包
 * @author pengzy
 *
 */
public class NoticeResponse extends IoBufferSerializer{

	private NoticeVO noticeVO;

	public NoticeResponse(NoticeVO noticeVO) {
		this.noticeVO = noticeVO;
	}

	@Override
	public void write() {
		writeBytes(noticeVO.getBytes());
	}
}
