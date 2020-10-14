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
	private final float direction;

	public OpenSetSpawnGuiPacket(PacketBuffer buf) {
		worldKey = RegistryKey.getOrCreateKey(net.minecraft.util.registry.Registry.WORLD_KEY, buf.readResourceLocation());
		boolean isNotNull = buf.readBoolean();
		if (isNotNull) {
			spawnPos = buf.readBlockPos();			
		}
		else {
			spawnPos = null;
		}
		newWorldKey = RegistryKey.getOrCreateKey(net.minecraft.util.registry.Registry.WORLD_KEY, buf.readResourceLocation());
		newSpawnPos = buf.readBlockPos();
		direction = buf.readFloat();
	}

	public OpenSetSpawnGuiPacket(RegistryKey<World> worldKey, @Nullable BlockPos spawnPos, RegistryKey<World> newWorldKey, BlockPos newSpawnPos, float direction) {
		this.worldKey = worldKey;
		this.spawnPos = spawnPos;
		this.newWorldKey = newWorldKey;
		this.newSpawnPos = newSpawnPos;
		this.direction = direction;
	}

	public void toBytes(PacketBuffer buf) {
		buf.writeResourceLocation(worldKey.getLocation());
		if (spawnPos != null) {
			buf.writeBoolean(true);
			buf.writeBlockPos(spawnPos);
		}
		else {
			buf.writeBoolean(false);
		}
		buf.writeResourceLocation(newWorldKey.getLocation());
		buf.writeBlockPos(newSpawnPos);
		buf.writeFloat(direction);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			SetSpawnScreen.open(worldKey, spawnPos, newWorldKey, newSpawnPos, direction);
		});
		ctx.get().setPacketHandled(true);
	}
}
