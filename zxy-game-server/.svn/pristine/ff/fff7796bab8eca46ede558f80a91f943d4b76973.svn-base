package com.jtang.gameserver.module.ally.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.icon.model.IconVO;

/**
 * 单个盟友相关的数据对象
 * 
 * @author pengzy
 * 
 */
public class AllyVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -483925042098630987L;

	/**
	 * 盟友ID
	 */
	public long actorId;
	
	/**
	 * 盟友名字
	 */
	public String name;

	/**
	 * 盟友等级
	 */
	public int level;
	
	/**
	 * 是否在线，0不在线，1在线
	 */
	public int isOnline;

	/**
	 * 最新切磋时间,用以判断切磋次数或记录被从盟友列表移出的时间, 以便再次被加为盟友的时候判断是否过了预期的时间,并可作为移除本数据的依据
	 */
	public int fightTime;
	
	/**
	 * 记录每天的切磋次数
	 */
	public int fightNum;
	
	/**
	 * 角色的失败数，非盟友
	 */
	public int failNum;
	
	/**
	 * 角色的胜利数，非盟友
	 */
	public int winNum;
	
	/**
	 * vip等级
	 */
	public int vipLevel;
	
	/**
	 * 头像和边框
	 */
	public IconVO iconVO;
	
	public static AllyVO valueOf(long actorId, String name, int level, int isOnline,int vipLevel,IconVO iconVO) {
		AllyVO allyVO = new AllyVO();
		allyVO.actorId = actorId;
		allyVO.name = name;
		allyVO.level = level;
		allyVO.isOnline = isOnline;
		allyVO.fightTime = 0;
		allyVO.fightNum = 0;
		allyVO.failNum = 0;
		allyVO.winNum = 0;
		allyVO.vipLevel = vipLevel;
		allyVO.iconVO = iconVO;
		return allyVO;
	}

	public String parseToString() {
		List<Object> attributeList = new ArrayList<Object>();
		attributeList.add(actorId);
		attributeList.add(fightTime);
		attributeList.add(fightNum);
		attributeList.add(failNum);
		attributeList.add(winNum);
		return StringUtils.collection2SplitString(attributeList, Splitable.ATTRIBUTE_SPLIT);
	}

	public static AllyVO valueOf(String[] voString) {
		AllyVO allyVO = new AllyVO();
		allyVO.actorId = Long.valueOf(voString[0]);
		allyVO.fightTime = Integer.valueOf(voString[1]);
		allyVO.fightNum = Integer.valueOf(voString[2]);
		allyVO.failNum = Integer.valueOf(voString[3]);
		allyVO.winNum = Integer.valueOf(voString[4]);
		return allyVO;
	}
	
	/**
	 * 写入包数据
	 * @param packet
	 */
	public void writePacket(IoBufferSerializer packet) {
		packet.writeLong(this.actorId);
		packet.writeString(this.name);
		packet.writeInt(this.level);
		packet.writeByte((byte)this.isOnline);
		packet.writeInt(this.fightTime);
		packet.writeInt(this.fightNum);
		packet.writeInt(this.failNum);
		packet.writeInt(this.winNum);
		packet.writeInt(this.vipLevel);
		packet.writeBytes(this.iconVO.getBytes());
	}

}
