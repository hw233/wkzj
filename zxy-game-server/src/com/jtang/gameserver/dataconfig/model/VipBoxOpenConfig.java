package com.jtang.gameserver.dataconfig.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName="vipBoxOpenConfig")
public class VipBoxOpenConfig implements ModelAdapter {

	/**
	 * 开启的次数
	 */
	public int count;
	
	/**
	 * 扣除点券的数量
	 */
	public int costTicket;
	
	/**
	 * 奖励的数量
	 */
	private String rewardNum;
	
	@FieldIgnore
	private Map<Integer,Integer> rewardNumMap = new HashMap<>();
	
	@Override
	public void initialize() {
		List<String[]> list = StringUtils.delimiterString2Array(rewardNum);
		for(String[] str:list){
			rewardNumMap.put(Integer.valueOf(str[0]), Integer.valueOf(str[1]));
		}
		
		this.rewardNum = null;
	}
	
	public int getRewardNum(){
		return RandomUtils.randomHit(1000, rewardNumMap);
	}

}
