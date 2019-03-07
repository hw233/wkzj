package com.jtang.gameserver.module.adventures.shop.shop.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.SHOP_BUY_FAIL;
import static com.jiatang.common.GameStatusCodeConstant.SHOP_BUY_MAX;
import static com.jiatang.common.GameStatusCodeConstant.SHOP_GOLD_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.SHOP_LEVEL_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.SHOP_NOT_BUY;
import static com.jiatang.common.GameStatusCodeConstant.SHOP_NOT_EXIST;
import static com.jiatang.common.GameStatusCodeConstant.VIP_LEVEL_NO_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.SHOP_MONTH_CARD;
import static com.jtang.core.protocol.StatusCode.DATA_VALUE_ERROR;
import static com.jtang.core.protocol.StatusCode.SUCCESS;
import static com.jtang.core.protocol.StatusCode.TICKET_NOT_ENOUGH;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.event.EventBus;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardType;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.DateUtils;
import com.jtang.gameserver.component.event.ShopBuyEvent;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.ShopConfig;
import com.jtang.gameserver.dataconfig.service.ShopService;
import com.jtang.gameserver.dbproxy.entity.Shop;
import com.jtang.gameserver.module.adventures.shop.shop.dao.ShopDao;
import com.jtang.gameserver.module.adventures.shop.shop.facade.ShopFacade;
import com.jtang.gameserver.module.adventures.shop.shop.helper.ShopPushHelp;
import com.jtang.gameserver.module.adventures.shop.shop.model.ShopVO;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.extapp.monthcard.facade.MonthCardFacade;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.GoldDecreaseType;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class ShopFacadeImpl implements ShopFacade, ActorLoginListener,ZeroListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShopFacadeImpl.class);

	@Autowired
	ShopDao shopDao;

	@Autowired
	private VipFacade vipFacade;

	@Autowired
	private ActorFacade actorFacade;

	@Autowired
	private EquipFacade equipFacade;

	@Autowired
	private GoodsFacade goodsFacade;
	@Autowired
	private HeroSoulFacade heroSoulFacade;

	@Autowired
	private Schedule schedule;

	@Autowired
	private PlayerSession playerSession;
	
	@Autowired
	private MonthCardFacade monthCardFacade;
	
	@Autowired
	private EventBus eventBus;

	@Override
	public Shop getShops(long actorId) {
		return shopDao.get(actorId);
	}

	@Override
	public TResult<Integer> buy(long actorId, int shopId,int num) {
		ShopConfig shopConfig = ShopService.getShop(shopId);// 获取配置
		Shop shop = shopDao.get(actorId);
		ShopVO shopVO = shop.getShopMap().get(shopId);// 获取数据库数据

		if(num < 1){
			return TResult.valueOf(DATA_VALUE_ERROR);
		}
		
		if(shopConfig == null){//商品id配置不存在
			return TResult.valueOf(SHOP_NOT_EXIST);
		}
		
		if(shopConfig.getMaxBuyCount() == 0){//商品不可购买
			return TResult.valueOf(SHOP_NOT_BUY);
		}
		
		int level = ActorHelper.getActorLevel(actorId);
		if (shopConfig.level > level){//掌教等级不足
			return TResult.valueOf(SHOP_LEVEL_NOT_ENOUGH);
		}
		int vipLevel = vipFacade.getVipLevel(actorId);
		if (shopConfig.getVipLevel() > vipLevel) {// vip等级不够
			return TResult.valueOf(VIP_LEVEL_NO_ENOUGH);
		}
		
		int monthCardDay = monthCardFacade.getDay(actorId);
		if(shopConfig.isMonthCard() && monthCardDay <= 0){
			return TResult.valueOf(SHOP_MONTH_CARD);
		}
		
		if (shopVO != null) {
			if(shopConfig.getMaxBuyCount() != -1){
				if (shopConfig.getMaxBuyCount() < shopVO.buyCount + num) {// 购买次数已经用完
					return TResult.valueOf(SHOP_BUY_MAX);
				}
			}
		}else{//今天还没购买过此道具
			shopVO = ShopVO.valueOf(shopId);
		}

		// 扣除点券
		int needTicket = shopConfig.getCostTicket() * num;
		boolean decreaseTicket = vipFacade.decreaseTicket(actorId, TicketDecreaseType.SHOP_BUY, needTicket,shopId,num);
		if (needTicket > 0 && !decreaseTicket) {
			return TResult.valueOf(TICKET_NOT_ENOUGH);
		}
		
		int needGold = shopConfig.costGold * num;
		boolean result = actorFacade.decreaseGold(actorId, GoldDecreaseType.SHOP_BUY, needGold);
		if(needGold > 0 && result == false){
			return TResult.valueOf(SHOP_GOLD_NOT_ENOUGH);
		}

		boolean buySuccess = addShop(actorId, shopConfig,num);
		if (buySuccess) {// 购买成功
			shopVO.buyCount += num;// 增加购买次数
			shopVO.resetTime = shopConfig.getResetTime();// 设置可购买类型
			shopDao.updateShopVO(actorId, shopVO);
//			ShopPushHelp.pushShopInfo(actorId, getShops(actorId).getShopVOs());
			
			//抛出购买成功事件
			eventBus.post(new ShopBuyEvent(actorId, shopId, shopVO.buyCount));
			return TResult.valueOf(SUCCESS);
		} else {// 购买失败
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("道具购买失败 商品id为[%s] 用户id为[%s]", shopId, actorId);
			}
			return TResult.valueOf(SHOP_BUY_FAIL);
		}
	}
	
	@Override
	public void onZero() {
		Set<Long> actorIds = playerSession.onlineActorList();
		for (Long actorId : actorIds) {// 清除在线玩家购买信息
			Shop shop = shopDao.get(actorId);
			ChainLock lock = LockUtils.getLock(shop);
			try {
				lock.lock();
				shop.cleanBuyInfo();
				shopDao.updateShop(shop);
				ShopPushHelp.pushShopInfo(actorId, shop.getShopVOs());
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
	}

	public boolean addShop(long actorId, ShopConfig shopConfig, int num) {
		short result = 0;
		switch (RewardType.getType(shopConfig.getType())) {
		case GOODS:
			result = goodsFacade.addGoodsVO(actorId, GoodsAddType.SHOP_BUY, shopConfig.id,shopConfig.number * num).statusCode;
			break;
		case EQUIP:
			result = equipFacade.addEquip(actorId, EquipAddType.SHOP_BUY, shopConfig.id).statusCode;
			break;
		case HEROSOUL:
			result = heroSoulFacade.addSoul(actorId, HeroSoulAddType.SHOP_BUY, shopConfig.id,shopConfig.number * num).statusCode;
			break;
		default:
			break;
		}
		return result == 0?true:false;
	}

	@Override
	public void onLogin(long actorId) {
		Shop shop = shopDao.get(actorId);
		ChainLock lock = LockUtils.getLock(shop);
		try {
			lock.lock();
			if (!DateUtils.isToday(shop.buyTime)) {// 最后购买时间不是今天清空数据
				shop.cleanBuyInfo();
				shopDao.updateShop(shop);
			}
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
	}

}
