package com.blargsworkshop.sleepstone.spawn.packet;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.blargsworkshop.sleepstone.spawn.gui.SetSpawnScreen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class OpenSetSpawnGuiPacket {

	private final ResourceKey<Level> worldKey;
	private final @Nullable BlockPos spawnPos;
	private final ResourceKey<Level> newWorldKey;
	private final BlockPos newSpawnPos;
	private final float direction;

	public OpenSetSpawnGuiPacket(FriendlyByteBuf buf) {
		worldKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, buf.readResourceLocation());
		boolean isNotNull = buf.readBoolean();
		if (isNotNull) {
			spawnPos = buf.readBlockPos();
		}
		else {
			spawnPos = null;
		}
		newWorldKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, buf.readResourceLocation());
		newSpawnPos = buf.readBlockPos();
		direction = buf.readFloat();
	}

	public OpenSetSpawnGuiPacket(ResourceKey<Level> worldKey, @Nullable BlockPos spawnPos, ResourceKey<Level> newWorldKey, BlockPos newSpawnPos, float direction) {
		this.worldKey = worldKey;
		this.spawnPos = spawnPos;
		this.newWorldKey = newWorldKey;
		this.newSpawnPos = newSpawnPos;
		this.direction = direction;
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeResourceLocation(worldKey.location());
		if (spawnPos != null) {
			buf.writeBoolean(true);
			buf.writeBlockPos(spawnPos);
		}
		else {
			buf.writeBoolean(false);
		}
		buf.writeResourceLocation(newWorldKey.location());
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
