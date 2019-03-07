package com.jtang.gameserver.module.snatch.type;

/**
 * 抢夺的敌人类型
 * @author 0x737263
 *
 */
public enum SnatchEnemyType {

	/**
	 * 玩家
	 */
	ACTOR(0),

	/**
	 * 机器人
	 */
	ROBOT(1);
	
	private int type;

	private SnatchEnemyType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
	public static SnatchEnemyType valueOf(int type){
		for (SnatchEnemyType enemyType : SnatchEnemyType.values()) {
			if(enemyType.getType() == type){
				return enemyType;
			}
		}
		return null;
	}
			
	
}
