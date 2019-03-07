package com.jtang.gameserver.module.equipdevelop.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 装备、装备碎片提炼请求参数
 * @author hezh
 *
 */
public class EquipConvertRequest extends IoBufferSerializer {

	/** 类型：1-装备碎片；；2-装备*/
	private int type; 
	
	/** 装备uuid*/
	private long uuid;
	
	/** (装备、碎片)配置id*/
	private int configId;
	
	/** 数量*/
	private int num;
	
	public EquipConvertRequest(byte[] bytes) {
		super(bytes);
	}
	
	public EquipConvertRequest(){
		
	}
	
	@Override
	public void read() {
		type = this.readInt();
		uuid = this.readLong();
		configId = this.readInt();
		num = this.readInt();
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the uuid
	 */
	public long getUuid() {
		return uuid;
	}

	/**
	 * @return the configId
	 */
	public int getConfigId() {
		return configId;
	}
	
	/**
	 * @return the num
	 */
	public int getNum() {
		return num;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}


	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}


	/**
	 * @param configId the configId to set
	 */
	public void setConfigId(int configId) {
		this.configId = configId;
	}


	/**
	 * @param num the num to set
	 */
	public void setNum(int num) {
		this.num = num;
	}
	
	
}
