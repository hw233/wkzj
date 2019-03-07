package com.jtang.worldserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

/**
 * 比赛日程配置
 * @author 0x737263
 *
 */
@DataFile(fileName = "crossBattleDayConfig")
public class CrossBattleDayConfig implements ModelAdapter {

	/**
	 * 比赛第x天
	 */
	private int dayNum;
	
	/**
	 * 分组id
	 */
	private int groupId;
	
	/**
	 * 主场服务器id
	 */
	private int homeServerId;
	
	/**
	 * 客场服务器id(配0则轮空)
	 */
	private int awayServerId;

	
	@Override
	public void initialize() {
//		Assert.isTrue(homeServerId == awayServerId, String.format("homeServerId[%s] and awayServerId are the same!", homeServerId, awayServerId));
	}

	public int getDayNum() {
		return dayNum;
	}
	
	public int getGroupId() {
		return groupId;
	}

	public int getHomeServerId() {
		return homeServerId;
	}

	public int getAwayServerId() {
		return awayServerId;
	}

}
