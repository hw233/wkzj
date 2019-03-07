package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.sysmail.type.GetType;

/**
 * 系统发送的带附件的邮件
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为sysMailId mysql自增id,每个角色有多行记录
 * </pre>
 * @author 0x737263
 *
 */
@TableName(name="sysmail", type = DBQueueType.NONE)
public class Sysmail extends Entity<Long> {
	private static final long serialVersionUID = -1421415845675530304L;

	/**
	 * mysql自增id
	 */
	@Column(pk = true)
	private long sysMailId;
	
	/**
	 * 拥有该角色id
	 */
	@Column
	public long ownerActorId;
	
	/**
	 * 邮件内容
	 */
	@Column
	public String content;
	
	/**
	 * 奖励的物品列表
	 * 奖励类型_物品id_数量|奖励类型_物品id_数量|奖励类型_物品id_数量
	 */
	@Column
	private String attachGoods;
	
	/**
	 * 发送时间(超过x天可以根据这个字段来清空)
	 */
	@Column
	public int sendTime;
	
	/**
	 * 领取状态(0.未领取 1.已领取)
	 */
	@Column
	public int isGet;
	
	/**
	 * 邮件附件物品
	 * key:位置  value:奖励物品
	 */
	private List<RewardObject> attachGoodsList = new Vector<>();
	

	@Override
	public Long getPkId() {
		return sysMailId;
	}

	@Override
	public void setPkId(Long pk) {
		this.sysMailId = pk;
	}
	
	public void setAttachGoods(String attachGoods) {
		this.attachGoods = attachGoods;
	}
	
	/**
	 * 获取附件物品列表
	 * @return
	 */
	public List<RewardObject> getAttachGoodsList() {
		return this.attachGoodsList;
	}
	
	/**
	 * 更新附件物品列表
	 * @return
	 */
	public void setAttachGoodsList(List<RewardObject> list) {
		this.attachGoodsList = list;
	}
	
	/**
	 * 领取附件
	 */
	public void getAttach() {
		this.isGet = GetType.GET.getCode();
	}

	@Override
	public Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Sysmail entity = new Sysmail();
		entity.sysMailId = rs.getLong("sysMailId");
		entity.ownerActorId = rs.getLong("ownerActorId");
		entity.content = rs.getString("content");
		entity.attachGoods = rs.getString("attachGoods");
		entity.sendTime = rs.getInt("sendTime");
		entity.isGet = rs.getInt("isGet");
		return entity;
	}

	@Override
	protected void hasReadEvent() {
		if (StringUtils.isNotBlank(this.attachGoods)) {
			List<String[]> list = StringUtils.delimiterString2Array(this.attachGoods);
			for (String[] array : list) {
				this.attachGoodsList.add(RewardObject.valueOf(array));
			}
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.sysMailId);
		}
		value.add(this.ownerActorId);
		value.add(this.content);
		value.add(this.attachGoods);
		value.add(this.sendTime);
		value.add(this.isGet);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		this.attachGoods = reward2String();
	}
	
	public String reward2String(){
		List<RewardObject> attachGoodsList = getAttachGoodsList();
		List<String> goodsStringList = new ArrayList<String>();
		for (RewardObject rewardObject : attachGoodsList) {
			goodsStringList.add(rewardObject.parse2String());
		}
		return StringUtils.collection2SplitString(goodsStringList, Splitable.ELEMENT_DELIMITER);
	}

	public static Sysmail valueOf(long actorId) {
		Sysmail sysmail = new Sysmail();
		sysmail.ownerActorId = actorId;
		sysmail.content="";
		sysmail.sendTime=0;
		sysmail.isGet = 0;
		sysmail.setAttachGoods("");
		return sysmail;
	}

	public String getAttachGoods() {
		return attachGoods;
	}

	@Override
	protected void disposeBlob() {
		attachGoods = EMPTY_STRING;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (sysMailId ^ (sysMailId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sysmail other = (Sysmail) obj;
		if (sysMailId != other.sysMailId)
			return false;
		return true;
	}
	
	
}
