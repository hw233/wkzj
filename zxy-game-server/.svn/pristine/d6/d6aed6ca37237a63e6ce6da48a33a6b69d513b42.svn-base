package com.jtang.gameserver.module.battle.model;

import static com.jiatang.common.model.AttackerAttributeKey.NONE;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.type.ActionType;


/**
 * buffer变化数据包
 * @author vinceruan
 *
 */
public class BufferAction extends IoBufferSerializer implements Action {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BufferAction.class);
	/**
	 * 仙人的全局ID
	 */
	public byte uid;
	
	/**
	 * 效果id
	 */
	public int effectId;
	
	/**
	 * 1:增加
	 * 2:跳动
	 * 3:移除
	 */
	public byte type;
	
	/**
	 * 属性变化
	 */
	List<AttributeChange> attributeList = new ArrayList<>();
	
	@Override
	public void write() {
		this.writeByte(getActionType().getType());
		this.writeByte(this.uid);
		this.writeInt(this.effectId);
		this.writeByte(this.type);
		this.writeAttributeChanges(this.attributeList);
	}
	
	/**
	 * 往仙人身上附加一个buffer
	 * @param uid
	 * @param effectId
	 * @param bufferType
	 * @return
	 */
	public static BufferAction addBuffer(byte uid, int effectId, AttributeChange attrChange) {
		BufferAction br = new BufferAction();
		br.uid = uid;
		br.effectId = effectId;	
		br.type = 1;
		if (attrChange != null) {
			br.attributeList.add(attrChange);
		}
		return br;
	}
	
	/**
	 * 从仙人身上移除一个buffer
	 * @param uid
	 * @param effectId
	 * @return
	 */
	public static BufferAction removeBuffer(byte uid, int effectId, AttributeChange attrChange) {
		BufferAction br = new BufferAction();
		br.uid = uid;
		br.effectId = effectId;
		br.type = 3;
		if (attrChange != null) {
			br.attributeList.add(attrChange);
		}
		return br;
	}
	
	/**
	 * buffer跳动
	 * @param uid
	 * @param effectId
	 * @param attrAction
	 * @return
	 */
	public static BufferAction bufferHeartBeat(byte uid, int effectId, AttributeChange attrChange) {
		BufferAction br = new BufferAction();
		br.uid = uid;
		br.effectId = effectId;
		br.type = 2;
		if (attrChange != null) {
			br.attributeList.add(attrChange);
		}
		return br;
	}
	
	public String format(String indentStr) {
		return String.format("%sBufferAction:hero【%s】,bufferId【%d】, type【%d】, attrs:【%s】\r\n", indentStr, uid, effectId, type, formatAttr());
	}
	
	public String formatAttr() {
		StringBuilder sb = new StringBuilder();
		for (AttributeChange attr : attributeList) {
			sb.append(attr.name).append("->").append(attr.value).append(";");
		}
		return sb.toString();
	}
	
	/**
	 * 写入一个移动数据包
	 * @param action
	 */
	private void writeAttributeChanges(List<AttributeChange> attrs) {
		writeShort((short)attrs.size());
		for (AttributeChange attr : attrs) {
			writeAttributeChange(attr);
		}		
	}
	
	
	/**
	 * 写入属性变更
	 */
	private void writeAttributeChange(AttributeChange attr) {
		AttackerAttributeKey attrKey = AttackerAttributeKey.getByCode(attr.name);
		if (attrKey == NONE) return;
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
		case POSITION:
			Tile pos = (Tile)attrValue;
			Tile tile = FightData.transform.get(pos.getX() + "-" + pos.getY());
			writeByte((byte) tile.getX());
			writeByte((byte) tile.getY());
			break;
		default:
			LOGGER.error("不可序列化的类型:" + attrValue.getClass());
		}
	}

	@Override
	public ActionType getActionType() {
		return ActionType.BUFFER_ACTION;
	}
}
