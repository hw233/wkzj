//package com.jtang.gameserver.dataconfig.model;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.jtang.core.dataconfig.ModelAdapter;
//import com.jtang.core.dataconfig.annotation.DataFile;
//import com.jtang.core.dataconfig.annotation.FieldIgnore;
//import com.jtang.core.model.RewardObject;
//import com.jtang.core.utility.RandomUtils;
//import com.jtang.core.utility.Splitable;
//import com.jtang.core.utility.StringUtils;
//
//@DataFile(fileName="loveMonsterLeastConfig")
//public class LoveMonsterLeastConfig implements ModelAdapter {
//
//	/**
//	 * 难度id
//	 */
//	public int id;
//	
//	/**
//	 * 保底次数
//	 */
//	public String least;
//	
//	/**
//	 * 奖励
//	 */
//	public String reward;
//	
//	@FieldIgnore
//	private int begin;
//	
//	@FieldIgnore
//	private int end;
//	
//	@FieldIgnore
//	public List<RewardObject> rewardList = new ArrayList<>();
//	
//	@Override
//	public void initialize() {
//		List<Integer> leastList = StringUtils.delimiterString2IntList(least, Splitable.ATTRIBUTE_SPLIT);
//		this.begin = leastList.get(0);
//		this.end = leastList.get(1);
//		this.least = null;
//		
//		List<String[]> list = StringUtils.delimiterString2Array(reward);
//		for(String[] str:list){
//			RewardObject rewardObject = RewardObject.valueOf(str);
//			rewardList.add(rewardObject);
//		}
//		this.reward = null;
//	}
//	
//	public int randomLeast(){
//		return RandomUtils.nextInt(begin, end);
//	}
//
//}
