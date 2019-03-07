package com.jtang.gameserver.dataconfig.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardType;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.StringUtils;
/**
 * 宝箱使用保底配置
 * @author ludd
 *
 */
@DataFile(fileName = "goodsUseConfig")
public class GoodsUseConfig implements ModelAdapter {
	/**
	 * 物品id
	 */
	public int useGoodsId;
	/**
	 * 使用次数最小值
	 */
	public int numberOfUseMin;
	/**
	 * 使用次数最大值
	 */
	public int numberOfUseMax;
	/**
	 * 奖励物品
	 */
	private String rewardGoods;
	@FieldIgnore
	private Map<Integer,LeastGoods> map = new HashMap<>();
	@Override
	public void initialize() {
		if (StringUtils.isNotBlank(this.rewardGoods)) {
			List<String[]> list = StringUtils.delimiterString2Array(rewardGoods);
			for (String[] item : list) {
				LeastGoods leastGoods = new LeastGoods(item);
				map.put(leastGoods.id, leastGoods);
			}
			this.rewardGoods = null;
		}
	}
	

	/**
	 * 保底物品
	 * @author ludd
	 *
	 */
	public class LeastGoods {
		/**
		 * 唯一标识
		 */
		public int id;
		/**
		 * 数量
		 */
		public int num;
		/**
		 * 奖励类型
		 */
		public RewardType rewardType;
		/**
		 * 概率千分比
		 */
		public int rate;

		public LeastGoods(String[] item) {
			super();
			item = StringUtils.fillStringArray(item, 4, "0");
			this.rewardType = RewardType.getType(Integer.valueOf(item[0]));
			this.id = Integer.valueOf(item[1]);
			this.num = Integer.valueOf(item[2]);
			this.rate = Integer.valueOf(item[3]);
		}
		
		
	}


	public LeastGoods getRandomGoods() {
		Map<Integer, Integer> keys = new HashMap<Integer, Integer>();
		for (Integer key : map.keySet()) {
			LeastGoods leastGoods = map.get(key);
			keys.put(key, leastGoods.rate);
		}
		
		Integer key = RandomUtils.randomHit(1000, keys);
		if(key == null){
			return null;
		}
		return map.get(key);
		
	}
}
