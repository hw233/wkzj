package com.jtang.gameserver.module.adventures.bable.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.schedule.Schedule;
import com.jtang.gameserver.dataconfig.model.BableRankRewardConfig;
import com.jtang.gameserver.dataconfig.service.BableRankService;
import com.jtang.gameserver.dataconfig.service.BableService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.BableRecord;
import com.jtang.gameserver.module.adventures.bable.dao.BableRecordDao;
import com.jtang.gameserver.module.adventures.bable.facade.BableRankFacade;
import com.jtang.gameserver.module.adventures.bable.model.BableRankVO;
import com.jtang.gameserver.module.icon.facade.IconFacade;
import com.jtang.gameserver.module.icon.model.IconVO;
import com.jtang.gameserver.module.sysmail.facade.SysmailFacade;
import com.jtang.gameserver.module.sysmail.type.SysmailType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;

@Component
public class BableRankFacadeImpl implements BableRankFacade{

	private static final Logger LOGGER = LoggerFactory.getLogger(BableRankFacadeImpl.class);
	@Autowired
	private BableRecordDao bableRecordDao;
	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private VipFacade vipFacade;
	
	@Autowired
	private Schedule schedule;
	
	@Autowired
	private SysmailFacade sysmailFacade;
	@Autowired
	private IconFacade iconFacade;
	
	/**
	 * 统计标志
	 */
	private boolean statistics = false;
	
	@Override
	public Map<Integer,List<BableRankVO>> getRank() {
		Map<Integer,List<BableRankVO>> map = new HashMap<>();
		for(Integer bableId : BableService.getBABLE_ID_LIST()){
			List<BableRecord> ranks = bableRecordDao.getRank(bableId);
			List<BableRankVO> rankVOs = record2VO(ranks);
			map.put(bableId, rankVOs);
		}
		return map;
	}

	/**
	 * 创建排行（系统启动时及每天零点时创建排行榜）
	 */
	@Override
	public void createRank() {
		statistics = true;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("登天塔统计开始");
		}
		bableRecordDao.createRank();
		statistics = false;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("登天塔统计结束");
		}
	}
	
	/**
	 * record转vo
	 * @param records
	 * @return
	 */
	private List<BableRankVO> record2VO(List<BableRecord> records) {
		List<BableRankVO> bableRankVOs = new ArrayList<>();
		for (int i = 0; i < records.size(); i++) {
			BableRecord bableRecord = records.get(i);
			int rank = i + 1;
			Actor actor = actorFacade.getActor(bableRecord.actorId);
			int vipLevel = vipFacade.getVipLevel(actor.getPkId());
			IconVO iconVO = iconFacade.getIconVO(bableRecord.actorId);
			BableRankVO bableRankVO = new BableRankVO(actor.getPkId(), bableRecord.bableId, bableRecord.maxFloor, bableRecord.maxStar, rank,
					actor.actorName, vipLevel, actor.level,iconVO);
			bableRankVOs.add(bableRankVO);
		}

		return bableRankVOs;
	}

	/**
	 * 发送每日排行奖励到系统邮箱
	 */
	@Override
	public void sendReward() {
		Map<Integer, List<BableRecord>> ranks = bableRecordDao.allRankRecords();
		for (Entry<Integer, List<BableRecord>> entry : ranks.entrySet()) {
			int key = entry.getKey();
			List<BableRecord> values = entry.getValue();
			for (int i = 0; i < values.size(); i++) {
				BableRecord bableRecord = values.get(i);
				int rank = i + 1;
				BableRankRewardConfig cfg = BableRankService.get(key, rank);
				if (cfg != null) {
					sysmailFacade.sendSysmail(bableRecord.actorId, SysmailType.BABLE_RANK, cfg.rewardObjects, bableRecord.bableId, rank);
				} else {
					LOGGER.warn(String.format("登天塔排行榜奖励不存在，bableId:[%s]，rank：[%s]", key, rank));
				}
			}
		}
	}
	
	@Override
	public boolean isStatistics() {
		return this.statistics;
	}
}
