package com.jtang.worldserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

/**
 * 跨服战每日伤害排名表
 * 
 * <pre>
 * (每日开赛前清除前一天的)
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * 
 * @author 0x737263
 * 
 */
@TableName(name = "crossBattleHurtRank", type = DBQueueType.IMPORTANT)
public class CrossBattleHurtRank extends Entity<Long> {
	private static final long serialVersionUID = -2265933598662661133L;

	/**
	 * 角色id
	 */
	@Column(pk = true)
	public long actorId;

	/**
	 * 所在服务器id
	 */
	@Column
	public int serverId;

	/**
	 * 本服总伤伤量排名
	 */
	@Column
	public int rank;

	/**
	 * 造成伤害总量
	 */
	@Column
	public long totalHurt;

	/**
	 * 累计杀人次数(每日报名时更新)
	 */
	@Column
	public int totalKillNum;

	/**
	 * 连续杀人次数(比赛中玩家死后清零)
	 */
	@Column
	public int continueKillNum;

	/**
	 * 被杀次数(每日报名时更新)
	 */
	@Column
	public int beKilledNum;

	/**
	 * 累计杀人奖劢贡献点(每日报名时清空该数据)
	 */
	@Column
	public int dayKillExchangePoint;

	/**
	 * 累计排名奖劢贡献点(每日报名时清空该数据)
	 */
	@Column
	public int dayRankExchangePoint;
	/**
	 * 获胜服务器贡献点(每日报名时清空该数据)
	 */
	@Column
	public int dayWinServerExchangePoint;

	/**
	 * 整个赛季结束后已领取的奖励id 格式:awardId_awardId
	 */
	@Column
	private String endAwardRecord;

	/**
	 * 最近一次物品奖励记录(每天开赛后会清空该数据) 格式: RewardObject 类型_物品id_数量|类型_物品id_数量
	 */
	@Column
	private String dayGoodsRecord;
	
	/**
	 * 角色名
	 */
	@Column
	public String actorName;
	
	public List<RewardObject> endAwardRecordList = new ArrayList<>();

	public List<RewardObject> dayGoodsRecordList = new ArrayList<>();

	@Override
	public Long getPkId() {
		return actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		CrossBattleHurtRank crossBattleHurtrank = new CrossBattleHurtRank();
		crossBattleHurtrank.actorId = rs.getLong("actorId");
		crossBattleHurtrank.serverId = rs.getInt("serverId");
		crossBattleHurtrank.rank = rs.getInt("rank");
		crossBattleHurtrank.totalHurt = rs.getLong("totalHurt");
		crossBattleHurtrank.totalKillNum = rs.getInt("totalKillNum");
		crossBattleHurtrank.continueKillNum = rs.getInt("continueKillNum");
		crossBattleHurtrank.beKilledNum = rs.getInt("beKilledNum");
		crossBattleHurtrank.dayKillExchangePoint = rs.getInt("dayKillExchangePoint");
		crossBattleHurtrank.dayRankExchangePoint = rs.getInt("dayRankExchangePoint");
		crossBattleHurtrank.dayWinServerExchangePoint = rs.getInt("dayWinServerExchangePoint");
		crossBattleHurtrank.endAwardRecord = rs.getString("endAwardRecord");
		crossBattleHurtrank.dayGoodsRecord = rs.getString("dayGoodsRecord");
		crossBattleHurtrank.actorName = rs.getString("actorName");
		return crossBattleHurtrank;
	}

	@Override
	protected void hasReadEvent() {

		List<String[]> list = StringUtils.delimiterString2Array(dayGoodsRecord);
		for (String[] array : list) {
			dayGoodsRecordList.add(RewardObject.valueOf(array));
		}
		list = StringUtils.delimiterString2Array(endAwardRecord);
		for (String[] array : list) {
			endAwardRecordList.add(RewardObject.valueOf(array));
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> list = new ArrayList<Object>();
		if (containsPK) {
			list.add(actorId);
		}
		list.add(serverId);
		list.add(rank);
		list.add(totalHurt);
		list.add(totalKillNum);
		list.add(continueKillNum);
		list.add(beKilledNum);
		list.add(dayKillExchangePoint);
		list.add(dayRankExchangePoint);
		list.add(dayWinServerExchangePoint);
		list.add(endAwardRecord);
		list.add(dayGoodsRecord);
		list.add(actorName);
		return list;
	}

	@Override
	protected void beforeWritingEvent() {
		if (endAwardRecordList.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (RewardObject rewardObject : endAwardRecordList) {
				sb.append(rewardObject.parse2String());
				sb.append(Splitable.ELEMENT_DELIMITER);
			}
			sb.deleteCharAt(sb.length() - 1);
			endAwardRecord = sb.toString();
		} else {
			endAwardRecord = "";
		}
		
		if (dayGoodsRecordList.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (RewardObject rewardObject : dayGoodsRecordList) {
				sb.append(rewardObject.parse2String());
				sb.append(Splitable.ELEMENT_DELIMITER);
			}
			sb.deleteCharAt(sb.length() - 1);
			dayGoodsRecord = sb.toString();
		} else {
			dayGoodsRecord = "";
		}
	}

	public static CrossBattleHurtRank valueOf(long actorId) {
		CrossBattleHurtRank crossBattleHurtrank = new CrossBattleHurtRank();
		crossBattleHurtrank.actorId = actorId;
		crossBattleHurtrank.serverId = 0;
		crossBattleHurtrank.rank = 0;
		crossBattleHurtrank.totalHurt = 0;
		crossBattleHurtrank.totalKillNum = 0;
		crossBattleHurtrank.continueKillNum = 0;
		crossBattleHurtrank.beKilledNum = 0;
		crossBattleHurtrank.dayKillExchangePoint = 0;
		crossBattleHurtrank.dayRankExchangePoint = 0;
		crossBattleHurtrank.dayWinServerExchangePoint = 0;
		crossBattleHurtrank.endAwardRecord = "";
		crossBattleHurtrank.dayGoodsRecord = "";
		crossBattleHurtrank.actorName = "";
		return crossBattleHurtrank;
	}

	@Override
	protected void disposeBlob() {
		dayGoodsRecord = EMPTY_STRING;
		endAwardRecord = EMPTY_STRING;
	}

}
