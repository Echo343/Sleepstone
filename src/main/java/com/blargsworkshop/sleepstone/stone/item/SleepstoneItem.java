package com.blargsworkshop.sleepstone.stone.item;

import java.util.Optional;

import com.blargsworkshop.common.BlargsWorkshop;
import com.blargsworkshop.common.sound.SoundManager;
import com.blargsworkshop.common.text.Chat;
import com.blargsworkshop.common.utility.WorldHelper;
import com.blargsworkshop.sleepstone.Registry;
import com.blargsworkshop.sleepstone.stone.capability.IStoneCooldown;
import com.blargsworkshop.sleepstone.stone.capability.StoneCooldownProvider;
import com.blargsworkshop.sleepstone.stone.effect.WarpSicknessEffectInstance;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class SleepstoneItem extends Item {
	
//    private static final Logger LOG = LogManager.getLogger();
	private static final int CHANNEL_DURATION = 20 * 4;
	
	public SleepstoneItem() {
		super(new Item.Properties().group(BlargsWorkshop.TAB).maxStackSize(1));
	}
		
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entityLiving;
			int timeUsed = this.getUseDuration(stack) - timeLeft;
			if (WorldHelper.isServer(worldIn)) {
				if (timeUsed >= CHANNEL_DURATION) {
					this.warpPlayerToSpawn((ServerPlayerEntity) player);
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
	public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
		if (livingEntityIn instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) livingEntityIn;
			if (WorldHelper.isServer(worldIn)) {
				boolean isWarpReady = this.getUseDuration(stack) - count >= CHANNEL_DURATION;
				if (isWarpReady) {
					Chat.showStatusMessage(player, TextFormatting.GREEN, "warp.is.ready");
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
	
	private void playSound(PlayerEntity player, World world, SoundEvent sound) {
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
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand handIn) {
		ItemStack itemstack = player.getHeldItem(handIn);
		if (player.isPotionActive(Registry.Effects.WARP_SICKNESS)) {
			// Warp Sickness
			if (WorldHelper.isClient(world)) {		
				itemstack.getCapability(StoneCooldownProvider.STONE_COOLDOWN_CAPABILITY).ifPresent((cooldown) -> {			
					if (world.getGameTime() - cooldown.getWorldTime() >= IStoneCooldown.NOWARP_COOLDOWN) {
						SoundManager.playSoundAtEntity(player, Registry.Sounds.NOWARP);
						cooldown.setWorldTime(world.getGameTime());
					}
				});
				Chat.showStatusMessage(player, TextFormatting.DARK_PURPLE, "warp.fail.suffering_effects_of_warp_sickness");
			}
			return ActionResult.resultFail(itemstack);
		}
		else {
			// Start Channeling
			player.setActiveHand(handIn);
			return ActionResult.resultConsume(itemstack);
		}
	}

	/**
	 * returns the action that specifies what animation to play when the items is
	 * being used
	 */
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	/**
	 * How long it takes to use or consume an item
	 */
	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	protected void warpPlayerToSpawn(ServerPlayerEntity player) {
		World world = player.getEntityWorld();
		if (WorldHelper.isServer(world)) {
			BlockPos blockpos = player.func_241140_K_();
			ServerWorld serverworld = player.getServerWorld().getServer().getWorld(player.func_241141_L_());
			if (serverworld != null) {
				if (blockpos != null) {
					boolean isSpawnForced = false;
					boolean keepEverything = true;
					float spawnAngle = player.func_242109_L();
					Optional<Vector3d> optional;
					optional = PlayerEntity.func_242374_a(serverworld, blockpos, spawnAngle, isSpawnForced, keepEverything);
					if (optional.isPresent()) {
						// Warp
						SoundManager.playSoundAtEntity(player, Registry.Sounds.WARP_OUT);
						player.teleport(serverworld, optional.get().getX(), optional.get().getY(), optional.get().getZ(), player.rotationYaw, player.rotationPitch);
						
						// Resync some things that Vanilla is missing:
						if (world != serverworld) {					
					        for (EffectInstance effectinstance : player.getActivePotionEffects()) {
					            player.connection.sendPacket(new SPlayEntityEffectPacket(player.getEntityId(), effectinstance));
					        }
					        player.setExperienceLevel(player.experienceLevel);
						}
				        
				        // TODO - stop all sounds for player
				        player.addPotionEffect(new WarpSicknessEffectInstance());
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
