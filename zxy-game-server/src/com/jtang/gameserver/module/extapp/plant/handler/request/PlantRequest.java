package com.jtang.gameserver.module.extapp.plant.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class PlantRequest extends IoBufferSerializer {

	/**
	 * 种植的id
	 */
	public int plantId;

	public PlantRequest(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void read() {
		this.plantId = readInt();
	}

}
