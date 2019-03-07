package com.jtang.gameserver.module.app.model.extension;

import java.io.Serializable;
import java.util.Map;

import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.app.type.AppKey;

/**
 * 活动记录扩展字段接口
 * 实例是多例的
 * @author ludd
 *
 */
public interface BaseRecordInfoVO extends Serializable{
	
	/**
	 * 获取活动id
	 * @return
	 */
	public abstract EffectId getAppId();
	
	/**
	 * 序列化扩展字段
	 * @return
	 */
	public abstract String parse2String();
	
	/**
	 * 实例化一个新的实现对象
	 * 重要:返回一个新的实例
	 * @param record
	 * @return
	 */
	public abstract BaseRecordInfoVO valueOf(String record);

	/**
	 * 扩展字段转成map
	 * @return
	 */
	public abstract Map<AppKey, Object> getRecordInfoMaps();

}
