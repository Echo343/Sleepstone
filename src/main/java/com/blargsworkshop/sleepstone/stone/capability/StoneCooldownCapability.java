package com.blargsworkshop.sleepstone.stone.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid="sleepstone")
public class StoneCooldownCapability {
	public static final Capability<IStoneCooldown> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});
	
	@SubscribeEvent
	public static void register(RegisterCapabilitiesEvent event) {
		event.register(IStoneCooldown.class);
	}
	
	private StoneCooldownCapability() {}
}
