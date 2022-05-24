package com.blargsworkshop.common.utility;

import net.minecraft.world.level.Level;

public class WorldHelper {

	public static boolean isServer(Level worldObj) {
		return !worldObj.isClientSide;
	}
	
	public static boolean isClient(Level worldObj) {
		return !isServer(worldObj);
	}
}
