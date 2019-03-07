package com.jtang.gameserver.module.icon.hander;

public interface IconCmd {

	/**
	 * 获取头像信息
	 * 请求{@code Request}
	 * 返回{@code IconResponse}
	 */
	byte ICON_INFO = 1;
	
	/**
	 * 设置头像
	 * 请求{@code IconRequest}
	 * 返回{@code Response}
	 */
	byte SET_ICON = 2;
	
	/**
	 * 设置边框
	 * 请求{@code IconRequest}
	 * 返回{@code Response}
	 */
	byte SET_FRAM = 3;
	
	/**
	 * 推送头像解锁
	 * 推送{@code IconUnLockRespones}
	 */
	byte PUSH_ICON_UNLOCK = 4;
	
	/**
	 * 推送边框解锁
	 * 推送{@code FramUnLockResponse}
	 */
	byte PUSH_FRAM_UNLOCK = 5;
	
	/**
	 * 获取正在使用的头像和边框
	 * 请求{@code IconVORequest}
	 * 返回{@code IconVOResponse}
	 */
	byte GET_ICON_VO = 6;

	/**
	 * 修改性别
	 * 请求{@code SexRequest}
	 * 返回{@code Response}
	 */
	byte MODIFY_SEX = 7;

	/**
	 * 锁定头像
	 * 推送{@code IconUnLockRespones}
	 */
	byte PUSH_ICON_LOCK = 8;
}
