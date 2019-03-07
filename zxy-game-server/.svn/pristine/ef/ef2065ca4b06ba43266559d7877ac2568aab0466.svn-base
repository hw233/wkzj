package javaloader;

import com.jtang.core.context.SpringContext;
import com.jtang.core.utility.HttpUtils;

public class HttpTest {

public static void main(String[] args) {
	Integer maxTotal = (Integer) SpringContext.getBean("httputil.maxtotal");
	System.out.println(maxTotal);
	String result = HttpUtils.sendGet("http://pay.mumayi.com/user/index/validation?uid=123456&token=abcde");
	System.out.println(result);
	}
}
