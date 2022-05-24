package com.blargsworkshop.common.utility;

import net.minecraft.world.World;

public class WorldHelper {

	public static boolean isServer(World worldObj) {
		return !worldObj.isRemote;
	}
	
	public static boolean isClient(World worldObj) {
		return !isServer(worldObj);
	}
}
