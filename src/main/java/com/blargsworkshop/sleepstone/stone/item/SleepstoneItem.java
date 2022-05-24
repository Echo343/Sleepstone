package com.blargsworkshop.sleepstone.stone.item;

import java.util.Optional;

import com.blargsworkshop.common.sound.SoundManager;
import com.blargsworkshop.common.text.Chat;
import com.blargsworkshop.common.utility.WorldHelper;
import com.blargsworkshop.sleepstone.Registry;
import com.blargsworkshop.sleepstone.Sleepstone;
import com.blargsworkshop.sleepstone.stone.capability.IStoneCooldown;
import com.blargsworkshop.sleepstone.stone.capability.StoneCooldownProvider;
import com.blargsworkshop.sleepstone.stone.effect.WarpSicknessEffectInstance;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SleepstoneItem extends Item {
	
//    private static final Logger LOG = LogManager.getLogger();
	private static final int CHANNEL_DURATION = 20 * 4;
	
	public SleepstoneItem() {
		super(new Item.Properties().tab(Sleepstone.TAB).stacksTo(1));
	}
		
	@Override
	public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof Player) {
			Player player = (Player) entityLiving;
			int timeUsed = this.getUseDuration(stack) - timeLeft;
			if (WorldHelper.isServer(worldIn)) {
				if (timeUsed >= CHANNEL_DURATION) {
					this.warpPlayerToSpawn((ServerPlayer) player);
					Chat.clearStatusMessage(player);
				}
				else if (timeUsed < CHANNEL_DURATION) {
					SoundManager.playSoundAtEntityWithRange(player, Registry.Sounds.FIZZLE, 8D, 0.25F);
				}
			}
			else {
				if (timeUsed < CHANNEL_DURATION) {
					SoundManager.playSoundAtEntity(player, Registry.Sounds.FIZZLE);
				}
			}
		}
	}

	@Override
	public void onUseTick(Level worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
		if (livingEntityIn instanceof Player) {
			Player player = (Player) livingEntityIn;
			if (WorldHelper.isServer(worldIn)) {
				boolean isWarpReady = this.getUseDuration(stack) - count >= CHANNEL_DURATION;
				if (isWarpReady) {
					Chat.showStatusMessage(player, TextColor.fromLegacyFormat(ChatFormatting.GREEN), "warp.is.ready");
				}			
			}
			int i = this.getUseDuration(stack) - count;
			switch (i) {
				case 5:
				case 15:
					playSound(player, worldIn, Registry.Sounds.CHANNEL1);
					break;
				case 25:
				case 33:
					playSound(player, worldIn, Registry.Sounds.CHANNEL15);
					break;
				case 41:
				case 48:
					playSound(player, worldIn, Registry.Sounds.CHANNEL30);
					break;
				case 55:
				case 61:
					playSound(player, worldIn, Registry.Sounds.CHANNEL60);
					break;
				case 67:
				case 72:
				case 77:
					playSound(player, worldIn, Registry.Sounds.CHANNEL120);
					break;
				default:
					if (WorldHelper.isClient(worldIn) && i > 80	&& (i - 2) % 23 == 0) {
						SoundManager.playSoundAtEntity(player, Registry.Sounds.CHANNEL_WAITING, 0.6F);
					}
					break;
			}
		}
	}
	
	private void playSound(Player player, Level world, SoundEvent sound) {
		if (WorldHelper.isClient(world)) {
			SoundManager.playSoundAtEntity(player, sound);
		}
		else {
			SoundManager.playSoundAtEntityWithRange(player, sound, 8D, 0.25F);
		}
	}

	/**
	 * Called to trigger the item's "innate" right click behavior. To handle when
	 * this item is used on a Block, see {@link #onItemUse}.
	 */
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand handIn) {
		ItemStack itemstack = player.getItemInHand(handIn);
		if (player.hasEffect(Registry.Effects.WARP_SICKNESS)) {
			// Warp Sickness
			if (WorldHelper.isClient(world)) {		
				itemstack.getCapability(StoneCooldownProvider.STONE_COOLDOWN_CAPABILITY).ifPresent((cooldown) -> {			
					if (world.getGameTime() - cooldown.getWorldTime() >= IStoneCooldown.NOWARP_COOLDOWN) {
						SoundManager.playSoundAtEntity(player, Registry.Sounds.NOWARP);
						cooldown.setWorldTime(world.getGameTime());
					}
				});
				Chat.showStatusMessage(player, TextColor.fromLegacyFormat(ChatFormatting.DARK_PURPLE), "warp.fail.suffering_effects_of_warp_sickness");
			}
			return InteractionResultHolder.fail(itemstack);
		}
		else {
			// Start Channeling
			player.startUsingItem(handIn);
			return InteractionResultHolder.consume(itemstack);
		}
	}

	/**
	 * returns the action that specifies what animation to play when the items is
	 * being used
	 */
	@Override
	public UseAnim getUseAnimation(ItemStack p_41452_) {
      return UseAnim.BOW;
	}

	/**
	 * How long it takes to use or consume an item
	 */
	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	protected void warpPlayerToSpawn(ServerPlayer player) {
		Level world = player.getCommandSenderWorld();
		if (WorldHelper.isServer(world)) {
			BlockPos blockpos = player.getRespawnPosition();
			ServerLevel serverworld = player.getLevel().getServer().getLevel(player.getRespawnDimension());
			if (serverworld != null) {
				if (blockpos != null) {
					boolean isSpawnForced = false;
					boolean keepEverything = true;
					float spawnAngle = player.getRespawnAngle();
					Optional<Vec3> optional;
					optional = Player.findRespawnPositionAndUseSpawnBlock(serverworld, blockpos, spawnAngle, isSpawnForced, keepEverything);
					if (optional.isPresent()) {
						// Warp
						SoundManager.playSoundAtEntity(player, Registry.Sounds.WARP_OUT);
						player.teleportTo(serverworld, optional.get().x(), optional.get().y(), optional.get().z(), player.yRotO, player.xRotO);
						
						// Resync some things that Vanilla is missing:
						if (world != serverworld) {					
					        for (MobEffectInstance effectinstance : player.getActiveEffects()) {
					            player.connection.send(new ClientboundUpdateMobEffectPacket(player.getId(), effectinstance));
					        }
					        player.setExperienceLevels(player.experienceLevel);
						}
				        
				        // TODO - stop all sounds for player
				        player.addEffect(new WarpSicknessEffectInstance());
				        SoundManager.playSoundAtEntityFromServer(player, Registry.Sounds.WARP_IN);
					} else {
						// Don't warp
						// TODO - use a different sound here.
						Chat.addChatMessage(player, "warp.fail.spawn_block_has_been_blocked_or_destroyed");
						SoundManager.playSoundAtEntityFromServer(player, Registry.Sounds.FIZZLE);
					}
				}
				else {
					// Don't warp
					// TODO - use a different sound here.
					Chat.addChatMessage(player, "warp.fail.no.spawn.set");
					SoundManager.playSoundAtEntityFromServer(player, Registry.Sounds.FIZZLE);
				}
			}
		}
	}

}
