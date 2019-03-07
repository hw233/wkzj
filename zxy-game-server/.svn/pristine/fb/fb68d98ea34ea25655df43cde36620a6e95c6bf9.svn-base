package com.jtang.gameserver.module.test.handler.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;

public class DataTypeRequest extends IoBufferSerializer {
	
	public byte byteValue;
	
	public short shortValue;
	
	public int intValue;
	
	public long longValue;
	
	public float floatValue;
	
	public String stringValue;
	
	public List<Integer> list;
	
	public Map<Integer,Integer> mapInt;
	
	public Map<String,String> mapString;

	public DataTypeRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {		
		this.byteValue = readByte();
		this.shortValue = readShort();
		this.intValue = readInt(); 
		this.longValue = readLong();
		this.floatValue = readFloat();
		this.stringValue = readString();
		
		this.list = new ArrayList<Integer>();
		short len = readShort();
		for (short i = 0; i < len; i++) {
			list.add(readInt());
		}
		
		this.mapInt = new HashMap<Integer, Integer>();
		len = readShort();
		for(short i=0;i< len;i++) {
			mapInt.put(readInt(), readInt());
		}
		
		this.mapString =  new HashMap<String,String>();
		len = readShort();
		String key,value;
		for(short i=0;i< len;i++) {
			key = readString();
			value = readString();	
			mapString.put(key, value);
		}

	}
	
	public static DataTypeRequest valueOf(byte[] bytes) {
		DataTypeRequest request = new DataTypeRequest(bytes);
		return request;
	}
}
