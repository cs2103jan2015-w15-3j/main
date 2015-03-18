package com.equinox;
import java.util.HashMap;
import java.util.Map;


public class InputStringKeyword {
	
	private static final String KEY_ADD = "add";
	private static final String KEY_MARK = "mark";
	private static final String KEY_DELETE = "delete";
	private static final String KEY_SEARCH = "search";
	private static final String KEY_EDIT = "edit";
	private static final String KEY_DISPLAY = "display";
	private static final String KEY_UNDO = "undo";
	private static final String KEY_REDO = "redo";
	private static final String KEY_BY = "by";
	private static final String KEY_FROM = "from";
	private static final String KEY_ON = "on";
	private static final String KEY_AT = "at";
	private static final String KEY_NAME = "name";
	private static final String KEY_NAME_ABV = "-n";
	private static final String KEY_START = "start";
	private static final String KEY_START_ABV = "-s";
	private static final String KEY_END = "end";
	private static final String KEY_END_ABV = "-e";
	private static final String KEY_EXIT = "exit";
	private static final String KEY_DATE_ABV = "-dt";
	private static final String KEY_TIME_ABV = "-t";
	private static final String KEY_DAY_ABV = "-d";
	private static final String KEY_MONTH_ABV = "-m";

    private static Map<String, Keywords> keywords;
    private static Map<String, Keywords> addKeywords;
    private static Map<String, Keywords> commands;

    static {
    	commands = new HashMap<String, Keywords>();
    	commands.put(KEY_ADD, Keywords.ADD);
        commands.put(KEY_MARK, Keywords.MARK);
        commands.put(KEY_DELETE, Keywords.DELETE);
        commands.put(KEY_SEARCH, Keywords.SEARCH);
        commands.put(KEY_EDIT, Keywords.EDIT);
        commands.put(KEY_DISPLAY, Keywords.DISPLAY);
        commands.put(KEY_UNDO, Keywords.UNDO);
        commands.put(KEY_REDO, Keywords.REDO);
        commands.put(KEY_EXIT, Keywords.EXIT);
        
        addKeywords = new HashMap<String, Keywords>();
        addKeywords.put(KEY_BY, Keywords.BY);
        addKeywords.put(KEY_FROM, Keywords.FROM);
        addKeywords.put(KEY_ON, Keywords.ON);
        addKeywords.put(KEY_AT, Keywords.AT);
    	
        keywords = new HashMap<String, Keywords>(commands);
        keywords.put(KEY_START, Keywords.START);
        keywords.put(KEY_START_ABV, Keywords.START);
        keywords.put(KEY_END, Keywords.END);
        keywords.put(KEY_END_ABV, Keywords.END);
        keywords.put(KEY_NAME, Keywords.NAME);
        keywords.put(KEY_NAME_ABV, Keywords.NAME);
        keywords.put(KEY_DATE_ABV, Keywords.DATE);
        keywords.put(KEY_TIME_ABV, Keywords.TIME);
        keywords.put(KEY_DAY_ABV, Keywords.DAY);
        keywords.put(KEY_MONTH_ABV, Keywords.MONTH);

    }

    /**
     * Checks if the String encodes a keyword
     * 
     * @param s String to be checked
     * @return the enum constant representing the keyword.
     */
    public static boolean isKeyword(String s) {
        return keywords.containsKey(s);
    }

    /**
     * Retrieves the enum constant that represents the keyword encoded in the String.
     * 
     * @param s String containing the keyword.
     * @return the enum constant representing the command.
     */
    public static Keywords getKeyword(String s) {
        return keywords.get(s);
    }
    
    /**
     * Checks if the String encodes a command.
     * 
     * @param s
     * @return
     */
    public static boolean isCommand(String s) {
    	return commands.containsKey(s);
    }
    
    /**
     * Retrieves the enum constant that represents the command encoded in the String.
     * 
     * @param s String containing the keyword.
     * @return the enum constant representing the command.
     */
    public static Keywords getCommand(String s) {
    	return commands.get(s);
    }
    
    /**
     * Checks if the String encodes an add keyword.
     * 
     * @param s String to be checked.
     * @return the enum constant representing the keyword.
     */
    public static boolean isAddKeyword(String s) {
    	return addKeywords.containsKey(s);
    }
    
    /**
     * Retrieves the enum constant that represents the add keyword encoded in the String.
     * 
     * @param s String containing the keyword
     * @return the enum constant representing the add keyword
     */
    public static Keywords getAddKeyword(String s) {
    	return addKeywords.get(s);
    }
}
