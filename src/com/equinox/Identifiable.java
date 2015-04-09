package com.equinox;

public interface Identifiable<T extends Identifiable> {
	
	public int getId();
	public T getPlaceholder();
	public boolean isPlaceholder();
	public T copy();
}
