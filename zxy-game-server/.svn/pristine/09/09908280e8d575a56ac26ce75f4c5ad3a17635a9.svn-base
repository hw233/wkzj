package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jiatang.common.model.EquipVO;
import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.CollectionUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

/**
 * 装备库
 * <pre>
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色对应一条记录
 * </pre>
 * @author liujian
 *
 */
@TableName(name="equips", type = DBQueueType.IMPORTANT)
public class Equips extends Entity<Long> {
	private static final long serialVersionUID = 990777257009909541L;

	/**
	 * 角色id  主键
	 */
	@Column(pk = true)
	private long actorId;
	
	/**
	 * 装备存储Blob字段
	 * <pre>
	 * 格式：EquipVO对象|EquipVO对象|EquipVO对象
	 * {@code EquipVO}
	 * </pre>
	 */
	@Column
	private String equips;
		
	/**
	 * 炼器宗师,上一次装备合成时间
	 */
	@Column
	public int composeTime;
	
	/**
	 * 合成次数
	 */
	@Column
	public int composeNum;
	
	/**
	 * 重置次数
	 */
	@Column
	public int resetNum;
	
	/**
	 * 重置的时间
	 */
	@Column
	public int resetTime;
	
	/**
	 * 装备集合
	 * <pre>
	 * equips转换成的对象
	 * key: equipVO的uuid	value: {@code EquipVO}
	 * </pre>
	 */
	private Map<Long, EquipVO> equipMap = new ConcurrentHashMap<>();
	
	@Override
	public Long getPkId() {
		return this.actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}
	
	/**
	 * 转换equips中的装备字符串为map集合
	 * @return
	 */
	public Map<Long, EquipVO> getEquipMap() {
		return equipMap;
	}
	
	/**
	 * 获取装备Vo
	 * @param uuid
	 * @return
	 */
	public EquipVO getEquipVo(String uuid) {
		return getEquipMap().get(uuid);
	}
	
	/**
	 * 添加装备
	 * @param equipVo
	 */
	public void addEquipVo(EquipVO equipVo) {
		getEquipMap().put(equipVo.uuid, equipVo);
	}
	
	/**
	 * 删除装备
	 * @param equipVo
	 */
	public void removeEquipVo(EquipVO equipVo) {
		if (equipVo == null) {
			return;
		}
		Map<Long, EquipVO> equipMap = this.getEquipMap();
		if (!CollectionUtils.isEmpty(equipMap) && equipMap.containsKey(equipVo.uuid)) {
			equipMap.remove(equipVo.uuid);
		}
	}
	
	public static Equips valueOf(long actorId) {
		Equips entity = new Equips();
		entity.actorId = actorId;
		entity.equips = "";
		entity.composeTime = 0;
		entity.resetNum = 0;
		entity.resetTime = 0;
		return entity;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Equips equip = new Equips();
		equip.actorId = rs.getLong("actorId");
		equip.equips = rs.getString("equips");
		equip.composeTime = rs.getInt("composeTime");
		equip.composeNum = rs.getInt("composeNum");
		equip.resetNum = rs.getInt("resetNum");
		equip.resetTime = rs.getInt("resetTime");
		return equip;
	}
	
	@Override
	protected void hasReadEvent() {
		
		//初始化equips转换为对象
		if (StringUtils.isNotBlank(this.equips)) {
			List<String[]> list = StringUtils.delimiterString2Array(this.equips);
			for (String[] array : list) {
				EquipVO vo = EquipVO.valueOf(array);
				equipMap.put(vo.uuid, vo);
			}
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.actorId);
		}
		value.add(this.equips);
		value.add(this.composeTime);
		value.add(this.composeNum);
		value.add(this.resetNum);
		value.add(this.resetTime);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {

		Map<Long, EquipVO> equipMap = this.getEquipMap();
		List<String> equipStringList = new ArrayList<String>();
		for (EquipVO vo : equipMap.values()) {
			equipStringList.add(vo.parse2String());
		}
		this.equips = StringUtils.collection2SplitString(equipStringList, Splitable.ELEMENT_DELIMITER);
	}
	
	public void reset() {
		this.composeNum = 0;
		this.composeTime = 0;
		this.equipMap.clear();
	}
	
	@Override
	protected void disposeBlob() {
		this.equips = EMPTY_STRING;
	}
}
