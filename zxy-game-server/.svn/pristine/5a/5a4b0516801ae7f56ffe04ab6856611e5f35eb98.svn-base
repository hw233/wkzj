package com.jtang.gameserver.module.goods.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;

/**
 * 物品vo
 * @author 0x737263
 *
 */
public class GoodsVO extends IoBufferSerializer implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8195418564510589426L;

//	/**
//	 * uuid，物品唯一id
//	 */
//	public long uuid;

	/**
	 * 物品配置id
	 */
	public int goodsId;
	
	/**
	 * 重叠数量
	 */
	public int num;
	
	/**
	 * 合成结束时间
	 */
	public int composeTime;
	
	/**
	 * 
	 * @param goodsString
	 * @return
	 */
	public static GoodsVO valueOf(String[] goodsArray) {
		GoodsVO vo = new GoodsVO();
		goodsArray = StringUtils.fillStringArray(goodsArray, 3, "-1");
//		vo.uuid = Long.valueOf(goodsArray[0]);
		vo.goodsId = Integer.valueOf(goodsArray[0]);
		vo.num = Integer.valueOf(goodsArray[1]);
		vo.composeTime = Integer.valueOf(goodsArray[2]);
		return vo;
	}
	
	/**
	 * 
	 * @param goodsId
	 * @return
	 */
	public static GoodsVO valueOf(int goodsId, int num) {
		GoodsVO vo = new GoodsVO();
//		vo.uuid = UUIDUtils.getUUID(Game.getServerId());
		vo.goodsId = goodsId;
		vo.num = num;
		vo.composeTime = -1;
		return vo;
	}
	
	/**
	 * 
	 * @return
	 */
	public String parse2String() {
		List<Object> list = new ArrayList<Object>();
//		list.add(this.uuid);
		list.add(this.goodsId);
		list.add(this.num);
		list.add(this.composeTime);
		return StringUtils.collection2SplitString(list, Splitable.ATTRIBUTE_SPLIT);
	}
	
	@Override
	public void write() {
		writeLong(Long.valueOf(this.goodsId));
		writeInt(this.goodsId);
		writeInt(this.num);
		if(this.composeTime > 0){
			if(TimeUtils.getNow() - this.composeTime > 0){
				writeInt(0);
			}else{
				writeInt(this.composeTime - TimeUtils.getNow());
			}
		}else{
			writeInt(this.composeTime);
		}
	}
	
}
