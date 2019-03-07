package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jiatang.common.model.BufferVO;
import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.buffer.model.HeroBuffer;

/**
 * 玩家的所有仙人buffer信息
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author vinceruan
 *
 */
@TableName(name="buffers", type = DBQueueType.IMPORTANT)
public class Buffers extends Entity<Long> {
	private static final long serialVersionUID = 6313059509049355144L;

	/**
	 * 角色id
	 */
	@Column(pk=true)
	private long actorId;
	
	/**
	 * buffer字符串(最多300条)
	 * HeroBuffer对象|HeroBuffer对象
	 * <pre>
	 * 即:英雄id_buff值,属性code,buff类型_buff值,属性code,buff类型|
	 * </pre>
	 */
	@Column
	private String buffers;
	
	/**
	 * 仙人的buffer列表
	 * 格式是:
	 * Map<HeroId,HeroBuffer>
	 */
	private Map<Integer, HeroBuffer> heroBufferMap = new ConcurrentHashMap<>();
	
	@Override
	public Long getPkId() {
		return this.actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}
	
	public Map<Integer, HeroBuffer> getHeroBufferMap() {
		return this.heroBufferMap;
	}
	
	/**
	 * 添加一个buffer
	 * @param heroId
	 * @param buffer
	 */
	public void addBuffer(int heroId, BufferVO buffer) {
		Map<Integer, HeroBuffer> heroBufferMap = this.getHeroBufferMap();
		HeroBuffer hb = heroBufferMap.get(heroId);
		if (hb == null) {
			hb = new HeroBuffer(heroId);
			heroBufferMap.put(heroId, hb);
		}
		hb.addBuffer(buffer);
	}
	
	/**
	 * 添加多个buffer
	 * @param heroId
	 * @param list
	 */
	public void addBuffers(int heroId, List<BufferVO> list) {
		Map<Integer, HeroBuffer> heroBufferMap = this.getHeroBufferMap();
		HeroBuffer hb = heroBufferMap.get(heroId);
		if (hb == null) {
			hb = new HeroBuffer(heroId);
			heroBufferMap.put(heroId, hb);
		}
		for (BufferVO buffer : list) {
			hb.addBuffer(buffer);
		}
	}
	
	/**
	 * 将某一个分类的buffer移除
	 * @param heroId
	 * @param sourceType
	 * @return
	 */
	public List<BufferVO> removeBufferBySourceType(int heroId, int sourceType) {
		Map<Integer, HeroBuffer> heroBufferMap = this.getHeroBufferMap();
		HeroBuffer hb = heroBufferMap.get(heroId);
		if (hb == null) {
			return null;
		}
		return hb.removeBuffersByType(sourceType);
	}
	
	/**
	 * 初始化Buffers
	 * @param actorId
	 * @return
	 */
	public static Buffers valueOf(long actorId) {
		Buffers buffers = new Buffers();
		buffers.actorId = actorId;
		return buffers;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Buffers buffers = new Buffers();
		buffers.actorId = rs.getLong("actorId");
		buffers.buffers = rs.getString("buffers");
		return buffers;
	}

	@Override
	protected void hasReadEvent() {
		
		if (StringUtils.isNotBlank(this.buffers)) {
			List<String> strList = StringUtils.delimiterString2List(this.buffers, Splitable.ELEMENT_SPLIT);
			for (String str : strList) {
				HeroBuffer heroBuffer = new HeroBuffer(str);
				this.heroBufferMap.put(heroBuffer.heroId, heroBuffer);
			}
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.actorId);
		}
		value.add(this.buffers);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		
		List<String> blobList = new ArrayList<String>();
		for (HeroBuffer buffer : this.heroBufferMap.values()) {
			String str = buffer.parseModel2Str();
			if (StringUtils.isNotBlank(str)) {
				blobList.add(str);
			}			
		}
		this.buffers = StringUtils.collection2SplitString(blobList, Splitable.ELEMENT_DELIMITER);		
	}
	
	public void reset() {
		this.heroBufferMap.clear();
	}
	
	@Override
	protected void disposeBlob() {
		this.buffers = EMPTY_STRING;
	}
	
}
