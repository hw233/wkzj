package com.jtang.gameserver.module.dailytask.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

public class DailyTaskVO extends IoBufferSerializer {
	/**
	 * 任务id
	 */
	private int taskId;
	/**
	 * 是否领取 1：领取 0：未领取
	 */
	private byte isGet;
	/**
	 * 进度
	 */
	private int progress;
	/**
	 * 是否完成
	 */
	private byte complte;
	
	public DailyTaskVO(String[] strs) {
		this.taskId = Integer.valueOf(strs[0]);
		this.isGet = Byte.valueOf(strs[1]);
		this.progress = Integer.valueOf(strs[2]);
		this.complte = Byte.valueOf(strs[3]);
	}
	
	@Override
	public void write() {
		this.writeBuffer.clear();
		this.writeInt(this.taskId);
		this.writeByte(this.isGet);
		this.writeInt(this.progress);
		this.writeByte(this.complte);
	}
	
	
	public DailyTaskVO(int taskId) {
		super();
		this.taskId = taskId;
	}


	public String parser2String() {
		List<Object> list = new ArrayList<>();
		list.add(this.taskId);
		list.add(this.isGet);
		list.add(this.progress);
		list.add(this.complte);
		return StringUtils.collection2SplitString(list, Splitable.ATTRIBUTE_SPLIT);
	}
	
	public void string2vo(String str) {
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}


	public byte getIsGet() {
		return isGet;
	}

	public void setIsGet(byte isGet) {
		this.isGet = isGet;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}
	
	public void setComplte(byte complte) {
		this.complte = complte;
	}
	
	public boolean isComplte() {
		return this.complte == 1? true :false;
	}
	
	
	
	
}
