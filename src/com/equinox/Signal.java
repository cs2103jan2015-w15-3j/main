package com.equinox;

/**
 * The Signal Class is used as a container for the messages displayed to the
 * user at the end of an operation.
 * 
 * It also contains the formats for different signals.
 * 
 * @author paradite
 *
 */
public class Signal {
	
    /**
     * Add Handler Signals
     */
    // Success messages
    public static final String ADD_SUCCESS_SIGNAL_FORMAT = "%1$s successfully added.";

    // Error messages
    public static final String UNKNOWN_ADD_ERROR = "Error: Unknown add error";
    public static final String INVALID_PARAMS_FOR_ADD_HANDLER = "Error: The number of parameters is invalid."
    		+ System.lineSeparator()
    		+ "\t Supported formats:"
    		+ System.lineSeparator()
    		+ "\t Floating tasks: add <title>"
    		+ System.lineSeparator()
    		+ "\t Deadlines: add <title> by/on/at <date>"
    		+ System.lineSeparator()
    		+ "\t Events: add <title> from <time> to <time> on <date>"
    		+ System.lineSeparator()
    		+ "\t\t add <title> from <date> to <date>";
    
    /**
     * Display Handler Signals
     */
    public static final String DISPLAY_SUCCESS_SIGNAL_FORMAT = "";
    public static final String DISPLAY_EMPTY_SIGNAL_FORMAT = "The list is empty";

    /**
     * MarkHandler Signals
     */
    //Success messages
    public static final String MARK_SUCCESS_SIGNAL_FORMAT = "%1$s successfully added."; 
    
    //Failure messages
    public static final String UNKNOWN_MARK_ERROR = "Error: Unknown mark error.";
    public static final String INVALID_PARAMS_FOR_MARK_HANDLER = "Error: The number of parameters is invalid."
    			+ System.lineSeparator()
    			+ "\t Supported format:"
    			+ System.lineSeparator()
    			+ "\t mark <indexNumber>";
    public static final String NULL_MAP_EXCEPTION = "Error: Memory is null.";
    public static final String NULL_TODO_EXCEPTION = "Error: %1$s";
   
    /**
     * General Signals
     */

    public static final String INVALID_COMMAND_FORMAT = "Error: %1$s command is invalid!"
            + System.lineSeparator()
            + "Supported commands: add, mark, delete, edit, undo, etc...";

    public static final String EMPTY_PARAM_EXCEPTION = "Error: At least one parameter is unspecified and empty.";
    
    public static final String DATE_UNDEFINED_EXCEPTION = "Error: Date is undefined; %1$s";
    
    private String message;

    /**
     * Constructor for Signal
     * 
     * @param signal
     */
    public Signal(String signal) {
        this.message = signal;
    }

    @Override
    public String toString() {
        return message;
    }

    public static boolean areParamsEqual(String[] params1, String[] params2){
    	//check if params1 and params2 are null
    	if(params1 == null && params2 == null){
    		return true;
    	} else if(params1 == null && params2 !=null) {
    		return false;
    	} else if(params1 != null && params2 == null){
    		return false;
    	}
    	
    	//Neither params1 nor params2 are null.
    	//Check for equal length
    	if(params1.length != params2.length){
    		return false;
    	}
    	//Every string in params1 is equal to every corresponding string in params2
    	int index = 0;
    	for(String str : params1){
    		if(!str.equals(params2[index])){
    			return false;
    		}
    		index++;
    	}
    	return true;
    }
    
    @Override
    //for unit testing purposes
    public boolean equals(Object obj){
    	if(this == obj){
    		return true;
    	}
    	if(obj == null){
    		return false;
    	}
    	if(this.getClass() != obj.getClass()){
    		return false;
    	}
    	final Signal other = (Signal) obj;
    	
        if (!(this.message == other.message)) {
    		return false;
    	}
    	return true;	
    }
 }
