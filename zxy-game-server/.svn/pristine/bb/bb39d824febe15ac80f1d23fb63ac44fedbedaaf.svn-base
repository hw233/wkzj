package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName="chatConfig")
public class ChatConfig implements ModelAdapter {
	/**
	 * 1.装备 2.仙人
	 */
	public int id;
	
	/**
	 * 添加装备类型
	 */
	private String addEquipType;
	
	/**
	 * 添加仙人类型
	 */
	private String addHeroType;
	
	/**
	 * 系统聊天限制获取仙人星级
	 */
	public int heroStar;
	
	/**
	 * 系统聊天限制获取装备(武器)星级
	 */
	public int weaponStar;
	
	/**
	 * 系统聊天限制获取装备(防具)星级
	 */
	public int armorStar;
	
	/**
	 * 系统聊天限制获取装备(饰品)星级
	 */
	public int ornamentsStar;
	
	/**
	 * 添加装备类型
	 */
	@FieldIgnore
	public List<Integer> addEquipTypeList = new ArrayList<>();
	
	/**
	 * 添加仙人类型
	 */
	@FieldIgnore
	public List<Integer> addHeroTypeList = new ArrayList<>();
	
	

	@Override
	public void initialize() {
		addEquipTypeList=StringUtils.delimiterString2IntList(addEquipType, Splitable.ATTRIBUTE_SPLIT);
		addHeroTypeList=StringUtils.delimiterString2IntList(addHeroType, Splitable.ATTRIBUTE_SPLIT);
		
		this.addEquipType = null;
		this.addHeroType = null;
	}



	public void clear() {
		
	}

}
