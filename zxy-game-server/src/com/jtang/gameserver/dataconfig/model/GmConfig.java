package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName = "gmConfig")
public class GmConfig implements ModelAdapter {
	
	/**
	 * gm开始id
	 */
	public long fromActorId;
	
	/**
	 * gm结束id
	 */
	public long endActorId;

	@Override
	public void initialize() {
		
	}

}
