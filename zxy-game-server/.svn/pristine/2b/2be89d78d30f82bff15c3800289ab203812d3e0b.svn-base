package com.jtang.gameserver.module.battle.model;

import static com.jiatang.common.model.AttackerAttributeKey.NONE;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.battle.type.ActionType;


/**
 * 技能释放数据包
 * @author vinceruan
 * 
 */
public class SkillAction extends IoBufferSerializer implements Action {

	private static final Logger LOGGER = LoggerFactory.getLogger(SkillAction.class);
	/**
	 * 攻击发起人(或技能释放者)的uid
	 * key :uid
	 * value:y正方向是0，逆时针增加0-7；
	 */
	public List<Byte> attackerUids = new ArrayList<>();

//	/**
//	 * 攻击者方向
//	 * 0：不转身，1：转身
//	 */
//	public List<Byte> directions = new ArrayList<>();
	/**
	 * 技能id,普通攻击用0表示
	 */
	public int skillId;
		
	/**
	 * 技能目标以及血量值
	 */	
	public List<SkillTarget> targetList = new ArrayList<>();
	
	/**
	 * 技能引起的buffer
	 */
	public List<Action> triggerActions = new ArrayList<>();
	/**
	 * 距离0：技能 1：近程普攻 2：远程普攻
	 */
	public byte distance;
	

	public static SkillAction valueOf(List<Byte> attackerUids, int skillId, List<SkillTarget> targetList, List<Action> triggerActions, byte distance) {
		SkillAction atkRes = new SkillAction();
		atkRes.attackerUids = attackerUids; 
//		atkRes.directions = dires;
		atkRes.skillId = skillId;
		atkRes.targetList = targetList;
		atkRes.triggerActions = triggerActions;
		atkRes.distance = distance;
		return atkRes;
	}
	
	public String format(String indentStr) {
		StringBuilder sd = new StringBuilder();
		StringBuilder str = new StringBuilder();
		for (SkillTarget tar : targetList) {
			str.append(tar.targetId).append(":");
			for (AttributeChange ch : tar.attributeList) {
				str.append(ch.name).append("->").append(ch.value.toString()).append(";");
			}
		}
		StringBuilder triBuffs = new StringBuilder();
		for (Action act : triggerActions) {
			triBuffs.append(act.format("")).append("\r\n");
		}
		String msg = String.format("%s%s:caster【%s】,skill【%d】,targets:【%s】, triggerActions:【%s】", indentStr, getClass().getSimpleName(), StringUtils.collection2SplitString(attackerUids, ","), skillId, str.toString(), triBuffs.toString());
		sd.append(msg);		
		sd.append("\r\n");
		return sd.toString();
	}
	
	public boolean isEmpty() {
		return this.targetList.isEmpty();
	}
	
	@Override
	public ActionType getActionType() {
		return ActionType.SKILL_ACTION;
	}
	
	@Override
	public void write() {
		writeByte(getActionType().getType());
//		writeShort((short) this.attackerUids.size());
//		for (Map.Entry<Long, Byte> uid : this.attackerUids.entrySet()) {
//			writeLong(uid.getKey());
//			writeByte(uid.getValue());
//		}
		writeByteList(this.attackerUids);
		writeInt(skillId);
		writeShort((short)targetList.size());
		for (SkillTarget tar : targetList) {
			writeByte(tar.targetId);
			writeAttributeChanges(tar.attributeList);
		}
		writeShort((short)triggerActions.size());
		for (Action a : triggerActions) {
			this.writeBytes(a.getBytes());
		}
		writeByte(this.distance);
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
}
