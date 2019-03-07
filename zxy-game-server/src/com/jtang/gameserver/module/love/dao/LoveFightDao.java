package com.jtang.gameserver.module.love.dao;

import java.util.Collection;
import java.util.List;

import com.jtang.gameserver.dbproxy.entity.LoveRank;

public interface LoveFightDao {

	public LoveRank get(long actorId1,long actorId2);

	public Collection<LoveRank> getTopRanks(int minRank, int maxRank);

	public Collection<LoveRank> getRankList(int viewRank, int rank, int viewUp,int viewDown, List<Integer> upList);

	public void changeRank(LoveRank loveRank1, LoveRank loveRank2);
	
	public void formatTable();

	public void remove(long actorId1, long actorId2);

}
