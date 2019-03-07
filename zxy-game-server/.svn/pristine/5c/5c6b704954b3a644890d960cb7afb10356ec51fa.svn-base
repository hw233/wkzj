package com.jtang.gameserver.module.adventures.bable.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dbproxy.entity.Bable;
import com.jtang.gameserver.module.adventures.bable.model.BableHistoryVO;
import com.jtang.gameserver.module.adventures.bable.type.BableState;
/**
 * 登天塔信息数据协议
 * @author ludd
 *
 */
public class BableDataResponse extends IoBufferSerializer {
	
	/**
	 * 登天塔状态
	 * 0.塔外
	 * 1.登塔中
	 * 2.结束
	 */
	public int bableState;
	
	/**
	 * 每日可重置次数
	 */
	public int num;
	
	/**
	 * 每日已重置次数
	 */
	public int useNum;
	
	/**
	 * 当前所在通天塔
	 */
	public int bableType;
	
	/**
	 * 历史最高层数
	 */
	public int historyFloor;
	
	/**
	 * 历史最高通天币
	 */
	public int historyStar;
	
	/**
	 * 当前层数
	 */
	public int floor;
	
	/**
	 * 当前通天币
	 */
	public int star;
	
	/**
	 * 已使用通天币
	 */
	public int useStar;
	
	/**
	 * 重试次数
	 */
	public int refightNum;
	
	/**
	 * 已用重试次数
	 */
	public int useRefight;
	
	/**
	 * 自动登塔倒计时时间
	 * -1：未进行自动登塔
	 * 0：倒计时结束，已可领取
	 */
	public int autoTime;
	
	/**
	 * 使用物品id
	 */
	public int useGoodsId;
	
	
	public BableDataResponse(Bable bable, int resetNum,int refightNum,int bableType, int autoTime, int useGoodsId) {
		this.num = resetNum;
		this.useNum = bable.resetNum;
		BableHistoryVO bableHistory = null;
		if(bable.bableStateVO == null){
			this.bableState = BableState.OUT_BABLE.getState();
			this.bableType = bableType;
			this.floor = 0;
			this.star = 0;
			this.useStar = 0;
			this.useRefight = 0;
			bableHistory =  bable.getHostoryVO(bableType);
		}else{
			this.bableType = bable.bableStateVO.type;
			this.floor = bable.bableStateVO.floor;
			this.star = bable.bableStateVO.star;
			this.useStar = bable.bableStateVO.useStar;
			this.useRefight = bable.bableStateVO.useRetryNum;
			this.bableState = bable.bableStateVO.state;
			bableHistory = bable.getHostoryVO(bable.bableStateVO.type);
		}
		if(bableHistory == null){
			this.historyFloor = 0;
			this.historyStar = 0;
		}else{
			this.historyFloor = bableHistory.floor;
			this.historyStar = bableHistory.star;
		}
		this.refightNum = refightNum;
		this.autoTime = autoTime;
		this.useGoodsId = useGoodsId;
	}

	@Override
	public void write() {
		writeByte((byte)bableState);
		writeInt(num);
		writeInt(useNum);
		writeByte((byte)bableType);
		writeInt(historyFloor);
		writeInt(historyStar);
		writeInt(floor);
		writeInt(star);
		writeInt(useStar);
		writeInt(refightNum);
		writeInt(useRefight);
		writeInt(this.autoTime);
		writeInt(this.useGoodsId);
	}

}
