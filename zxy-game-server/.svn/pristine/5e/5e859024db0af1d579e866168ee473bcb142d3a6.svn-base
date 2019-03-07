package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName = "giftConfig")
public class GiftConfig implements ModelAdapter {
	/**
	 * 角色等级
	 */
	private int actorLevel;
	
	/**
	 * 礼物配置
	 */
	private String gifts;
	
	/**
	 * 大礼配置
	 */
	private String giftPackage;
	
	@FieldIgnore
	private List<AwardGoodsPacketConfig> giftList = new ArrayList<>();
	
	@FieldIgnore
	private List<AwardGoodsPacketConfig> giftPackageList = new ArrayList<>();
	
	@Override
	public void initialize() {
		parseGoods(this.gifts, this.giftList);
		parseGoods(this.giftPackage, this.giftPackageList);
		
		this.gifts = null;
		this.giftPackage = null;
	}

	/**
	 * 解释物品包的奖励配置
	 */
	protected void parseGoods(String input, List<AwardGoodsPacketConfig> output) {
		List<String> list = StringUtils.delimiterString2List(input, Splitable.ELEMENT_SPLIT);
		for (String str : list) {
			List<String> items = StringUtils.delimiterString2List(str, Splitable.ATTRIBUTE_SPLIT);
			int rate = Integer.valueOf(items.get(0));
			AwardGoodsPacketConfig conf = new AwardGoodsPacketConfig();
			conf.rate = rate;
			for (int i = 1; i < items.size(); i+=2) {
				Integer goodsId = Integer.valueOf(items.get(i));
				Integer num = Integer.valueOf(items.get(i+1));
				conf.goodsMap.put(goodsId, num);
			}
			output.add(conf);
		}
	}

	public int getActorLevel() {
		return actorLevel;
	}

	public List<AwardGoodsPacketConfig> getGiftList() {
		return giftList;
	}

	public List<AwardGoodsPacketConfig> getGiftPackageList() {
		return giftPackageList;
	}
}
