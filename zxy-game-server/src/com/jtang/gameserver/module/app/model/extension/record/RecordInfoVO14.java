package com.jtang.gameserver.module.app.model.extension.record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRecordInfoVO;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.app.type.AppKey;

/**
 * 佛祖宝箱
 * 宝箱id_已购买次数
 * @author jianglf
 *
 */
@AppVO
public class RecordInfoVO14 implements BaseRecordInfoVO {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 672466321338841161L;
	
	/**
	 * 宝箱id
	 */
	public int goodsId;
	
	/**
	 * 消耗精力次数
	 */
	public int costEnergyNum;
	
	/**
	 * 消耗点券次数
	 */
	public int costTicketNum;

	@Override
	public EffectId getAppId() {
		return EffectId.EFFECT_ID_14;
	}

	@Override
	public String parse2String() {
		StringBuffer sb = new StringBuffer();
		sb.append(goodsId);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(costEnergyNum);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(costTicketNum);
		return sb.toString();
	}

	@Override
	public BaseRecordInfoVO valueOf(String record) {
		RecordInfoVO14 recordInfoVO1014 = new RecordInfoVO14();
		List<Integer> list = StringUtils.delimiterString2IntList(record, Splitable.ATTRIBUTE_SPLIT);
		if(list.isEmpty() == false){
			recordInfoVO1014.goodsId = list.get(0);
			recordInfoVO1014.costEnergyNum = list.get(1);
			recordInfoVO1014.costTicketNum = list.get(2);
		}else{
			recordInfoVO1014.goodsId = 0;
			recordInfoVO1014.costEnergyNum = 0;
			recordInfoVO1014.costTicketNum = 0;
		}
		return recordInfoVO1014;
	}

	@Override
	public Map<AppKey, Object> getRecordInfoMaps() {
		Map<AppKey, Object> map = new HashMap<AppKey, Object>();
		map.put(AppKey.APP_STRING, parse2String());
		return map;
	}

}