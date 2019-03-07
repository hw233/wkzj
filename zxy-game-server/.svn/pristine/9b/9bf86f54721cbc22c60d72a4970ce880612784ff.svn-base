package com.jtang.gameserver.module.treasure.model;

import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.TreasureConfig;
import com.jtang.gameserver.dataconfig.model.RewardConfig;

public class GridVO extends IoBufferSerializer {

	/**
	 * 格子横坐标
	 */
	public int gridX;

	/**
	 * 格子纵坐标
	 */
	public int gridY;

	/**
	 * 格子奖励
	 */
	public RewardObject rewardObject;

	/**
	 * 是否有怪物
	 */
	public int monster;

	/**
	 * 是否已领取 0.未领取,1.已领取
	 */
	public int state;

	/**
	 * 是否有大奖 0.否,1.是
	 */
	public int isBigGift;

	public static GridVO valueOf(TreasureConfig config, int level) {
		GridVO gridVO = new GridVO();
		gridVO.gridX = config.gridX;
		gridVO.gridY = config.gridY;
		RewardConfig reward = config.getContent();
		if (reward != null) {
			int num = FormulaHelper.executeCeilInt(reward.num, level);
			gridVO.rewardObject = new RewardObject(RewardType.getType(reward.type), reward.rewardId, num);
		} else {
			gridVO.rewardObject = null;
		}
		gridVO.monster = RandomUtils.is1000Hit(config.monster) == true ? 1 : 0;
		gridVO.state = 0;
		gridVO.isBigGift = 0;
		return gridVO;
	}

	public boolean isGet() {
		return state == 1;
	}

	public boolean isBigGift() {
		return isBigGift == 1;
	}

	public boolean isMonster() {
		return monster == 1;
	}

	@Override
	public void write() {
		writeInt(gridX);
		writeInt(gridY);
		writeBytes(rewardObject.getBytes());
		writeInt(monster);
		writeInt(state);
		writeInt(isBigGift);
	}

}
