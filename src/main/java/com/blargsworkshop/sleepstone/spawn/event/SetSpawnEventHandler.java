package com.blargsworkshop.sleepstone.spawn.event;

import com.blargsworkshop.sleepstone.network.Networking;
import com.blargsworkshop.sleepstone.spawn.capability.SetSpawnChoiceProvider;
import com.blargsworkshop.sleepstone.spawn.packet.OpenSetSpawnGuiPacket;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.BedBlock;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

public class SetSpawnEventHandler {

	@SubscribeEvent
	public void onSpawnPointSet(PlayerSetSpawnEvent event) {
		event.getPlayer().getCapability(SetSpawnChoiceProvider.SET_SPAWN_CHOICE_CAPABILITY).ifPresent((spawnChoice) -> {
			ServerPlayer player = (ServerPlayer) event.getPlayer();
			if (spawnChoice.isSetSpawnChoiceActive() || player.getRespawnPosition() == null) {
				spawnChoice.setSpawnChoice(false);
			}
			else {
				if (
					event.isForced() == false &&
					event.getNewSpawn() != null &&
					event.getPlayer().getServer().getLevel(event.getSpawnWorld()).getBlockState(event.getNewSpawn()).getBlock() instanceof BedBlock
				) {
					if (event.getPlayer() instanceof ServerPlayer) {
//						ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
						if (
							(event.getNewSpawn() != null && !event.getNewSpawn().equals(player.getRespawnPosition())) ||
							!player.getRespawnDimension().equals(event.getSpawnWorld())
						) {
							Networking.INSTANCE.send(
									PacketDistributor.PLAYER.with(() -> player),
									new OpenSetSpawnGuiPacket(
											player.getRespawnDimension(),
											player.getRespawnPosition(),
											event.getSpawnWorld(),
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
