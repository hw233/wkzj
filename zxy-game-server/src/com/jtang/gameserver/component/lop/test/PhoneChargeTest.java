package com.jtang.gameserver.component.lop.test;

import com.jtang.core.context.SpringContext;
import com.jtang.core.lop.LopClient;
import com.jtang.core.lop.result.LopResult;
import com.jtang.gameserver.component.lop.request.GivePhoneChargeRequest;

public class PhoneChargeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		LopClient client = (LopClient) SpringContext.getBean(LopClient.class);

		GivePhoneChargeRequest request = new GivePhoneChargeRequest();
		request.channelId = 10;
		request.platformId = 11;
		request.uid = "6095";
		request.serverId = 21;
		request.actorId = 88080389;
		request.mobilenum = "13456789012";
		request.actorName = "player1";
		request.actId = 1013L;

		LopResult lopResult = client.executeResult(request);
		System.out.println(lopResult.message);
		System.out.println(lopResult.successed);
	}

}
