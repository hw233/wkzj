package com.jtang.gameserver.module.crossbattle.model;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;

public class World2GameForward extends IoBufferSerializer {
	private Object[] forwardObject;

	public World2GameForward(Object... forwardObject) {
		super();
		this.forwardObject = forwardObject;
	}
	
	@Override
	public void write() {
		for (Object obj : forwardObject) {
			if (obj instanceof IoBufferSerializer){
				this.writeBytes(((IoBufferSerializer) obj).getBytes());
			}  else if (obj instanceof List){
				List<?> list =  (List<?>) obj;
				this.writeShort((short) list.size());
				for (Object object : list) {
					if (object instanceof IoBufferSerializer) {
						this.writeBytes(((IoBufferSerializer) object).getBytes());
					} else {
						writeObject(object);
					}
				}
			} else {
				writeObject(obj);
				
			}
		}
	}

}
