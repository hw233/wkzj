package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.mina.util.ConcurrentHashSet;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.hole.model.HoleNotifyVO;
/**
 * 洞府
 * 
 * @author ludd 
 * --以下为db说明--------------------------- 
 * 主键为角色id,每个角色一跳数据
 */
@TableName(name = "holeNotify", type = DBQueueType.IMPORTANT)
public class HoleNotify extends Entity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2594699299844168045L;

	/**
	 * 角色id
	 */
	@Column(pk = true)
	private long actorId;
	
	/**
	 * 通知结构
	 */
	@Column
	private String notify;
	
	private ConcurrentHashSet<HoleNotifyVO> notifyVOs = new ConcurrentHashSet<>();
	
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
		HoleNotify holeNotify = new HoleNotify();
		holeNotify.actorId = rs.getLong("actorId");
		holeNotify.notify = rs.getString("notify");
		return holeNotify;
	}

	@Override
	protected void hasReadEvent() {
		List<String[]> strs = StringUtils.delimiterString2Array(notify);
		for (String[] str : strs) {
			HoleNotifyVO heHoleNotifyVO = new HoleNotifyVO(str);
			notifyVOs.add(heHoleNotifyVO);
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.actorId);
		}
		value.add(this.notify);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		this.notify = StringUtils.collection2SplitString(this.notifyVOs, Splitable.ELEMENT_DELIMITER);
	}

	@Override
	protected void disposeBlob() {
		this.notify = EMPTY_STRING;
		
	}
	

	public static HoleNotify valueOf(long actorId) {
		HoleNotify holeNotify = new HoleNotify();
		holeNotify.actorId = actorId;
		return holeNotify;
	}
	
	public void add(HoleNotifyVO vo) {
		this.notifyVOs.add(vo);
	}
	public void remove(HoleNotifyVO vo) {
		this.notifyVOs.remove(vo);
	}
	
	public Set<HoleNotifyVO> getList() {
		return this.notifyVOs;
	}
	

}
