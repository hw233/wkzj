package com.jtang.gameserver.module.refine.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.refine.model.RefineResult;

public class RefineEquipResponse extends IoBufferSerializer {

	/**
	 * 精炼的属性，查看{@code EquipAttributeKey}对应的值
	 */
	public int key;
	
	/**
	 * 精炼附加值
	 */
	public int addVal;
	
	
	public RefineEquipResponse(TResult<RefineResult> result) {
		this.key = result.item.key.getCode();
		this.addVal = result.item.addVal;
	}

	@Override
	public void write() {
		writeInt(key);
		writeInt(addVal);
	}

}
