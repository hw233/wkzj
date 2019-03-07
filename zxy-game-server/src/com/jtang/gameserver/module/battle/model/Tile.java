package com.jtang.gameserver.module.battle.model;


/**
 * 地图坐标
 * x,y值
 * @author 0x737263
 *
 */
public class Tile{

	private int x;
	private int y;

	public Tile() {
		this.x = 0;
		this.y = 0;
	}

	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * 获取当前点到目标点的平方距离
	 * @param targetTile		目标点
	 * @return
	 */
	public int getDis(Tile targetTile){
		Double d = Math.sqrt((targetTile.getX() - this.getX())*(targetTile.getX() - this.getX()) 
				  +(targetTile.getY() - this.getY())*(targetTile.getY() - this.getY()));
		return d.intValue();
	}

	@Override
	public String toString() {
		return "Tile [x=" + x + ", y=" + y + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Tile)) {
			return false;
		}
		
		Tile tile = (Tile)obj;
		if (tile.getX() == x && tile.getY() == y) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	
	
}
