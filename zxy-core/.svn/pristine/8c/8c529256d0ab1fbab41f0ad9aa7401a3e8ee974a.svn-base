package com.jtang.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

/**
 * 奖励对象
 * @author 0x737263
 *
 */
public class RewardObject extends IoBufferSerializer implements Serializable {
	private static final long serialVersionUID = 1661501554146668392L;

	/**
	 * 奖励类型
	 */
	public RewardType rewardType;

	/**
	 * 奖励id(根据类型来匹配)
	 */
	public int id;

	/**
	 * 奖励数量
	 */
	public int num;
	
	public RewardObject() {
	}

	public RewardObject(RewardType rewardType, int id, int num) {
		this.rewardType = rewardType;
		this.id = id;
		this.num = num;
	}
	
	public static RewardObject valueOf(String[] record) {
		record = StringUtils.fillStringArray(record, 3, "0");
		RewardObject rewardObject = new RewardObject();
		rewardObject.rewardType = RewardType.getType(Integer.valueOf(record[0]));
		rewardObject.id = Integer.valueOf(record[1]);
		rewardObject.num = Integer.valueOf(record[2]);
		return rewardObject;
	}
	
	public String parse2String() {
		return parse2String(Splitable.ATTRIBUTE_SPLIT);
	}
	
	public String parse2String(String splitable) {
		List<Object> list = new ArrayList<Object>();
		list.add(rewardType.getCode());
		list.add(id);
		list.add(num);
		return StringUtils.collection2SplitString(list, splitable);
	}

	@Override
	public void write() {
		writeInt(rewardType.getCode());
		writeInt(id);
		writeInt(num);
	}
	
	@Override
	public String toString() {
		return parse2String();
	}
	
	@Override
	public void readBuffer(IoBufferSerializer buffer) {
		this.rewardType = RewardType.getType(buffer.readInt());
		this.id = buffer.readInt();
		this.num = buffer.readInt();
	}
	
}
