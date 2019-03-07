package com.jtang.gameserver.module.lineup.handler.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dbproxy.entity.Lineup;
import com.jtang.gameserver.module.lineup.model.LineupHeadItem;
//import com.jtang.sm2.module.lineup.model.LineupGridItem;

/**
 * @author vinceruan
 *
 */
public class LineupInfoResponse extends IoBufferSerializer {
	/**
	 * 已经解锁的格子数
	 */
	private int activedGridCount;
	
	/**
	 * 页面顶部的格子列表
	 */
	private List<LineupHeadItem> heroList;
	
	/**
	 * 上阵的装备在3*3阵型中的索引
	 * 格式是:Map<EquipUUId,GridIndex>
	 * GridIndex 是指阵型页面底部3*3格子的索引值(按从左往右、从上往下的顺序排序,从1开始)
	 */
	private Map<Long, Byte> equips;
	
	@Override
	public void write() {
		writeByte((byte)activedGridCount);
		writeShort((short)heroList.size());
		for (LineupHeadItem item : heroList) {
			writeByte((byte)item.headIndex);
			writeInt(item.heroId);
			writeByte((byte)item.gridIndex);
		}
		writeBuffer.putShort((short) equips.size());
		for (Entry<Long, Byte> entry : equips.entrySet()) {
			writeLong(entry.getKey());
			writeByte(entry.getValue());
		}
	}
	
	public LineupInfoResponse(Lineup lineup) {
		this.activedGridCount = (byte) lineup.activedGridCount;
		List<LineupHeadItem> list = new ArrayList<>();
		for (LineupHeadItem lineupHeadItem : lineup.getHeadItemList()) {
			list.add(lineupHeadItem);
		}
		this.heroList = list;
		
		equips = new HashMap<Long, Byte>();
		
		for (LineupHeadItem grid : lineup.getHeadItemList()) {			
			if (grid.atkEquipUuid != 0) {
				equips.put(grid.atkEquipUuid, (byte)grid.headIndex);
			}
			if (grid.defEquipUuid != 0) {
				equips.put(grid.defEquipUuid, (byte)grid.headIndex);
			}
			if (grid.decorationUuid != 0) {
				equips.put(grid.decorationUuid, (byte)grid.headIndex);
			}
		}
	}
	
}
