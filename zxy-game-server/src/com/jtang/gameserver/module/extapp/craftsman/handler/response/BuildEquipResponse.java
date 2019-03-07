package com.jtang.gameserver.module.extapp.craftsman.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;

public class BuildEquipResponse extends IoBufferSerializer {
	
	public byte isSuccess;

	public int buildNum;

	public List<RewardObject> resultList = new ArrayList<RewardObject>();
	
	public BuildEquipResponse(boolean isSuccess, int buildNum, List<RewardObject> resultList){
		this.isSuccess = isSuccess? (byte)1 : (byte)0;
		this.buildNum = buildNum;
		this.resultList = resultList;
	}
	
	@Override
	public void write() {
		writeByte(isSuccess);
		writeInt(buildNum);
		writeShort((short)resultList.size());
		for (RewardObject rewardObject : resultList) {
			writeBytes(rewardObject.getBytes());
		}
	}
	
}
