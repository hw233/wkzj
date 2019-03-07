package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dataconfig.model.LadderGlobalConfig;
import com.jtang.gameserver.dataconfig.service.LadderService;
import com.jtang.gameserver.module.ladder.model.FightInfoVO;

/**
 * 天梯表
 * @author jianglf
 *
 */
@TableName(name = "ladder", type = DBQueueType.IMPORTANT)
public class Ladder extends Entity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1156956272482339504L;

	/**
	 * 玩家id
	 */
	@Column(pk = true)
	public long actorId;
	
	/**
	 * 赢场
	 */
	@Column
	public int win;
	
	/**
	 * 输场
	 */
	@Column
	public int lost;
	
	/**
	 * 积分
	 */
	@Column
	public int score;
	
	/**
	 * 连胜场次
	 */
	@Column
	public int winStreak;
	
	/**
	 * 战斗记录
	 */
	@Column
	public String fightInfo;
	
	/**
	 * 赛季奖励
	 * type_id_num|...
	 */
	@Column
	public String reward;
	
	/**
	 * 可战斗次数
	 */
	@Column
	public int fightNum;
	
	/**
	 * 上一次刷新战斗次数时间
	 */
	@Column
	public int flushFightTime;
	
	/**
	 * 点券购买战斗次数
	 */
	@Column
	public int costTicketNum;
	
	/**
	 * 点券购买战斗次数时间
	 */
	@Column
	public int ticketFlushTime;
	
	/**
	 * 更换对手次数
	 */
	@Column
	public int flushActor;
	
	/**
	 * 更换对手时间
	 */
	@Column
	public int flushActorTime;
	
	/**
	 * 最后一次战斗时间
	 */
	@Column
	public int lastFightTime;
	
	/**
	 * 当前赛季id
	 */
	@Column
	public long sportId;
	
	/**
	 * 赛季排名
	 */
	@Column
	public int rank;
	
	/**
	 * 历史赛季最高排名
	 */
	@Column
	public int historyRank;
	
	/**
	 * 刷新对手列表
	 * actorId_actorId_...
	 */
	@Column
	public String actors;
	
	/**
	 * 战斗记录
	 */
	public ConcurrentLinkedQueue<FightInfoVO> fightList = new ConcurrentLinkedQueue<>();
	
	/**
	 * 赛季奖励
	 */
	public List<RewardObject> rewardList = new ArrayList<>();
	
	/**
	 * 对手列表
	 */
	public List<Long> actorList = new ArrayList<>();
	
	@Override
	public Long getPkId() {
		return actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum)
			throws SQLException {
		Ladder ladder = new Ladder();
		ladder.actorId = rs.getLong("actorId");
		ladder.win = rs.getInt("win");
		ladder.lost = rs.getInt("lost");
		ladder.score = rs.getInt("score");
		ladder.winStreak = rs.getInt("winStreak");
		ladder.fightInfo = rs.getString("fightInfo");
		ladder.reward = rs.getString("reward");
		ladder.fightNum = rs.getInt("fightNum");
		ladder.flushFightTime = rs.getInt("flushFightTime");
		ladder.costTicketNum = rs.getInt("costTicketNum");
		ladder.ticketFlushTime = rs.getInt("ticketFlushTime");
		ladder.flushActor = rs.getInt("flushActor");
		ladder.flushActorTime = rs.getInt("flushActorTime");
		ladder.lastFightTime = rs.getInt("lastFightTime");
		ladder.sportId = rs.getLong("sportId");
		ladder.rank = rs.getInt("rank");
		ladder.historyRank = rs.getInt("historyRank");
		ladder.actors = rs.getString("actors");
		return ladder;
	}

	@Override
	protected void hasReadEvent() {
		List<String[]> fightList = StringUtils.delimiterString2Array(this.fightInfo);
		for(String[] str:fightList){
			this.fightList.add(new FightInfoVO(str));
		}
		
		List<String[]> list = StringUtils.delimiterString2Array(this.reward);
		for(String[] str:list){
			RewardObject reward = RewardObject.valueOf(str);
			rewardList.add(reward);
		}
		
		this.actorList = StringUtils.delimiterString2LongList(actors, Splitable.ATTRIBUTE_SPLIT);
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> values = new ArrayList<>();
		if(containsPK){
			values.add(this.actorId);
		}
		values.add(this.win);
		values.add(this.lost);
		values.add(this.score);
		values.add(this.winStreak);
		values.add(this.fightInfo);
		values.add(this.reward);
		values.add(this.fightNum);
		values.add(this.flushFightTime);
		values.add(this.costTicketNum);
		values.add(this.ticketFlushTime);
		values.add(this.flushActor);
		values.add(this.flushActorTime);
		values.add(this.lastFightTime);
		values.add(this.sportId);
		values.add(this.rank);
		values.add(this.historyRank);
		values.add(this.actors);
		return values;
	}

	@Override
	protected void beforeWritingEvent() {
		StringBuffer fightString = new StringBuffer();
		for(FightInfoVO fightInfoVO:fightList){
			fightString.append(fightInfoVO.parser2String()).append(Splitable.ELEMENT_DELIMITER);
		}
		if(fightString.length() > 0){
			fightString.deleteCharAt(fightString.length() - 1);
			this.fightInfo = fightString.toString();
		}
		StringBuffer rewardString = new StringBuffer();
		for(RewardObject rewardObject:rewardList){
			rewardString.append(rewardObject.toString()).append(Splitable.ELEMENT_DELIMITER);
		}
		if(rewardString.length() > 0){
			rewardString.deleteCharAt(rewardString.length() - 1);
			this.reward = rewardString.toString();
		}
		StringBuffer actorString = new StringBuffer();
		for(Long actorId:actorList){
			actorString.append(actorId).append(Splitable.ATTRIBUTE_SPLIT);
		}
		if(actorString.length() > 0){
			actorString.deleteCharAt(actorString.length() - 1);
			this.actors = actorString.toString();
		}
	}

	@Override
	protected void disposeBlob() {
		fightInfo = EMPTY_STRING;
		reward = EMPTY_STRING;
	}

	public static Ladder valueOf(long actorId) {
		Ladder ladder = new Ladder();
		ladder.actorId = actorId;
		LadderGlobalConfig config = LadderService.getGlobalConfig();
		ladder.fightNum = config.maxFightNum;
		ladder.flushFightTime = TimeUtils.getNow();
		ladder.score = config.baseScore;
		return ladder;
	}

	public void cleanLastSport(long nowSport) {
		this.win = 0;
		this.lost = 0;
		this.score = LadderService.getGlobalConfig().baseScore;
		this.winStreak = 0;
		this.fightList.clear();
		this.sportId = nowSport;
	}
	
	public boolean isAddUseNum(int configFlushTime, int maxStar) {
		int now = TimeUtils.getNow();
		int num = (now - flushFightTime) / configFlushTime;
		if (num == 0) {
			return false;
		}
		if (num + fightNum > maxStar) {
			fightNum = maxStar;
		} else {
			fightNum += num;
		}
		flushFightTime = now - ((now - flushFightTime) % configFlushTime);
		return true;
	}

}
