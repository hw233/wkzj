package com.jtang.gameserver.dataconfig.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName = "resetHeroEquipConfig")
public class ResetHeroEquipConfig implements ModelAdapter {
	
	/**
	 * 类型
	 * 1.仙人    2.装备
	 */
	public int type;
	
	/**
	 * 扣除的点券    
	 * star_costTicket|star_costTicket
	 */
	public String costTicket;
	
	/**
	 * 返还物品id
	 */
	public int returnGoodsId;
	
	/**
	 * 返还的点券map
	 */
	@FieldIgnore
	private Map<Integer,Integer> costTicketMap = new HashMap<>();

	@Override
	public void initialize() {
		costTicketMap.clear();
		List<String[]> list = StringUtils.delimiterString2Array(costTicket);
		for(String[] str:list){
			costTicketMap.put(Integer.valueOf(str[0]), Integer.valueOf(str[1]));
		}
		
		this.costTicket = null;
	}
	
	public int getCostTicket(int star){
		return costTicketMap.get(star);
	}
	
}
