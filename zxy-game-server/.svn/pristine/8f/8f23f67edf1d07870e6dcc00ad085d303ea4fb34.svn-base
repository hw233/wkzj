package com.jtang.gameserver.dataconfig.model;

import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

/**
 * 碎片配置
 * @author liujian
 *
 */
@DataFile(fileName = "pieceConfig")
public class PieceConfig implements ModelAdapter{

	/**
	 * 碎片类型
	 */
	public int type;
	
	/**
	 * 品质(1-6)
	 */
	public int star;
	
	/**
	 * 原料投入
	 */
	public String composeSum;
	
	/**
	 * 合成品候选集合
	 */
	public String outputCollection;
	
	/**
	 * 原料投入
	 */
	@FieldIgnore
	public Map<Integer, Integer> requireMap = null;
	
	/**
	 * 合成品候选集合
	 */
	@FieldIgnore
	public List<Integer> outputList = null;
	   
	@Override
	public void initialize() {
		requireMap = StringUtils.delimiterString2IntMap(composeSum);
		outputList = StringUtils.delimiterString2IntList(outputCollection, Splitable.ELEMENT_SPLIT);
		this.composeSum = null;
		this.outputCollection = null;
	}

}
