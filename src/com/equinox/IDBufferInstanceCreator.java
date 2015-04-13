package com.equinox;


import java.lang.reflect.Type;

import com.equinox.Memory.IDBuffer;
import com.google.gson.InstanceCreator;
//@author A0110839-unused

/**
 * This class is unused because the IDBuffer class is now a separate class
 * from Memory, when previously it was a nested inner class, which led to problems
 * in deserialisation.
 * 
 * Utility class that creates an instance of IDBuffer for Gson to
 * serialise and deserialise the nested IDBuffer class in Memory 
 */

public class IDBufferInstanceCreator implements InstanceCreator<Memory.IDBuffer>{

	/**
	 * Creates an instance of IDBuffer
	 * 
	 * @param Type 
	 */
	@Override
	public IDBuffer createInstance(Type arg0) {
		Memory memoryInstance = new Memory();
		return memoryInstance.new IDBuffer();
	}
}
