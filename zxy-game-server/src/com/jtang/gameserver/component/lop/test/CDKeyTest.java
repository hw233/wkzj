package com.jtang.gameserver.component.lop.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jtang.core.context.SpringContext;
import com.jtang.core.lop.LopClient;
import com.jtang.core.lop.result.ListLopResult;
import com.jtang.gameserver.component.lop.request.CdkeyLOPRequest;
import com.jtang.gameserver.component.lop.response.CdkeyLOPResponse;

public class CDKeyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		LopClient client = (LopClient) SpringContext.getBean(LopClient.class);

		CdkeyLOPRequest request = new CdkeyLOPRequest();
		request.channelId = 999;
		request.cdKey = "4A19BFE7867F";
		request.platformId = 11;
		request.uid = "uid1111";
		request.serverId = 15;
		request.actorId = 9991;

		
		//ListLopResult<CdkeyLOPResponse> result = client.executeListResult(request, CdkeyLOPResponse.class);
		
		String json = client.execute(request);
		ListLopResult<CdkeyLOPResponse> result = JSON.parseObject(json, new TypeReference<ListLopResult<CdkeyLOPResponse>>() {
		});
		
		System.out.println(result.message);
		for (CdkeyLOPResponse rsp : result.item) {
			System.out.println(String.format("goodsId:%s  goodName:%s", rsp.goodsId, rsp.goodsNum));
		}
		
	}

}
