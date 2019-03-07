package com.jtang.gameserver.module.vampiir.handler.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 吸灵之后返回的结果
 * 
 * @author pengzy
 * 
 */
public class DoVampiirResponse extends IoBufferSerializer {
	private static final Logger LOGGER = LoggerFactory.getLogger(DoVampiirResponse.class);
	/**
	 * 仙人id
	 */
	private int heroId;
	/**
	 * 英雄升级后的等级
	 */
	private int level;

	/**
	 * 英雄升级后的经验
	 */
	private int exp;

	public DoVampiirResponse(int heroId, int level, int exp) {
		this.heroId = heroId;
		this.level = level;
		this.exp = exp;
	}

	@Override
	public void write() {
		writeInt(heroId);
		writeInt(level);
		writeInt(exp);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("吸灵成功后:heroId = " + heroId + "level = " + level + "exp = " + exp);
		}
	}

}
