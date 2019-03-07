package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.IconConfig;
import com.jtang.gameserver.dataconfig.model.IconFramConfig;
import com.jtang.gameserver.module.icon.model.IconVO;
import com.jtang.gameserver.module.icon.type.IconFramType;

@Component
public class IconService extends ServiceAdapter {
	

	/** 仙人头像 **/
	private static Map<Byte,List<IconConfig>> ICON_MAP = new HashMap<>();
	
	/** 头像边框 **/
	private static Map<Integer,IconFramConfig> ICON_FRAM_MAP = new HashMap<>();

	
	@Override
	public void clear() {
		ICON_MAP.clear();
		ICON_FRAM_MAP.clear();
	}

	@Override
	public void initialize() {
		List<IconConfig> iconList = dataConfig.listAll(this, IconConfig.class);
		for(IconConfig config:iconList){
			if(ICON_MAP.containsKey((byte)config.star)){
				ICON_MAP.get((byte)config.star).add(config);
			}else{
				List<IconConfig> list = new ArrayList<>();
				list.add(config);
				ICON_MAP.put((byte)config.star, list);
			}
		}
		
		List<IconFramConfig> iconFramList = dataConfig.listAll(this, IconFramConfig.class);
		for(IconFramConfig config:iconFramList){
			ICON_FRAM_MAP.put(config.framId, config);
		}
	}
	
	public static List<IconConfig> getIcon(byte heroStar){
		return ICON_MAP.get(heroStar);
	}
	
	public static List<Integer> getOneStarIcon(){
		List<IconConfig> configs = getIcon((byte) 1);
		List<Integer> list = new ArrayList<>();
		for(IconConfig icon:configs){
			list.add(icon.heroId);
		}
		return list;
	}
	
	public static List<Integer> getTwoStarIcon(){
		List<IconConfig> configs = getIcon((byte) 2);
		List<Integer> list = new ArrayList<>();
		for(IconConfig icon:configs){
			list.add(icon.heroId);
		}
		return list;
	}

	public static List<IconFramConfig> getAllIconFram(){
		return new ArrayList<>(ICON_FRAM_MAP.values());
	}

	public static IconVO randomIconVO() {
		int iconKey = ICON_MAP.keySet().size();
		int iconRandom = RandomUtils.nextIntIndex(iconKey);
		List<IconConfig> iconList = new ArrayList<List<IconConfig>>(ICON_MAP.values()).get(iconRandom);
		IconConfig iconConfig = iconList.get(RandomUtils.nextIntIndex(iconList.size()));
		
		IconFramConfig framConfig = null;
		for(IconFramConfig config : getAllIconFram()){
			if(config.framType == IconFramType.COMMON.getCode()){
				framConfig = config;
			}
		}
		return new IconVO(iconConfig.heroId, framConfig.framId, (byte)0);
	}
	
	public static IconVO getdefIconVO(){
		int icon = getOneStarIcon().get(0);
		int fram = getAllIconFram().get(0).framId;
		return new IconVO(icon,fram, (byte)0);
	}
	
}
