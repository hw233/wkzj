package com.jtang.gameserver.module.notify.handler.response;
//package com.jtang.sm2.module.notify.handler.response;
//
//import com.jtang.core.protocol.WritablePacket;
//import com.jtang.sm2.module.notify.model.AbstractNotifyVO;
//
///**
// * 推送信息数据包
// * @author pengzy
// *
// */
//public class NotifyVOResponse extends WritablePacket {
//	
//	/**
//	 * 类型不同时，读取的内容也会不同
//	 */
//	private AbstractNotifyVO notifyVO;
//
//	public NotifyVOResponse(AbstractNotifyVO notifyVO) {
//		this.notifyVO = notifyVO;
//	}
//
//	@Override
//	public void write() {
//		notifyVO.write();
//	}
//}
