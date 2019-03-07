package com.jtang.gameserver.module.battle.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.type.ActionType;
import com.jtang.gameserver.module.goods.model.GoodsVO;


/**
 * 掉落物品
 * @author vinceruan
 *
 */
public class DropGoodsAction extends IoBufferSerializer implements Action {
	public Position positioin;//物品掉落位置
	public GoodsVO dropGoods; //掉落物品实体
	
	public DropGoodsAction(GoodsVO goods, int x, int y) {
		this.dropGoods = goods;
		this.positioin = new Position((byte)x, (byte)y);
	}

	@Override
	public String format(String indentStr) {
		return String.format("%sDropGoodsAction:position:【%s,%s】, goods:【%s,%s,%s】\r\n", indentStr,
				positioin.x, positioin.y, dropGoods.goodsId, dropGoods.goodsId, dropGoods.num);
	}	
	
	@Override
	public ActionType getActionType() {
		return ActionType.DROPGOODS_ACTION;
	}
	
	@Override
	public void write() {
		writeByte(getActionType().getType());
		writeBytes(this.positioin.getBytes());
		writeBytes(this.dropGoods.getBytes());
	}
}
