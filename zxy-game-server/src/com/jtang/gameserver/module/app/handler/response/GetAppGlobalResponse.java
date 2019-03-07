package com.jtang.gameserver.module.app.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.app.model.AppGlobalVO;
/**
 * 活动配置回复
 * @author ludd
 *
 */
public class GetAppGlobalResponse extends IoBufferSerializer {

	/**
	 * key:活动id
	 * value：活动配置
	 */
	private List<AppGlobalVO> configList = new ArrayList<AppGlobalVO>();
	
	@Override
	public void write() {
		this.writeShort((short) this.configList.size());
		for (AppGlobalVO vo : this.configList) {
			this.writeBytes(vo.getBytes());
		}
	}
	
	public GetAppGlobalResponse(List<AppGlobalVO> list) {
		configList = list;
	}
	
	public GetAppGlobalResponse(AppGlobalVO vo) {
		configList.add(vo);
	}

}
