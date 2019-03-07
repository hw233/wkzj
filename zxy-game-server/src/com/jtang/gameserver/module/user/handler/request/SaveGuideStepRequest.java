package com.jtang.gameserver.module.user.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 保存新手引导数据
 * @author 0x737263
 *
 */
public class SaveGuideStepRequest extends IoBufferSerializer {
	
	/**
	 * 引导类型id
	 */
	public int key;
	
	/**
	 * 引导值
	 */
	public int value;
	
	public SaveGuideStepRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		this.key = readInt();
		this.value = readInt();
	}

}
