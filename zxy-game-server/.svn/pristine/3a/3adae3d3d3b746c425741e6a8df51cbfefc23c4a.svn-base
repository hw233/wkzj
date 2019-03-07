package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

/**
 * 福神眷顾触发条件配置
 * @author ludd
 *
 */
@DataFile(fileName = "favorTriggerConfig")
public class FavorTriggerConfig implements ModelAdapter {
	/**
	 * 触发类型(福神眷顾触发条件，满足任意条件则触发)
	 * 1->抢夺
     * 2->登天塔
	 */
	public int type;
	/**
	 * 触发值 
     * type=1时，value表示抢夺次数
     * type=2时，value表示登塔次数
	 */
	public String value;
	@Override
	public void initialize() {

	}

}
