package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.module.app.AppInit;
import com.jtang.gameserver.module.app.model.extension.BaseGlobalInfoVO;
/**
 * 活动全局表
 *  <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为appId,一个活动一条记录
 * </pre>
 * @author ludd
 *
 */
@TableName(name="appGlobal", type = DBQueueType.DEFAULT)
public class AppGlobal extends Entity<Long> {
	private static final long serialVersionUID = 4195856117354977819L;

	/**
	 * 活动Id
	 */
	@Column(pk = true)
	private long appId;
	
	/**
	 * 活动全局扩展字段
	 */
	@Column
	private String globalInfo;

	/**
	 * configInfo序列化后的对象
	 */
	private BaseGlobalInfoVO globalInfoVO;

	/**
	 * 操作时间
	 */
	@Column
	public int operationTime;

	@Override
	public Long getPkId() {
		return this.appId;
	}

	@Override
	public void setPkId(Long pk) {
		this.appId = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet resultset, int rowNum) throws SQLException {
		AppGlobal appGlobal = new AppGlobal();
		appGlobal.appId = resultset.getLong("appId");
		appGlobal.globalInfo = resultset.getString("globalInfo");
		appGlobal.operationTime = resultset.getInt("operationTime");
		return appGlobal;
	}

	@Override
	protected void hasReadEvent() {

		this.globalInfoVO = AppInit.getGlobalInfoVO(appId,this.globalInfo);
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.appId);
		}
		value.add(this.globalInfo);
		value.add(this.operationTime);

		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		if (globalInfoVO == null) {
			return;
		}
		this.globalInfo = globalInfoVO.parse2String();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getGlobalInfoVO() {
		if (globalInfoVO == null) {
			return null;
		}
		return (T)globalInfoVO;
	}
	
	public static AppGlobal valueOf(long appId) {
		AppGlobal appGlobal = new AppGlobal();
		appGlobal.setPkId(appId);
		appGlobal.globalInfo = "";
		appGlobal.globalInfoVO =  AppInit.getGlobalInfoVO(appId, appGlobal.globalInfo);
		appGlobal.operationTime = TimeUtils.getNow();
		return appGlobal;
	}

	public void reset() {
		this.globalInfo = "";
		this.operationTime = 0;
		this.globalInfoVO = AppInit.getGlobalInfoVO(appId,this.globalInfo);
	}
	
	@Override
	protected void disposeBlob() {
		this.globalInfo = EMPTY_STRING;
	}

}
