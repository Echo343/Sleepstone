package com.blargsworkshop.sleepstone.spawn.packet;

import java.util.function.Supplier;

import com.blargsworkshop.sleepstone.spawn.capability.SetSpawnChoiceProvider;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class SetSpawnPacket {

	private final BlockPos pos;
	private final ResourceLocation worldResourceLocation;

	public SetSpawnPacket(PacketBuffer buf) {
		pos = buf.readBlockPos();
		worldResourceLocation = buf.readResourceLocation();
	}

	public SetSpawnPacket(RegistryKey<World> world, BlockPos pos) {
		this.pos = pos;
		this.worldResourceLocation = world.func_240901_a_();
	}

	public void toBytes(PacketBuffer buf) {
		buf.writeBlockPos(pos);
		buf.writeResourceLocation(worldResourceLocation);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			RegistryKey<World> world = RegistryKey.func_240903_a_(net.minecraft.util.registry.Registry.WORLD_KEY, this.worldResourceLocation);
			ctx.get().getSender().getCapability(SetSpawnChoiceProvider.SET_SPAWN_CHOICE_CAPABILITY).ifPresent((spawnChoice) -> {
				spawnChoice.setSpawnChoice(true);
			});
			ctx.get().getSender().func_241153_a_(world, pos, false, true);
		});
		ctx.get().setPacketHandled(true);
	}
}
