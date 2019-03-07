package com.jtang.gameserver.module.app.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.app.model.AppRecordVO;
/**
 * 活动记录回复
 * @author ludd
 *
 */
public class GetAppRecordResponse extends IoBufferSerializer {

	/**
	 * key:活动id
	 * value：活动记录
	 */
	private List<AppRecordVO> recordList = new ArrayList<AppRecordVO>();
	
	@Override
	public void write() {
		this.writeShort((short) this.recordList.size());
		for (AppRecordVO appRecordVO : recordList) {
			this.writeBytes(appRecordVO.getBytes());
		}
	}
	
	public GetAppRecordResponse() {
	}
	
	public GetAppRecordResponse(List<AppRecordVO> list) {
		recordList = list;
	}
	
	public GetAppRecordResponse(AppRecordVO appRecordVO) {
		recordList.add(appRecordVO);
	}

}
