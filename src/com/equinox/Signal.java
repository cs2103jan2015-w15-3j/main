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
	
    public static final String AddSuccessSignalFormat = "%1$s successfully added.";

    public static final String DisplaySuccessSignalFormat = "";

    public static final String invalidCommandFormat = "Error: %1$s command is invalid!"
            + System.lineSeparator()
            + "Supported commands: add, mark, delete, edit, undo, etc...";


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
