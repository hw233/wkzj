package test;

import com.jtang.core.context.SpringContext;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.love.facade.LoveFacade;
import com.jtang.gameserver.module.love.handler.response.LoveInfoResponse;

public class LoveTest {

	public static void main(String[] args) {
		LoveFacade loveFacade = (LoveFacade) SpringContext.getBean(LoveFacade.class);
		LoveInfoResponse info = loveFacade.getLoveInfo(754975761L);
		Result result = loveFacade.marry(754975761L, 754975762L);
		TResult<LoveInfoResponse> result1 = loveFacade.acceptMarry(754975762L, (byte)1, 754975761L);
		System.out.println(info);
		System.out.println(result.statusCode);
		System.out.println(result1.statusCode);
//		Result result = loveFacade.giveGift(754975761L);
//		Result result = loveFacade.acceptGift(754975762L);
//	    TResult<LoveInfoResponse> result = loveFacade.unMarry(754975761L);
//		System.out.println(result.statusCode);
	}

}
