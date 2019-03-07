package com.jtang.gameserver.module.power.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.model.PowerShopConfig;

public class PowerShopVO extends IoBufferSerializer {

	/**
	 * 商品id
	 */
	public int id;
	
	/**
	 * 物品id
	 */
	public int goodsId;
	
	/**
	 * 物品类型
	 */
	public int type;
	
	/**
	 * 物品数量
	 */
	public int num;
	
	/**
	 * 购买消耗的物品数量
	 */
	public int costGoods;
	
	public PowerShopVO(){
		
	}
	
	public PowerShopVO(PowerShopConfig powerShopConfig){
		this.id = powerShopConfig.id;
		this.goodsId = powerShopConfig.itemId;
		this.type = powerShopConfig.type;
		this.num = powerShopConfig.num;
		this.costGoods = powerShopConfig.costGoods;
	}
	
	public static PowerShopVO valueOf(String str[]){
		str = StringUtils.fillStringArray(str, 5, "0");
		PowerShopVO loveShopVO = new PowerShopVO();
		loveShopVO.id = Integer.valueOf(str[0]);
		loveShopVO.goodsId = Integer.valueOf(str[1]);
		loveShopVO.type = Integer.valueOf(str[2]);
		loveShopVO.num = Integer.valueOf(str[3]);
		loveShopVO.costGoods = Integer.valueOf(str[4]);
		return loveShopVO;
	}
	
	public String parser2String(){
		StringBuffer sb = new StringBuffer();
		sb.append(id).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(goodsId).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(type).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(num).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(costGoods);
		return sb.toString();
	}
	
	@Override
	public void write() {
		writeInt(id);
		writeInt(goodsId);
		writeByte((byte)type);
		writeInt(num);
		writeInt(costGoods);
	}
}
