package com.equinox;

import java.lang.reflect.Type;

import com.equinox.Memory.IDBuffer;
import com.google.gson.InstanceCreator;

/**
 * Utility class that creates an instance of IDBuffer for Gson to
 * serialise and deserialise the nested IDBuffer class in Memory 
 * 
 * @author Jonathan Lim Siu Chi || ign3sc3nc3
 *
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
