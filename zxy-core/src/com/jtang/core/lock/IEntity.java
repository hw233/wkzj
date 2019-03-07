package com.jtang.core.lock;

public abstract interface IEntity<T extends Comparable<T>> {
	public abstract T getIdentity();
}