package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;

/**
 * 战斗录象
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为通知的id（当该条通知删除的时候也会同时删除该录像)
 * </pre>
 * @author 0x737263
 *
 */
@TableName(name="fightvideo", type = DBQueueType.NONE)
public class FightVideo extends Entity<Long> {
	private static final long serialVersionUID = -3825758589157668683L;

	/**
	 * mysql自增加id
	 */
	@Column(pk = true)
	private long videoId;
	
	/**
	 * 录像所属角色id
	 */
	@Column
	public long actorId;
	
	/**
	 * 录像对应的通知id
	 */
	@Column
	private long notifyId;
	
	/**
	 * 录像数据
	 */
	@Column
	public byte[] videoData;
	
	@Override
	public Long getPkId() {
		return this.videoId;
	}

	@Override
	public void setPkId(Long pk) {
		this.videoId = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		FightVideo fightVideo = new FightVideo();
		fightVideo.videoId = rs.getLong("videoId");
		fightVideo.actorId = rs.getLong("actorId");
		fightVideo.notifyId = rs.getLong("notifyId");
		fightVideo.videoData = rs.getBytes("videoData");
		return fightVideo;
	}

	@Override
	protected void hasReadEvent() {
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(videoId);
		}
		value.add(actorId);
		value.add(notifyId);
		value.add(videoData);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
	}
	
	public static FightVideo valueOf(long actorId, long notifyId, byte[] videoData) {
		FightVideo entity = new FightVideo();
		entity.actorId = actorId;
		entity.notifyId = notifyId;
		entity.videoData = videoData;
		return entity;
	}
	
	@Override
	protected void disposeBlob() {
		
	}

}
