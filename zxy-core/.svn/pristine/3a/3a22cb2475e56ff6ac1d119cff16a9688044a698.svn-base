package com.jiatang.common.model;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 精灵,所有生物接口
 * 这个接口用于处理所以通用的属性.
 * @author 0x737263
 *
 */
public abstract class Sprite extends IoBufferSerializer {
	 
	/**
	 * 每个精灵在生命周期内都会有一个临时的uid.当对象构造的时候就会分配一个。
	 */
	private static final AtomicLong AUTO_UID = new AtomicLong(1);
	
	/**
	 * 精灵唯一id
	 */
	protected long spriteId;
	
	protected Sprite() {
		this.spriteId = AUTO_UID.getAndIncrement();
	}
	
	/**
	 * 获取仙人的id
	 * @return
	 */
	public long getSpriteId() {
		return this.spriteId;
	}
	
	/**
	 * 获取攻击范围(格子数)
	 * @return
	 */
	public abstract int getAtkScope();
	
	/**
	 * 获取攻击力
	 * @return
	 */
	public abstract int getAtk();
	
	/**
	 * 获取生命值
	 * @return
	 */
	public abstract int getHp();
	
	/**
	 * 获取防御力
	 * @return
	 */
	public abstract int getDefense();
	
	/**
	 * 获取等级
	 * @return
	 */
	public abstract int getLevel();
	
	/**
	 * 获取精灵的配置ID
	 * @return
	 */
	public abstract int getHeroId();
	
	/**
	 * 获取主动技能
	 * @return
	 */
	public abstract int getSkillId();
	
	/**
	 * 获取被动技能
	 * @return
	 */
	public abstract List<Integer> getPassiveSkillList();
	
	/**
	 * 获取血最大值
	 * @return
	 */
	public abstract int getMaxHp();
	
	/**
	 * 设置血最大值
	 */
	public abstract void setMaxHp(int value);
	
	@Override
	public void write() {
		
	}
	
	public void setSpriteId(long spriteId) {
		this.spriteId = spriteId;
	}
}
