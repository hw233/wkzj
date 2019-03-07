package com.jtang.gameserver.module.extapp.vipbox.facade;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.vipbox.handler.response.VipBoxConfigResponse;
import com.jtang.gameserver.module.extapp.vipbox.handler.response.VipBoxResponse;

public interface VipBoxFacade {

	/**
	 * 获取箱子信息
	 * @param actorId
	 * @return
	 */
	
	public TResult<VipBoxResponse> getInfo(long actorId);

	
	/**
	 * 获取箱子
	 * @param actorId
	 * @return
	 */
	public Result getBox(long actorId);
	
	/**
	 * 开启箱子
	 */
	public TResult<List<RewardObject>> openBox(long actorId,int useNum);


	/**
	 * 获取活动开启关闭时间
	 * @return
	 */
	public TResult<VipBoxConfigResponse> getConfig();

}
