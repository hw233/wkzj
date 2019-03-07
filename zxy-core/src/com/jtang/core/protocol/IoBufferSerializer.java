package com.jtang.core.protocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtang.core.mina.SocketConstant;
import com.jtang.core.utility.BufferFactory;
import com.jtang.core.utility.CollectionUtils;

/**
 * iobuffer读写序列化类
 * @author 0x737263
 *
 */
public abstract class IoBufferSerializer {
	private static Logger LOGGER = LoggerFactory.getLogger(IoBufferSerializer.class);
	
	/**
	 * 数字类型读取错误时默认返回值
	 */
	protected static byte DEFAULT_NUMBER_VALUE = 0;
	
	/**
	 * 字符类型读取错误时默认返回值
	 */
	protected static String DEFAULT_STRING_VALUE = "";
	
	protected static int BYTE_LEN = 1;
	protected static int SHORT_LEN = 2;
	protected static int INTEGER_LEN = 4;
	protected static int LONG_LEN = 8;
	protected static int FLOAT_LEN = 4;	
	protected static int DOUBLE_LEN = 8;
	
	protected IoBuffer writeBuffer;
	
	protected IoBuffer readBuffer;
	
	public IoBufferSerializer() {
		setWriteBuffer();
	}
	
	/**
	 * 默认为读取方式
	 * @param bytes	读取的数组
	 */
	public IoBufferSerializer(byte[] bytes) {
//		readBuffer = IoBuffer.wrap(bytes);
		readBuffer = BufferFactory.getIoBuffer(bytes);
		read();
	}

	/**
	 * 设置读取buffer
	 */
	protected void setWriteBuffer() {
//		writeBuffer = IoBuffer.allocate(DOUBLE_LEN);
//		writeBuffer.setAutoExpand(true);
		writeBuffer = BufferFactory.getIoBuffer(DOUBLE_LEN, true);
	}

	/**
	 * 设置写buffer
	 * @param bytes	读取的数组
	 */
	protected void setReadBuffer(byte[] bytes) {
//		readBuffer = IoBuffer.wrap(bytes);
		readBuffer = BufferFactory.getIoBuffer(bytes);
		read();
	}
	
	/**
	 * byte数组方式读取数据流，子类要调用有参构造
	 * @param bytes
	 */
	protected void read() {
		throw new RuntimeException("override the method please");
	}
	
	/**
	 * iobuffer方式读取数值流
	 * @param buffer
	 */
	public void readBuffer(IoBufferSerializer buffer) {
		throw new RuntimeException("override the method please");
	}
	
	/**
	 * 用于
	 * @throws Exception 
	 */
	public void write() {
		try {
			throw new Exception("override the method please");
		} catch(Exception ex) {	
		}
	}
	
	public byte readByte() {
		if (calcCapacity() >= BYTE_LEN) {
			return readBuffer.get();
		}
		return DEFAULT_NUMBER_VALUE;
	}

	public short readShort() {
		if (calcCapacity() >= SHORT_LEN) {
			return readBuffer.getShort();
		}
		return DEFAULT_NUMBER_VALUE;
	}

	public int readInt() {
		if (calcCapacity() >= INTEGER_LEN) {
			return readBuffer.getInt();
		}
		return DEFAULT_NUMBER_VALUE;
	}

	public long readLong() {
		if (calcCapacity() >= LONG_LEN) {
			return readBuffer.getLong();
		}
		return DEFAULT_NUMBER_VALUE;
	}

	public float readFloat() {
		if (calcCapacity() >= FLOAT_LEN) {
			return readBuffer.getFloat();
		}
		return DEFAULT_NUMBER_VALUE;
	}

	public double readDouble() {
		if (calcCapacity() >= DOUBLE_LEN) {
			return readBuffer.getDouble();
		}
		return DEFAULT_NUMBER_VALUE;
	}

	public String readString() {
		if (calcCapacity() < SHORT_LEN) {
			return DEFAULT_STRING_VALUE;
		}

		int size = readBuffer.getShort();
		if (calcCapacity() < size) {
			return DEFAULT_STRING_VALUE;
		}

		byte[] bytes = new byte[size];
		readBuffer.get(bytes);

		return new String(bytes, SocketConstant.CHARSET);
	}

	public String readBigString() {
		if (calcCapacity() < INTEGER_LEN) {
			return DEFAULT_STRING_VALUE;
		}

		int size = readBuffer.getInt();
		if (calcCapacity() < size) {
			return DEFAULT_STRING_VALUE;
		}

		byte[] bytes = new byte[size];
		readBuffer.get(bytes);

		return new String(bytes, SocketConstant.CHARSET);
	}
	
	/**
	 * 读取一个byte数组(带长度)
	 * @return
	 */
	public byte[] readByteArray() {
		int len = readInt();
		byte[] bytes = new byte[len];
		readBuffer.get(bytes);
		return bytes;
	}

	public List<Integer> readIntegerList() {
		List<Integer> list = new ArrayList<Integer>();
		if (calcCapacity() > SHORT_LEN) {
			int size = readBuffer.getShort();
			for (int i = 0; i < size; i++) {
				list.add(this.readInt());
			}
		}
		return list;
	}
	public Map<Integer, Integer> readIntMap() {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		if (calcCapacity() > SHORT_LEN) {
			int size = readBuffer.getShort();
			for (int i = 0; i < size; i++) {
				int key = readInt();
				int value = readInt();
				map.put(key, value);
				
			}
		}
		return map;
	}

	public List<Long> readLongList() {
		List<Long> list = new ArrayList<Long>();
		if (calcCapacity() > SHORT_LEN) {
			int size = readBuffer.getShort();
			for (int i = 0; i < size; i++) {
				list.add(this.readLong());
			}
		}
		return list;
	}

	public List<String> readStringList() {
		List<String> list = new ArrayList<>();
		if (calcCapacity() > SHORT_LEN) {
			int size = readBuffer.getShort();
			for (int i = 0; i < size; i++) {
				list.add(this.readString());
			}
		}
		return list;
	}

	/**
	 * 计算ByteBuffer的容量
	 * 
	 * @return
	 */
	protected int calcCapacity() {
		if (readBuffer == null) {
			return 0;
		}
		return readBuffer.limit() - readBuffer.position();
	}

	public void writeByte(Byte value) {
		writeBuffer.put(value);
	}

	public void writeShort(Short value) {
		writeBuffer.putShort(value);
	}

	public void writeInt(Integer value) {
		writeBuffer.putInt(value);
	}

	public void writeLong(Long value) {
		writeBuffer.putLong(value);
	}

	public void writeFloat(Float value) {
		writeBuffer.putFloat(value);
	}

	public void writeDouble(Double value) {
		writeBuffer.putDouble(value);
	}

	public void writeLongList(List<Long> list) {
		if (CollectionUtils.isEmpty(list)) {
			writeBuffer.putShort((short) 0);
			return;
		}
		writeBuffer.putShort((short) list.size());
		for (long item : list) {
			writeBuffer.putLong(item);
		}
	}
	public void writeByteList(List<Byte> list) {
		if (CollectionUtils.isEmpty(list)) {
			writeBuffer.putShort((short) 0);
			return;
		}
		writeBuffer.putShort((short) list.size());
		for (byte item : list) {
			writeBuffer.put(item);
		}
	}

	public void writeLongSet(Set<Long> set) {
		if (CollectionUtils.isEmpty(set)) {
			writeBuffer.putShort((short) 0);
			return;
		}
		writeBuffer.putShort((short) set.size());
		for (long item : set) {
			writeBuffer.putLong(item);
		}
	}

	public void writeIntList(List<Integer> list) {
		if (CollectionUtils.isEmpty(list)) {
			writeBuffer.putShort((short) 0);
			return;
		}
		writeBuffer.putShort((short) list.size());
		for (int item : list) {
			writeBuffer.putInt(item);
		}
	}

	public void writeIntMap(Map<Integer, Integer> map) {
		if (CollectionUtils.isEmpty(map)) {
			writeBuffer.putShort((short) 0);
			return;
		}
		writeBuffer.putShort((short) map.size());
		for (Entry<Integer, Integer> entry : map.entrySet()) {
			writeBuffer.putInt(entry.getKey());
			writeBuffer.putInt(entry.getValue());
		}
	}

	public void writeByteIntMap(Map<Byte, Integer> map) {
		if (CollectionUtils.isEmpty(map)) {
			writeBuffer.putShort((short) 0);
			return;
		}
		writeBuffer.putShort((short) map.size());
		for (Entry<Byte, Integer> entry : map.entrySet()) {
			writeBuffer.put(entry.getKey());
			writeBuffer.putInt(entry.getValue());
		}
	}

	public void writeByteLongMap(Map<Byte, Long> map) {
		if (CollectionUtils.isEmpty(map)) {
			writeBuffer.putShort((short) 0);
			return;
		}
		writeBuffer.putShort((short) map.size());
		for (Entry<Byte, Long> entry : map.entrySet()) {
			writeBuffer.put(entry.getKey());
			writeBuffer.putLong(entry.getValue());
		}
	}

	public void writeIntByteMap(Map<Integer, Integer> map) {
		if (CollectionUtils.isEmpty(map)) {
			writeBuffer.putShort((short) 0);
			return;
		}
		writeBuffer.putShort((short) map.size());
		for (Entry<Integer, Integer> entry : map.entrySet()) {
			writeBuffer.putInt(entry.getKey());
			writeBuffer.put(entry.getValue().byteValue());
		}
	}

	public void writeLongIntMap(Map<Long, Integer> map) {
		if (CollectionUtils.isEmpty(map)) {
			writeBuffer.putShort((short) 0);
			return;
		}
		writeBuffer.putShort((short) map.size());
		for (Entry<Long, Integer> entry : map.entrySet()) {
			writeBuffer.putLong(entry.getKey());
			writeBuffer.putInt(entry.getValue());
		}
	}

	public void writeStringList(List<String> list) {
		if (CollectionUtils.isEmpty(list)) {
			writeBuffer.putShort((short) 0);
			return;
		}
		writeBuffer.putShort((short) list.size());
		for (String item : list) {
			writeString(item);
		}
	}

	public void writeStringIntMap(Map<String, Integer> map) {
		if (CollectionUtils.isEmpty(map)) {
			writeBuffer.putShort((short) 0);
			return;
		}
		writeBuffer.putShort((short) map.size());
		for (Entry<String, Integer> entry : map.entrySet()) {
			writeString(entry.getKey());
			writeBuffer.putInt(entry.getValue());
		}
	}

	public void writeString(String value) {
		if (value == null || value.isEmpty()) {
			writeShort((short) 0);
			return;
		}

		byte data[] = value.getBytes(SocketConstant.CHARSET);
		short len = (short) data.length;
		writeBuffer.putShort(len);
		writeBuffer.put(data);
	}

	public void writeBigString(String value) {
		if (value == null || value.isEmpty()) {
			writeInt(0);
			return;
		}

		byte data[] = value.getBytes(SocketConstant.CHARSET);
		int len = data.length;
		writeBuffer.putInt(len);
		writeBuffer.put(data);
	}

	public void writeBytes(byte value[]) {
		writeBuffer.put(value);
	}
	/**
	 * 写入数组（前面带长度）
	 * @param value
	 */
	public void writeByteAarry(byte value[]) {
		writeBuffer.putInt(value.length);
		writeBuffer.put(value);
	}

	public void writeObject(Object object) {
		if (object instanceof Integer) {
			writeBuffer.putInt((int) object);
			return;
		}

		if (object instanceof Long) {
			writeBuffer.putLong((long) object);
			return;
		}

		if (object instanceof Short) {
			writeBuffer.putShort((short) object);
			return;
		}

		if (object instanceof Byte) {
			writeBuffer.put((byte) object);
			return;
		}

		if (object instanceof String) {
			String value = (String) object;
			writeString(value);
			return;
		}
		LOGGER.error("不可序列化的类型:" + object.getClass());
	}

	/**
	 * 返回buffer数组
	 * 
	 * @return
	 */
	public synchronized byte[] getBytes() {
		write();
		byte[] bytes = null;
		if (writeBuffer.limit() == 0) {
			bytes = writeBuffer.array();
		} else {
			bytes = new byte[writeBuffer.position()];
			writeBuffer.flip();
			writeBuffer.get(bytes);
		}
		writeBuffer.clear();

		return bytes;
	}
	
}
