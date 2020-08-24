package com.blargsworkshop.sleepstone.spawn.packet;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.blargsworkshop.sleepstone.spawn.gui.SetSpawnScreen;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class OpenSetSpawnGuiPacket {

	private final RegistryKey<World> worldKey;
	private final @Nullable BlockPos spawnPos;
	private final RegistryKey<World> newWorldKey;
	private final BlockPos newSpawnPos;

	public OpenSetSpawnGuiPacket(PacketBuffer buf) {
		worldKey = RegistryKey.func_240903_a_(net.minecraft.util.registry.Registry.WORLD_KEY, buf.readResourceLocation());
		boolean isNotNull = buf.readBoolean();
		if (isNotNull) {
			spawnPos = buf.readBlockPos();			
		}
		else {
			spawnPos = null;
		}
		newWorldKey = RegistryKey.func_240903_a_(net.minecraft.util.registry.Registry.WORLD_KEY, buf.readResourceLocation());
		newSpawnPos = buf.readBlockPos();
	}

	public OpenSetSpawnGuiPacket(RegistryKey<World> worldKey, @Nullable BlockPos spawnPos, RegistryKey<World> newWorldKey, BlockPos newSpawnPos) {
		this.worldKey = worldKey;
		this.spawnPos = spawnPos;
		this.newWorldKey = newWorldKey;
		this.newSpawnPos = newSpawnPos;
	}

	public void toBytes(PacketBuffer buf) {
		buf.writeResourceLocation(worldKey.func_240901_a_());
		if (spawnPos != null) {
			buf.writeBoolean(true);
			buf.writeBlockPos(spawnPos);
		}
		else {
			buf.writeBoolean(false);
		}
		buf.writeResourceLocation(newWorldKey.func_240901_a_());
		buf.writeBlockPos(newSpawnPos);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			SetSpawnScreen.open(worldKey, spawnPos, newWorldKey, newSpawnPos);
		});
		ctx.get().setPacketHandled(true);
	}
}
