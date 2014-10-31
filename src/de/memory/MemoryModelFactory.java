package de.memory;

import aQute.bnd.annotation.component.Component;
import de.memory.api.IMemoryModel;
import de.memory.api.IMemoryModelFactory;

@Component
public class MemoryModelFactory implements IMemoryModelFactory {

	@Override
	public IMemoryModel createModel() {
		// TODO Auto-generated method stub
		return new MemoryModel();
	}

}
