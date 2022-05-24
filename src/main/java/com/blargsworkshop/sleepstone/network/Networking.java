package com.blargsworkshop.sleepstone.network;

import java.util.Optional;

import com.blargsworkshop.sleepstone.Sleepstone;
import com.blargsworkshop.sleepstone.spawn.packet.OpenSetSpawnGuiPacket;
import com.blargsworkshop.sleepstone.spawn.packet.SetSpawnPacket;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class Networking {
	public static SimpleChannel INSTANCE;
	private static int ID = 0;

	public static int nextId() {
		return ID++;
	}

	public static void registerMessages() {
		INSTANCE = NetworkRegistry
				.newSimpleChannel(new ResourceLocation(Sleepstone.MOD_ID, "network"), () -> "1.0", (s) -> true, (s) -> true);

		INSTANCE.registerMessage(
				nextId(),
				SetSpawnPacket.class,
				SetSpawnPacket::toBytes,
				SetSpawnPacket::new,
				SetSpawnPacket::handle,
				Optional.of(NetworkDirection.PLAY_TO_SERVER));

		INSTANCE.registerMessage(
				nextId(),
				OpenSetSpawnGuiPacket.class,
				OpenSetSpawnGuiPacket::toBytes,
				OpenSetSpawnGuiPacket::new,
				OpenSetSpawnGuiPacket::handle,
				Optional.of(NetworkDirection.PLAY_TO_CLIENT));
	}
}
