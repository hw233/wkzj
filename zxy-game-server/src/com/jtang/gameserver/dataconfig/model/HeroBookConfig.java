package com.jtang.gameserver.dataconfig.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;

/**
 * 仙人图鉴配置
 * @author ludd
 *
 */
@DataFile(fileName = "heroBookConfig")
public class HeroBookConfig implements ModelAdapter {

	@FieldIgnore
	private static final Logger LOGGER = LoggerFactory.getLogger(HeroBookConfig.class);
	/**
	 * 仙人Id
	 */
	private int heroId;
	
	/**
	 * 需要掌教等级
	 */
	private int heroLevel;
	
	/**
	 * 需要潜修次数
	 */
	private int delveNum;

	@Override
	public void initialize() {	
	}

	public int getHeroId() {
		return heroId;
	}
	
	public int getHeroLevel() {
		return heroLevel;
	}
	
	public int getDelveNum() {
		return delveNum;
	}
}
