package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dataconfig.service.LoveService;
import com.jtang.gameserver.module.love.model.BossFightVO;
/**
 * 婚姻系统实体
 * <pre>
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author jerry
 *
 */
@TableName(name="love", type = DBQueueType.IMPORTANT)
public class Love extends Entity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8753368357975306634L;
	
	@Column(pk = true)
	private long actorId;
	
	/**
	 * 已结婚的角色id
	 */
	@Column
	private long loveActorId;
	
	/**
	 * 离婚时间
	 */
	@Column
	private int unloveTime;
	
	/**
	 * 被请求结婚的角色id_超时时间
	 */
	@Column
	private String acceptInfo;
	
	/**
	 * 礼物
	 */
	@Column
	private String gift;
	
	/**
	 * 收礼时间
	 */
	@Column
	private int giveGiftTime;
	/**
	 * 送礼时间
	 */
	@Column
	private int reciveGiftTime;
	
	/**
	 * 排行已使用挑战次数
	 */
	@Column
	public int rankFightNum;
	
	/**
	 * 上次排行挑战时间
	 */
	@Column
	public int rankFightTime;
	
	/**
	 * 排行已使用刷新次数(补满挑战次数)
	 */
	@Column
	public int rankFlushNum;
	
	/**
	 * 排行上次刷新时间
	 */
	@Column
	public int rankFlushTime;
	
	/**
	 * 仙侣合作使用状态
	 */
	@Column
	private String fightState;
	
	/**
	 * 仙侣合作操作时间
	 */
	@Column
	public int fightTime;
	
	
	private List<RewardObject> giftList = Collections.synchronizedList(new ArrayList<RewardObject>());
	
	private ConcurrentHashMap<Long, Integer> acceptMap = new ConcurrentHashMap<>();
	
	public Map<Integer,BossFightVO> fightStateMap = new HashMap<>();

	@Override
	public Long getPkId() {
		return this.actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum)
			throws SQLException {
		Love love = new Love();
		love.actorId = rs.getLong("actorId");
		love.loveActorId = rs.getLong("loveActorId");
		love.unloveTime = rs.getInt("unloveTime");
		love.acceptInfo = rs.getString("acceptInfo");
		love.gift =  rs.getString("gift");
		love.giveGiftTime =  rs.getInt("giveGiftTime");
		love.reciveGiftTime =  rs.getInt("reciveGiftTime");
		love.rankFightNum =  rs.getInt("rankFightNum");
		love.rankFightTime =  rs.getInt("rankFightTime");
		love.rankFlushNum =  rs.getInt("rankFlushNum");
		love.rankFlushTime =  rs.getInt("rankFlushTime");
		love.fightState =  rs.getString("fightState");
		love.fightTime =  rs.getInt("fightTime");
		return love;
	}

	@Override
	protected void hasReadEvent() {
		List<String[]> list = StringUtils.delimiterString2Array(gift);
		for (String[] str : list) {
			RewardObject obj = RewardObject.valueOf(str);
			giftList.add(obj);
		}
		
		Map<Long, Integer> map = StringUtils.delimiterString2Long_IntMap(acceptInfo);
		this.acceptMap.putAll(map);
		
		List<String[]> fightList = StringUtils.delimiterString2Array(fightState);
		for (String[] str : fightList) {
			BossFightVO vo = BossFightVO.valueOf(str);
			fightStateMap.put(vo.id, vo);
		}
		
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK){
		    value.add(this.actorId);
		}
	    value.add(this.loveActorId);
	    value.add(this.unloveTime);
	    value.add(this.acceptInfo);
	    value.add(this.gift);
	    value.add(this.giveGiftTime);
	    value.add(this.reciveGiftTime);
	    value.add(this.rankFightNum);
	    value.add(this.rankFightTime);
	    value.add(this.rankFlushNum);
	    value.add(this.rankFlushTime);
	    value.add(this.fightState);
	    value.add(this.fightTime);
	    return value;
	}

	@Override
	protected void beforeWritingEvent() {
		List<String> list = new ArrayList<>();
		for (RewardObject obj : this.giftList) {
			list.add(obj.toString());
		}
		this.gift = StringUtils.collection2SplitString(list, Splitable.ELEMENT_DELIMITER);
		this.acceptInfo = StringUtils.numberMap2String(this.acceptMap);
		
		StringBuffer sb = new StringBuffer();
		for(BossFightVO vo : fightStateMap.values()){
			sb.append(vo.parser2String()).append(Splitable.ELEMENT_DELIMITER);
		}
		if(fightStateMap.size() > 0){
			sb.deleteCharAt(sb.length() - 1);
		}
		this.fightState = sb.toString();
	}

	@Override
	protected void disposeBlob() {
		this.gift = EMPTY_STRING;
		this.acceptInfo = EMPTY_STRING;
		this.fightState = EMPTY_STRING;
		
	}

	public static Love valueOf(long actorId) {
		Love love = new Love();
		love.actorId = actorId;
		love.fightStateMap = LoveService.initFight();
		return love;
	}

	public long getLoveActorId() {
		return loveActorId;
	}

	public int getUnloveTime() {
		return unloveTime;
	}
	
	public boolean married() {
		return loveActorId > 0;
	}
	

	public void setLoveActorId(long loveActorId) {
		this.loveActorId = loveActorId;
	}

	public void setUnloveTime(int unloveTime) {
		this.unloveTime = unloveTime;
	}


	public byte getHasGift() {
		if (married()) {
			if (giftList.size() > 0) {
				return (byte)1;
			}
			
			if (DateUtils.isToday(this.reciveGiftTime)){
				return (byte)2;
			}
		}
		
		return 0;
		
		
	}
	
	public byte hasGive() {
		if (married()) {
			return (byte) (DateUtils.isToday(this.giveGiftTime)? 1 : 0) ;
		}
		return 1;
	}
	
	
	public ConcurrentHashMap<Long, Integer> getAcceptMap() {
		return acceptMap;
	}
	
	public void addGift(List<RewardObject> rewardObjects) {
		this.giftList.addAll(rewardObjects);
	}
	public List<RewardObject> removeGift() {
		List<RewardObject> result = new ArrayList<>();
		result.addAll(this.giftList);
		this.giftList.clear();
		return result;
	}
	
	public int getGiveGiftTime() {
		return giveGiftTime;
	}
	
	public void setGiveGiftTime(int giveGiftTime) {
		this.giveGiftTime = giveGiftTime;
	}
	
	public void setReciveGiftTime(int reciveGiftTime) {
		this.reciveGiftTime = reciveGiftTime;
	}
	
	public void reset() {
		this.rankFightNum = 0;
		this.rankFightTime = TimeUtils.getNow();
		this.rankFlushNum = 0;
		this.rankFlushTime = TimeUtils.getNow();
	}
	

}
