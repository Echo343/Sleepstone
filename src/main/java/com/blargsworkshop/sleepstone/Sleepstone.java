package com.blargsworkshop.sleepstone;

import java.util.Arrays;
import java.util.Optional;

import com.blargsworkshop.sleepstone.network.Networking;
import com.blargsworkshop.sleepstone.spawn.event.SetSpawnEventHandler;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("sleepstone")
public class Sleepstone {
	public static final boolean DEBUG = false;
	
	 // Directly reference a slf4j logger
//    private static final Logger LOGGER = LogUtils.getLogger();
    
	public static final String MOD_ID = "sleepstone";
	public static final CreativeModeTab TAB;
	
	static {
		Optional<CreativeModeTab> op = Arrays.stream(CreativeModeTab.TABS).filter((tab) -> {
			return tab.getRecipeFolderName().equalsIgnoreCase("blargsTab");
		}).findFirst();

		if (op.isPresent()) {
			TAB = op.get();
		} else {
			TAB = new CreativeModeTab("blargsTab") {
				@Override
				@OnlyIn(Dist.CLIENT)
				public ItemStack makeIcon() {
					return new ItemStack(Blocks.SMITHING_TABLE);
				}
			};
		}
	}

	public Sleepstone() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

		Registry.init();		

		MinecraftForge.EVENT_BUS.register(new SetSpawnEventHandler());
	}

	private void setup(final FMLCommonSetupEvent event) {
		Networking.registerMessages();		
	}
	
//	public static final CreativeModeTab TAB = new CreativeModeTab("blargsTab") {
//        @Override
//		@OnlyIn(Dist.CLIENT)
//        public ItemStack makeIcon() {
//            return new ItemStack(Blocks.SMITHING_TABLE);
//        }
//    };
}
