package com.blargsworkshop.sleepstone.spawn.capability;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.blargsworkshop.sleepstone.Sleepstone;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid="sleepstone")
public class SetSpawnChoiceProvider implements ICapabilityProvider {
	
	private static final ResourceLocation IDENTIFIER = new ResourceLocation(Sleepstone.MOD_ID, "spawn_choice");
	
	private final ISetSpawnChoice backend = new SetSpawnChoice();
	private final LazyOptional<ISetSpawnChoice> optionalData = LazyOptional.of(() -> backend);
	
	void invalidate() {
		optionalData.invalidate();
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		return SetSpawnChoiceCapability.INSTANCE.orEmpty(cap, this.optionalData);
	}
	
	@SubscribeEvent
	public static void onAttachCapabilityToEntity(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof Player) {
			SetSpawnChoiceProvider provider = new SetSpawnChoiceProvider();
			event.addCapability(IDENTIFIER, provider);
		}
	}
}
