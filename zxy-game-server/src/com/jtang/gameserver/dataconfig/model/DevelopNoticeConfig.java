package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.Splitable;

/**
 * 装备突破公告配置
 * @author hezh
 *
 */
@DataFile(fileName = "developNoticeConfig")
public class DevelopNoticeConfig implements ModelAdapter{

	/** 需要公告的突破次数 7_9*/
	private String developNum;
	
	/** 公告内容 恭喜{0}将{1}精铸到+{2}*/
	private String systemMsg;
	
	/** 需要公告的突破次数列表*/
	@FieldIgnore
	private List<Integer> noticeList; 
	
	@Override
	public void initialize() {
		String[] arr = developNum.split(Splitable.ATTRIBUTE_SPLIT);
		noticeList = new ArrayList<Integer>();
		for (int i = 0; i < arr.length; i++) {
			noticeList.add(Integer.parseInt(arr[i]));
		}
	}

	/**
	 * @return the developNum
	 */
	public String getDevelopNum() {
		return developNum;
	}

	/**
	 * @param developNum the developNum to set
	 */
	public void setDevelopNum(String developNum) {
		this.developNum = developNum;
	}

	/**
	 * @return the systemMsg
	 */
	public String getSystemMsg() {
		return systemMsg;
	}

	/**
	 * @param systemMsg the systemMsg to set
	 */
	public void setSystemMsg(String systemMsg) {
		this.systemMsg = systemMsg;
	}

	/**
	 * @return the noticeList
	 */
	public List<Integer> getNoticeList() {
		return noticeList;
	}

	/**
	 * @param noticeList the noticeList to set
	 */
	public void setNoticeList(List<Integer> noticeList) {
		this.noticeList = noticeList;
	}

	
	
}
