package com.jtang.gameserver.module.app.model.extension;

import java.io.Serializable;
import java.util.Map;

import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.app.type.AppKey;

/**
 * 活动配置扩展字段接口
 * 全局数据，单例即可
 * @author ludd
 *
 */
public interface BaseGlobalInfoVO extends Serializable{
	
	/**
	 * 获取活动id
	 * @return
	 */
	EffectId getAppId();
	
	/**
	 * 初始化
	 * @param record
	 */
    void init(String record);	
	
	/**
	 * 序列化扩展字段
	 * @return
	 */
	String parse2String();
    
	/**
	 * 扩展字段转成map
	 * @return
	 */
	Map<AppKey, Object> getGlobalInfoMaps();

}
