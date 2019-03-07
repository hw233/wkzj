package com.jtang.gameserver.module.adventures.favor.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

/**
 * 福神眷顾中的特权VO，表示界面中的三种特权，分别用1、2、3表示
 * @author pengzy
 *
 */
public class PrivilegeVO extends IoBufferSerializer implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -879606308005266375L;

	/**
	 * 特权Id，分别为1、2、3
	 */
	private int privilegeId;
	
	/**
	 * 已使用次数
	 */
	private int usedNum;
	
	public static PrivilegeVO valueOf(String[] privilege) {
		PrivilegeVO privilegeVO = new PrivilegeVO();
		privilegeVO.privilegeId = Integer.valueOf(privilege[0]);
		privilegeVO.usedNum = Integer.valueOf(privilege[1]);
		return privilegeVO;
	}
	
	
	public String parseToString() {
		List<String> list = new ArrayList<>();
		list.add(String.valueOf(privilegeId));
		list.add(String.valueOf(usedNum));
		return StringUtils.collection2SplitString(list, Splitable.ATTRIBUTE_SPLIT);
	}
	
	public int getPrivilegeId() {
		return privilegeId;
	}
	
	public void setPrivilegeId(int privilegeId) {
		this.privilegeId = privilegeId;
	}
	
	public int getUsedNum() {
		return usedNum;
	}
	
	public void setUsedNum(int usedNum) {
		this.usedNum = usedNum;
	}
	
	public static PrivilegeVO valueOf(int privilegeId) {
		PrivilegeVO privilegeVO = new PrivilegeVO();
		privilegeVO.privilegeId = privilegeId;
		privilegeVO.usedNum = 0;
		return privilegeVO;
	}
	public static PrivilegeVO valueOf(int privilegeId, int useNum) {
		PrivilegeVO privilegeVO = new PrivilegeVO();
		privilegeVO.privilegeId = privilegeId;
		privilegeVO.usedNum = useNum;
		return privilegeVO;
	}


	@Override
	public void write() {
		this.writeInt(this.privilegeId);
		this.writeInt(this.usedNum);
	}

}
