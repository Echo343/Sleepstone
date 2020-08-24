package com.blargsworkshop.sleepstone.stone.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class StoneCooldownStorage implements IStorage<IStoneCooldown> {

	@Override
	public INBT writeNBT(Capability<IStoneCooldown> capability, IStoneCooldown instance, Direction side) {
		return LongNBT.valueOf(instance.getWorldTime());
	}

	@Override
	public void readNBT(Capability<IStoneCooldown> capability, IStoneCooldown instance, Direction side, INBT nbt) {
		instance.setWorldTime(((LongNBT) nbt).getLong());
	}

}
