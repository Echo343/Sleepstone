package com.blargsworkshop.common.sound;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

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
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;
			player.getEntityWorld().playSound(null, new BlockPos(player.getPosX(), player.getPosY(), player.getPosZ()), sound, player.getSoundCategory(), 1f, 1f);
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
	public static void playSoundAtEntityWithRange(@Nonnull PlayerEntity player, SoundEvent soundIn, double range, float volume) {
		net.minecraftforge.event.entity.PlaySoundAtEntityEvent event = net.minecraftforge.event.ForgeEventFactory.onPlaySoundAtEntity(player, soundIn, player.getSoundCategory(), volume, 1.0F);
		if (event.isCanceled() || event.getSound() == null) return;
		soundIn = event.getSound();
		SoundCategory category = event.getCategory();
		volume = event.getVolume();
		double x = player.getPosX();
		double y = player.getPosY();
		double z = player.getPosZ();
		player.getEntityWorld().getServer().getPlayerList().sendToAllNearExcept(player, x, y, z, range, player.getEntityWorld().getDimensionKey(), new SPlaySoundEffectPacket(soundIn, category, x, y, z, volume, 1.0F));
	}

}
