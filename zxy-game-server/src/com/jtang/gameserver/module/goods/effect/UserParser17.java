package com.jtang.gameserver.module.goods.effect;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.event.EventBus;
import com.jtang.core.model.RewardObject;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.component.event.VIPBoxTaskEvent;
import com.jtang.gameserver.module.extapp.vipbox.facade.VipBoxFacade;
import com.jtang.gameserver.module.goods.constant.GoodsRule;
import com.jtang.gameserver.module.goods.type.UseGoodsParserType;
import com.jtang.gameserver.module.goods.type.UseGoodsResult;
import com.jtang.gameserver.module.goods.type.UseGoodsResultType;

@Component
public class UserParser17 extends UseParser {

	@Autowired
	VipBoxFacade vipBoxFacade;
	
	@Autowired
	EventBus eventBus;
	
	@Override
	protected UseGoodsParserType getParserType() {
		return UseGoodsParserType.TYPE17;
	}

	@Override
	public TResult<List<UseGoodsResult>> handler(long actorId, int goodsId,int useNum, int useFlag) {
		TResult<List<RewardObject>> result = vipBoxFacade.openBox(actorId,useNum);
		if(result.isFail()){
			return TResult.valueOf(result.statusCode);
		}else{
			List<UseGoodsResult> list = new ArrayList<UseGoodsResult>();
			for(RewardObject rewardObject:result.item){
				switch(rewardObject.rewardType){
				case EQUIP:
					list.add(new UseGoodsResult(rewardObject.id, UseGoodsResultType.EQUIP, rewardObject.num));
					break;
				case GOLD:
					list.add(new UseGoodsResult(GoodsRule.GOODS_ID_GOLD, UseGoodsResultType.GOODS, rewardObject.num));
					break;
				case GOODS:
					list.add(new UseGoodsResult(rewardObject.id, UseGoodsResultType.GOODS, rewardObject.num));
					break;
				case HEROSOUL:
					list.add(new UseGoodsResult(rewardObject.id, UseGoodsResultType.HERO_SOUL, rewardObject.num));
					break;
				case TICKET:
					list.add(new UseGoodsResult(GoodsRule.GOODS_ID_TICKET, UseGoodsResultType.GOODS,rewardObject.num));
				default:
					break;
				}
			}
			//触发每日任务
			eventBus.post(new VIPBoxTaskEvent(actorId,useNum));
			return TResult.sucess(list);
		}
	}
}
