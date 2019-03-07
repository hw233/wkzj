package com.jiatang.common.model;

import java.io.Serializable;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * BUFF对象
 * @author vinceruan
 *
 */
public class BufferVO extends IoBufferSerializer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3631477638042005762L;

	/**
	 * 影响到的属性
	 */
	public HeroVOAttributeKey key;
	
	/**
	 * 加成值
	 */
	public int addVal;
	
	/**
	 * 产生buffer的原因(参考${code BufferSourceType})
	 */
	public int sourceType;
	
	public BufferVO(HeroVOAttributeKey key, int addVal, int sourceType) {
		this.key = key;
		this.addVal = addVal;
		this.sourceType = sourceType;
	}
	
	/**
	 * 战场中临时buff
	 * @param key
	 * @param addVal
	 */
	public BufferVO(HeroVOAttributeKey key, int addVal) {
		this.key = key;
		this.addVal = addVal;
	}
	
	public BufferVO() {
	}
	
	@Override
	public void write() {
		this.writeByte(key.getCode());
		this.writeInt(addVal);
		this.writeInt(sourceType);
	}
	
	@Override
	public void readBuffer(IoBufferSerializer buffer) {
		this.key = HeroVOAttributeKey.getByCode(buffer.readByte());
		this.addVal = buffer.readInt();
		this.sourceType = buffer.readInt();
	}
}
