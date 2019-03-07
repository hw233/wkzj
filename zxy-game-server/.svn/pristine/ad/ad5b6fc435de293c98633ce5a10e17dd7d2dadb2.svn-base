package com.jtang.gameserver.module.snatch.type;

/**
 * 成就类型
 * @author vinceruan
 *
 */
public enum SnatchAchimentType {
	
	/**
	 * 战斗就可获得奖励
	 */
	TYPE1(1),
	
	/**
	 * 赢才会获得奖励
	 */
	TYPE2(2);
	
	private int type;
	
	private SnatchAchimentType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
	
	public static SnatchAchimentType valueOf(int type){
		for (SnatchAchimentType enemyType : SnatchAchimentType.values()) {
			if(enemyType.getType() == type){
				return enemyType;
			}
		}
		return null;
	}
	
}
