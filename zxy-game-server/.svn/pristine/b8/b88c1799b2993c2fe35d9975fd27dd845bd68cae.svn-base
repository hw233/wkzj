import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.TimeUtils;


public class Test {
	private static Object o = new Object();
	public static void main(String[] args) {
//		List<Test1> list = new ArrayList<>();
//		for (int i = 1; i <= 10; i++) {
//			
//			Test1 test1 = new Test1();
//			test1.rank = i;
//			test1.id = i;
//			list.add(test1);
//		}
//		for (int i = 0; i < 10; i++) {
//			Thread t = new Thread(test(list));
//			t.start();
//		}
//		try {
//			TimeUnit.SECONDS.sleep(10);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		for (Test1 test1 : list) {
//			System.out.println(test1.id + ":" +test1.rank);
//		}
		
//		enum
//		{
//			GOODS = 0x0;
//			EQUIP = 0x1;
//			HERO  = 0x2;
//			UNKNOW = 0x3;
//		}
//		
		long hashId = 0L;
		int undefine = 0;
		int serverId = 0x3ff;
		int uid = 0;
		int sec = TimeUtils.getNow();
		
		
		assert(undefine >= 0 && undefine <= 0x03);
		assert(serverId >= 0 && serverId <= 0x3ff);
		assert(uid >= 0 && uid <= 0xfffff);
		
		
		/*
		assert((undefine & 0x03) == 0x03);
		assert((serverId & 0x3ff) == 0x3ff);
		assert((uid & 0xfffff) == 0xfffff);
		*/

		hashId = ((long)sec << 32);
		
		undefine &= 0x03;
		hashId |= (long)undefine << 30;
		
		serverId &= 0x3ff;
		hashId |= (long)serverId << 20;
		
		uid &= 0xfffff;
		hashId |= (long)uid;
		Long temp = hashId;
		System.out.println(Long.toHexString(temp));
		System.out.println(Long.toBinaryString(temp));
		
		for (int i = 0; i < 10; i++) {
			System.out.println(UUID.randomUUID().toString());
		}
	}
	
	
	public static void change(Test1 t1, Test1 t2) {
		ChainLock lock = LockUtils.getLock(t1, t2);
		lock.lock();
			int temp1 = t1.rank;
			int temp2 = t2.rank;
			t1.rank = temp2;
			t2.rank = temp1;
			lock.unlock();
	}
	
	public static Runnable test(final List<Test1> list){
		return new Runnable() {
			
			@Override
			public void run() {
				int i = 0;
				while(i <= 1000) {
					int index1 = RandomUtils.nextIntIndex(list.size());
					int index2 = RandomUtils.nextIntIndex(list.size());
					if (index1 != index2) {
						Test1 t1 = list.get(index1);
						Test1 t2 = list.get(index2);
						change(t1, t2);
					}
					i++;
				}
			}
		};
	}
	
	
	

}
class Test1{
	public int id;
	public int rank;
}