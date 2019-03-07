package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

/**
 * js脚本配置
 * @author 0x737263
 *
 */
@DataFile(fileName = "scriptConfig")
public class ScriptConfig implements ModelAdapter {

	/**
	 * 配置id
	 */
	private int id;
	
	/**
	 * js文件名
	 * 路径是:resource/script/
	 */
	private String file;
	
	@Override
	public void initialize() {
	}

	public int getId() {
		return id;
	}

	public String getFile() {
		return file;
	}

}
