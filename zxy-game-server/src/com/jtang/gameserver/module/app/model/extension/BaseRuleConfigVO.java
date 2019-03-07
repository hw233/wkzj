package com.jtang.gameserver.module.app.model.extension;

import com.jtang.gameserver.module.app.type.EffectId;

/**
 * 配置文件
 * @author ludd
 *
 */
public interface BaseRuleConfigVO {

	/**
	 * 获取活动id
	 * @return
	 */
	EffectId getEffectId();
	
	/**
	 * 初始化
	 * @param record
	 */
    void init(String record);	
	
}
