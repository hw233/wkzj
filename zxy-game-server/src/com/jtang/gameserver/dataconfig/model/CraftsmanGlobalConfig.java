package com.jtang.gameserver.dataconfig.model;

import java.util.Date;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.StringUtils;
/**
 * 神匠来袭配置
 * @author lig
 *
 */
@DataFile(fileName = "craftsmanGlobalConfig")
public class CraftsmanGlobalConfig implements ModelAdapter {

	/**
	 * 打造次数
	 */
	public int buildCount;
	/**
	 * 开放日期
	 */
	private String openDate;
	/**
	 * 开放时间
	 */
	private String closeDate;
	
	/**
	 * 活动要打造的装备
	 */
	private String buildEquips;
	
	@FieldIgnore
	public Date openDateTime = new Date();
	
	@FieldIgnore
	public Date closeDateTime = new Date();
	
	@FieldIgnore
	public Map<Integer, Integer> buildEquipsMap = null;
	
	@Override
	public void initialize() {
		openDateTime = DateUtils.string2Date(this.openDate, "yyyy-MM-dd HH:mm:ss");
		closeDateTime = DateUtils.string2Date(this.closeDate, "yyyy-MM-dd HH:mm:ss");
		
		buildEquipsMap = StringUtils.delimiterString2IntMap(buildEquips);
		this.buildEquips = null;
	}
	
	
	
	
}
