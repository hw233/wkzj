package com.jtang.gameserver.module.buffer.facade;

import java.util.List;

import com.jiatang.common.model.BufferVO;
import com.jiatang.common.model.HeroVOAttributeKey;
import com.jtang.gameserver.module.buffer.model.HeroBuffer;

/**
 * buffer服务接口
 * @author vinceruan
 *
 */
public interface BufferFacade {
//	/**
//	 * 查询
//	 * @param actorId
//	 * @return
//	 */
//	public Buffers get(long actorId);
//	
//	/**
//	 * 更新buffer列表
//	 * @param userBuffer
//	 * @return
//	 */
//	public boolean update(long actorId);
	
	/**
	 * 获取某个仙人身上的buffer
	 * @param actorId
	 * @param heroId
	 * @return
	 */
	public HeroBuffer getHeroBuffer(long actorId, int heroId);
	
	/**
	 * 根据buffer类型移除buffer
	 * @param actorId 角色ID
	 * @param heroId 仙人Id
	 * @param bufferSourceType buffer类型
	 */
	public void removeBufferBySourceType(long actorId, int heroId, int... bufferSourceType);
	
	/**
	 * 获取buf加成数值
	 * @param actorId 角色Id
	 * @param heroId 英雄Id
	 * @param bufferSourceType buffer类型
	 * @return
	 */
	public int getBuffAddValue(long actorId, int heroId, HeroVOAttributeKey key, int bufferSourceType);
	
	/**
	 * 获取英雄身上buffer列表
	 * @param actorId
	 * @param heroId
	 * @return
	 */
	public List<BufferVO> getBufferList(long actorId, int heroId);
	
	/**
	 * 添加一个英雄身上buffer
	 * @param actorId
	 * @param heroId
	 * @param bufferVO
	 */
	public void addBuff(long actorId, int heroId, BufferVO... bufferVO);
	
}
