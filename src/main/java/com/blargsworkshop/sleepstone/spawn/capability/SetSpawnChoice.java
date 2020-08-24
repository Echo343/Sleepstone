package com.blargsworkshop.sleepstone.spawn.capability;

public class SetSpawnChoice implements ISetSpawnChoice {
	
	private boolean isActive = false;

	@Override
	public boolean isSetSpawnChoiceActive() {
		return isActive;
	}

	@Override
	public void setSpawnChoice(boolean isActive) {
		this.isActive = isActive;
	}

}
