package com.jtang.gameserver.module.hero.handler.response;

import static com.jiatang.common.model.HeroVOAttributeKey.NONE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jiatang.common.model.HeroVOAttributeKey;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.model.AttributeChange;

/**
 * 推送仙人的属性变更
 * @author vinceruan
 *
 */
public class UpdateHeroResponse extends IoBufferSerializer {
	
	/**
	 * 仙人Id
	 */
	public int heroId;
	
	/**
	 * 变更的属性, AttributeChange的name参考 {@code HeroVOAttributeKey}
	 */
	public List<AttributeChange> list;
	
	
	public UpdateHeroResponse(int heroId, Map<HeroVOAttributeKey, Object> attrs) {
		this.heroId = heroId;
		list = new ArrayList<>();
		for (Entry<HeroVOAttributeKey, Object> entry : attrs.entrySet()) {
			list.add(AttributeChange.valueOf(entry.getKey(), entry.getValue()));
		}
	}

	@Override	
	public void write() {
		writeInt(heroId);
		writeShort((short)list.size());
		for (AttributeChange attr : list) {
			HeroVOAttributeKey attrKey = HeroVOAttributeKey.getByCode(attr.name);
			if (attrKey == NONE) continue;
			Object attrValue = attr.value;
			writeByte(attrKey.getCode());
			
			switch (attrKey.getDataType()) {
			case INT:
				writeInt((int) attrValue);
				break;
			case LONG:
				writeLong((long) attrValue);
				break;
			case SHORT:
				writeShort((short) attrValue);
				break;
			case BYTE:
				writeByte((byte) attrValue);
				break;
			case STRING:
				String value = (String) attrValue;
				writeString(value);
				break;
			case INT_LIST:
				@SuppressWarnings("unchecked")
				List<Integer> list = (List<Integer>)attrValue;
				writeIntList(list);
				break;
			default:
				throw new RuntimeException("不可序列化的类型:" + attrValue.getClass());
			}	
		}
	}
}
