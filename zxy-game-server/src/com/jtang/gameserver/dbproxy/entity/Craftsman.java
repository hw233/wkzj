package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
/**
 * 神匠来袭
 * <pre>
 * --以下为db说明---------------------------
 * 说明:
 * >主键为actorId,每个角色有一行记录
 * </pre>
 * @author lig
 *
 */
@TableName(name="craftsman", type=DBQueueType.IMPORTANT)
public class Craftsman extends Entity<Long> {
	private static final long serialVersionUID = 3610514194696935233L;

	/**
	 * 角色id
	 */
	@Column(pk = true)
	private long actorId;
	
	/**
	 * 打造次数
	 */
	@Column
	public byte buildNum;
	
	@Column
	public int operationTime;
	
	@Override
	public Long getPkId() {
		return actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet resultset, int rowNum) throws SQLException {
		Craftsman man = new Craftsman();
		man.actorId = resultset.getLong("actorId");
		man.buildNum = resultset.getByte("buildNum");
		man.operationTime = resultset.getInt("operationTime");
		return man;
	}

	@Override
	protected void hasReadEvent() {

	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.actorId);

		}
		value.add(this.buildNum);
		value.add(this.operationTime);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {

	}

	public static Craftsman valueOf(long actorId) {
		Craftsman man = new Craftsman();
		man.actorId = actorId;
		man.buildNum = 0;
		return man;
	}
	
	@Override
	protected void disposeBlob() {
		
	}

	public void reset() {
		this.buildNum = 0;
		
	}

	
	
	

}
