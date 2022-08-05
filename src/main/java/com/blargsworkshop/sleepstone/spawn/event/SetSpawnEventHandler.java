package com.blargsworkshop.sleepstone.spawn.event;

import com.blargsworkshop.sleepstone.network.Networking;
import com.blargsworkshop.sleepstone.spawn.capability.SetSpawnChoiceCapability;
import com.blargsworkshop.sleepstone.spawn.packet.OpenSetSpawnGuiPacket;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.BedBlock;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

public class SetSpawnEventHandler {

	@SubscribeEvent
	public void onSpawnPointSet(PlayerSetSpawnEvent event) {
		event.getEntity().getCapability(SetSpawnChoiceCapability.INSTANCE).ifPresent((spawnChoice) -> {
			ServerPlayer player = (ServerPlayer) event.getEntity();
			if (spawnChoice.isSetSpawnChoiceActive() || player.getRespawnPosition() == null) {
				spawnChoice.setSpawnChoice(false);
			}
			else {
				if (
					!event.isForced() &&
					event.getNewSpawn() != null &&
					event.getEntity().getServer().getLevel(event.getSpawnLevel()).getBlockState(event.getNewSpawn()).getBlock() instanceof BedBlock
				) {
					if (event.getEntity() instanceof ServerPlayer) {
						if (
							(event.getNewSpawn() != null && !event.getNewSpawn().equals(player.getRespawnPosition())) ||
							!player.getRespawnDimension().equals(event.getSpawnLevel())
						) {
							Networking.INSTANCE.send(
									PacketDistributor.PLAYER.with(() -> player),
									new OpenSetSpawnGuiPacket(
											player.getRespawnDimension(),
											player.getRespawnPosition(),
											event.getSpawnLevel(),
											event.getNewSpawn(),
											player.getRespawnAngle()));
							event.setCanceled(true);
						}
					}
				}
			}
		});
	}
}
