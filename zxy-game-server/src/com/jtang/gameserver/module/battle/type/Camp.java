package com.jtang.gameserver.module.battle.type;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import com.jtang.gameserver.module.battle.model.Tile;

/**
 * 阵营,战斗中使用
 * 共有上下两个阵型
 * 抽象出阵型的概念, 主要是为了在处理战斗时方便找出敌方和友方，还有就是不同阵型的角色在向前移动时y值变化不一样(+1或-1)
 * @author vinceruan
 *
 */
public enum Camp {
	
	/**
	 * 上面
	 */
	ABOVE(0),
	
	/**
	 * 下面
	 */
	BELOW(1);
	private final byte type;
	private Camp(int type) {
		this.type = (byte)type;
	}
	
	public byte getType() {
		return type;
	}

	public Camp getEnermyCamp() {
		if (this == ABOVE) {
			return BELOW;
		} else {
			return ABOVE;
		}
	}
	
	/**
	 * 获取后面格子的坐标
	 * @return
	 */
	public Tile getBehindPosition(Tile tile) {
		Tile t = new Tile();
		t.setX(tile.getX());
		if (this == ABOVE) {
			t.setY(tile.getY() + 1);
		} else {
			t.setY(tile.getY() - 1);
		}
		return t;
	}
	
	/**
	 * 获取前面的格子
	 * @param tile
	 * @return
	 */
	public Tile getAheadPosition(Tile tile) {
		Tile t = new Tile();
		t.setX(tile.getX());
		if (this == ABOVE) {
			t.setY(tile.getY() - 1);
		} else {
			t.setY(tile.getY() + 1);
		}
		return t;
	}
	
	/**
	 * 获取右边格子的坐标
	 * @param tile
	 * @return
	 */
	public Tile getRightPosition(Tile tile) {
		Tile t = new Tile();
		t.setY(tile.getY());
		if (this == ABOVE) {
			t.setX(tile.getX() - 1);
		} else {
			t.setX(tile.getX() + 1);
		}
		return t;
	}
	
	/**
	 * 获取左边格子的坐标
	 * @param tile
	 * @return
	 */
	public Tile getLefPosition(Tile tile) {
		Tile t = new Tile();
		t.setY(tile.getY());
		if (this == ABOVE) {
			t.setX(tile.getX() + 1);
		} else {
			t.setX(tile.getX() - 1);
		}
		return t;
	}
	
	/**
	 * 获取左前方格子的坐标
	 * @param tile
	 * @return
	 */
	public Tile getFowardLeftPosition(Tile tile) {
		if (this == ABOVE) {
			return new Tile(tile.getX() - 1, tile.getY() + 1);
		} else {
			return new Tile(tile.getX() + 1, tile.getY() - 1);
		}
	}
	
	/**
	 * 获取右前方的格子坐标
	 * @param tile
	 * @return
	 */
	public Tile getFowardRightPosition(Tile tile) {
		if (this == ABOVE) {
			return new Tile(tile.getX() - 1, tile.getY() - 1);
		} else {
			return new Tile(tile.getX() +1, tile.getY() + 1);
		}
	}
	
	/**
	 * target 是否站在reference的后面
	 * @param target
	 * @param reference
	 * @return
	 */
	public boolean isBehind(Tile target, Tile reference) {
		if (this == ABOVE) {
			return target.getY() > reference.getY();
		} else {
			return target.getY() < reference.getY();
		}
	}
	/**
	 * target 是否站在reference的前面
	 * @param target
	 * @param reference
	 * @return
	 */
	public boolean isAhead(Tile target, Tile reference) {
		if (this == ABOVE) {
			return target.getY() < reference.getY();
		} else {
			return target.getY() > reference.getY();
		}
	}
	
	/**
	 * target 是否站在reference的右面
	 * @param target
	 * @param reference
	 * @return
	 */
	public boolean isRight(Tile target, Tile reference) {
		if (this == ABOVE) {
			return target.getX() < reference.getX();
		} else {
			return target.getX() > reference.getX();
		}
	}
	
	/**
	 * 获取朝指定方向走一步的坐标
	 * @param from
	 * @param direct
	 * @return
	 */
	public Tile move(Tile from, MoveDirection direct) {
		if (this == ABOVE) {
			switch (direct) {
			case FORWARD:
				return new Tile(from.getX(), from.getY() - 1);
			case BACKWARD:
				return new Tile(from.getX(), from.getY() + 1);
			case LEFT:
				return new Tile(from.getX() + 1, from.getY());
			default:
				return new Tile(from.getX() - 1, from.getY());
			}
		} else {
			switch (direct) {
			case FORWARD:
				return new Tile(from.getX(), from.getY() + 1);
			case BACKWARD:
				return new Tile(from.getX(), from.getY() - 1);
			case LEFT:
				return new Tile(from.getX() - 1, from.getY());
			default:
				return new Tile(from.getX() + 1, from.getY());
			}
		}
	}
	
	/**
	 * 获取往前或往后跳N步的坐标
	 * @param from
	 * @param direct
	 * @return
	 */
	public Tile jump(Tile from, int step) {
		if (this == ABOVE) {
			return new Tile(from.getX(), from.getY() - step);
		} else {
			return new Tile(from.getX(), from.getY() + step);
		}
	}
	
	/**
	 * 由前往后排序
	 * @param srcTiles
	 * @return
	 */
	public Set<Tile> sortByCol(Collection<Tile> srcTiles) {
		Set<Tile> tempSet = new TreeSet<Tile>(new Comparator<Tile>() {
			@Override
			public int compare(Tile o1, Tile o2) {
				if (Camp.this == ABOVE) {
					return o1.getY() - o2.getY();
				} else {
					return o2.getY() - o1.getY();
				}
			}			
		});
		tempSet.addAll(srcTiles);
		return tempSet;
	}
}
