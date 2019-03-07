package com.jtang.gameserver.module.gift.handler;

/**
 * 礼物模块命令码
 * @author vinceruan
 *
 */
public interface GiftCmd {
	
	/**
	 * 送礼
	 * <pre>
	 * 请求:{@code GiveGiftRequest}
	 * 响应:{@code Response}
	 * </pre>
	 */
	byte GIVE_GIFT = 1;
	
	/**
	 * 收礼
	 * <pre>
	 * 请求:{@code AcceptGiftRequest}
	 * 响应:{@code AcceptGiftResponse}
	 * </pre>
	 */
	byte ACCEPT_GIFT =2;
	
	/**
	 * 获取大礼包
	 * <pre>
	 * 请求:{@code Request}
	 * 响应:{@code OpenGiftPackageResponse}
	 * </pre> 
	 */
	byte OPEN_GIFT_PACKAGE = 3;
	
	/**
	 * 获取礼物信息
	 * <pre>
	 * 请求:{@code Request}
	 * 响应:{@code GiftInfoResponse}
	 * </pre>
	 */
	byte GET_GIFT_INFO = 4;
	
	/**
	 * 推送礼物的状态
	 * <pre>
	 * 请求:null
	 * 响应:{@code PushGiftStateResponse}
	 * </pre>
	 */
	byte PUSH_NEW_GIFT = 5;
	
	/**
	 * 推送大礼包进度条状态
	 *  <pre>
	 *  相应:{@code GiftInfoResponse}
	 *  </pre>
	 * */
	byte GIFT_PACKAGE_INFO = 6;
	
	/**
	 * 一键送礼
	 * 请求:{@code Request}
	 * 返回:{@code Response}
	 */
	byte ONE_KEY_GIVE_GIFT = 7;
	
	/**
	 * 一键收礼
	 * 请求:{@code Request}
	 * 返回:{@code AcceptGiftResponse}
	 */
	byte ONE_KEY_ACCEPT_GIFT = 8;
}
