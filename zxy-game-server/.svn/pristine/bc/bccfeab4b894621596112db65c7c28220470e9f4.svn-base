package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.RandomNameConfig;

/**
 * 随机名服务类
 * @author 0x737263
 *
 */
@Component
public class RandomNameService  extends ServiceAdapter {

	private static List<String> LAST_NAME_LIST = new ArrayList<>();
	private static List<String> FIRST_NAME_LIST1 = new ArrayList<>();
	private static List<String> FIRST_NAME_LIST2 = new ArrayList<>();
	
	@Override
	public void clear() {
		LAST_NAME_LIST.clear();
		FIRST_NAME_LIST1.clear();
		FIRST_NAME_LIST2.clear();
	}
	
	@Override
	public void initialize() {
		List<RandomNameConfig> list = dataConfig.listAll(this, RandomNameConfig.class);
		for(RandomNameConfig config : list) {
			LAST_NAME_LIST.add(config.getLastName());
			
			FIRST_NAME_LIST1.add(config.getMaleFirstName1());
			FIRST_NAME_LIST1.add(config.getFemaleFirstName1());
			
			FIRST_NAME_LIST2.add(config.getMaleFirstName2());
			FIRST_NAME_LIST2.add(config.getFemaleFirstName2());
		}
	}
	
	/**
	 * 随机角色名
	 * @return
	 */
	public static String randActorName() {
		StringBuilder name = new StringBuilder();
		
		int index = RandomUtils.nextIntIndex(LAST_NAME_LIST.size());
		name.append(LAST_NAME_LIST.get(index));
		
		index = RandomUtils.nextIntIndex(FIRST_NAME_LIST1.size());
		name.append(FIRST_NAME_LIST1.get(index));
		
		index = RandomUtils.nextIntIndex(FIRST_NAME_LIST2.size());
		name.append(FIRST_NAME_LIST2.get(index));
		
		return name.toString();
	}

}
