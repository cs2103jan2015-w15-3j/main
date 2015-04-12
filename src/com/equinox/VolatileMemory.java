package com.equinox;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

import com.equinox.exceptions.StateUndefinedException;

public class VolatileMemory {
	private Stack<Boolean> undoIsRule;
	private Stack<Boolean> redoIsRule;
	private UndoRedoStack<Todo> todoStacks;
	private UndoRedoStack<RecurringTodoRule> ruleStacks;
	private static final int STATE_STACK_MAX_SIZE = 5;

	public VolatileMemory(HashMap<Integer, Todo> allTodos, IDBuffer<Todo> idBuffer, HashMap<Integer, RecurringTodoRule> recurringRules, IDBuffer<RecurringTodoRule> recurringIdBuffer) {
		this.undoIsRule = new Stack<Boolean>();
		this.redoIsRule = new Stack<Boolean>();
		this.todoStacks = new UndoRedoStack<Todo>(allTodos, idBuffer, STATE_STACK_MAX_SIZE);
		this.ruleStacks = new UndoRedoStack<RecurringTodoRule>(recurringRules, recurringIdBuffer, STATE_STACK_MAX_SIZE);
	}
	
	public void save(Todo todo) {
		todoStacks.save(todo);
		undoIsRule.push(false);
		flushRedoStacks();
	}
	
	public void save(RecurringTodoRule rule) {
		ruleStacks.save(rule);
		undoIsRule.push(true);
		flushRedoStacks();
	}

	public void undo() throws StateUndefinedException {
		try {
			boolean isRule = undoIsRule.pop();
			redoIsRule.push(isRule);
			if(isRule) {
				ruleStacks.restoreHistoryState();
			} else {
				todoStacks.restoreHistoryState();
			}
		} catch (EmptyStackException e) {
			throw new StateUndefinedException(ExceptionMessages.NO_HISTORY_STATES);
		}
	}
	

	public void redo() throws StateUndefinedException {
		try {
			boolean isRule = redoIsRule.pop();
			undoIsRule.push(isRule);
			if (isRule) {
				ruleStacks.restoreFutureState();
			} else {
				todoStacks.restoreFutureState();
			}
		} catch (EmptyStackException e) {
			throw new StateUndefinedException(ExceptionMessages.NO_FUTURE_STATES);
		}
	}
	
	public void flushStacks() {
		todoStacks.flushStacks();
		ruleStacks.flushStacks();
	}
	
	private void flushRedoStacks() {
		todoStacks.flushRedoStack();
		ruleStacks.flushRedoStack();
		redoIsRule.clear();
	}
	
}
