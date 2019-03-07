package com.jtang.gameserver.module.recruit.type;


public enum RecruitType {
	SMALL((byte)1),
	BIG((byte)2);
	private final byte code;
	private RecruitType(byte code) {
		this.code = code;
	}
	public static RecruitType get(int type) {
		for(RecruitType taskType : RecruitType.values()){
			if(taskType.getCode() == type)
				return taskType;
		}
		return null;
	}
	
	public byte getCode() {
		return code;
	}
}
