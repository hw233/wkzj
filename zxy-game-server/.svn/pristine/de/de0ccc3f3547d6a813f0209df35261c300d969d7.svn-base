package com.jtang.gameserver.module.recruit.handler.response;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 随机招募仙人响应
 * @author 0x737263
 *
 */
public class RandRecruitResponse extends IoBufferSerializer {

	/**
	 * 聚仙信息
	 */
	private GetInfoResponse getInfoResponse;
	/**
	 * 结果
	 */
	private List<RewardObject> result;
	





	public RandRecruitResponse(GetInfoResponse getInfoResponse,
			List<RewardObject> result) {
		super();
		this.getInfoResponse = getInfoResponse;
		this.result = result;
	}






	@Override
	public void write() {
		
		this.writeBytes(this.getInfoResponse.getBytes());
		
		this.writeShort((short) result.size());
		for (RewardObject rewardObject : result) {
			this.writeBytes(rewardObject.getBytes());
		}
	}

}
