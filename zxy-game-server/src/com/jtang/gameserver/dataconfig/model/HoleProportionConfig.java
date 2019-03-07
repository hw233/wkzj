package com.jtang.gameserver.dataconfig.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName = "holeProportionConfig")
public class HoleProportionConfig implements ModelAdapter {

	/**
	 * 故事战场id
	 */
	public int storyBattleId;

	/**
	 * 洞府触发几率 洞府id1_几率|洞府id2_几率
	 */
	public String holeProportion;

	@FieldIgnore
	private Map<Integer, String> holeMap = new HashMap<Integer, String>();

	@Override
	public void initialize() {
		parseHoleMap();
		this.holeProportion = null;
	}

	public void parseHoleMap() {
		List<String> list = StringUtils.delimiterString2List(holeProportion, Splitable.ELEMENT_SPLIT);
		for (String item : list) {
			List<String> attrs = StringUtils.delimiterString2List(item, Splitable.ATTRIBUTE_SPLIT);
			int holeId = Integer.valueOf(attrs.get(0));
			String proportion = attrs.get(1);
			holeMap.put(holeId, proportion);
		}
	}

	/**
	 * 随机洞府 返回0则没随机到
	 * @return
	 */
	public int getHole(int level) {
		int proportion = 0;
		int number = RandomUtils.nextInt(0, 1000);
		for (Integer key : holeMap.keySet()) {
			proportion += FormulaHelper.executeInt(holeMap.get(key), level);
			if (number < proportion) {
				return key;
			}
		}
		return 0;
	}
}
