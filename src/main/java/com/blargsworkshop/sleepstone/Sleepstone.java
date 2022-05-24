package com.blargsworkshop.sleepstone;

import org.slf4j.Logger;

import com.blargsworkshop.sleepstone.network.Networking;
import com.blargsworkshop.sleepstone.spawn.capability.ISetSpawnChoice;
import com.blargsworkshop.sleepstone.spawn.capability.SetSpawnChoice;
import com.blargsworkshop.sleepstone.spawn.capability.SetSpawnChoiceStorage;
import com.blargsworkshop.sleepstone.spawn.event.SetSpawnEventHandler;
import com.blargsworkshop.sleepstone.stone.capability.IStoneCooldown;
import com.blargsworkshop.sleepstone.stone.capability.StoneCooldown;
import com.blargsworkshop.sleepstone.stone.capability.StoneCooldownStorage;
import com.mojang.logging.LogUtils;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("sleepstone")
public class Sleepstone {
	public static final boolean DEBUG = false;
	
	 // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    
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
	
	public static final CreativeModeTab TAB = new CreativeModeTab("blargsTab") {
        @Override
		@OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(Blocks.SMITHING_TABLE);
        }
    };
}
