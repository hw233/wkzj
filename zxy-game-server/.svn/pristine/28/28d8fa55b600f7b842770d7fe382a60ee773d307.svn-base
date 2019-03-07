package com.jtang.gameserver.module.delve.handler.response;

import java.util.Map;
import java.util.Set;

import com.jiatang.common.model.HeroVOAttributeKey;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.delve.model.DoDelveResult;
/**
 * 潜修结果返回消息
 * @author ludd
 *
 */
public class DoDelveResponse extends IoBufferSerializer {

	/**
	 * 仙人ID
	 */
	private int heroId;
	/**
	 * <pre>
	 * 属性提升结果key:属性值 {@code HeroVOAttributeKey}, value:属性结果 {@code DoDelveResult}
	 * <pre>
	 */
	private Map<HeroVOAttributeKey, DoDelveResult> data;
	
	
	
	public DoDelveResponse(int heroId, Map<HeroVOAttributeKey, DoDelveResult> data) {
		super();
		this.heroId = heroId;
		this.data = data;
	}
	@Override
	public void write() {
		this.writeInt(heroId);
		Set<HeroVOAttributeKey> keys = data.keySet();
		this.writeShort((short)keys.size());
		
		for (HeroVOAttributeKey heroVOAttributeKey : keys) {
			DoDelveResult doDelveResult = data.get(heroVOAttributeKey);
			this.writeByte(heroVOAttributeKey.getCode());
			this.writeBytes(doDelveResult.getBytes());
		}
	}
	

}
