package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.model.IconFramConfig;
import com.jtang.gameserver.dataconfig.service.IconService;
import com.jtang.gameserver.module.icon.type.IconFramType;

@TableName(name = "icon", type = DBQueueType.IMPORTANT)
public class Icon extends Entity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1688064542912869034L;
	
	/**
	 * 玩家id
	 */
	@Column(pk=true)
	public long actorId;
	
	/**
	 * 解锁的一星仙人
	 */
	@Column
	public String oneStarHero;
	
	/**
	 * 解锁的二星仙人
	 */
	@Column
	public String twoStarHero;
	
	/**
	 * 解锁的三星仙人
	 */
	@Column
	public String threeStarHero;
	
	/**
	 * 解锁的四星仙人
	 */
	@Column
	public String fourStarHero;
	
	/**
	 * 解锁的五星仙人
	 */
	@Column
	public String fiveStarHero;
	
	/**
	 * 解锁的六星仙人
	 */
	@Column
	public String sixStarHero;
	
	/**
	 * 解锁的特殊头像
	 */
	@Column
	public String otherStarHero;
	
	/**
	 * 解锁的边框
	 */
	@Column
	public String unLockFram;
	
	/**
	 * 目前使用的头像
	 */
	@Column
	public int icon;
	
	/**
	 * 目前使用的边框
	 */
	@Column
	public int fram;
	
	/**
	 * 性别
	 * 0.女 1.男
	 */
	@Column
	public byte sex;
	
	/**
	 * 修改性别次数
	 */
	@Column
	public int modifySexNum;
	
	
	private List<Integer> oneStar = new ArrayList<>();
	private List<Integer> twoStar = new ArrayList<>();
	private List<Integer> threeStar = new ArrayList<>();
	private List<Integer> fourStar = new ArrayList<>();
	private List<Integer> fiveStar = new ArrayList<>();
	private List<Integer> sixStar = new ArrayList<>();
	private List<Integer> otherStar = new ArrayList<>();
	
	private List<Integer> frams = new ArrayList<>();

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
		Icon icon = new Icon();
		icon.actorId = rs.getLong("actorId");
		icon.oneStarHero = rs.getString("oneStarHero");
		icon.twoStarHero = rs.getString("twoStarHero");
		icon.threeStarHero = rs.getString("threeStarHero");
		icon.fourStarHero = rs.getString("fourStarHero");
		icon.fiveStarHero = rs.getString("fiveStarHero");
		icon.sixStarHero = rs.getString("sixStarHero");
		icon.otherStarHero = rs.getString("otherStarHero");
		icon.unLockFram = rs.getString("unLockFram");
		icon.icon = rs.getInt("icon");
		icon.fram = rs.getInt("fram");
		icon.sex = rs.getByte("sex");
		icon.modifySexNum = rs.getInt("modifySexNum");
		return icon;
	}

	@Override
	protected void hasReadEvent() {
		oneStar = StringUtils.delimiterString2IntList(oneStarHero, Splitable.ATTRIBUTE_SPLIT);
		twoStar = StringUtils.delimiterString2IntList(twoStarHero, Splitable.ATTRIBUTE_SPLIT);
		threeStar = StringUtils.delimiterString2IntList(threeStarHero, Splitable.ATTRIBUTE_SPLIT);
		fourStar = StringUtils.delimiterString2IntList(fourStarHero, Splitable.ATTRIBUTE_SPLIT);
		fiveStar = StringUtils.delimiterString2IntList(fiveStarHero, Splitable.ATTRIBUTE_SPLIT);
		sixStar = StringUtils.delimiterString2IntList(sixStarHero, Splitable.ATTRIBUTE_SPLIT);
		otherStar = StringUtils.delimiterString2IntList(otherStarHero, Splitable.ATTRIBUTE_SPLIT);
		frams = StringUtils.delimiterString2IntList(unLockFram, Splitable.ATTRIBUTE_SPLIT);
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> values = new ArrayList<>();
		if(containsPK){
			values.add(this.actorId);
		}
		values.add(oneStarHero);
		values.add(twoStarHero);
		values.add(threeStarHero);
		values.add(fourStarHero);
		values.add(fiveStarHero);
		values.add(sixStarHero);
		values.add(otherStarHero);
		values.add(unLockFram);
		values.add(icon);
		values.add(fram);
		values.add(sex);
		values.add(modifySexNum);
		return values;
	}

	@Override
	protected void beforeWritingEvent() {
		this.oneStarHero = StringUtils.collection2SplitString(oneStar, Splitable.ATTRIBUTE_SPLIT);
		this.twoStarHero = StringUtils.collection2SplitString(twoStar, Splitable.ATTRIBUTE_SPLIT);
		this.threeStarHero = StringUtils.collection2SplitString(threeStar, Splitable.ATTRIBUTE_SPLIT);
		this.fourStarHero = StringUtils.collection2SplitString(fourStar, Splitable.ATTRIBUTE_SPLIT);
		this.fiveStarHero = StringUtils.collection2SplitString(fiveStar, Splitable.ATTRIBUTE_SPLIT);
		this.sixStarHero = StringUtils.collection2SplitString(sixStar, Splitable.ATTRIBUTE_SPLIT);
		this.otherStarHero = StringUtils.collection2SplitString(otherStar, Splitable.ATTRIBUTE_SPLIT);
		this.unLockFram = StringUtils.collection2SplitString(frams, Splitable.ATTRIBUTE_SPLIT);
	}

	@Override
	protected void disposeBlob() {
		this.oneStarHero = EMPTY_STRING;
		this.twoStarHero = EMPTY_STRING;
		this.threeStarHero = EMPTY_STRING;
		this.fourStarHero = EMPTY_STRING;
		this.fiveStarHero = EMPTY_STRING;
		this.sixStarHero = EMPTY_STRING;
		this.otherStarHero = EMPTY_STRING;
		this.unLockFram = EMPTY_STRING;
	}
	
	public static Icon valueOf(long actorId){
		Icon icon = new Icon();
		icon.actorId = actorId;
		//初始化可使用1、2星仙人头像
		icon.oneStar = IconService.getOneStarIcon();
		icon.twoStar = IconService.getTwoStarIcon();
		//初始化可使用普通头像边框
		List<IconFramConfig> list = IconService.getAllIconFram();
		for(IconFramConfig config : list){
			if(config.framType == IconFramType.COMMON.getCode()){
				icon.frams.add(config.framId);
			}
		}
		icon.icon = icon.oneStar.get(0);
		icon.fram = icon.frams.get(0);
		return icon;
	}

	public List<Integer> getHeroId() {
		List<Integer> heroIds = new ArrayList<>();
		heroIds.addAll(oneStar);
		heroIds.addAll(twoStar);
		heroIds.addAll(threeStar);
		heroIds.addAll(fourStar);
		heroIds.addAll(fiveStar);
		heroIds.addAll(sixStar);
		heroIds.addAll(otherStar);
		return heroIds;
	}

	public List<Integer> getFram() {
		return frams;
	}

	public List<Integer> getHeroId(int star) {
		switch(star){
		case 1:
			return oneStar;
		case 2:
			return twoStar;
		case 3:
			return threeStar;
		case 4:
			return fourStar;
		case 5:
			return fiveStar;
		case 6:
			return sixStar;
		default:
			return otherStar;	
		}
	}

	public void unLockHero(int star, int heroId) {
		switch(star){
		case 1:
			oneStar.add(heroId);
			break;
		case 2:
			twoStar.add(heroId);
			break;
		case 3:
			threeStar.add(heroId);
			break;
		case 4:
			fourStar.add(heroId);
			break;
		case 5:
			fiveStar.add(heroId);
			break;
		case 6:
			sixStar.add(heroId);
			break;
		default:
			otherStar.add(heroId);
		}
	}

}
