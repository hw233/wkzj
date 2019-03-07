package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RandomRewardObject;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
/**
 * 欢乐摇奖
 * @author lig
 *
 */
@DataFile(fileName = "ernieRewardConfig")
public class ErnieRewardConfig implements ModelAdapter {

	/**
	 *抽奖次数
	 */
	public int count;
	
	/**
	 *本次抽奖花费点券
	 */
	public int costTicket;
	/**
	 *本次抽奖物品集合
	 */
	private String goods;
	
	@FieldIgnore
	public List<RandomRewardObject> goodsList = new ArrayList<RandomRewardObject>();
	
	@Override
	public void initialize() {
		List<String> list = StringUtils.delimiterString2List(goods, Splitable.ELEMENT_SPLIT);
		for (String str : list) {
			String[] value = StringUtils.split(str, Splitable.ATTRIBUTE_SPLIT);
			RandomRewardObject e = RandomRewardObject.valueOf(value);
			goodsList.add(e);
		}
		
		Collections.unmodifiableList(goodsList);
	}
	
	
	
	
	
}
