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
    public static final int SIGNAL_NO_PREVIOUS_STATE = -4;

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
    	
    	if(!(this.type == other.type) || !(Signal.areParamsEqual(this.params, other.params))){
    		return false;
    	}
    	return true;	
    }
 }
