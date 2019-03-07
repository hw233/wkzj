package com.jtang.core.utility;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;

public class BufferFactory {
	public static ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
	
	private static final int INIT_SIZE = 8;
	/**
	 * 获取一个iobuffer
	 * @param bytes
	 * @return
	 */
	public static IoBuffer getIoBuffer(byte[] bytes) {
		IoBuffer ioBuffer =  IoBuffer.wrap(bytes);
		ioBuffer.order(BYTE_ORDER);
		return ioBuffer;
	}
	/**
	 * 获取一个iobuffer
	 * @param autoExpand
	 * @return
	 */
	public static IoBuffer getIoBuffer(boolean autoExpand) {
		IoBuffer ioBuffer =  IoBuffer.allocate(INIT_SIZE);
		ioBuffer.order(BYTE_ORDER);
		ioBuffer.setAutoExpand(autoExpand);
		return ioBuffer;
	}
	/**
	 * 获取一个iobuffer
	 * @param capacity
	 * @param autoExpand
	 * @return
	 */
	public static IoBuffer getIoBuffer(int capacity, boolean autoExpand) {
		IoBuffer ioBuffer =  IoBuffer.allocate(capacity);
		ioBuffer.order(BYTE_ORDER);
		ioBuffer.setAutoExpand(autoExpand);
		return ioBuffer;
	}
	
}
