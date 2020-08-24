package com.blargsworkshop.sleepstone.spawn.capability;

import com.blargsworkshop.sleepstone.Sleepstone;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SetSpawnChoiceProvider implements ICapabilityProvider {
	
	@CapabilityInject(ISetSpawnChoice.class)
	public static final Capability<ISetSpawnChoice> SET_SPAWN_CHOICE_CAPABILITY = null;
	
	private LazyOptional<ISetSpawnChoice> instance = LazyOptional.of(SET_SPAWN_CHOICE_CAPABILITY::getDefaultInstance);
	
	public void invalidate() {
		instance.invalidate();
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return cap == SET_SPAWN_CHOICE_CAPABILITY ? instance.cast() : LazyOptional.empty();
	}
	
	@SubscribeEvent
	public static void onAttachCapabilityToEntity(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof PlayerEntity) {
			SetSpawnChoiceProvider provider = new SetSpawnChoiceProvider();
			event.addCapability(new ResourceLocation(Sleepstone.MOD_ID, "spawn_choice"), provider);
			event.addListener(provider::invalidate);
		}
	}
}
