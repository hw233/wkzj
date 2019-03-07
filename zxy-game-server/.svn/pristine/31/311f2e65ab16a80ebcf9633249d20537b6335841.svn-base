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
 * 年兽活动全局信息表
 * --以下为db说明---------------------------
 * 主键为难度id,一个难度对应一行数据
 * </pre>
 * @author ludd
 *
 */
@TableName(name="beastGlobal", type=DBQueueType.DEFAULT)
public class BeastGlobal extends Entity<Long> {
	private static final long serialVersionUID = 7822031289620812215L;

	/**
	 * 年兽配置id
	 * beastGlobalConfig 中的 monsterConfigId
	 */
	@Column(pk = true)
	private long configId;
	
	/**
	 * 上次boss收到的伤害总和
	 */
	@Column
	public int lastHurt;
	
	/**
	 * 打死boss耗时（秒）
	 */
	@Column
	public int bossDeadUseTime;
	
	@Override
	public Long getPkId() {
		return configId;
	}

	@Override
	public void setPkId(Long pk) {
		this.configId = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		BeastGlobal entity = new BeastGlobal();
		entity.configId = rs.getLong("configId");
		entity.lastHurt = rs.getInt("lastHurt");
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
			value.add(this.configId);
		}
		value.add(this.lastHurt);
		value.add(this.bossDeadUseTime);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
	}

	public static BeastGlobal valueOf(long difficult) {
		BeastGlobal entity = new BeastGlobal();
		entity.configId = difficult;
		return entity;
	}
	
	@Override
	protected void disposeBlob() {
		
	}
	
}
