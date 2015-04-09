package com.equinox;

public interface UndoableRedoable<T extends UndoableRedoable<T>> {
	
	public int getId();
	public T getPlaceholder();
	public boolean isPlaceholder();
	public T copy();
}
