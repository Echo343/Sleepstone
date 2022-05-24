package com.blargsworkshop.sleepstone.spawn.packet;

import java.util.function.Supplier;

import com.blargsworkshop.sleepstone.spawn.capability.SetSpawnChoiceProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class SetSpawnPacket {

	private final BlockPos pos;
	private final float direction;
	private final ResourceLocation worldResourceLocation;

	public SetSpawnPacket(FriendlyByteBuf buf) {
		pos = buf.readBlockPos();
		direction = buf.readFloat();
		worldResourceLocation = buf.readResourceLocation();
	}

	public SetSpawnPacket(ResourceKey<Level> world, BlockPos pos, float direction) {
		this.pos = pos;
		this.direction = direction;
		this.worldResourceLocation = world.location();
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeFloat(direction);
		buf.writeResourceLocation(worldResourceLocation);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ResourceKey<Level> world = ResourceKey.create(Registry.DIMENSION_REGISTRY, this.worldResourceLocation);
			ctx.get().getSender().getCapability(SetSpawnChoiceProvider.SET_SPAWN_CHOICE_CAPABILITY).ifPresent((spawnChoice) -> {
				spawnChoice.setSpawnChoice(true);
			});
			ctx.get().getSender().setRespawnPosition(world, pos, direction, false, true);
		});
		ctx.get().setPacketHandled(true);
	}
}
