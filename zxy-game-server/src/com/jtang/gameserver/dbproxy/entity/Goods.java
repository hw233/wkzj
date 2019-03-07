package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.goods.model.GoodsVO;

/**
 * 物品
 * <pre>
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色对应一条记录
 * </pre>
 * @author 0x737263
 *
 */
@TableName(name="goods", type = DBQueueType.IMPORTANT)
public class Goods extends Entity<Long> {
	private static final long serialVersionUID = -4052907146224444453L;

	/**
	 * 角色id  主键
	 */
	@Column(pk = true)
	private long actorId;
	
	/**
	 * 物品存储Blob字段(最大300个不同goodsid的物品)
	 * <pre>
	 * 格式：uuid_goodsid_num|uuid_goodsid_num|uuid_goodsid_num
	 * {@code GoodsVO}
	 * </pre>
	 */
	@Column
	private String goods;
	
	/**
	 * 使用记录
	 * <pre>
	 * 格式goodsId_usenum|...
	 * </pre>
	 */
	@Column
	private String useRecord;
	
	/**
	 * 物品集合
	 * <pre>
	 * 由equips字符串序列化得来
	 * key: goodsId
	 * value: {@code GoodsVO}
	 * </pre>
	 */
	private Map<Integer, GoodsVO> goodsMap = new ConcurrentHashMap<>();
	
	/**
	 * 使用记录集合
	 * key:物品id
	 * value: 使用次数
	 */
	public Map<Integer, Integer> useRecordMap = new ConcurrentHashMap<>();
	
	/**
	 * 保底次数
	 * key:物品id
	 * value:保底次数
	 */
	private Map<Integer, Integer> leastNum = new ConcurrentHashMap<>();
	
	/**
	 * 
	 * @return
	 */
	public Map<Integer, GoodsVO> getGoodsMap() {
		return this.goodsMap;
	}
	
	public GoodsVO getGoodsVO(int goodsId) {
		return getGoodsMap().get(goodsId);
	}
	
	public GoodsVO addGoodsVO(int goodsId, int num) {
		GoodsVO vo = getGoodsVO(goodsId);
		if (vo == null) {
			vo = GoodsVO.valueOf(goodsId, num);
			getGoodsMap().put(goodsId, vo);
		} else {
			vo.num += num;
		}
		return vo;
	}
	
	public boolean decreaseGoods(int goodsId, int num) {
		GoodsVO vo = getGoodsVO(goodsId);
		if (vo == null) {
			return false;
		}
		if (vo.num < num) {
			return false;
		}

		vo.num -= num;
		if (vo.num < 1) {
			Map<Integer, GoodsVO> goodsMap = getGoodsMap();
			goodsMap.remove(goodsId);
		}
		return true;
	}
	
	@Override
	public Long getPkId() {
		return this.actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}
	
	public static Goods valueOf(long actorId) {
		Goods entity = new Goods();
		entity.actorId = actorId;
		entity.goods = "";
		return entity;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Goods entity = new Goods();
		entity.actorId = rs.getLong("actorId");
		entity.goods = rs.getString("goods");
		entity.useRecord = rs.getString("useRecord");
		return entity;
	}

	@Override
	protected void hasReadEvent() {
		
		if (StringUtils.isNotBlank(this.goods)) {
			List<String[]> list = StringUtils.delimiterString2Array(this.goods);
			for (String[] array : list) {
				GoodsVO vo = GoodsVO.valueOf(array);
				this.goodsMap.put(vo.goodsId, vo);
			}
		}
		
		if (StringUtils.isNotBlank(this.useRecord)) {
			this.useRecordMap = StringUtils.delimiterString2IntMap(this.useRecord);
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.actorId);
		}
		value.add(this.goods);
		value.add(this.useRecord);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {

		Map<Integer, GoodsVO> goodsMap = getGoodsMap();
		List<String> goodsStringList = new ArrayList<String>();
		for (GoodsVO vo : goodsMap.values()) {
			goodsStringList.add(vo.parse2String());
		}
		this.goods = StringUtils.collection2SplitString(goodsStringList, Splitable.ELEMENT_DELIMITER);
		this.useRecord = StringUtils.map2DelimiterString(this.useRecordMap, Splitable.ATTRIBUTE_SPLIT, Splitable.ELEMENT_DELIMITER);
	}
	
	
	/**
	 * 获取保底次数
	 * @param goodsId
	 * @return
	 */
	public int getLeastNum(int goodsId) {
		if (this.leastNum.containsKey(goodsId)) {
			return this.leastNum.get(goodsId);
		}
		return 0;
	}
	
	/**
	 * 设置保底次数
	 * @param goodsId
	 * @param num
	 */
	public void setLeastNum(int goodsId, int num){
		this.leastNum.put(goodsId, num);
	}
	
	public void reset() {
		this.goodsMap.clear();
		this.useRecordMap.clear();
	}
	
	@Override
	protected void disposeBlob() {
		this.goods = EMPTY_STRING;
	}

}
