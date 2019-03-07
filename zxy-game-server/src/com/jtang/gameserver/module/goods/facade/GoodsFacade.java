package com.jtang.gameserver.module.goods.facade;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.goods.handler.response.StartComposeResponse;
import com.jtang.gameserver.module.goods.model.GoodsVO;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.goods.type.UseGoodsResult;

/**
 * 物品业务接口
 * @author 0x737263
 *
 */
public interface GoodsFacade {
	
	/**
	 * 获取物品列表
	 * @param actorId	角色id
	 * @return
	 */
	Collection<GoodsVO> getList(long actorId);
	
	/**
	 * 物品
	 * @param actorId	角色id
	 * @param goodsId	物品id
	 * @return
	 */
	GoodsVO getGoodsVO(long actorId, int goodsId);
	
	/**
	 * 添加多个物品
	 * @param actorId
	 * @param type		物品添加类型
	 * @param goods key:goodsId value:num
	 * @return
	 */
	TResult<List<Long>> addGoodsVO(long actorId, GoodsAddType type, Map<Integer, Integer> goods);
	
	/**
	 * 添加物品(金币和点券id特殊处理)
	 * @param actorId	角色id
	 * @param type		物品添加类型
	 * @param goodsId	物品id
	 * @param num		物品数量
	 * @return 返回物品uuid
	 */
	TResult<Long> addGoodsVO(long actorId, GoodsAddType type, int goodsId, int num);
	
	/**
	 * 获取物品总数量
	 * @param actorId	角色id
	 * @param goodsId	物品id
	 */
	int getCount(long actorId, int goodsId);
	
	/**
	 * 使用物品
	 * @param actorId	角色id
	 * @param goodsId	物品id
	 * @param useNum	使用个数
	 * @param useFlag	使用标志
	 * @return
	 */
	TResult<List<UseGoodsResult>> useGoods(long actorId, int goodsId, int useNum, int useFlag, String phoneNum);
	
	/**
	 * 扣除物品
	 * @param actorId 	角色Id
	 * @param type		物品消耗类型
	 * @param goodsId 	物品id
	 * @param useNum 	物品数量
	 * @return
	 */
	Result decreaseGoods(long actorId, GoodsDecreaseType type, int goodsId, int useNum);
	
	/**
	 * 检查是否拥有物品
	 * @param actorId
	 * @param type
	 * @param subType
	 * @param star
	 * @return 返回物品Id 不存在返回0
	 */
	int hasGoodsByTypeStar(long actorId, int type, int subType, int star);
	/**
	 * 物品保底
	 * @param actorId
	 * @param goodsId
	 * @return 是否保底
	 */
	boolean leastGoods(long actorId, int goodsId) ;

	/**
	 * 出售金币
	 * @param actorId
	 * @param goodsId
	 * @param goodsNum
	 * @return
	 */
	TResult<Integer> sellGoods(long actorId, int goodsId, int goodsNum);

	/**
	 * 开始装备碎片合成
	 * @param actorId
	 * @param goodsId
	 * @return
	 */
	TResult<StartComposeResponse> composeGoods(long actorId, int goodsId);
	
}
