package com.jtang.gameserver.module.notify.model.impl;

import java.util.List;

import com.jtang.gameserver.module.notify.model.AbstractNotifyVO;

/**
 * 感谢盟友，在通知收件箱领取（ 合作关卡，试炼洞，送礼）时，感谢对方
 * @author 0x737263
 *
 */
public class ThankAllyNotifyVO extends AbstractNotifyVO {
	private static final long serialVersionUID = 1785722621220625790L;
	
	/**
	 * 感谢类型
	 */
	public int thankType;
	
	public ThankAllyNotifyVO() {
		
	}
	
	public ThankAllyNotifyVO(int thankType) {
		this.thankType = thankType;
	}
	
	
	@Override
	protected void subClazzWrite() {
		writeByte((byte) this.thankType);
	}
	
	@Override
	protected void subClazzString2VO(String[] items) {
		thankType = Byte.valueOf(items[0]);
	}
	
	@Override
	protected void subClazzParse2String(List<String> attributes) {
		attributes.add(String.valueOf(thankType));
	}
}
