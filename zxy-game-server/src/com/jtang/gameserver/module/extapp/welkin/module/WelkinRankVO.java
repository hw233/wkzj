package com.jtang.gameserver.module.extapp.welkin.module;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.Splitable;

public class WelkinRankVO extends IoBufferSerializer {

	/**
	 * acotrId
	 */
	public long actorId;
	/**
	 * 名字
	 */
	public String name;
	/**
	 * 等级
	 */
	public int level;
	/**
	 * vip等级
	 */
	public int vipLevel;
	/**
	 * 探物次数
	 */
	public int useNum;
	/**
	 * 名次
	 */
	public int rank;
	
	@Override
	public void write() {
		writeLong(actorId);
		writeString(name);
		writeInt(level);
		writeInt(vipLevel);
		writeInt(useNum);
		writeInt(rank);
	}
	
	public String parseToString(){
		StringBuffer sb = new StringBuffer();
		sb.append(actorId);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(name);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(level);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(vipLevel);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(useNum);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(rank);
		return sb.toString();
	}
}
