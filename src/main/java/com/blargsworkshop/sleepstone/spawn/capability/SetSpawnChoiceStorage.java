package com.blargsworkshop.sleepstone.spawn.capability;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class SetSpawnChoiceStorage implements IStorage<ISetSpawnChoice> {

	@Override
	public INBT writeNBT(Capability<ISetSpawnChoice> capability, ISetSpawnChoice instance, Direction side) {
		return ByteNBT.valueOf(instance.isSetSpawnChoiceActive());
	}

	@Override
	public void readNBT(Capability<ISetSpawnChoice> capability, ISetSpawnChoice instance, Direction side, INBT nbt) {
		boolean isActive = ((ByteNBT) nbt).getByte() == 1 ? true : false;
		instance.setSpawnChoice(isActive);
	}

}
