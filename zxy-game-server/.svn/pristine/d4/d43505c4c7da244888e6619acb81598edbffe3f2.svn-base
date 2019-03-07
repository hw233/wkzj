import java.util.ArrayList;
import java.util.List;

import javaloader.ScriptInvoke;
import javaloader.ScriptItem;
import test.TestScript;

public class Launcher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//扫描某个指定的目录。获取每个文件的包包和类名 
		//实例化每个类，存储到ScriptInvoke集合中.
		//ScriptInvoke提供 根据id获取脚本实例对象
		//
		
		List<ScriptItem> list = new ArrayList<>();
		list.add(new ScriptItem(1001, "test.TestPrintScript", "C:\\TestPrintScript.java"));
		ScriptInvoke.init(list);
		
		TestScript script = (TestScript) ScriptInvoke.get(1001);
		if (script != null) {
			script.print();
		}
	}

}
