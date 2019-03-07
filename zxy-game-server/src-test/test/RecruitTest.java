package test;

import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.jtang.core.context.SpringContext;
import com.jtang.core.model.RewardObject;
import com.jtang.core.result.TResult;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.module.recruit.facade.RecruitFacade;
import com.jtang.gameserver.module.recruit.facade.impl.RecruitFacadeImpl;

public class RecruitTest {

	public static void main(String[] args) {
//		RecruitFacade recruitFacade = (RecruitFacade) SpringContext.getBean(RecruitFacadeImpl.class);
//		for (int i = 0; i < 6; i++) {
//			
//			TResult<RewardObject> result = recruitFacade.singleRecruit(42010149033L, (byte)1);
//			System.out.println(result.statusCode);
//			System.out.println(result.item.id);
//		}
		byte[] a = new  byte[10240];
		byte[] b = new  byte[10240];
		for (int i = 0; i < a.length; i++) {
			a[i] = (byte) RandomUtils.nextInt(0, 127);
		}
		long start = System.currentTimeMillis();
			Deflater deflater = new Deflater();
			deflater.setInput(a);
			deflater.finish();
			int len =deflater.deflate(b);
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		System.out.println(len);
		
	}
}
