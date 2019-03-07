package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.gameserver.module.user.type.RechargeType;

@DataFile(fileName="rechargeRateConfig")
public class RechargeRateConfig implements ModelAdapter, Comparable<RechargeRateConfig> {
	/**
	 * 充值id
	 */
	public int rechargeId;
	/**
	 * 多少钱（人民币）
	 */
	public float price;
	/**
	 * 等于多少点卷
	 */
	public int ticket;
	/**
	 * 赠送多少点卷
	 */
	public int give;
	/**
	 * 是否为月卡
	 * 0.不是1.是
	 */
	private int rechargeType;
	
	@Override
	public void initialize() {

	}
	@Override
	public int compareTo(RechargeRateConfig o) {
		if (this.give < o.give){
			return -1;
		} else if (this.give ==  o.give){
			return 0;
		} else {
			return 1;
		}
	}
	
	public RechargeType getRechargeType(){
		return RechargeType.getType(rechargeType);
	}

}
