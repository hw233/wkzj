package com.jtang.gameserver.module.goods.effect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.TResult;
import com.jtang.core.utility.Splitable;
import com.jtang.gameserver.dataconfig.model.GoodsConfig;
import com.jtang.gameserver.dataconfig.service.GoodsService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.model.GoodsVO;
import com.jtang.gameserver.module.goods.type.UseGoodsParserType;
import com.jtang.gameserver.module.goods.type.UseGoodsResult;
import com.jtang.gameserver.module.user.facade.ActorFacade;

/**
 * 物品使用解析器
 * @author ludd
 *
 */
@Component
public abstract class UseParser {

	private static Map<UseGoodsParserType, UseParser> pasrsers = new HashMap<UseGoodsParserType, UseParser>();
	@Autowired
	protected ActorFacade actorFacade;
	@Autowired
	protected GoodsFacade goodsFacade;
	
	@PostConstruct
	void init() {
		pasrsers.put(getParserType(), this);
	}
	/**
	 * 获取解析器类型
	 * @return
	 */
	protected abstract UseGoodsParserType getParserType();
	
	/**
	 * 解析器处理使用
	 * @param actorId
	 * @param goodsId
	 * @param useNum
	 * @param useFlag
	 * @return
	 */
	public abstract TResult<List<UseGoodsResult>> handler(long actorId, int goodsId, int useNum, int useFlag);
	
	/**
	 * 获取解析器
	 * @param useGoodsParserType
	 * @return
	 */
	public static UseParser getParser(UseGoodsParserType useGoodsParserType){
		return pasrsers.get(useGoodsParserType);
	}
	
	protected boolean checkGoodsEnough(long actorId, int goodsId, int useNum){
		GoodsConfig goodsConfig = GoodsService.get(goodsId);
		if (goodsConfig.depends != null && goodsConfig.depends != ""){
			String[] depends = goodsConfig.depends.split(Splitable.ATTRIBUTE_SPLIT);
			int dependsId = Integer.valueOf(depends[0]);
			int dependsNum = Integer.valueOf(depends[1]);
			GoodsVO dependGoods = goodsFacade.getGoodsVO(actorId, dependsId);
			if (dependGoods == null || dependGoods.num < dependsNum){
				return false;
			}
		}
		GoodsVO goods = goodsFacade.getGoodsVO(actorId, goodsId);
		if (goods == null || goods.num < useNum){
			return false;
		}
		return true;
	}
	
	/**
	 * 检查物品使用等级是否达到
	 * @param actorId
	 * @param useLevel
	 * @return
	 */
	protected boolean checkLevel(long actorId, int useLevel) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor.level >= useLevel) {
			return true;
		}
		return false;
	}
}
