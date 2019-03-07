package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dataconfig.service.SnatchService;
import com.jtang.gameserver.module.snatch.constant.SnatchRule;
import com.jtang.gameserver.module.snatch.helper.SnatchHelper;
import com.jtang.gameserver.module.snatch.model.Enemy;

/**
 * 抢夺
 * <pre>
 * --以下为db说明---------------------------
 * 说明:
 * >主键为actorId,每个角色有一行记录
 * </pre>
 * @author liujian
 *
 */
@TableName(name="snatch", type = DBQueueType.IMPORTANT)
public class Snatch extends Entity<Long> {
	private static final long serialVersionUID = -1188208831748956942L;

	/**
	 * 角色id  主键
	 */
	@Column(pk = true)
	private long actorId;
	
	/**
	 * 玩家的积分
	 */
	@Column
	public int score;
		
	/**
	 * 正在进行的成就,格式:成就id_已完成次数
	 */
	@Column
	private String achivement;
	
	/**
	 * 最近交手的敌人
	 * 格式:
	 * 敌人id_对战时间点_攻击类型(我抢他、他抢我)|敌人id_对战时间点_攻击类型(我抢他、他抢我)
	 */
	@Column
	public String enemies;
	
	/**
	 * 抢夺次数
	 */
	@Column
	public int snatchNum;
	
	/**
	 * 已经抢夺的次数
	 */
	@Column
	public int useSnatch;
	
	/**
	 * 上次战斗时间
	 */
	@Column
	public int lastFightTime;
	
	/**
	 * 上次点券购买次数时间
	 */
	@Column
	public int lastTicketTime;
	
	/**
	 * 点券已购买次数
	 */
	@Column
	public int ticketNum;
	
	/**
	 * 上次自动回复战斗次数的时间
	 */
	@Column
	public int flushFightTime;
	
	/**
	 * 上次领取任务的时间
	 */
	@Column
	public int getTime;
	
	/**
	 * 正在进行的成就
	 * key: 成就id  value:成就值
	 */
	private Map<Integer, Integer> achimentMap = new ConcurrentHashMap<>();
	
	/**
	 * 敌人列表
	 */
	private List<Enemy> enemyList = new Vector<>();
	
	@Override
	public Long getPkId() {
		return actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}

	public static Snatch valueOf(long actorId) {
		Snatch snatch = new Snatch();
		snatch.actorId = actorId;
		snatch.score = 0;
		snatch.achivement = "";
		snatch.achimentMap = SnatchHelper.getInitAchieve();
		snatch.snatchNum = SnatchService.get().snatchMaxNum;
		return snatch;
	}

	public Map<Integer, Integer> getAchimentMap() {
		return this.achimentMap;
	}
	
	/**
	 * 完成某成就
	 * @param achimentId
	 */
	public void finishAchiment(int achimentId) {
		this.getAchimentMap().remove(achimentId);
	}
	
	/**
	 * 添加新成就
	 * @param achimentId
	 * @param num
	 */
	public void newAchiment(int achimentId, int num) {
		this.getAchimentMap().put(achimentId, num);
	}
	
	/**
	 * 添加多个新成就
	 * @param map
	 */
	public void newAchiment(Map<Integer, Integer> map) {
		this.getAchimentMap().putAll(map);
	}

	/**
	 * 获取旧敌列表
	 * @return
	 */
	public List<Enemy> getEnemyList() {
		return this.enemyList;
	}
		
	/**
	 * 添加最近交手的敌人
	 * @param enmy
	 */
	public void addEnemy(Enemy enmy) {
		List<Enemy> list = getEnemyList();
		list.add(enmy);
		if (list.size() > SnatchRule.SNATCH_RESERVE_ENEMY_COUNT) {
			list.remove(0);
		}
	}
	
	/**
	 * 变更积分
	 * @param scoreChange
	 */
	public void changeScore(int scoreChange) {
		this.score += scoreChange;
		if (this.score < 1) {
			this.score = 0;
		}
	}
	
	@Override
	public int hashCode() {
		// 为了匹配方便。搞个简单的hashcode
		return (actorId + "|" + score).hashCode();
	}


	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Snatch snatch = new Snatch();
		snatch.actorId = rs.getLong("actorId");
		snatch.score = rs.getInt("score");
		snatch.achivement = rs.getString("achivement");
		snatch.enemies = rs.getString("enemies");
		snatch.snatchNum = rs.getInt("snatchNum");
		snatch.useSnatch = rs.getInt("useSnatch");
		snatch.lastFightTime = rs.getInt("lastFightTime");
		snatch.lastTicketTime = rs.getInt("lastTicketTime");
		snatch.ticketNum = rs.getInt("ticketNum");
		snatch.flushFightTime = rs.getInt("flushFightTime");
		snatch.getTime = rs.getInt("getTime");
		return snatch;
	}

	@Override
	protected void hasReadEvent() {
		
		//成就对象处理
		this.achimentMap = StringUtils.delimiterString2IntMap(this.achivement);
		
		
		//敌人列表处理
		List<String> enemyStringList = StringUtils.delimiterString2List(this.enemies, Splitable.ELEMENT_SPLIT);
		for (String item : enemyStringList) {
			List<String> atts = StringUtils.delimiterString2List(item, Splitable.ATTRIBUTE_SPLIT);
			enemyList.add(Enemy.valueOf(atts));
		}
		// 清除旧数据，保留x天的最近交手的敌人
		Iterator<Enemy> iter = enemyList.iterator();
		while (iter.hasNext()) {
			Enemy e = iter.next();
			if (DateUtils.getNowInSecondes() - e.snatchTime >= SnatchRule.SNATCH_RESERVE_ENEMY_DAY * 24 * 3600) {
				iter.remove();
			} else {
				break;
			}
		}
		
	}


	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.actorId);
		}
		value.add(this.score);
		value.add(this.achivement);
		value.add(this.enemies);
		value.add(this.snatchNum);
		value.add(this.useSnatch);
		value.add(this.lastFightTime);
		value.add(this.lastTicketTime);
		value.add(this.ticketNum);
		value.add(this.flushFightTime);
		value.add(this.getTime);
		return value;
	}


	@Override
	protected void beforeWritingEvent() {
		
		this.achivement = StringUtils.map2DelimiterString(this.getAchimentMap(), Splitable.ATTRIBUTE_SPLIT, Splitable.ELEMENT_DELIMITER);
		
		StringBuilder sb = new StringBuilder("");
		if (enemyList.size() > 0) {
			for (Enemy e : this.enemyList) {
				sb.append(e.actorId).append(Splitable.ATTRIBUTE_SPLIT);
				sb.append(e.snatchTime).append(Splitable.ATTRIBUTE_SPLIT);
				sb.append(e.hatredType).append(Splitable.ELEMENT_DELIMITER);
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		this.enemies = sb.toString();
	}
	
	@Override
	protected void disposeBlob() {
		achivement = EMPTY_STRING;
		enemies = EMPTY_STRING;
	}

	public void setAchieveMent(Map<Integer, Integer> achieve) {
		this.achimentMap = achieve;
	}
	
	public boolean isAddUseNum(int configFlushTime, int maxStar) {
		int now = TimeUtils.getNow();
		int num = (now - flushFightTime) / configFlushTime;
		if (num == 0) {
			return false;
		}
		if (num + snatchNum > maxStar) {
			snatchNum = maxStar;
		} else {
			snatchNum += num;
		}
		flushFightTime = now - ((now - flushFightTime) % configFlushTime);
		return true;
	}
	
}
