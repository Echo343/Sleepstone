package com.blargsworkshop.sleepstone.stone.capability;

public interface IStoneCooldown {
	
	public static final int NOWARP_COOLDOWN = 20 * 2;
	
	public void setWorldTime(long time);
	public long getWorldTime();
}
