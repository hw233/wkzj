package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName = "lineupUnlockConfig")
public class LineupUnlockConfig implements ModelAdapter {
	private int index;
	private int needActorLevel;
	private int needTick;
	private boolean autoUnlock;
	
	@Override
	public void initialize() {
		
	}

	public int getIndex() {
		return index;
	}

	public int getNeedActorLevel() {
		return needActorLevel;
	}
	
	public int getNeedTick() {
		return needTick;
	}

	public boolean isAutoUnlock() {
		return autoUnlock;
	}
}
