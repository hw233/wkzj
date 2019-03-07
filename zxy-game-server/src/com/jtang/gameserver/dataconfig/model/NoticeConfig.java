package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
/**
 * 公告配置
 * @author pengzy
 *
 */
@DataFile(fileName = "noticeConfig")
public class NoticeConfig implements ModelAdapter{
	/**
	 * 公告类型
	 */
	private int type;
	/**
	 * 公告的触发条件
	 */
	private String conditions;
	
	@FieldIgnore
	private List<Integer> conditionList = new ArrayList<>();
	@Override
	public void initialize() {
		List<String> list = StringUtils.delimiterString2List(conditions, Splitable.ATTRIBUTE_SPLIT);
		for(String condition : list){
			conditionList.add(Integer.valueOf(condition));
		}
		this.conditions = null;
	}

	public int getType() {
		return type;
	}

	public List<Integer> getConditionList() {
		return conditionList;
	}
	
	public int getCondition(){
		return conditionList.get(0);
	}
}
