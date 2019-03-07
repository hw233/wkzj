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
///**
// * 装备强化室的数据实体,强化室的初始等级为1
// * <pre>
// * 
// * --以下为db说明---------------------------
// * 主键为actorId,每个角色仅有一行记录
// * </pre>
// * @author pengzy
// *
// */
//@TableName(name = "enhanced", type = DBQueueType.IMPORTANT)
//public class Enhanced extends Entity<Long> {
//	private static final long serialVersionUID = -2065734874790167478L;
//
//	/**
//	 * 角色id
//	 */
//	@Column(pk = true)
//	private long actorId;
//	
//	/**
//	 * 等级
//	 */
//	@Column
//	public int level;
//
//	@Override
//	public Long getPkId() {
//		return actorId;
//	}
//
//	@Override
//	public void setPkId(Long pk) {
//		actorId = pk;
//	}
//
//	public static Enhanced valueOf(long actorId) {
//		Enhanced enhanced = new Enhanced();
//		enhanced.actorId = actorId;
//		enhanced.level = 1;
//		return enhanced;
//	}
//
//	@Override
//	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
//		Enhanced enhanced = new Enhanced();
//		enhanced.actorId = rs.getLong("actorId");
//		enhanced.level = rs.getInt("level");
//		return enhanced;
//	}
//
//	@Override
//	protected void hasReadEvent() {	
//	}
//
//	@Override
//	protected ArrayList<Object> writeData(Boolean containsPK) {
//		ArrayList<Object> value = new ArrayList<>();
//		if (containsPK){
//		    value.add(actorId);
//		}
//	    value.add(level);
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
//
//}
