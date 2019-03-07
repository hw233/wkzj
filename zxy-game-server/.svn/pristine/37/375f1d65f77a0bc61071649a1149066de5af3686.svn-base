package com.jtang.gameserver.module.test.handler.response;

import java.util.List;
import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;

public class DataTypeResponse extends IoBufferSerializer {
	
	public byte byteValue;
	
	public short shortValue;
	
	public int intValue;
	
	public long longValue;
	
	public float floatValue;
	
	public String stringValue;
	
	public List<Integer> list;
	
	public Map<Integer,Integer> mapInt;
	
	public Map<String,String> mapString;

	@Override
	public void write() {
//		setIoBuffer(15, true);

		writeByte(this.byteValue);
		writeShort(this.shortValue);
		writeInt(this.intValue);
		writeLong(this.longValue);
		writeFloat(this.floatValue);
		writeString(this.stringValue);

		writeShort((short)this.list.size());
		for(Integer i : list) {
			writeInt(i);
		}
		
		writeShort((short)this.mapInt.size());
		for(Integer i : mapInt.keySet()) {
			writeInt(i);
			writeInt(mapInt.get(i));
		}
		
		writeShort((short)this.mapString.size());
		for(String i : mapString.keySet()) {
			writeString(i);
			writeString(mapString.get(i));
		}
	}

}
