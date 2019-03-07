package com.jtang.gameserver.module.sysmail.handler;

public interface SysmailCmd {

	/**
	 * 登陆获取系统邮箱列表
	 * <pre>
	 * 请求：{@code Request}
	 * 回复：{@code SysmailListResponse}
	 * </pre>
	 */
	byte GET_SYSMAIL_LIST = 1;
	
	/**
	 * 领取附件
	 * <pre>
	 * 请求：{@code SysmailRequest}
	 * 回复：{@code SysmailResponse}
	 * </pre>
	 */
	byte GET_ATTACH = 2;
	
	/**
	 * 删除邮件
	 * <pre>
	 * 请求：{@code SysmailRequest}
	 * 回复：{@code Response}
	 * </pre>
	 */
	byte REMOVE_SYSMAIL = 3;
	
	/**
	 * 推送邮件
	 * <pre>
	 * 推送:{@code SysmailListResponse}
	 * </pre>
	 */
	byte PUSH_SYSMAIL = 4;
	
	/**
	 * 一键领取邮件
	 * 请求:{@code Request}
	 * 返回:{@code Response}
	 */
	byte ONE_KEY_GET_ATTACH = 5;

}
