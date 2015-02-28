package com.equinox;
/**
 * The Signal Class is used for constructing a signal as a feedback mechanism
 * for interactions between different processes
 * 
 * The signals indicates whether an operation is successful or not.
 * 
 * It also contains parameters for displaying purposes, which are to be parsed
 * and printed to the screen using {@link DisplayHandler}.
 * 
 * @author paradite
 *
 */
public class Signal {
	
    public static final int SIGNAL_ERROR = -1;
    public static final int SIGNAL_SUCCESS = 1;
    public static final int SIGNAL_EMPTY_TODO = 2;
    public static final int SIGNAL_NOT_FOUND = 404;
    public static final int SIGNAL_INVALID_COMMAND = -2;
    public static final int SIGNAL_INVALID_PARAMS = -3;

    private int type;
    private String[] params;

    public Signal(int type, String[] params) {
        this.type = type;
        this.params = params;
    }

    public Signal(int type) {
        this.type = type;
        this.params = null;
    }

    public int getType() {
        return this.type;
    }

    public String[] getParams() {
        return this.params;
    }

}
