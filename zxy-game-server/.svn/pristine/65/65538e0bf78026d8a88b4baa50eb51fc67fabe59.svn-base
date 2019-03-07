//package com.jtang.gameserver.dbproxy.entity;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//
//import com.jtang.core.db.DBQueueType;
//import com.jtang.core.db.Entity;
//import com.jtang.core.db.annotation.Column;
//import com.jtang.core.db.annotation.TableName;
//
///**
// * 精炼库
// * <pre>
// * --以下为db说明---------------------------
// * 主键为actorId,每个角色对应一条记录
// * </pre>
// * @author liujian
// *
// */
//@TableName(name="refine", type = DBQueueType.IMPORTANT)
//public class Refine extends Entity<Long> {
//	private static final long serialVersionUID = 7609447051271123388L;
//
//	/**
//	 * 角色id  主键
//	 */
//	@Column(pk = true)
//	private long actorId;
//	
//	/**
//	 * 精炼室等级
//	 */
//	@Column
//	public int level;
//
//	@Override
//	public Long getPkId() {
//		return this.actorId;
//	}
//
//	@Override
//	public void setPkId(Long pk) {
//		this.actorId = pk;
//	}
//	
//	public static Refine valueOf(long actorId) {
//		Refine refine = new Refine();
//		refine.actorId = actorId;
//		refine.level = 1;  //TODO 默认是一级。这个要做到rule配置里
//		return refine;
//	}
//
//	@Override
//	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
//		Refine refine = new Refine();
//		refine.actorId = rs.getLong("actorId");
//		refine.level = rs.getInt("level");
//		return refine;
//	}
//
//	@Override
//	protected void hasReadEvent() {
//	}
//
//	@Override
//	protected ArrayList<Object> writeData(Boolean containsPK) {
//		ArrayList<Object> value = new ArrayList<>();
//		if (containsPK) {
//			value.add(this.actorId);
//		}
//		value.add(this.level);
//		return value;
//	}
//
//	@Override
//	protected void beforeWritingEvent() {
//	}
//
//	public void reset() {
//		this.level = 1;
//	}
//}
