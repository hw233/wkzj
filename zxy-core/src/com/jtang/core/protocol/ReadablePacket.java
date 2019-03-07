//package com.jtang.core.protocol;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.mina.core.buffer.IoBuffer;
//
//import com.jtang.core.mina.SocketConstant;
//
///**
// * 提供接口将基本数据类型转换成字节流,
// * 所有Response包中的发送类都要继承此类
// * @author vinceruan
// *
// */
//public abstract class ReadablePacket {
//
//	/**
//	 * 数字类型读取错误时默认返回值
//	 */
//	protected static byte DEFAULT_NUMBER_VALUE = 0;
//	
//	/**
//	 * 字符类型读取错误时默认返回值
//	 */
//	protected static String DEFAULT_STRING_VALUE = "";
//	
//	private static int BYTE_LEN = 1;
//	
//	private static int SHORT_LEN = 2;
//	
//	private static int INTEGER_LEN = 4;
//	
//	private static int LONG_LEN = 8;
//	
//	private static int FLOAT_LEN = 4;	
//	
//	private static int DOUBLE_LEN = 8;
//	/**
//	 * 读写处理iobuffer
//	 * IoBuffer.allocate(value.getLength());
//	 * buffer.setAutoExpand(true);    
//	 */
//	protected IoBuffer buffer;
//	
//	/**
//	 * 读
//	 * @param bytes
//	 */
//	public abstract void read(byte[] bytes);	
//	
//	protected void setIoBuffer(byte[] bytes) {
//		buffer = IoBuffer.allocate(bytes.length + 3);
//		buffer.put(bytes);
//		buffer.flip();
//	}
//	
//	
//	protected byte readByte() {
//		
//		if(calcCapacity() >= BYTE_LEN) {
//			return buffer.get();
//		}
//		return DEFAULT_NUMBER_VALUE;
//	}
//	
//	protected short readShort() {
//		if(calcCapacity() >= SHORT_LEN) {
//			return buffer.getShort();
//		}
//		return DEFAULT_NUMBER_VALUE;
//	}
//	
//	protected int readInt() {
//		if(calcCapacity() >= INTEGER_LEN) {
//			return buffer.getInt();
//		}
//		return DEFAULT_NUMBER_VALUE;
//	}
//	
//	protected long readLong() {
//		if(calcCapacity() >= LONG_LEN) {
//			return buffer.getLong();
//		}
//		return DEFAULT_NUMBER_VALUE;	
//	}
//	
//	protected float readFloat() {
//		if(calcCapacity() >= FLOAT_LEN) {
//			return buffer.getFloat();
//		}
//		return DEFAULT_NUMBER_VALUE;
//	}
//	
//	protected double readDouble() {
//		if(calcCapacity() >= DOUBLE_LEN) {
//			return buffer.getDouble();
//		}
//		return DEFAULT_NUMBER_VALUE;
//	}
//	
//	protected String readString() {
//		if(calcCapacity() < SHORT_LEN) {
//			return DEFAULT_STRING_VALUE;	
//		}
//	
//		int size = buffer.getShort();
//		if(calcCapacity() < size) {
//			return DEFAULT_STRING_VALUE;
//		}
//		
//		byte[] bytes = new byte[size];
//		buffer.get(bytes);
//		
//		return new String(bytes, SocketConstant.CHARSET);
//	}
//	protected String readBigString() {
//		if(calcCapacity() < INTEGER_LEN) {
//			return DEFAULT_STRING_VALUE;	
//		}
//		
//		int size = buffer.getInt();
//		if(calcCapacity() < size) {
//			return DEFAULT_STRING_VALUE;
//		}
//		
//		byte[] bytes = new byte[size];
//		buffer.get(bytes);
//		
//		return new String(bytes, SocketConstant.CHARSET);
//	}
//	
//	protected List<Integer> readIntegerList() {
//		List<Integer> list = new ArrayList<Integer>();
//		if (calcCapacity() > SHORT_LEN) {
//			int size = buffer.getShort();
//			for (int i = 0; i < size; i++) {
//				list.add(this.readInt());
//			}
//		}
//		return list;
//	}
//	
//	protected List<Long> readLongList() {
//		List<Long> list = new ArrayList<Long>();
//		if (calcCapacity() > SHORT_LEN) {
//			int size = buffer.getShort();
//			for (int i = 0; i < size; i++) {
//				list.add(this.readLong());
//			}
//		}
//		return list;
//	}
//	
//	protected List<String> readStringList() {
//		List<String> list = new ArrayList<>();
//		if (calcCapacity() > SHORT_LEN) {
//			int size = buffer.getShort();
//			for (int i = 0; i < size; i++) {
//				list.add(this.readString());
//			}
//		}
//		return list;
//	}
//	
//	/**
//	 * 计算ByteBuffer的容量
//	 * @return
//	 */
//	protected int calcCapacity() {
//		if (buffer == null) {
//			return 0;
//		}
//		return buffer.limit() - buffer.position();
//	}
//	
//}
