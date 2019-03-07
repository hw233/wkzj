package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

/**
 * 角色添加活力可超上限类型
 * @author jianglf
 *
 */
@DataFile(fileName="actorVitConfig")
public class ActorVitConfig implements ModelAdapter {

	/**
	 * 类型
	 */
	public int id;
	
	@Override
	public void initialize() {
		
	}

}
