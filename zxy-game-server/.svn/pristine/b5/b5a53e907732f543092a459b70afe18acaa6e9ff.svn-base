package com.jtang.gameserver.module.notify.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.module.icon.model.IconVO;
import com.jtang.gameserver.module.notify.type.BattleResultType;
import com.jtang.gameserver.module.notify.type.NotifyType;
import com.jtang.gameserver.module.snatch.helper.SnatchHelper;

/**
 * 通知基础类
 * @author 0x737263
 *
 */
public abstract class AbstractNotifyVO extends IoBufferSerializer implements NotifyVO, Serializable {
	private static final long serialVersionUID = 7461205910574066970L;

	/**
	 * 消息类型  1.切磋 2.试炼洞  3.抢夺  4.抢夺给盟友的通知  5.合作关卡 6.加为盟友  7.删除盟友
	 */
	public int type;
	
	/**
	 * 自增加id
	 */
	public long nId;
	
	/**
	 * 此信息的所有者角色Id
	 */
	public long ownerActorId;
	
	/**
	 * 非所有者的等级
	 */
	public int actorLevel;
	
	/**
	 * 发送角色id
	 */
	public long fromActorId;
	
	/**
	 * 发送者仙人id
	 */
	public IconVO iconVO;
	
	/**
	 * 接收角色id
	 */
	public long toActorId;
	
	/**
	 * 战斗结果  1.胜利  2.失败
	 */
	public int battleResult;
	
	/**
	 * 发送时间
	 */
	public int sendTime;
	
	/**
	 * 是否已读
	 */
	public int isReaded;

	
	public void setAbstractVO(NotifyType type, long ownerActorId, int actorLevel, long fromActorId, long toActorId, BattleResultType battleResult) {
		this.type = type.getCode();
		this.ownerActorId = ownerActorId;
		this.actorLevel = actorLevel;
		this.fromActorId = fromActorId;
		this.toActorId = toActorId;
		this.battleResult = battleResult.getCode();
		this.sendTime = TimeUtils.getNow();
		this.isReaded = 0;
	}

	@Override
	public void write() {
		writeLong(nId);
		writeByte((byte) type);
		writeLong(fromActorId);
		writeBytes(iconVO.getBytes());
		writeLong(toActorId);
		writeInt(battleResult);
		writeInt(sendTime);
		writeString(getActorName());
		writeInt(actorLevel);
		writeByte((byte) isReaded);
		subClazzWrite();
	}
	
	protected abstract void subClazzWrite();
	
	/**
	 * 获取非拥有该邮件的角色名
	 * @return
	 */
	private String getActorName() {
		if (ownerActorId == fromActorId) {
			return SnatchHelper.getTargetActorName(toActorId);
		}
		return SnatchHelper.getTargetActorName(fromActorId);
	}
	
	@Override
	public void parse2VO(String[] items) {
		if (items == null || items.length < 1) {
			return;
		}
		
		this.type = Byte.valueOf(items[0]);
		this.nId = Long.valueOf(items[1]);
		this.ownerActorId = Long.valueOf(items[2]);
		this.fromActorId = Long.valueOf(items[3]);
		this.toActorId = Long.valueOf(items[4]);
		this.battleResult = Integer.valueOf(items[5]);
		this.sendTime = Integer.valueOf(items[6]);
		this.actorLevel = Integer.valueOf(items[7]);
		this.isReaded = Byte.valueOf(items[8]);
//		this.iconVO.icon = Integer.valueOf(items[9]);
//		this.iconVO.fram = Integer.valueOf(items[10]);
		//头像边框不存库

		int size = items.length - 9;
		if(size > 0) {
			String[] subItems = new String[size];
			System.arraycopy(items, 9, subItems, 0, subItems.length);
			subClazzString2VO(subItems);			
		}
	}
	
	/**
	 * 给子类属性赋值
	 * @param items
	 */
	protected abstract void subClazzString2VO(String[] items);
	
	@Override
	public String parse2String() {
		List<String> attributes = new ArrayList<>();
		attributes.add(String.valueOf(type));  //第一个为通知类型
		attributes.add(String.valueOf(nId));
		attributes.add(String.valueOf(ownerActorId));
		attributes.add(String.valueOf(fromActorId));
		attributes.add(String.valueOf(toActorId));
		attributes.add(String.valueOf(battleResult));
		attributes.add(String.valueOf(sendTime));
		attributes.add(String.valueOf(actorLevel));
		attributes.add(String.valueOf(isReaded));
//		attributes.add(String.valueOf(iconVO.icon));
//		attributes.add(String.valueOf(iconVO.fram));
		//头像边框不存库
		subClazzParse2String(attributes);
		return StringUtils.collection2SplitString(attributes, Splitable.ATTRIBUTE_SPLIT);
	}
	
	/**
	 * 添加字类属性
	 * @param attributes
	 */
	protected abstract void subClazzParse2String(List<String> attributes);
	
}
