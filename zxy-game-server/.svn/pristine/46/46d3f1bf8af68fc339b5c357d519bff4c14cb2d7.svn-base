package com.jtang.gameserver.module.recruit.type;

public enum RecruitRandomType {
	SINGLE((byte)1),
	MUTI((byte)2);
	private final byte code;
	private RecruitRandomType(byte code) {
		this.code = code;
	}
	public static RecruitRandomType get(int type) {
		for(RecruitRandomType taskType : RecruitRandomType.values()){
			if(taskType.getCode() == type)
				return taskType;
		}
		return null;
	}
	
	public byte getCode() {
		return code;
	}
}
