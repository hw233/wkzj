package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.adventures.vipactivity.model.GiveEquipVO;
import com.jtang.gameserver.module.adventures.vipactivity.model.VipBaseVO;
import com.jtang.gameserver.module.adventures.vipactivity.type.VipLevelType;

@TableName(name="vipprivilege", type = DBQueueType.IMPORTANT)
public class VipPrivilege extends Entity<Long> {
	private static final long serialVersionUID = 5868080189246577297L;

	@Column(pk = true)
	public long actorId;
	
	/**
	 * vipLevel_xxx_xxx_xxx|vipLevel_xxx
	 * 
	 * 天财地宝 12_actorId_时间
	 */
	@Column
	public String extension;
	

	@Override
	public Long getPkId() {
		return actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}
	
	/**
	 * key:viplevel
	 * value:actorId_次数_时间
	 */
	private Map<Integer,VipBaseVO> extensionMap;
	
	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		VipPrivilege vipActivity = new VipPrivilege();
		vipActivity.actorId = rs.getLong("actorId");
		vipActivity.extension = rs.getString("extension");
		return vipActivity;
	}

	@Override
	protected void hasReadEvent() {
		extensionMap = new HashMap<Integer, VipBaseVO>();
		if (!extension.isEmpty()) {
			String[] vipExtension = StringUtils.split(extension, Splitable.ELEMENT_SPLIT);
			putVipInfo(vipExtension);
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(actorId);
		}
		value.add(extension);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		List<String> list = new ArrayList<>();
		for (VipBaseVO vo : extensionMap.values()) {
			list.add(vo.parseToString());
		}
		extension = StringUtils.collection2SplitString(list, Splitable.ELEMENT_DELIMITER);
	}
	
	private void putVipInfo(String vipExtension[]) {
		for (String str : vipExtension) {
			int vipLevel = Integer.valueOf(StringUtils.split(str, Splitable.ATTRIBUTE_SPLIT)[0]);
			switch (VipLevelType.get(vipLevel)) {
			case VIP1:
				break;
			case VIP2:
				break;
			case VIP3:
				break;
			case VIP4:
				break;
			case VIP5:
				break;
			case VIP6:
				break;
			case VIP7:
				break;
			case VIP8:
				break;
			case VIP9:
				break;
			case VIP10:
				break;
			case VIP11:
				break;
			case VIP12:
				break;
			case VIP13:
				extensionMap.put(vipLevel, GiveEquipVO.valueOf(str));
				break;
			default:
				break;
			}
		}
	}

	public static VipPrivilege valueOf(long actorId) {
		VipPrivilege vipActivity = new VipPrivilege();
		vipActivity.actorId = actorId;
		vipActivity.extension = "";
		vipActivity.extensionMap = new HashMap<>();
		return vipActivity;
	}
	
	public Map<Integer, VipBaseVO> getExtensionMap() {
		return extensionMap;
	}
	
	public void reset() {
		this.extensionMap.clear();
	}

	@Override
	protected void disposeBlob() {
		extension = EMPTY_STRING;
	}
}
