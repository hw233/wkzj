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
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dataconfig.service.PowerService;
import com.jtang.gameserver.module.power.model.PowerShopVO;
/**
 * 排行挑战表
 * @author jianglf
 *
 */
@TableName(name = "powerExt", type = DBQueueType.IMPORTANT)
public class PowerExt extends Entity<Long> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7870066329226166107L;

	@Column(pk = true)
	public long actorId;
	
	/**
	 * 上次挑战时间
	 */
	@Column
	public int fightTime;
	
	/**
	 * 已使用刷新次数
	 */
	@Column
	public int flushNum;
	
	/**
	 * 上次刷新时间
	 */
	@Column
	public int flushTime;
	
	/**
	 * 历史最高排行
	 */
	@Column
	public int historyRank;
	
	/**
	 * 今天获取的气势值
	 */
	@Column
	public int moraleNum;
	
	/**
	 * 刷新商店次数
	 */
	@Column
	public int flushShopNum;
	
	/**
	 * 刷新商店时间
	 */
	@Column
	public int flushShopTime;
	
	/**
	 * 商品列表
	 */
	@Column
	public String reward;
	
	public Map<Integer,PowerShopVO> rewardMap = new ConcurrentHashMap<>();
	
	@Override
	public Long getPkId() {
		return actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum)
			throws SQLException {
		PowerExt powerExt = new PowerExt();
		powerExt.actorId = rs.getLong("actorId");
		powerExt.fightTime = rs.getInt("fightTime");
		powerExt.flushNum = rs.getInt("flushNum");
		powerExt.flushTime = rs.getInt("flushTime");
		powerExt.historyRank = rs.getInt("historyRank");
		powerExt.moraleNum = rs.getInt("moraleNum");
		powerExt.flushShopNum = rs.getInt("flushShopNum");
		powerExt.flushShopTime = rs.getInt("flushShopTime");
		powerExt.reward = rs.getString("reward");
		return powerExt;
	}

	@Override
	protected void hasReadEvent() {
		List<String[]> list = StringUtils.delimiterString2Array(reward);
		for (String[] str : list) {
			PowerShopVO powerShopVO = PowerShopVO.valueOf(str);
			rewardMap.put(powerShopVO.id,powerShopVO);
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> values = new ArrayList<>();
		if(containsPK){
			values.add(actorId);
		}
		values.add(fightTime);
		values.add(flushNum);
		values.add(flushTime);
		values.add(historyRank);
		values.add(moraleNum);
		values.add(flushShopNum);
		values.add(flushShopTime);
		values.add(reward);
		return values;
	}

	@Override
	protected void beforeWritingEvent() {
		StringBuffer sb = new StringBuffer();
		for(PowerShopVO vo : rewardMap.values()){
			sb.append(vo.parser2String()).append(Splitable.ELEMENT_DELIMITER);
		}
		if(rewardMap.isEmpty() == false){
			sb.deleteCharAt(sb.length() - 1);
		}
		this.reward = sb.toString();
	}

	@Override
	protected void disposeBlob() {
		this.reward = EMPTY_STRING;
	}

	public static PowerExt valueOf(long actorId) {
		PowerExt powerExt = new PowerExt();
		powerExt.actorId = actorId;
		powerExt.historyRank = PowerService.getMaxRewardRank();
		powerExt.rewardMap = PowerService.initShop();
		return powerExt;
	}

	public void reset() {
		this.flushNum = 0;
		this.flushTime = TimeUtils.getNow();
		this.moraleNum = 0;
		this.rewardMap = PowerService.initShop();
		this.flushShopNum = 0;
		this.flushShopTime = 0;
	}

}
