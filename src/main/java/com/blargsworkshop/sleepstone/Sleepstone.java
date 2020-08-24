package com.blargsworkshop.sleepstone;

import com.blargsworkshop.sleepstone.network.Networking;
import com.blargsworkshop.sleepstone.spawn.capability.ISetSpawnChoice;
import com.blargsworkshop.sleepstone.spawn.capability.SetSpawnChoice;
import com.blargsworkshop.sleepstone.spawn.capability.SetSpawnChoiceStorage;
import com.blargsworkshop.sleepstone.spawn.event.SetSpawnEventHandler;
import com.blargsworkshop.sleepstone.stone.capability.IStoneCooldown;
import com.blargsworkshop.sleepstone.stone.capability.StoneCooldown;
import com.blargsworkshop.sleepstone.stone.capability.StoneCooldownStorage;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("sleepstone")
public class Sleepstone {
	public static final boolean DEBUG = false;

	// Directly reference a log4j logger.
//    private static final Logger LOG = LogManager.getLogger();
	public static final String MOD_ID = "sleepstone";

	public Sleepstone() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

		Registry.init();		

		MinecraftForge.EVENT_BUS.register(new SetSpawnEventHandler());
	}

	private void setup(final FMLCommonSetupEvent event) {
		Networking.registerMessages();
		CapabilityManager.INSTANCE.register(IStoneCooldown.class, new StoneCooldownStorage(), StoneCooldown::new);
		CapabilityManager.INSTANCE.register(ISetSpawnChoice.class, new SetSpawnChoiceStorage(), SetSpawnChoice::new);
	}



}
