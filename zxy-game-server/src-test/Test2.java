import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;


public class Test2 {

	public static void main(String[] args) {
//		IdTableJdbc jdbc = (IdTableJdbc) SpringContext.getBean(IdTableJdbc.class);
//		long temp = jdbc.queryForLong("SELECT UNIX_TIMESTAMP()");
//		System.out.println(temp);
//		System.out.println(TimeUtils.getNow());;
//		DecimalFormat df = new DecimalFormat(".00");
//		df.f
//		int a = 2;
//		double b = 1000.0;
//		double c = a/b * 10.0 * 2.0;
//		System.out.println(c);
//		BigDecimal bigDecimal = new BigDecimal(70/1000.0f).setScale(2, BigDecimal.ROUND_DOWN);
//		System.out.println(bigDecimal);
//		BigDecimal bigDecimal1 = new BigDecimal(0.01);
//		BigDecimal bb = bigDecimal.add(bigDecimal1);
//		System.out.println(bb.setScale(2, BigDecimal.ROUND_DOWN));
//		System.out.println(0.05 + 0.01);
//	    System.out.println(1.0 - 0.42);
//	    System.out.println(4.015 * 100);
//	    System.out.println(123.3 / 100);
//		Test2 t = new Test2();
//		ChainLock lock = LockUtils.getLock(t);
//		ChainLock lock1 = LockUtils.getLock(t);
//		System.out.println(lock);
		test("aaa");
		
	}
	
	private static void test(String... args) {
		String result = "%s";
		result = String.format(result, args);
		System.out.println(result);
	}

}
