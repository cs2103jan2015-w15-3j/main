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
	private static final String KEY_NAME_ABV = "-n"; // TODO: Propose using suffix _ABV to denote abbreviated forms.
	private static final String KEY_START = "start";
	private static final String KEY_START_ABV = "-s";
	private static final String KEY_END = "end";
	private static final String KEY_END_ABV = "-e";
	private static final String KEY_EXIT = "exit";
	private static final String KEY_DATE = "-dt";
	private static final String KEY_TIME = "-t";
	private static final String KEY_DAY = "-d";
	private static final String KEY_MONTH = "-m";
	private static final String KEY_YEAR = "-y";

    private static Map<String, KEYWORDS> keywords;
    private static Map<String, KEYWORDS> dateKeywords;
    private static Map<String, KEYWORDS> commands;

    static {
    	commands = new HashMap<String, KEYWORDS>();
    	commands.put(KEY_ADD, KEYWORDS.ADD);
        commands.put(KEY_MARK, KEYWORDS.MARK);
        commands.put(KEY_DELETE, KEYWORDS.DELETE);
        commands.put(KEY_SEARCH, KEYWORDS.SEARCH);
        commands.put(KEY_EDIT, KEYWORDS.EDIT);
        commands.put(KEY_DISPLAY, KEYWORDS.DISPLAY);
        commands.put(KEY_UNDO, KEYWORDS.UNDO);
        commands.put(KEY_REDO, KEYWORDS.REDO);
        commands.put(KEY_EXIT, KEYWORDS.EXIT);
        
        dateKeywords = new HashMap<String, KEYWORDS>();
        dateKeywords.put(KEY_BY, KEYWORDS.BY);
        dateKeywords.put(KEY_FROM, KEYWORDS.FROM);
        dateKeywords.put(KEY_ON, KEYWORDS.ON);
        dateKeywords.put(KEY_AT, KEYWORDS.AT);
    	
        keywords = new HashMap<String, KEYWORDS>(commands);
        keywords.put(KEY_START, KEYWORDS.START);
        keywords.put(KEY_START_ABV, KEYWORDS.START);
        keywords.put(KEY_END, KEYWORDS.END);
        keywords.put(KEY_END_ABV, KEYWORDS.END);
        keywords.put(KEY_NAME, KEYWORDS.NAME);
        keywords.put(KEY_NAME_ABV, KEYWORDS.NAME);
        keywords.put(KEY_DATE, KEYWORDS.DATE);
        keywords.put(KEY_TIME, KEYWORDS.TIME);
        keywords.put(KEY_DAY, KEYWORDS.DAY);
        keywords.put(KEY_MONTH, KEYWORDS.MONTH);
        keywords.put(KEY_YEAR, KEYWORDS.YEAR);
    }

    public static boolean isKeyword(String s) {
        return keywords.containsKey(s);
    }

    public static KEYWORDS getKeyword(String s) {
        return keywords.get(s);
    }
    
    public static boolean isCommand(String s) {
    	return commands.containsKey(s);
    }
    
    public static KEYWORDS getCommand(String s) {
    	return commands.get(s);
    }
    
    public static boolean isDateKeyword(String s) {
    	return dateKeywords.containsKey(s);
    }
    
    public static KEYWORDS getDateKeyword(String s) {
    	return dateKeywords.get(s);
    }
}
