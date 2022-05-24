package com.blargsworkshop.common.sound;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

/**
 * Use the api in this class to pay sounds.
 */
public class SoundManager {

	/**
	 * Plays a sound at the entity's position.
	 * This must be called from both the client and the server.
	 * @param entity
	 * @param sound
	 * @param volume relative to 1.0
	 * @param pitch relative to 1.0
	 */
	public static void playSoundAtEntity(Entity entity, SoundEvent sound, float volume) {
		entity.playSound(sound, volume, 1F);
	}
	
	/**
	 * Plays a sound at the entity's position.
	 * This must be called from both the client and the server.
	 * @param entity
	 * @param sound
	 */
	public static void playSoundAtEntity(Entity entity, SoundEvent sound) {
		playSoundAtEntity(entity, sound, 1f);
	}
	
	/**
	 * Plays a sound at the entity's position.
	 * Use this method when only calling from the server.
	 * @param entity
	 * @param sound
	 */
	public static void playSoundAtEntityFromServer(Entity entity, SoundEvent sound) {
		if (entity instanceof Player) {
			Player player = (Player) entity;
			player.getCommandSenderWorld().playSound(null, new BlockPos(player.getX(), player.getY(), player.getZ()), sound, player.getSoundSource(), 1f, 1f);
		}
		else {
			playSoundAtEntity(entity, sound);
		}
	}
	
	/***
	 * Plays sound at entity to all within range. Must be called from server only.
	 * @param player
	 * @param soundIn
	 * @param range normal is 16D
	 * @param volume
	 */
	public static void playSoundAtEntityWithRange(@Nonnull Player player, SoundEvent soundIn, double range, float volume) {
		net.minecraftforge.event.entity.PlaySoundAtEntityEvent event = net.minecraftforge.event.ForgeEventFactory.onPlaySoundAtEntity(player, soundIn, player.getSoundSource(), volume, 1.0F);
		if (event.isCanceled() || event.getSound() == null) return;
		soundIn = event.getSound();
		SoundSource category = event.getCategory();
		volume = event.getVolume();
		double x = player.getX();
		double y = player.getY();
		double z = player.getZ();
		player.getCommandSenderWorld().getServer().getPlayerList().broadcast(player, x, y, z, range, player.getCommandSenderWorld().dimension(), new ClientboundSoundPacket(soundIn, category, x, y, z, volume, 1.0F));
	}

}
