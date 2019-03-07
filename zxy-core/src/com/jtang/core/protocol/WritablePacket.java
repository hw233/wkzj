//package com.jtang.core.protocol;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.apache.mina.core.buffer.IoBuffer;
//
//import com.jtang.core.mina.SocketConstant;
//import com.jtang.core.utility.CollectionUtils;
//
///**
// * 提供接口将基本数据类型转换成字节流,
// * 所有Response包中的发送类都要继承此类
// * @author vinceruan
// *
// */
//public abstract class WritablePacket {
//	private static final Log LOGGER = LogFactory.getLog(WritablePacket.class);
//	
//	/**
//	 * 写处理iobuffer
//	 * <pre> 
//	 * IoBuffer.allocate(value.getLength());
//	 * buffer.setAutoExpand(true);
//	 * </pre>
//	 */
//	protected IoBuffer buffer;
//
//	/**
//	 * 写
//	 */
//	public abstract void write();
//
//	protected WritablePacket() {
//		setIoBuffer(8, true);
//	}
//	
//	public void setIoBuffer(IoBuffer buffer) {
//		this.buffer = buffer;
//	}
//
//	public void setIoBuffer(int length, boolean autoExpand) {
//		buffer = IoBuffer.allocate(length);
//		buffer.setAutoExpand(autoExpand);
//	}
//
//	public void writeByte(Byte value) {
//		buffer.put(value);
//	}
//
//	public void writeShort(Short value) {
//		buffer.putShort(value);
//	}
//
//	public void writeInt(Integer value) {
//		buffer.putInt(value);
//	}
//
//	public void writeLong(Long value) {
//		buffer.putLong(value);
//	}
//
//	public void writeFloat(Float value) {
//		buffer.putFloat(value);
//	}
//	
//	public void writeDouble(Double value) {
//		buffer.putDouble(value);
//	}
//	
//	public void writeLongList(List<Long> list) {
//		if (CollectionUtils.isEmpty(list)) {
//			buffer.putShort((short)0);
//			return;
//		}
//		buffer.putShort((short)list.size());
//		for (long item : list) {
//			buffer.putLong(item);
//		}
//	}
//	
//	public void writeLongSet(Set<Long> set) {
//		if (CollectionUtils.isEmpty(set)) {
//			buffer.putShort((short)0);
//			return;
//		}
//		buffer.putShort((short)set.size());
//		for (long item : set) {
//			buffer.putLong(item);
//		}
//	}
//	
//	public void writeIntList(List<Integer> list) {
//		if (CollectionUtils.isEmpty(list)) {
//			buffer.putShort((short)0);
//			return;
//		}
//		buffer.putShort((short)list.size());
//		for (int item : list) {
//			buffer.putInt(item);
//		}
//	}
//	
//	public void writeIntMap(Map<Integer, Integer> map) {
//		if (CollectionUtils.isEmpty(map)) {
//			buffer.putShort((short)0);
//			return;
//		}
//		buffer.putShort((short)map.size());
//		for (Entry<Integer, Integer> entry : map.entrySet()) {
//			buffer.putInt(entry.getKey());
//			buffer.putInt(entry.getValue());
//		}
//	}
//	
//	public void writeByteIntMap(Map<Byte, Integer> map) {
//		if (CollectionUtils.isEmpty(map)) {
//			buffer.putShort((short)0);
//			return;
//		}
//		buffer.putShort((short)map.size());
//		for (Entry<Byte, Integer> entry : map.entrySet()) {
//			buffer.put(entry.getKey());
//			buffer.putInt(entry.getValue());
//		}
//	}
//	
//	public void writeByteLongMap(Map<Byte, Long> map) {
//		if (CollectionUtils.isEmpty(map)) {
//			buffer.putShort((short)0);
//			return;
//		}
//		buffer.putShort((short)map.size());
//		for (Entry<Byte, Long> entry : map.entrySet()) {
//			buffer.put(entry.getKey());
//			buffer.putLong(entry.getValue());
//		}
//	}
//	
//	public void writeIntByteMap(Map<Integer, Integer> map) {
//		if (CollectionUtils.isEmpty(map)) {
//			buffer.putShort((short)0);
//			return;
//		}
//		buffer.putShort((short)map.size());
//		for (Entry<Integer, Integer> entry : map.entrySet()) {
//			buffer.putInt(entry.getKey());
//			buffer.put(entry.getValue().byteValue());
//		}
//	}
//	
//	public void writeLongIntMap(Map<Long, Integer> map) {
//		if (CollectionUtils.isEmpty(map)) {
//			buffer.putShort((short)0);
//			return;
//		}
//		buffer.putShort((short)map.size());
//		for (Entry<Long, Integer> entry : map.entrySet()) {
//			buffer.putLong(entry.getKey());
//			buffer.putInt(entry.getValue());
//		}
//	}
//	
//	public void writeStringList(List<String> list) {
//		if (CollectionUtils.isEmpty(list)) {
//			buffer.putShort((short)0);
//			return;
//		}
//		buffer.putShort((short)list.size());
//		for (String item : list) {
//			writeString(item);
//		}
//	}
//
//	public void writeStringIntMap(Map<String, Integer> map) {
//		if (CollectionUtils.isEmpty(map)) {
//			buffer.putShort((short)0);
//			return;
//		}
//		buffer.putShort((short)map.size());
//		for (Entry<String, Integer> entry : map.entrySet()) {
//			writeString(entry.getKey());
//			buffer.putInt(entry.getValue());
//		}
//	}
//	
//	public void writeString(String value) {
//		if (value == null || value.isEmpty()) {
//			writeShort((short) 0);
//			return;
//		}
//
//		byte data[] = value.getBytes(SocketConstant.CHARSET);
//		short len = (short) data.length;
//		buffer.putShort(len);
//		buffer.put(data);
//	}
//	public void writeBigString(String value) {
//		if (value == null || value.isEmpty()) {
//			writeInt(0);
//			return;
//		}
//		
//		byte data[] = value.getBytes(SocketConstant.CHARSET);
//		int len =  data.length;
//		buffer.putInt(len);
//		buffer.put(data);
//	}
//
//	public void writeBytes(byte value[]) {
//		buffer.put(value);
//	}
//	
//	public void writeObject(Object object) {
//		if (object instanceof Integer) {
//			buffer.putInt((int)object);
//			return;
//		} 
//		
//		if (object instanceof Long) {
//			buffer.putLong((long)object);
//			return;
//		}
//		
//		if (object instanceof Short) {
//			buffer.putShort((short)object);
//			return;
//		}
//		
//		if (object instanceof Byte) {
//			buffer.put((byte)object);
//			return;
//		}
//		
//		if (object instanceof String) {
//			String value = (String)object;
//			writeString(value);
//			return;
//		}
//		LOGGER.error("不可序列化的类型:" + object.getClass());
//	}
//	
//	/**
//	 * 返回buffer数组
//	 * 
//	 * @return
//	 */
//	public byte[] getBytes() {
//		write();
//		byte[] bytes = null;
//		if(buffer.limit() == 0) {
//			bytes = buffer.array();
//		} else {
//			bytes = new byte[buffer.position()];
//			buffer.flip();
//			buffer.get(bytes);
//		}
//		buffer.clear();
//		
//		return bytes;
//	}
//}
