package com.equinox;

import java.lang.reflect.Type;

import com.equinox.Memory.IDBuffer;
import com.google.gson.InstanceCreator;

public class IDBufferInstanceCreator implements InstanceCreator<Memory.IDBuffer>{

	@Override
	public IDBuffer createInstance(Type arg0) {
		Memory memoryInstance = new Memory();
		return memoryInstance.new IDBuffer();
	}
}
