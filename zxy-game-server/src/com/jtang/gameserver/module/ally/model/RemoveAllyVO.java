package com.jtang.gameserver.module.ally.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;

/**
 * 被删除的盟友
 * 
 * @author pengzy
 * 
 */
public class RemoveAllyVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5012004889882322037L;
	/**
	 * 盟友ID
	 */
	private long actorId;
	/**
	 * 被删除时间
	 */
	private int removeTime;

	public long getActorId() {
		return actorId;
	}

	public int getRemoveTime() {
		return removeTime;
	}

	public static RemoveAllyVO valueOf(long actorId) {
		RemoveAllyVO removedVO = new RemoveAllyVO();
		removedVO.actorId = actorId;
		removedVO.removeTime = TimeUtils.getNow();
		return removedVO;
	}

	public String parseToString() {
		List<Object> attributeList = new ArrayList<Object>();
		attributeList.add(actorId);
		attributeList.add(removeTime);
		return StringUtils.collection2SplitString(attributeList, Splitable.ATTRIBUTE_SPLIT);
	}

	public static RemoveAllyVO valueOf(String[] voSA) {
		RemoveAllyVO removedVO = new RemoveAllyVO();
		removedVO.actorId = Long.valueOf(voSA[0]);
		removedVO.removeTime = Integer.valueOf(voSA[1]);
		return removedVO;
	}
}
