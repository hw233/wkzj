package com.jtang.gameserver.module.battle.model;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jiatang.common.model.HeroVOAttributeKey;

/**
 * 记录仙人的属性变化。
 * @author vinceruan
 *
 */
public class AttributeChange {
	/**
	 * 属性
	 */
	public byte name;

	/**
	 * 属性值(变化后的值)
	 */
	public Object value;

	public static AttributeChange valueOf(AttackerAttributeKey key, Object value) {
		AttributeChange attr = new AttributeChange();
		attr.name = key.getCode();
		Object val = null;
		switch (key.getDataType()) {
		case INT:
			val = Integer.valueOf(value.toString());
			break;
		case LONG:
			val = Long.valueOf(value.toString());
			break;
		case SHORT:
			val = Short.valueOf(value.toString());
			break;
		case BYTE:
			val = Byte.valueOf(value.toString());
			break;
		default:
			val = value;
			break;
		}
		attr.value = val;
		return attr;
	}
	
	public static AttributeChange valueOf(HeroVOAttributeKey key, Object value) {
		AttributeChange attr = new AttributeChange();
		attr.name = key.getCode();
		Object val = null;
		switch (key.getDataType()) {
		case INT:
			val = Integer.valueOf(value.toString());
			break;
		case LONG:
			val = Long.valueOf(value.toString());
			break;
		case SHORT:
			val = Short.valueOf(value.toString());
			break;
		case BYTE:
			val = Byte.valueOf(value.toString());
			break;
		default:
			val = value;
			break;
		}
		attr.value = val;
		return attr;
	}
	
	public String toString() {
		return String.format("%d->%s", name, value);
	}
}
