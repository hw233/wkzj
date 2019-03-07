package com.jtang.gameserver.module.extapp.ernie.handler;

import com.jtang.gameserver.module.extapp.ernie.handler.response.ErnieInfoResponse;
import com.jtang.gameserver.module.extapp.ernie.handler.response.ErnieRecordResponse;
import com.jtang.gameserver.module.extapp.ernie.handler.response.ErnieStatusResponse;


public interface ErnieCmd {
	/**
	 * 获取活动状态
	 * <pre>
	 * 请求：@{code Request}
	 * 回复：@{code {@link ErnieStatusResponse}}
	 * 推送：@{code {@link ErnieStatusResponse}}
	 * </pre>
	 */
	byte STATUS = 1;
	
	/**
	 * 获取活动信息
	 * <pre>
	 * 请求：@{code Request}
	 * 回复：@{code {@link ErnieRecordResponse}}
	 * </pre>
	 */
	byte ERNIE_RECORD = 2;
	
	/**
	 * 获取摇奖信息
	 * <pre>
	 * 请求：@{code Request}
	 * 回复：@{code {@link ErnieInfoResponse}}
	 * </pre>
	 */
	byte ERNIE_INFO = 3;
	
	/**
	 * 抽奖
	 * <pre>
	 * 请求：@{code Request}
	 * 回复：@{code {@link ErnieInfoResponse}}
	 * </pre>
	 */
	byte RUN_ERNIE = 4;
	
}
