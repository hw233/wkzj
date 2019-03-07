package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.StringUtils;

/**
 * 
 * @author ligang
 *
 */
@DataFile(fileName = "onlineGiftsConfig")
public class OnlineGiftsConfig implements ModelAdapter {

	public int index;
	
	public int time;
	
	private String reward;
	
	@FieldIgnore
	public List<RewardObject> rewardsList; 
	@Override
	public void initialize() {
		List<String[]> list = StringUtils.delimiterString2Array(reward);
		rewardsList = new ArrayList<RewardObject>();
		for (String[] param : list) {
			RewardObject object = RewardObject.valueOf(param);
			rewardsList.add(object);
		}
	}

}
