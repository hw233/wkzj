package com.jtang.gameserver.dataconfig.model;

import java.util.HashMap;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName="boxConfig")
public class BoxConfig implements ModelAdapter {

	/**
	 * 宝箱id
	 */
	public int boxId;
	
	/**
	 * 装备星级
	 * 星级_几率|...
	 * 类型 0
	 */
	public String equipStar;
	
	/**
	 * 装备类型
	 * 装备类型<1.武器 2.防具 3.饰品>_几率;
	 */
	public String equipType;
	
	/**
	 * 碎片星级
	 * 星级_几率|...
	 * 类型 1
	 */
	public String fragmentStar;
	
	/**
	 * 碎片类型
	 * 碎片类型<1.武器 2.防具 3.饰品>_几率|...;
	 */
	public String fragmentType;
	
	/**
	 * 物品类型
	 * 物品类型_几率|...
	 * 类型 2
	 */
	public String goods;
	
	/**
	 * 金币(公式x1代表等级)
	 * 类型 3
	 */
	public String gold;
	
	/**
	 * 出产几率
	 */
	public String proportion;
	
	@FieldIgnore
	public Map<Integer,Integer> equipStarMap = new HashMap<>();
	
	@FieldIgnore
	public Map<Integer,Integer> equipTypeMap = new HashMap<>();
	
	@FieldIgnore
	public Map<Integer,Integer> fragmentStarMap = new HashMap<>();
	
	@FieldIgnore
	public Map<Integer,Integer> fragmentTypeMap = new HashMap<>();
	
	@FieldIgnore
	public Map<Integer,Integer> goodsMap = new HashMap<>();
	
	@FieldIgnore
	public Map<Integer,Integer> proportionMap = new HashMap<>();
	
	@Override
	public void initialize() {
		equipStarMap = StringUtils.delimiterString2IntMap(equipStar);
		equipStar = null;
		
		equipTypeMap = StringUtils.delimiterString2IntMap(equipType);
		equipType = null;
		
		fragmentStarMap = StringUtils.delimiterString2IntMap(fragmentStar);
		fragmentStar = null;
		
		fragmentTypeMap = StringUtils.delimiterString2IntMap(fragmentType);
		fragmentType = null;
		
		goodsMap = StringUtils.delimiterString2IntMap(goods);
		goods = null;
		
		proportionMap = StringUtils.delimiterString2IntMap(proportion);
		proportion = null;
	}

}
