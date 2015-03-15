package com.equinox;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import com.equinox.exceptions.NullTodoException;
import com.equinox.exceptions.StateUndefinedException;

/**
 * Stores all Todos and keeps state information allowing Undo and Redo
 * operations. Maximum number of states that can be stored by Memory is
 * {@value #STACK_MAX_SIZE}.
 * 
 * @author Ho Wei Li || IkarusWill
 *
 */
public class Memory {

	private static final String REGEX_SPACE = "\\s";
	private static final int STACK_MAX_SIZE = 5;
	private static final int ID_BUFFER_INITIAL_SIZE = 5;
	private static final int ID_BUFFER_MAX_SIZE = 2 * ID_BUFFER_INITIAL_SIZE;
	private final IDBuffer idBuffer = new IDBuffer();
	private int startingId;
	private HashMap<Integer, Todo> memoryMap;
	private LinkedList<Todo> undoStack;
	private LinkedList<Todo> redoStack;
	private HashMap<String, ArrayList<Integer>> nameMap;
	private HashMap<LocalDate, ArrayList<Integer>> dateMap;
	private HashMap<LocalTime, ArrayList<Integer>> timeMap;
//	private HashMap<Integer, ArrayList<Integer>> yearMap;
	private HashMap<Integer, ArrayList<Integer>> monthMap;
	private HashMap<Integer, ArrayList<Integer>> dayMap;
	
	/**
	 * Constructs an empty Memory object.
	 */
	public Memory() {
		this.startingId = 0;
		this.memoryMap = new HashMap<Integer, Todo>();
		this.undoStack = new LinkedList<Todo>();
		this.redoStack = new LinkedList<Todo>();
		this.nameMap = new HashMap<String, ArrayList<Integer>>();
		this.dateMap = new HashMap<LocalDate, ArrayList<Integer>>();
		this.timeMap = new HashMap<LocalTime, ArrayList<Integer>>();
//		this.yearMap = new HashMap<Integer, ArrayList<Integer>>();
		this.monthMap = new HashMap<Integer, ArrayList<Integer>>();
		this.dayMap = new HashMap<Integer, ArrayList<Integer>>();
	}

	/**
	 * Adds the specified Todo to memory. The current state is saved prior to
	 * any operation.
	 * 
	 * @param todo the Todo to be added.
	 */
	public void add(Todo todo) {
		int id = todo.getId();
		save(todo.getPlaceholder());
		flushRedoStack();
		memoryMap.put(id, todo);

		// inserts fields in Todo into various hashmaps
		// TODO: Propose refactoring into nested class.
		insertToNameMap(todo.getTitle(), id);
		DateTime startDateTime = todo.getStartTime();
		DateTime endDateTime = todo.getEndTime();
		if (startDateTime != null) {
			LocalDate startDate = startDateTime.toLocalDate();
			insertToDateMap(startDate, id);
			LocalTime startTime = startDateTime.toLocalTime();
			insertToTimeMap(startTime, id);
//			int startYear = startDateTime.getYear();
//			insertToYearMap(startYear, id);
			int startMonth = startDateTime.getMonthOfYear();
			insertToMonthMap(startMonth, id);
			int startDay = startDateTime.getDayOfWeek();
			insertToDayMap(startDay, id);
		}
		if (endDateTime != null) {
			LocalDate endDate = endDateTime.toLocalDate();
			insertToDateMap(endDate, id);
			LocalTime endTime = endDateTime.toLocalTime();
			insertToTimeMap(endTime, id);
//			int endYear = endDateTime.getYear();
//			insertToYearMap(endYear, id);
			int endMonth = endDateTime.getMonthOfYear();
			insertToMonthMap(endMonth, id);
			int endDay = endDateTime.getDayOfWeek();
			insertToDayMap(endDay, id);
		}
	}

	private void insertToDayMap(int day, int id) {
		if (dayMap.containsKey(day)) {
			dayMap.get(day).add(id);
		} else {
			ArrayList<Integer> newIdList = new ArrayList<Integer>();
			newIdList.add(id);
			dayMap.put(day, newIdList);
		}
	}

	private void insertToMonthMap(int month, int id) {
		if (monthMap.containsKey(month)) {
			monthMap.get(month).add(id);
		} else {
			ArrayList<Integer> newIdList = new ArrayList<Integer>();
			newIdList.add(id);
			monthMap.put(month, newIdList);
		}
	}

//	/**
//	 * Inserts year property of Todo into timeMap along with the Todo's id
//	 * 
//	 * @param time
//	 * @param id
//	 */
//	private void insertToYearMap(int year, int id) {
//		if (yearMap.containsKey(year)) {
//			yearMap.get(year).add(id);
//		} else {
//			ArrayList<Integer> newIdList = new ArrayList<Integer>();
//			newIdList.add(id);
//			yearMap.put(year, newIdList);
//		}
//	}

	/**
	 * Inserts time property of Todo into timeMap along with the Todo's id
	 * 
	 * @param time
	 * @param id
	 */
	private void insertToTimeMap(LocalTime time, int id) {
		if (timeMap.containsKey(time)) {
			timeMap.get(time).add(id);
		} else {
			ArrayList<Integer> newIdList = new ArrayList<Integer>();
			newIdList.add(id);
			timeMap.put(time, newIdList);
		}

	}

	/**
	 * Inserts date property of Todo into dateMap along with the Todo's id
	 * 
	 * @param date
	 * @param id
	 */
	private void insertToDateMap(LocalDate date, int id) {
		if (dateMap.containsKey(date)) {
			dateMap.get(date).add(id);
		} else {
			ArrayList<Integer> newIdList = new ArrayList<Integer>();
			newIdList.add(id);
			dateMap.put(date, newIdList);
		}

	}

	/**
	 * Inserts each word in Todo name string into nameMap along with the Todo's
	 * id
	 * 
	 * @param name
	 * @param id
	 */
	private void insertToNameMap(String name, int id) {
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
	 * Retrieves the Todo identified by the specified ID from the memory.
	 * 
	 * @param id the ID of the Todo to be retrieved.
	 * @return the Todo object identified by the specified ID.
	 * @throws NullTodoException if the Todo identified by the specified ID does
	 *             not exist.
	 */
	public Todo get(int id) throws NullTodoException {
		Todo returnTodo = memoryMap.get(id);
		if (returnTodo == null) {
			throw new NullTodoException(ExceptionMessages.NULL_TODO_EXCEPTION);
		}
		return returnTodo;
	}

	/**
	 * Retrieves the Todo identified by the specified ID from the memory for
	 * editing. The current state is saved prior to any operation.
	 * 
	 * @param id the ID of the Todo to be retrieved.
	 * @return the Todo object identified by the specified ID.
	 * @throws NullTodoException if the Todo identified by the specified ID does
	 *             not exist.
	 */
	public Todo setterGet(int id) throws NullTodoException {
		Todo returnTodo = memoryMap.get(id);
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
	 * @param id the ID of the Todo to be removed.
	 * @throws NullTodoException if the Todo identified by the specified ID does
	 *             not exist.
	 */
	public Todo remove(int id) throws NullTodoException {
		Todo returnTodo = memoryMap.get(id);
		if (returnTodo == null) {
			throw new NullTodoException(ExceptionMessages.NULL_TODO_EXCEPTION);
		}
		save(returnTodo);
		flushRedoStack();
		memoryMap.remove(id);
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
	 * @param toBeSaved the Todo to be saved.
	 */
	private void save(Todo toBeSaved) {
		// If undo stack has exceeded max size, discard earliest state.
		Todo toBeSavedCopy = new Todo(toBeSaved);
		if (undoStack.size() > STACK_MAX_SIZE) {
			int id = undoStack.removeFirst().getId();
			if (!memoryMap.containsKey(id)) {
				releaseId(id);
				;
			}
		}
		undoStack.add(toBeSavedCopy);
	}

	/**
	 * Flushes the redoStack of all states of Todos.
	 */
	private void flushRedoStack() {
		while (!redoStack.isEmpty()) {
			int id = redoStack.pollLast().getId();
			if (!memoryMap.containsKey(id)) {
				releaseId(id);
				;
			}
		}
	}

	/**
	 * Restores the latest history state of the memory. Also known as the undo
	 * operation.
	 * 
	 * @throws StateUndefinedException if there are no history states to restore
	 *             to.
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
		Todo inMemory = memoryMap.get(id);

		// If Todo does not exist in memory, use placeholder.
		if (inMemory == null) {
			inMemory = fromStack.getPlaceholder();
		}

		// Redo stack will not exceed maximum size.
		redoStack.add(inMemory);

		// If Todo from stack is a placeholder, delete Todo indicated by its
		// ID in the memory.
		if (fromStack.getCreatedOn() == null) {
			memoryMap.remove(id);
		} else {
			memoryMap.put(id, fromStack);
		}
	}

	/**
	 * Restores the latest future state of the memory. Also known as the redo
	 * operation.
	 * 
	 * @throws StateUndefinedException if there are no future states to restore
	 *             to.
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
		Todo inMemory = memoryMap.get(id);

		// If Todo does not exist in memory, use placeholder.
		if (inMemory == null) {
			inMemory = fromStack.getPlaceholder();
		}

		save(inMemory);

		// If Todo from stack is a placeholder, delete Todo indicated by its
		// ID in the memory.
		if (fromStack.getCreatedOn() == null) {
			memoryMap.remove(id);
		} else {
			memoryMap.put(id, fromStack);
		}
	}

	/**
	 * Method to get all the Todos for displaying purposes.
	 * 
	 * @return all Todos as Collection
	 */
	public Collection<Todo> getAllTodos() {
		return memoryMap.values();
	}

	/**
	 * Obtains an ID number from the pool of available ID numbers.
	 * 
	 * @return the ID obtained.
	 */
	public int obtainFreshId() {
		return idBuffer.get();
	}

	/**
	 * Releases the specified ID number to the pool of available ID numbers for
	 * future use by new Todos.
	 * 
	 * @param id the ID to be released.
	 */
	public void releaseId(int id) {
		idBuffer.put(id);
	}

	/**
	 * Serves as a buffer of fixed size for new Todos to draw their ID from.
	 * 
	 * @author Ikarus
	 *
	 */
	private class IDBuffer {
		private TreeSet<Integer> buffer;

		private IDBuffer() {
			buffer = new TreeSet<Integer>();
			for (int i = startingId; i < startingId + ID_BUFFER_INITIAL_SIZE; i++) {
				buffer.add(i);
			}
		}

		private int get() {
			if (buffer.size() == 1) {
				loadToSize();
			}
			return buffer.pollFirst();
		}

		private void put(int id) {
			buffer.add(id);
			if (buffer.size() > ID_BUFFER_MAX_SIZE) {
				unloadToSize();
			}
		}

		private void loadToSize() { //TODO: Bug: Remove 5 elements or so from a
									// memory of size 10. Will load duplicate ID
									// unnecessarily
			int largestId = buffer.last();
			for (int i = largestId; i < largestId + ID_BUFFER_INITIAL_SIZE; i++) {
				buffer.add(i);
			}
		}

		private void unloadToSize() {
			for (int i = 0; i < ID_BUFFER_INITIAL_SIZE; i++) {
				buffer.pollLast();
			}
		}
	}

	/**
	 * Gets the arrayList of Todo ids from the nameMap based on searchKey given
	 * 
	 * @param searchKey
	 * @return ArrayList of Todo ids if searchkey is in nameMap, empty ArrayList
	 *         otherwise
	 */
	public ArrayList<Integer> searchName(String searchKey) {
		if (nameMap.containsKey(searchKey)) {
			return nameMap.get(searchKey);
		}
		return new ArrayList<Integer>();
	}
	
	/**
	 * Gets the arrayList of Todo ids from the dateMap based on searchDate given
	 * 
	 * @param searchDate
	 * @return ArrayList of Todo ids if searchDate is in dateMap, empty ArrayList
	 *         otherwise
	 */
	public ArrayList<Integer> searchDate(LocalDate searchDate) {
		if (dateMap.containsKey(searchDate)) {
			return dateMap.get(searchDate);
		}
		return new ArrayList<Integer>();
	}

	/**
	 * Gets the arrayList of Todo ids from the timeMap based on searchTime given
	 * 
	 * @param searchTime
	 * @return ArrayList of Todo ids if searchkey is in timeMap, empty ArrayList
	 *         otherwise
	 */
	public ArrayList<Integer> searchTime(LocalTime searchTime) {
		if (timeMap.containsKey(searchTime)) {
			return timeMap.get(searchTime);
		}
		return new ArrayList<Integer>();
	}

//	public ArrayList<Integer> searchYear(int searchYear) {
//		if (yearMap.containsKey(searchYear)) {
//			return yearMap.get(searchYear);
//		}
//		return new ArrayList<Integer>();
//	}

	public ArrayList<Integer> searchMonth(int searchMonth) {
		if (monthMap.containsKey(searchMonth)) {
			return monthMap.get(searchMonth);
		}
		return new ArrayList<Integer>();
	}

	public ArrayList<Integer> searchDay(int searchDay) {
		if (dayMap.containsKey(searchDay)) {
			return dayMap.get(searchDay);
		}
		return new ArrayList<Integer>();
	}
}
