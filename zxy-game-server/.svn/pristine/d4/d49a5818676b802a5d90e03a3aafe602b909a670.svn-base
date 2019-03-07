package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

/**
 * 被吸灵损耗百分比配置
 * 
 * @author pengzy
 * 
 */
@DataFile(fileName = "vampiirStarConfig")
public class VampiirStarConfig implements ModelAdapter {
	/**
	 * 仙人星阶
	 */
	private int star;
	/**
	 * 被吸收时去掉损耗之后的百分比
	 */
	private int proportion;

	/**
	 * 仙人被吸灵的时候根据星阶增加的经验
	 */
	private int heroExp;
	/**
	 * 仙人魂魄提供的经验
	 */
	private int heroSoulExp;
	@Override
	public void initialize() {
	}

	public int getStar() {
		return star;
	}

	
	/**
	 * 被吸收时去掉损耗之后的百分比
	 * @return
	 */
	public float getProportion() {
		return proportion / 100.0f;
	}

	public int getHeroExp() {
		return heroExp;
	}

	public int getHeroSoulExp() {
		return heroSoulExp;
	}
	
}
