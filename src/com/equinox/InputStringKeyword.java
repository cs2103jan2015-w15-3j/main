package com.equinox;
import java.util.HashMap;


public class InputStringKeyword {

    public static final String KEY_ADD = "add";
    public static final String KEY_DELETE = "delete";
    public static final String KEY_ON = "on";
    public static final String KEY_AT = "at";
    public static final String KEY_MARK = "mark";

    public enum KEYWORDS {
        ADD, MARK, DELETE, SEARCH, EDIT, DISPLAY, UNDO, ON, AT;
    }

    public static HashMap<String, KEYWORDS> keywords;

    static {
        keywords = new HashMap<String, InputStringKeyword.KEYWORDS>();
        keywords.put(KEY_ADD, KEYWORDS.ADD);
        keywords.put(KEY_DELETE, KEYWORDS.DELETE);
        keywords.put(KEY_ON, KEYWORDS.ON);
        keywords.put(KEY_AT, KEYWORDS.AT);
        keywords.put(KEY_MARK, KEYWORDS.MARK);
    }

    public InputStringKeyword() {
    }

    public static boolean isKeyword(String s) {
        return keywords.containsKey(s);
    }

    public static KEYWORDS getKeyword(String s) {
        return keywords.get(s);
    }
}
