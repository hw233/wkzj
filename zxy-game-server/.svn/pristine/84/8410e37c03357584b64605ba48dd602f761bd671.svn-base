package javaloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * java文件脚本调用类
 * @author 0x737263
 *
 */
public class ScriptInvoke {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScriptInvoke.class);
	
	private static JavaClassLoader classLoader = new JavaClassLoader();
	
	/**
	 * 已加载脚本集合
	 */
	private Map<Integer, Script> SCRIPT_MAPS = new HashMap<>();
	
	/**
	 * singleton
	 */
	private static ScriptInvoke scriptInvoke;
	
	private static byte[] objectLock = new byte[1];

	private ScriptInvoke() {
	}
	
	public static ScriptInvoke getInstance() {
		synchronized (objectLock) {
			if (scriptInvoke == null) {
				scriptInvoke = new ScriptInvoke();
			}
		}
		return scriptInvoke;
	}
	
	public static void init(List<ScriptItem> scriptList) {
		File file = new File("bin");
		if (!file.exists()) {
			file.mkdir();
		}

		for (ScriptItem item : scriptList) {
			String fileString = getInstance().getFileString(item.getFilePath());
			try {
				Class<?> clazz = classLoader.javaCode2Clazz(item.getClassName(), fileString);
				if (clazz != null) {
					Script o = (Script) clazz.newInstance();
					getInstance().SCRIPT_MAPS.put(o.getId(), o);
				}
			} catch (Exception e) {
				LOGGER.error("{}", e);
			}
		}
	}
	
	private String getFileString(String filePath) {
		InputStream in = null;
		try {
			in = new FileInputStream(filePath);
			StringBuffer fileString = new StringBuffer();

			IoBuffer buffer = IoBuffer.allocate(10240);
			buffer.setAutoExpand(true);
			buffer.setAutoShrink(true);

			byte[] bytes = new byte[1024];
			int len = 0;
			while ((len = in.read(bytes)) != -1) {
				buffer.put(bytes, 0, len);
			}
			buffer.flip();

			byte[] allbytes = new byte[buffer.remaining()];
			buffer.get(allbytes);
			fileString.append(new String(allbytes, "UTF-8"));

			return fileString.toString();
		} catch (Exception ex) {
			return "";
		} finally {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
	}
	
	
	public static Script get(int scriptId) {
		return getInstance().SCRIPT_MAPS.get(scriptId);
	}
	
}
