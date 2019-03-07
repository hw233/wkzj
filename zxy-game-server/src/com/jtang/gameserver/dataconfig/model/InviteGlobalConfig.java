package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName="inviteGlobalConfig")
public class InviteGlobalConfig implements ModelAdapter {

	/**
	 * 开放等级
	 */
	public int openLevel;
	
	/**
	 * 邀请码使用最低等级
	 */
	public int minInviteLevel;
	
	/**
	 * 重置邀请人花费
	 */
	public int resetCost;
	
	@Override
	public void initialize() {
		
	}

}
