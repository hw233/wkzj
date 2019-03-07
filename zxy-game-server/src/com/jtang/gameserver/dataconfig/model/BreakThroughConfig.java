package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

/**
 * 魂魄投入产出
 * @author vinceruan
 *
 */
@DataFile(fileName = "breakThroughConfig")
public class BreakThroughConfig implements ModelAdapter {
	
	/**
	 * 品质
	 */
	private int star;
	
	/**
	 * 第几次突破
	 */
	private int breakOrder;
	
	/**
	 * 本次突破需要消耗的魂魄数
	 */
	private int soulCount;
	
	/**
	 * 本次突破需要的掌教等级
	 */
	public int level;
	
	/**
	 * 本次突破增加的可潜修次数	
	 */
	private int addDelve;
	
	/**
	 * 本次突破增加仙人的攻击
	 */
	public int attack;
	
	/**
	 * 本次突破增加仙人的防御
	 */
	public int defense;
	
	/**
	 * 本次突破增加仙人的血量
	 */
	public int hp;

	@Override
	public void initialize() {

	}

	public int getStar() {
		return star;
	}

	public int getBreakOrder() {
		return breakOrder;
	}

	public int getSoulCount() {
		return soulCount;
	}

	public int getAddDelve() {
		return addDelve;
	}
}
