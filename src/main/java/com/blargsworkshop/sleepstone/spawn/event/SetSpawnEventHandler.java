package com.blargsworkshop.sleepstone.spawn.event;

import com.blargsworkshop.sleepstone.network.Networking;
import com.blargsworkshop.sleepstone.spawn.capability.SetSpawnChoiceProvider;
import com.blargsworkshop.sleepstone.spawn.packet.OpenSetSpawnGuiPacket;

import net.minecraft.block.BedBlock;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class SetSpawnEventHandler {

	@SubscribeEvent
	public void onSpawnPointSet(PlayerSetSpawnEvent event) {
		event.getPlayer().getCapability(SetSpawnChoiceProvider.SET_SPAWN_CHOICE_CAPABILITY).ifPresent((spawnChoice) -> {
			ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
			if (spawnChoice.isSetSpawnChoiceActive() || player.func_241140_K_() == null) {
				spawnChoice.setSpawnChoice(false);
			}
			else {
				if (
					event.isForced() == false &&
					event.getNewSpawn() != null &&
					event.getPlayer().getServer().getWorld(event.getSpawnWorld()).getBlockState(event.getNewSpawn()).getBlock() instanceof BedBlock
				) {
					if (event.getPlayer() instanceof ServerPlayerEntity) {
//						ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
						if (
							(event.getNewSpawn() != null && !event.getNewSpawn().equals(player.func_241140_K_())) ||
							!player.func_241141_L_().equals(event.getSpawnWorld())
						) {
							Networking.INSTANCE.send(
									PacketDistributor.PLAYER.with(() -> player),
									new OpenSetSpawnGuiPacket(
											player.func_241141_L_(),
											player.func_241140_K_(),
											event.getSpawnWorld(),
											event.getNewSpawn()));
							event.setCanceled(true);
						}
					}
				}
			}
		});
	}
}
