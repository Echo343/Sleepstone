package com.blargsworkshop.sleepstone.stone.capability;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.blargsworkshop.sleepstone.Sleepstone;
import com.blargsworkshop.sleepstone.stone.item.SleepstoneItem;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid="sleepstone")
public class StoneCooldownProvider implements ICapabilityProvider {

	private static final ResourceLocation IDENTIFIER = new ResourceLocation(Sleepstone.MOD_ID, "stone_cooldown");

	private final IStoneCooldown backend = new StoneCooldown();
	private final LazyOptional<IStoneCooldown> optionalData = LazyOptional.of(() -> backend);

	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		return StoneCooldownCapability.INSTANCE.orEmpty(cap, this.optionalData);
	}

	void invalidate() {
		this.optionalData.invalidate();
	}

	@SubscribeEvent
	public static void onAttachCapabilitiesToItemStack(AttachCapabilitiesEvent<ItemStack> event) {
		if (event.getObject().getItem() instanceof SleepstoneItem) {
			final StoneCooldownProvider provider = new StoneCooldownProvider();
			event.addCapability(StoneCooldownProvider.IDENTIFIER, provider);
			event.addListener(provider::invalidate);
		}
	}
}
