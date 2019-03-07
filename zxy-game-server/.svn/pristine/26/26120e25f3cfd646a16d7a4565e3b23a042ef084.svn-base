package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
/**
 * <pre>
 * 集众降魔全局表
 * --以下为db说明---------------------------
 * 主键为难度id,一个难度对应一行数据
 * </pre>
 * @author ludd
 *
 */
@TableName(name="demonGlobal", type=DBQueueType.DEFAULT)
public class DemonGlobal extends Entity<Long> {
	private static final long serialVersionUID = 7822031289620812215L;

	/**
	 * 难度Id
	 */
	@Column(pk = true)
	private long difficult;
	
	/**
	 * 上次boss收到的伤害总和
	 */
	@Column
	public int lastHeart;
	
	/**
	 * 打死boss耗时（秒）
	 */
	@Column
	public int bossDeadUseTime;
	
	@Override
	public Long getPkId() {
		return difficult;
	}

	@Override
	public void setPkId(Long pk) {
		this.difficult = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		DemonGlobal entity = new DemonGlobal();
		entity.difficult = rs.getLong("difficult");
		entity.lastHeart = rs.getInt("lastHeart");
		entity.bossDeadUseTime = rs.getInt("bossDeadUseTime");
		return entity;
	}

	@Override
	protected void hasReadEvent() {
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.difficult);
		}
		value.add(this.lastHeart);
		value.add(this.bossDeadUseTime);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
	}

	public static DemonGlobal valueOf(long difficult) {
		DemonGlobal entity = new DemonGlobal();
		entity.difficult = difficult;
		return entity;
	}
	
	@Override
	protected void disposeBlob() {
		
	}
	
}
