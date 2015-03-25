package com.equinox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.equinox.exceptions.InvalidParamException;
import com.equinox.exceptions.NullTodoException;
import com.equinox.exceptions.StateUndefinedException;

/**
 * Stores all Todos and keeps state information allowing Undo and Redo
 * operations. Maximum number of states that can be stored by Memory is
 * {@value #STATE_STACK_MAX_SIZE}.
 * 
 * @author Ho Wei Li || IkarusWill
 *
 */
public class Memory {

	private static final String REGEX_SPACE = "\\s";
	private static final int STATE_STACK_MAX_SIZE = 5;
	private static final int ID_INITIAL = 0;
	private static final int ID_BUFFER_INITIAL_SIZE = 5;
	private static final int ID_BUFFER_MAX_SIZE = 2 * ID_BUFFER_INITIAL_SIZE;
	private static final int RECURRING_MAX_INSTANCES = 7;
    private HashMap<Integer, Todo> allTodos;
	private HashMap<Integer, List<Todo>> recurringTodos;
    private HashMap<Integer, RecurringTodoRule> recurringRules;
	private HashMap<Integer, DateTime> recurrenceLimits;
	private final IDBuffer idBuffer;
	private final IDBuffer recurringIdBuffer;
	private LinkedList<Todo> undoStack;
	private LinkedList<Todo> redoStack;
	private SearchMap searchMap;
	private StorageHandler storageHandler = new StorageHandler();

	/**
	 * Constructs an empty Memory object.
	 */
	public Memory() {
        this.allTodos = new HashMap<Integer, Todo>();
		this.recurringTodos = new HashMap<Integer, List<Todo>>();
        this.recurringRules = new HashMap<Integer, RecurringTodoRule>();
		this.recurrenceLimits = new HashMap<Integer, DateTime>();
		this.idBuffer = new IDBuffer();
		this.recurringIdBuffer = new IDBuffer();
		this.undoStack = new LinkedList<Todo>();
		this.redoStack = new LinkedList<Todo>();
		searchMap = new SearchMap();
	}

	/**
	 * Adds the specified Todo to memory. The current state is saved prior to
	 * any operation. Since add is a memory modifying command, the redoStack is
	 * flushed.
	 * <p>
	 * This operation also adds all parameters of the Todo specified into the
	 * SearchMap for indexing.
	 * 
	 * @param todo
	 *            the Todo to be added.
	 */
	public void add(Todo todo) {
		int id = todo.getId();
		save(todo.getPlaceholder());
		flushRedoStack();
        allTodos.put(id, todo);
		searchMap.add(todo);
	}

    /**
     * Handle adding of recurring tasks as rules
     * 
     * @param rule
     */
	public void add(RecurringTodoRule rule) {
        recurringRules.put(rule.getRecurringId(), rule);
        updateRecurringRules();
	}

    private void updateRecurringRules() {
        Collection<RecurringTodoRule> rules = recurringRules.values();
        for (Iterator<RecurringTodoRule> iterator = rules.iterator(); iterator
                .hasNext();) {
            RecurringTodoRule rule = (RecurringTodoRule) iterator.next();
            rule.updateTodoList(this);
        }
    }

	/**
	 * Retrieves the Todo identified by the specified ID from the memory.
	 * 
	 * @param id
	 *            the ID of the Todo to be retrieved.
	 * @return the Todo object identified by the specified ID.
	 * @throws NullTodoException
	 *             if the Todo identified by the specified ID does not exist.
	 */
	public Todo get(int id) throws NullTodoException {
        Todo returnTodo = allTodos.get(id);
		if (returnTodo == null) {
			throw new NullTodoException(ExceptionMessages.NULL_TODO_EXCEPTION);
		}
		return returnTodo;
	}

	/**
	 * Retrieves the Todo identified by the specified ID from the memory for
	 * editing. The current state is saved prior to any operation.
	 * 
	 * @param id
	 *            the ID of the Todo to be retrieved.
	 * @return the Todo object identified by the specified ID.
	 * @throws NullTodoException
	 *             if the Todo identified by the specified ID does not exist.
	 */
	public Todo setterGet(int id) throws NullTodoException {
        Todo returnTodo = allTodos.get(id);
		if (returnTodo == null) {
			throw new NullTodoException(ExceptionMessages.NULL_TODO_EXCEPTION);
		}
		save(returnTodo);
		flushRedoStack();
		return returnTodo;
	}

	/**
	 * Removes the Todo identified by the specified id from the memory. The
	 * current state is saved prior to any operation.
	 * 
	 * @param id
	 *            the ID of the Todo to be removed.
	 * @throws NullTodoException
	 *             if the Todo identified by the specified ID does not exist.
	 */
	public Todo remove(int id) throws NullTodoException {
        Todo returnTodo = allTodos.get(id);
		if (returnTodo == null) {
			throw new NullTodoException(ExceptionMessages.NULL_TODO_EXCEPTION);
		}
		save(returnTodo);
		flushRedoStack();
        allTodos.remove(id);
		searchMap.remove(returnTodo);
		return returnTodo;
	}

	/**
	 * Saves the a copy of the state of a Todo into the undo stack. If the Todo
	 * specified is null, a placeholder is used instead.
	 * <p>
	 * The stack never contains null values. <br>
	 * If the maximum stack size is reached, the earliest state is discarded. <br>
	 * If the stack and memory no longer contains a particular Todo, its ID is
	 * returned to the pool of available indices.
	 * 
	 * @param toBeSaved
	 *            the Todo to be saved.
	 */
	private void save(Todo toBeSaved) {
		// If undo stack has exceeded max size, discard earliest state.
		Todo toBeSavedCopy = new Todo(toBeSaved);
		if (undoStack.size() > STATE_STACK_MAX_SIZE) {
			int id = undoStack.removeFirst().getId();
            if (!allTodos.containsKey(id)) {
				releaseId(id);
			}
		}
		undoStack.add(toBeSavedCopy);
	}

	/**
	 * Flushes both undo and redo stacks. For use with exit command.
	 */
	void flushStacks() {
		flushRedoStack();
		flushUndoStack();
	}

	/**
	 * Flushes the undoStack of all states of Todos.
	 */
	private void flushUndoStack() {
		while (!undoStack.isEmpty()) {
			int id = undoStack.pollLast().getId();
            if (!allTodos.containsKey(id)) {
				releaseId(id);
			}
		}
	}

	/**
	 * Flushes the redoStack of all states of Todos.
	 */
	private void flushRedoStack() {
		while (!redoStack.isEmpty()) {
			int id = redoStack.pollLast().getId();
            if (!allTodos.containsKey(id)) {
				releaseId(id);
			}
		}
	}

	/**
	 * Restores the latest history state of the memory. Also known as the undo
	 * operation.
	 * 
	 * @throws StateUndefinedException
	 *             if there are no history states to restore to.
	 */
	public void restoreHistoryState() throws StateUndefinedException {
		Todo fromStack;
		try {
			fromStack = undoStack.removeLast();
		} catch (NoSuchElementException e) {
			throw new StateUndefinedException(
					ExceptionMessages.NO_HISTORY_STATES_EXCEPTION);
		}

		int id = fromStack.getId();
        Todo inMemory = allTodos.get(id);

		// If Todo does not exist in memory, use placeholder.
		if (inMemory == null) {
			inMemory = fromStack.getPlaceholder();
		}

		// Redo stack will not exceed maximum size.
		redoStack.add(inMemory);

		// If Todo from stack is a placeholder, delete Todo indicated by its
		// ID in the memory.
		if (fromStack.getCreatedOn() == null) {
            allTodos.remove(id);
		} else {
            allTodos.put(id, fromStack);
		}
	}

	/**
	 * Restores the latest future state of the memory. Also known as the redo
	 * operation.
	 * 
	 * @throws StateUndefinedException
	 *             if there are no future states to restore to.
	 */
	public void restoreFutureState() throws StateUndefinedException {
		Todo fromStack;
		try {
			fromStack = redoStack.removeLast();
		} catch (NoSuchElementException e) {
			throw new StateUndefinedException(
					ExceptionMessages.NO_FUTURE_STATES_EXCEPTION);
		}

		int id = fromStack.getId();
        Todo inMemory = allTodos.get(id);

		// If Todo does not exist in memory, use placeholder.
		if (inMemory == null) {
			inMemory = fromStack.getPlaceholder();
		}

		save(inMemory);

		// If Todo from stack is a placeholder, delete Todo indicated by its
		// ID in the memory.
		if (fromStack.getCreatedOn() == null) {
            allTodos.remove(id);
		} else {
            allTodos.put(id, fromStack);
		}
	}

	/**
	 * Method to get all the Todos for displaying purposes.
	 * 
	 * @return all Todos as Collection
	 */
	public Collection<Todo> getAllTodos() {
        updateRecurringRules();
        return allTodos.values();
	}

    private RecurringTodoRule getRecurringTodoRule(int recurringID) {
        return recurringRules.get(recurringID);
    }

	/**
	 * Obtains an ID number from the pool of available ID numbers.
	 * 
	 * @return the ID obtained.
	 */
	public int obtainFreshId() {
		return idBuffer.get();
	}

	public int obtainFreshRecurringId() {
		return recurringIdBuffer.get();
	}

	/**
	 * Releases the specified ID number to the pool of available ID numbers for
	 * future use by new Todos.
	 * 
	 * @param id
	 *            the ID to be released.
	 */
	public void releaseId(int id) {
		idBuffer.put(id);
	}

	public void releaseRecurringId(int recurringId) {
		recurringIdBuffer.put(recurringId);
	}

	/**
	 * Serves as a buffer of fixed size for new Todos to draw their ID from.
	 * 
	 * @author Ikarus
	 *
	 */
	protected class IDBuffer {
		private TreeSet<Integer> buffer;
		private int minFreeId;

		protected IDBuffer() {
			buffer = new TreeSet<Integer>();
			minFreeId = ID_INITIAL;
			for (int i = ID_INITIAL; i < ID_INITIAL + ID_BUFFER_INITIAL_SIZE; i++) {
				buffer.add(i);
			}
		}

		private int get() {
			if (buffer.size() == 1) {
				loadToSize();
			}
			int returnId = buffer.pollFirst();
			minFreeId = buffer.first();
			return returnId;
		}

		private void put(int id) {
			if (id < minFreeId) {
				minFreeId = id;
			}
			buffer.add(id);
			if (buffer.size() > ID_BUFFER_MAX_SIZE) {
				unloadToSize();
			}
		}

		private void loadToSize() {
			int minUnloadedId = minFreeId + 1;
			int i = minUnloadedId;

			while (i < minUnloadedId + ID_BUFFER_INITIAL_SIZE) {
                if (allTodos.containsKey(i)) {
					minUnloadedId++;
				} else {
					buffer.add(i);
					i++;
				}
			}
		}

		private void unloadToSize() {
			for (int i = 0; i < ID_BUFFER_INITIAL_SIZE; i++) {
				buffer.pollLast();
			}
		}
	}

	/**
	 * This class stores the mapping of various types of index to a list of Todo
	 * ids for the purpose of the search command
	 * 
	 * @author peanut11
	 *
	 */
	private class SearchMap {
		private HashMap<String, ArrayList<Integer>> nameMap;
		private HashMap<LocalDate, ArrayList<Integer>> dateMap;
		private HashMap<LocalTime, ArrayList<Integer>> timeMap;
		private HashMap<Integer, ArrayList<Integer>> monthMap;
		private HashMap<Integer, ArrayList<Integer>> dayMap;

		SearchMap() {
			this.nameMap = new HashMap<String, ArrayList<Integer>>();
			this.dateMap = new HashMap<LocalDate, ArrayList<Integer>>();
			this.timeMap = new HashMap<LocalTime, ArrayList<Integer>>();
			this.monthMap = new HashMap<Integer, ArrayList<Integer>>();
			this.dayMap = new HashMap<Integer, ArrayList<Integer>>();
		}

		/**
		 * This operation adds the properties of todo into the different maps.
		 * 
		 * @param todo
		 */
		public void add(Todo todo) {
			int id = todo.getId();

			addToNameMap(todo.getName(), id);

			DateTime startDateTime = todo.getStartTime();
			DateTime endDateTime = todo.getEndTime();
			if (startDateTime != null) {
				addToAllDateMaps(startDateTime, id);
			}
			if (endDateTime != null) {
				addToAllDateMaps(endDateTime, id);
			}

		}

		/**
		 * This operation adds the dateTime property of todo with the given id
		 * into the various date related maps
		 * 
		 * @param dateTime
		 * @param id
		 */
		private void addToAllDateMaps(DateTime dateTime, int id) {
			// add id to dateMap
			LocalDate date = dateTime.toLocalDate();
			if (dateMap.containsKey(date)) {
				dateMap.get(date).add(id);
			} else {
				ArrayList<Integer> newIdList = new ArrayList<Integer>();
				newIdList.add(id);
				dateMap.put(date, newIdList);
			}

			// add id to timeMap
			LocalTime time = dateTime.toLocalTime();
			if (timeMap.containsKey(time)) {
				timeMap.get(time).add(id);
			} else {
				ArrayList<Integer> newIdList = new ArrayList<Integer>();
				newIdList.add(id);
				timeMap.put(time, newIdList);
			}

			// add id to dayMap
			int day = dateTime.getDayOfWeek();
			if (dayMap.containsKey(day)) {
				dayMap.get(day).add(id);
			} else {
				ArrayList<Integer> newIdList = new ArrayList<Integer>();
				newIdList.add(id);
				dayMap.put(day, newIdList);
			}

			// add id to monthMap
			int month = dateTime.getMonthOfYear();
			if (monthMap.containsKey(month)) {
				monthMap.get(month).add(id);
			} else {
				ArrayList<Integer> newIdList = new ArrayList<Integer>();
				newIdList.add(id);
				monthMap.put(month, newIdList);
			}
		}

		/**
		 * This operation removes the different properties of given todo from
		 * the different maps.
		 * 
		 * @param todo
		 */
		public void remove(Todo todo) {
			int id = todo.getId();
			String name = todo.getName();
			removeIdFromNames(name, id);

			DateTime startDateTime = todo.getStartTime();
			DateTime endDateTime = todo.getEndTime();
			if (startDateTime != null) {
				removeIdFromAllDateMaps(startDateTime, id);
			}
			if (endDateTime != null) {
				removeIdFromAllDateMaps(endDateTime, id);
			}

		}

		/**
		 * This operation removes the DateTime property of todo with the given
		 * id from the various date related maps
		 * 
		 * @param dateTime
		 * @param id
		 */
		private void removeIdFromAllDateMaps(DateTime dateTime, int id) {
			// remove id from dateMap
			LocalDate date = dateTime.toLocalDate();
			int todoIdDateIndex = dateMap.get(date).indexOf(id);
			dateMap.get(date).remove(todoIdDateIndex);
			if (dateMap.get(date).isEmpty()) {
				dateMap.remove(date);
			}

			// remove id from timeMap
			LocalTime time = dateTime.toLocalTime();
			int todoIdTimeIndex = timeMap.get(time).indexOf(id);
			timeMap.get(time).remove(todoIdTimeIndex);
			if (timeMap.get(time).isEmpty()) {
				timeMap.remove(time);
			}

			// remove id from dayMap
			int day = dateTime.getDayOfWeek();
			int todoIdDayIndex = dayMap.get(day).indexOf(id);
			timeMap.get(day).remove(todoIdDayIndex);
			if (timeMap.get(day).isEmpty()) {
				timeMap.remove(day);
			}

			// remove id from monthMap
			int month = dateTime.getMonthOfYear();
			int todoIdMonthIndex = monthMap.get(month).indexOf(id);
			timeMap.get(month).remove(todoIdMonthIndex);
			if (timeMap.get(month).isEmpty()) {
				timeMap.remove(month);
			}
		}

		/**
		 * This operation removes the name of the todo with the given id from
		 * name map.
		 * 
		 * @param name
		 * @param id
		 */
		private void removeIdFromNames(String name, int id) {
			String[] nameArray = name.split(REGEX_SPACE);
			for (String x : nameArray) {
				int todoIdIndex = nameMap.get(x).indexOf(id);
				nameMap.remove(todoIdIndex);
				if (nameMap.get(x).isEmpty()) {
					nameMap.remove(x);
				}
			}
		}

		/**
		 * This operation adds the name property of the todo with the given id
		 * into the name map.
		 * 
		 * @param name
		 * @param id
		 */
		private void addToNameMap(String name, int id) {
			String[] nameArray = name.split(REGEX_SPACE);
			for (String x : nameArray) {
				if (nameMap.containsKey(x)) {
					nameMap.get(x).add(id);
				} else {
					ArrayList<Integer> newIdList = new ArrayList<Integer>();
					newIdList.add(id);
					nameMap.put(x, newIdList);
				}
			}
		}

		/**
		 * This operation retrieves a result of todo ids when searching the
		 * given searchKey within the given typeKey.
		 * 
		 * @param typeKey
		 * @param searchKey
		 * @return todoIds
		 */
		public ArrayList<Integer> getResult(Keywords typeKey, String searchKey) {
			// searchKey can only be String if searchType is name
			assert (typeKey == Keywords.NAME);
			ArrayList<Integer> toDoIds = new ArrayList<Integer>();

			if (nameMap.containsKey(searchKey)) {
				toDoIds = nameMap.get(searchKey);
			}
			return toDoIds;
		}

		/**
		 * This operation retrieves a result of todo ids when searching the
		 * given datetime within the given typeKey.
		 * 
		 * @param typeKey
		 * @param dateTime
		 * @return todoIds
		 */
		public ArrayList<Integer> getResult(Keywords typeKey, DateTime dateTime)
				throws InvalidParamException {
			ArrayList<Integer> toDoIds = new ArrayList<Integer>();
			switch (typeKey) {
				case DATE:
					LocalDate searchDate = dateTime.toLocalDate();
					if (dateMap.containsKey(searchDate)) {
						toDoIds = dateMap.get(searchDate);
					} // else searchDate is not in dateMap, toDoIds is empty
						// List
					break;
				case DAY:
					int searchDay = dateTime.getDayOfWeek();
					if (dayMap.containsKey(searchDay)) {
						toDoIds = dayMap.get(searchDay);
					}// else searchDay is not in dayMap, toDoIds is empty List
					break;
				case MONTH:
					int searchMonth = dateTime.getMonthOfYear();
					if (monthMap.containsKey(searchMonth)) {
						toDoIds = monthMap.get(searchMonth);
					}// else searchMonth is not in monthMap, toDoIds is empty
						// List
					break;
				case TIME:
					LocalTime searchTime = dateTime.toLocalTime();
					if (timeMap.containsKey(searchTime)) {
						toDoIds = timeMap.get(searchTime);
					}// else searchTime is not in timeMap, toDoIds is empty List
				default:
					throw new InvalidParamException(
							ExceptionMessages.INVALID_SEARCH_TYPE_EXCEPTION);
			}
			return toDoIds;
		}
	}

	/**
	 * This operation retrieves a list of ids of todos that has the given searchString
	 * in its property of given typeKey
	 * 
	 * @param typeKey
	 * @param searchString
	 * @return todoIds
	 */
	public ArrayList<Integer> search(Keywords typeKey, String searchString) {
		// search method with String type search key is only for search in Todo
		// names
		assert (typeKey == Keywords.NAME);

		ArrayList<Integer> tempResult;
		ArrayList<Integer> resultList = new ArrayList<Integer>();
		String[] paramArray = searchString.split(REGEX_SPACE);
		for (String searchKey : paramArray) {
			tempResult = searchMap.getResult(typeKey, searchKey);
			resultList.addAll(tempResult);
		}

		if (resultList.isEmpty()) {
			return new ArrayList<Integer>();
		} else {
			return resultList;
		}

	}

	/**
	 * This operation retrieves a list of ids of todos that has the given dateTime
	 * in its property of given typeKey
	 * 
	 * @param typeKey
	 * @param dateTime
	 * @return todoIds
	 */
	public ArrayList<Integer> search(Keywords typeKey, DateTime dateTime)
			throws InvalidParamException {
		return searchMap.getResult(typeKey, dateTime);
	}

	/**
	 * Saves this instance of memory to file by calling the storeMemoryToFile
	 * method in the StorageHandler object.
	 * 
	 * @author Jonathan Lim Siu Chi || ign3sc3nc3
	 */
	public void saveToFile() {
		storageHandler.storeMemoryToFile(this);
	}
}
