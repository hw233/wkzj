package com.jtang.gameserver.dataconfig.model;

import java.util.Date;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardType;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.StringUtils;

/**
 * 登天塔兑换配置
 * @author ludd
 *
 */
@DataFile(fileName = "bableExchangeConfig")
public class BableExchangeConfig implements ModelAdapter {

	/**
	 * 登天塔类型
	 */
	public int bableType;
	
	/**
	 * 兑换唯一id
	 */
	public int exchangeId;
	
	/**
	 * 1:物品，2：装备，3：魂魄
	 */
	public int type;
	
	/**
	 * 物品，装备，魂魄的ID，由type决定
	 */
	public int id;
	
	/**
	 * 每组数量
	 */
	public int num;
	
	/**
	 * 可兑换数量下限
	 */
	public int minNum;
	
	/**
	 * 可兑换数量上限
	 */
	public int maxNum;

	/**
	 * 几率
	 */
	public int proportion;
	
	/**
	 * 消耗星星数
	 */
	public int consumeStar;
	
	/**
	 * 开始兑换时间
	 */
	private String startTime;
	
	/**
	 * 结束兑换时间
	 */
	private String endTime;
	
	@FieldIgnore
	private int start;
	
	@FieldIgnore
	private int end;
	@Override
	public void initialize() {
		if (StringUtils.isNotBlank(startTime)) {
			Date dateStart = DateUtils.string2Date(startTime, "yyyy-MM-dd HH:mm:ss");
			Long ls = dateStart.getTime() / 1000;
			start = ls.intValue();
		}
		
		if (StringUtils.isNotBlank(endTime)) {
			Date dateEnd = DateUtils.string2Date(endTime, "yyyy-MM-dd HH:mm:ss");
			Long le = dateEnd.getTime() / 1000;
			end = le.intValue();
		}
		
		this.startTime = null;
		this.endTime = null;
	}
	
	public RewardType getType() {
		return RewardType.getType(type);
	}
	
	public int getEnd() {
		return end;
	}
	
	public int getStart() {
		return start;
	}
	
	

}
