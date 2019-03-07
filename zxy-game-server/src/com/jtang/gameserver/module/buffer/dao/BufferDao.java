package com.jtang.gameserver.module.buffer.dao;

import com.jtang.gameserver.dbproxy.entity.Buffers;

/**
 *  仙人buffer的数据访问接口
 * @author vinceruan
 *
 */
public interface BufferDao {
	/**
	 * 查询
	 * @param actorId
	 * @return
	 */
	public Buffers get(long actorId);
	
	/**
	 * 更新buffer列表
	 * @param userBuffer
	 * @return
	 */
	public boolean update(long actorId);
}
