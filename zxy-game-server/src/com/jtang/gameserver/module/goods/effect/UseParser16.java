package com.jtang.gameserver.module.goods.effect;

import static com.jiatang.common.GameStatusCodeConstant.ERNIE_EXCHANGE_RAN_OUT;
import static com.jiatang.common.GameStatusCodeConstant.GOODS_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.PHONE_NUM_CAN_NOT_EMPTY;
import static com.jiatang.common.GameStatusCodeConstant.PHONE_NUM_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.PHONE_NUM_FORMAT_ERROR;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.event.EventBus;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.service.ErnieService;
import com.jtang.gameserver.dbproxy.entity.AppGlobal;
import com.jtang.gameserver.module.app.dao.AppGlobalDao;
import com.jtang.gameserver.module.app.model.extension.global.GlobalInfoVO19;
import com.jtang.gameserver.module.app.model.extension.rulevo.ErnieVO;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.goods.type.UseGoodsParserType;
import com.jtang.gameserver.module.goods.type.UseGoodsResult;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;

/**
 * iPhone 6, 20元话费处理啊
 * @author ligang
 *
 */
@Component
public class UseParser16 extends UseParser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UseParser16.class);
	@Autowired
	private VipFacade vipFacade;
	
	@Autowired
	private EquipFacade equipFacade;
	
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	
	@Autowired
	private HeroFacade heroFacade;
	
	@Autowired
	private AppGlobalDao appGlobalDao;
	
	@Autowired
	private EventBus eventBus;
	@Override
	protected UseGoodsParserType getParserType() {
		return UseGoodsParserType.TYPE16;
	}

	@Override
	public TResult<List<UseGoodsResult>> handler(long actorId, int goodsId, int useNum, int useFlag) {
		return TResult.valueOf(GameStatusCodeConstant.USE_GOODS_ERROR);
	}
	
	public TResult<List<UseGoodsResult>> extHandler(long actorId, int goodsId, int useNum, int useFlag, String phoneNum) {
		
		boolean isInTime = ErnieService.isExchangeTime();
		if (isInTime == false) {
			int totalNum = goodsFacade.getCount(actorId, goodsId);
			goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.ERNIE, goodsId, totalNum);
			return TResult.valueOf(ERNIE_EXCHANGE_RAN_OUT);
		}
		boolean enough = checkGoodsEnough(actorId, goodsId, useNum);
		if (!enough){
			return TResult.valueOf(GOODS_NOT_ENOUGH);
		}
		int goodsCount = goodsFacade.getCount(actorId, goodsId);
		if(goodsCount < useNum){
			if(goodsCount == 0){
				return TResult.valueOf(GOODS_NOT_ENOUGH);
			}else{
				useNum = goodsCount;
			}
		}
		
		AppGlobal appInfo = appGlobalDao.get(EffectId.EFFECT_ID_19.getId());
		GlobalInfoVO19 info = appInfo.getGlobalInfoVO();
		List<ErnieVO> list = info.ernieVOMap.get(goodsId);
		if (list.isEmpty()) {
			return TResult.valueOf(GOODS_NOT_ENOUGH);
		}
		List<ErnieVO> collectList = get(actorId, goodsId, list);
		if (collectList.isEmpty()) {
			return TResult.valueOf(GOODS_NOT_ENOUGH);
		}
		if (collectList.size() < useNum) {
			return TResult.valueOf(GOODS_NOT_ENOUGH);
		}
		if (StringUtils.isBlank(phoneNum)) {
			//带你话号码不能为空
			return TResult.valueOf(PHONE_NUM_CAN_NOT_EMPTY);
		}
		if (phoneNum.length() < 11) {
			//号码错误
			return TResult.valueOf(PHONE_NUM_ERROR);
		}
		long phoneNumber = 0L;
		try {
			phoneNumber = Long.parseLong(phoneNum);
		} catch (NumberFormatException e) {
			return TResult.valueOf(PHONE_NUM_FORMAT_ERROR);
		}
		ChainLock lock = LockUtils.getLock(appInfo);
		Result decreaseResult = null;
		decreaseResult = goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.ERNIE, goodsId, useNum);
		if (decreaseResult.isFail()){
			return TResult.valueOf(decreaseResult.statusCode);
		}
		List<UseGoodsResult> resultList = new ArrayList<UseGoodsResult>();
		int realUse = Math.min(collectList.size(), useNum);
		try {
			lock.lock();
			for (int i = 0; i < realUse; i++) {
				ErnieVO ernieVO = collectList.get(i);
				ernieVO.phoneNum = phoneNumber;
			}
			appGlobalDao.update(appInfo);
		} catch (Exception e) {
			LOGGER.error("{}",e);
		} finally {
			lock.unlock();
		}
		return TResult.sucess(resultList);
	}
	
	private List<ErnieVO> get(long actorId, int goodsId, List<ErnieVO> source) {
		List<ErnieVO> collectList = new ArrayList<ErnieVO>();
		for (ErnieVO ernieVO : source) {
			if (ernieVO.actorId == actorId && ernieVO.goodsId == goodsId && ernieVO.phoneNum == 0L) {
				collectList.add(ernieVO);
			}
		}
		return collectList;
	}

}
