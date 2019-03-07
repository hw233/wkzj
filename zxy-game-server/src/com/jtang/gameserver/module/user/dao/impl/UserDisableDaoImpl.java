package com.jtang.gameserver.module.user.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.UserDisabled;
import com.jtang.gameserver.module.user.dao.UserDisabledDao;
import com.jtang.gameserver.module.user.type.DisableType;

@Component
public class UserDisableDaoImpl implements UserDisabledDao {

	@Autowired
	IdTableJdbc jdbc;

	@Override
	public void disableUser(long actorId, UserDisabled userDisabled) {
		LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("disabledType", DisableType.ACTOR_ID.getCode());
		condition.put("value", actorId);
		UserDisabled disable = jdbc.getFirst(UserDisabled.class, condition);
		if (disable == null) {
			jdbc.saveAndIncreasePK(userDisabled);
		}
	}

	@Override
	public void enableUser(UserDisabled disable) {
		jdbc.delete(disable);
	}

	@Override
	public List<UserDisabled> getDisableList(long actorId, String sim, String mac, String imei, String remoteIp) {

		StringBuffer sql = new StringBuffer();
		sql.append("select * from UserDisabled where");
		sql.append("((disabledType = 1 and value = ?)");
		sql.append("or(disabledType= 2 and value = ?)");
		sql.append("or(disabledType= 3 and value = ?)");
		sql.append("or(disabledType= 4 and value = ?)");
		sql.append("or(disabledType= 5 and value =?))");
		sql.append("and beginTime <= ?");
		sql.append("and endTime >= ?");

		final List<UserDisabled> disabledList = new ArrayList<>();
		jdbc.query(sql.toString(), new Object[] { actorId, sim, mac, imei, remoteIp, new Date(), new Date() },
		new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet arg0) throws SQLException {
				UserDisabled userDisabled = new UserDisabled();
				userDisabled.id = arg0.getLong("id");
				userDisabled.disabledType = arg0.getInt("disabledType");
				userDisabled.value = arg0.getString("value");
				userDisabled.beginTime = arg0.getDate("beginTime");
				userDisabled.endTime = arg0.getDate("endTime");
				disabledList.add(userDisabled);
			}
		});
		return disabledList;
	}

}
