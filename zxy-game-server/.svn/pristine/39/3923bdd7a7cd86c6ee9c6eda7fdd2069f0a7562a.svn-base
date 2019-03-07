package com.jtang.gameserver.module.app.model.extension.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseGlobalInfoVO;
import com.jtang.gameserver.module.app.model.extension.rulevo.ErnieVO;
import com.jtang.gameserver.module.app.type.AppKey;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.goods.constant.GoodsRule;

/**
 * 摇奖iphone6和话费活动全局配置
 * @author ligang
 *
 */
@AppVO
public class GlobalInfoVO19 implements BaseGlobalInfoVO {

	private static final long serialVersionUID = -2721011591169443612L;

	public Map<Integer,List<ErnieVO>> ernieVOMap = new LinkedHashMap<Integer,List<ErnieVO>>();
	@Override
	public EffectId getAppId() {
		return EffectId.EFFECT_ID_19;
	}

	@Override
	public void init(String record) {
		List<String[]> itemStrings = StringUtils.delimiterString2Array(record);
		if (itemStrings.isEmpty() == false) {
			for (String[] items : itemStrings) {
				items = StringUtils.fillStringArray(items, 3, "0");
				ErnieVO vo = new ErnieVO(items);
				List<ErnieVO> list = ernieVOMap.get(vo.goodsId);
				if (list == null) {
					list = new ArrayList<ErnieVO>();
				}
				list.add(vo);
				ernieVOMap.put(vo.goodsId, list);
			}
		} else {
			ernieVOMap.put(GoodsRule.GOODS_ID_IPHONE6, new ArrayList<ErnieVO>());
			ernieVOMap.put(GoodsRule.GOODS_ID_BILL, new ArrayList<ErnieVO>());
		}
	}

	@Override
	public String parse2String() {
		List<ErnieVO> ernieVOList = new LinkedList<ErnieVO>();
		for (List<ErnieVO> perList : ernieVOMap.values()) {
			ernieVOList.addAll(perList);
		}
		return StringUtils.collection2SplitString(ernieVOList, Splitable.ELEMENT_DELIMITER);
	}

	@Override
	public Map<AppKey, Object> getGlobalInfoMaps() {
		Map<AppKey, Object> map = new HashMap<AppKey, Object>();
		return map;
	}

}
