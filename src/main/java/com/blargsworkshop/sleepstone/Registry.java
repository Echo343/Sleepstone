package com.blargsworkshop.sleepstone;

import com.blargsworkshop.sleepstone.stone.effect.WarpSicknessEffect;
import com.blargsworkshop.sleepstone.stone.item.SleepstoneItem;

import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

public class Registry {
	static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Sleepstone.MOD_ID);
	static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Sleepstone.MOD_ID);
	static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Sleepstone.MOD_ID);

	public static void init() {
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
		SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	static {
		ITEMS.register("stone", SleepstoneItem::new);
		EFFECTS.register("warp_sickness", WarpSicknessEffect::new);
		SOUNDS.register("warp_in", () -> new SoundEvent(new ResourceLocation(Sleepstone.MOD_ID, "effect.warp.in")));
		SOUNDS.register("warp_out", () -> new SoundEvent(new ResourceLocation(Sleepstone.MOD_ID, "effect.warp.out")));
		SOUNDS.register("channel1", () -> new SoundEvent(new ResourceLocation(Sleepstone.MOD_ID, "effect.warp.channel.1")));
		SOUNDS.register("channel15", () -> new SoundEvent(new ResourceLocation(Sleepstone.MOD_ID, "effect.warp.channel.15")));
		SOUNDS.register("channel30", () -> new SoundEvent(new ResourceLocation(Sleepstone.MOD_ID, "effect.warp.channel.30")));
		SOUNDS.register("channel60", () -> new SoundEvent(new ResourceLocation(Sleepstone.MOD_ID, "effect.warp.channel.60")));
		SOUNDS.register("channel120", () -> new SoundEvent(new ResourceLocation(Sleepstone.MOD_ID, "effect.warp.channel.120")));
		SOUNDS.register("fizzle", () -> new SoundEvent(new ResourceLocation(Sleepstone.MOD_ID, "effect.warp.fizzle")));
		SOUNDS.register("channel_waiting", () -> new SoundEvent(new ResourceLocation(Sleepstone.MOD_ID, "effect.warp.channel.waiting")));
		SOUNDS.register("nowarp", () -> new SoundEvent(new ResourceLocation(Sleepstone.MOD_ID, "effect.warp.no.warp")));
	}

	// Items
	@ObjectHolder("sleepstone")
	public static class Items {
		public static final Item STONE = null;
	}
	
	// Effects
	@ObjectHolder("sleepstone")
	public static class Effects {
		public static final Effect WARP_SICKNESS = null;
	}
	
	// Sounds
	@ObjectHolder("sleepstone")
	public static class Sounds {
		public static final SoundEvent WARP_IN = null;
		public static final SoundEvent WARP_OUT = null;
		public static final SoundEvent CHANNEL1 = null;
		public static final SoundEvent CHANNEL15 = null;
		public static final SoundEvent CHANNEL30 = null;
		public static final SoundEvent CHANNEL60 = null;
		public static final SoundEvent CHANNEL120 = null;
		public static final SoundEvent FIZZLE = null;
		public static final SoundEvent CHANNEL_WAITING = null;
		public static final SoundEvent NOWARP = null;
	}
}
