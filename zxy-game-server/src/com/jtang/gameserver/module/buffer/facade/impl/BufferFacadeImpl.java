package com.jtang.gameserver.module.buffer.facade.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.BufferVO;
import com.jiatang.common.model.HeroVOAttributeKey;
import com.jtang.gameserver.module.buffer.dao.BufferDao;
import com.jtang.gameserver.module.buffer.facade.BufferFacade;
import com.jtang.gameserver.module.buffer.model.HeroBuffer;

/**
 * buffer模块服务接口
 * @author vinceruan
 *
 */
@Component
public class BufferFacadeImpl implements BufferFacade {
	@Autowired
	BufferDao bufferDao;

	
	@Override
	public HeroBuffer getHeroBuffer(long actorId, int heroId) {
		return this.bufferDao.get(actorId).getHeroBufferMap().get(heroId);
	}

	@Override
	public void removeBufferBySourceType(long actorId, int heroId, int... bufferSourceType) {
		HeroBuffer heroBuffer = getHeroBuffer(actorId, heroId);
		if (heroBuffer != null){
			for (int i : bufferSourceType) {
				heroBuffer.removeBuffersByType(i);
			}
			this.bufferDao.update(actorId);
		}
	}

	@Override
	public int getBuffAddValue(long actorId, int heroId, HeroVOAttributeKey key, int bufferSourceType) {
		HeroBuffer buf = getHeroBuffer(actorId, heroId);
		if (buf == null) {
			return 0;
		}
		
		int val = 0;
		for(List<BufferVO> list : buf.bufferTypeMap.values()) {
			for (BufferVO vo : list) {
				if (vo.key == key && vo.sourceType == bufferSourceType) {
					val += vo.addVal;
				}
			}
		}
		
		return val;
	}

	@Override
	public List<BufferVO> getBufferList(long actorId, int heroId) {
		HeroBuffer buf = getHeroBuffer(actorId, heroId);
		if (null != buf){
			return buf.get();
		}
		
		return new ArrayList<>();
	}

//	@Override
//	public boolean update(long actorId) {
//		
//		return this.bufferDao.update(actorId);
//	}

	@Override
	public void addBuff(long actorId, int heroId, BufferVO... bufferVO) {
		for (BufferVO bufferVO2 : bufferVO) {
			this.bufferDao.get(actorId).addBuffer(heroId, bufferVO2);
		}
		this.bufferDao.update(actorId);
	}



}
