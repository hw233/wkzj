package com.jtang.gameserver.module.goods.type;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 物品使用结果
 * @author ludd
 *
 */
public class UseGoodsResult extends IoBufferSerializer{
	/**
	 * 生成的Id
	 */
	public int id;
	/**
	 * 生成的类型
	 */
	public UseGoodsResultType type;
	/**
	 * 生产的数量
	 */
	public int num;
	public UseGoodsResult(int id, UseGoodsResultType type, int num) {
		super();
		this.id = id;
		this.type = type;
		this.num = num;
	}
	@Override
	public void write() {
		this.writeInt(this.id);
		this.writeByte(this.type.getType());
		this.writeInt(this.num);
	}
	
	public String parse2String() {
		return String.format("%s_%s+%s", id, type.getType(), num);
	}
	
}
