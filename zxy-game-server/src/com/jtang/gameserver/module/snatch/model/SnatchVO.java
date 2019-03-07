package com.jtang.gameserver.module.snatch.model;


/**
 * 抢夺返回信息vo
 * @author liujian
 *
 */
public class SnatchVO {

	/**
	 * 抢夺结果0失败，1成功
	 */
	public int result;
	
	/**
	 * 奖励的积分
	 */
	public int score;
	
	/**
	 * 魂魄/碎片/金币的配置id
	 */
	public int goodsId;
	
	/**
	 * 获得碎片、魂魄、金币的数量
	 */
	public int goodsNum;
	
	/**
	 * 剩余的金币数量
	 */
	public long golds;
	
	public static SnatchVO valueOf(boolean isWin, int score, int goodsId, int goodsNum) {
		SnatchVO snatchVo = new SnatchVO();
		snatchVo.result = isWin ? 1 : 0;
		snatchVo.score = score;
		snatchVo.goodsId = goodsId;
		snatchVo.goodsNum = goodsNum;
		return snatchVo;
	}
}
