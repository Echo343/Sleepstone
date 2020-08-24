package com.blargsworkshop.sleepstone.stone.capability;

public class StoneCooldown implements IStoneCooldown {
		
	private long worldTime = 0;

	@Override
	public void setWorldTime(long time) {
		this.worldTime = time;
	}

	@Override
	public long getWorldTime() {
		return this.worldTime;
	}

}
