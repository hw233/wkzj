package com.jtang.gameserver.module.notify.model.impl;
//package com.jtang.sm2.module.notify.model.impl;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import com.jtang.core.utility.Splitable;
//import com.jtang.core.utility.StringUtils;
//import com.jtang.sm2.module.notify.model.AbstractNotifyVO;
//
///**
// * 势力排名奖励通知结构
// * @author pengzy
// *
// */
//public class PowerRewardNotifyVO extends AbstractNotifyVO {
//	private static final long serialVersionUID = -56004781092720350L;
//
//	/**
//	 * 当前名次
//	 */
//	public int rank;
//	
//	/**
//	 * 仙人魂魄id
//	 */
//	public int heroSoulId;
//	
//	/**
//	 * 仙人魂魄数量
//	 */
//	public int heroSoulNum;
//	
//	/**
//	 * 奖励物品列表
//	 */
//	public Map<Integer, Integer> goods = new HashMap<>();
//	
//	public PowerRewardNotifyVO() {
//		
//	}
//	
//	public PowerRewardNotifyVO(int rank, int heroSoulId, int heroSoulNum, Map<Integer, Integer> goods) {
//		this.rank = rank;
//		this.heroSoulId = heroSoulId;
//		this.heroSoulNum = heroSoulNum;
//		this.goods = goods;
//	}
//	
////	public static PowerRewardNotifyVO valueOf(String extension) {
////		List<String> attributes = StringUtils.delimiterString2List(extension, Splitable.ATTRIBUTE_SPLIT);
////		// id_num_1:2,2:3,3,4
////		int rank = Integer.valueOf(attributes.get(0));
////		int heroSoulId = Integer.valueOf(attributes.get(1));
////		int heroSoulNum = Integer.valueOf(attributes.get(2));
////		Map<Integer, Integer> goodsMaps = StringUtils.keyValueString2IntMap(attributes.get(3));
////
////		return new PowerRewardNotifyVO(rank, heroSoulId, heroSoulNum, goodsMaps);
////	}
//
//	@Override
//	protected void subClazzWrite() {
//		writeInt(this.rank);
//		writeInt(this.heroSoulId);
//		writeInt(this.heroSoulNum);
//		writeShort((short) goods.size());
//		for (Entry<Integer, Integer> entry : goods.entrySet()) {
//			writeInt(entry.getKey());
//			writeInt(entry.getValue());
//		}
//	}
//
//	@Override
//	protected void subClazzString2VO(String[] items) {
//		// id_num_1:2,2:3,3,4
//		rank = Integer.valueOf(items[0]);
//		heroSoulId = Integer.valueOf(items[1]);
//		heroSoulNum = Integer.valueOf(items[2]);
//		goods.putAll(StringUtils.keyValueString2IntMap(items[3]));
//	}
//
//	@Override
//	protected void subClazzParse2String(List<String> attributes) {
//		attributes.add(String.valueOf(this.rank));
//		attributes.add(String.valueOf(this.heroSoulId));
//		attributes.add(String.valueOf(this.heroSoulNum));
//		String goodsString = StringUtils.map2DelimiterString(this.goods, Splitable.DELIMITER_ARGS, Splitable.BETWEEN_ITEMS);
//		attributes.add(goodsString);
//	}
//}
