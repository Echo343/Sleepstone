package com.blargsworkshop.sleepstone.stone.capability;

import com.blargsworkshop.sleepstone.Sleepstone;
import com.blargsworkshop.sleepstone.stone.item.SleepstoneItem;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class StoneCooldownProvider implements ICapabilityProvider {

	@CapabilityInject(IStoneCooldown.class)
	public static final Capability<IStoneCooldown> STONE_COOLDOWN_CAPABILITY = null;
	
	private LazyOptional<IStoneCooldown> instance = LazyOptional.of(STONE_COOLDOWN_CAPABILITY::getDefaultInstance);

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return cap == STONE_COOLDOWN_CAPABILITY ? instance.cast() : LazyOptional.empty();
	}

	@SubscribeEvent
	public static void onAttachCapabilitiesToItemStack(AttachCapabilitiesEvent<ItemStack> event) {
		if (event.getObject().getItem() instanceof SleepstoneItem) {
			event.addCapability(new ResourceLocation(Sleepstone.MOD_ID, "stone_cooldown"), new StoneCooldownProvider());
		}
	}
}
