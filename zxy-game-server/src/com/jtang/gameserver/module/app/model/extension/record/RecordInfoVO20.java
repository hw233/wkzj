package com.jtang.gameserver.module.app.model.extension.record;

import java.util.HashMap;
import java.util.Map;

import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRecordInfoVO;
import com.jtang.gameserver.module.app.type.AppKey;
import com.jtang.gameserver.module.app.type.EffectId;

/**
 * 《新号首日冲级奖励》
 * @author hezh
 *
 */
@AppVO
public class RecordInfoVO20 implements BaseRecordInfoVO {
	
	private static final long serialVersionUID = 701382340352120121L;

	/** 是否完成0.不可领取   1.可领取  2.已领取*/
	private int isFinish;
	
	@Override
	public EffectId getAppId() {
		return EffectId.EFFECT_ID_20;
	}

	@Override
	public String parse2String() {
		return String.valueOf(isFinish);
	}

	@Override
	public BaseRecordInfoVO valueOf(String record) {
		RecordInfoVO20 vo = new RecordInfoVO20();
		if (StringUtils.isNotBlank(record)) {
			vo.isFinish = Integer.parseInt(record);
		}
		return vo;
	}

	@Override
	public Map<AppKey, Object> getRecordInfoMaps() {
		Map<AppKey , Object> map = new HashMap<AppKey, Object>();
		map.put(AppKey.IS_FINISH,isFinish);
		return map;
	}

	public boolean isCanGet(){
		return isFinish == 1;
	}
	
	public boolean isGet(){
		return isFinish == 2;
	}

	/**
	 * @param isFinish the isFinish to set
	 */
	public void setIsFinish(int isFinish) {
		this.isFinish = isFinish;
	}
}
