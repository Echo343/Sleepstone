package com.blargsworkshop.sleepstone;

import com.blargsworkshop.sleepstone.stone.effect.WarpSicknessEffect;
import com.blargsworkshop.sleepstone.stone.item.SleepstoneItem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Registry {
	static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Sleepstone.MOD_ID);
	static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Sleepstone.MOD_ID);
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

	// TODO - I'm just making new RegistryObjects down below which is unnecessary since the above register commands return a RegistryObject.

	// Items
	public static class Items {
		public static final RegistryObject<Item> STONE = RegistryObject.create(new ResourceLocation(Sleepstone.MOD_ID, "stone"), ForgeRegistries.ITEMS);
	}

	// Effects
	public static class Effects {
		public static final RegistryObject<MobEffect> WARP_SICKNESS = RegistryObject.create(new ResourceLocation(Sleepstone.MOD_ID, "warp_sickness"), ForgeRegistries.MOB_EFFECTS);
	}

	// Sounds
	public static class Sounds {
		public static final RegistryObject<SoundEvent> WARP_IN = RegistryObject.create(new ResourceLocation(Sleepstone.MOD_ID, "warp_in"), ForgeRegistries.SOUND_EVENTS);
		public static final RegistryObject<SoundEvent> WARP_OUT = RegistryObject.create(new ResourceLocation(Sleepstone.MOD_ID, "warp_out"), ForgeRegistries.SOUND_EVENTS);
		public static final RegistryObject<SoundEvent> CHANNEL1 = RegistryObject.create(new ResourceLocation(Sleepstone.MOD_ID, "channel1"), ForgeRegistries.SOUND_EVENTS);
		public static final RegistryObject<SoundEvent> CHANNEL15 = RegistryObject.create(new ResourceLocation(Sleepstone.MOD_ID, "channel15"), ForgeRegistries.SOUND_EVENTS);
		public static final RegistryObject<SoundEvent> CHANNEL30 = RegistryObject.create(new ResourceLocation(Sleepstone.MOD_ID, "channel30"), ForgeRegistries.SOUND_EVENTS);
		public static final RegistryObject<SoundEvent> CHANNEL60 = RegistryObject.create(new ResourceLocation(Sleepstone.MOD_ID, "channel60"), ForgeRegistries.SOUND_EVENTS);
		public static final RegistryObject<SoundEvent> CHANNEL120 = RegistryObject.create(new ResourceLocation(Sleepstone.MOD_ID, "channel120"), ForgeRegistries.SOUND_EVENTS);
		public static final RegistryObject<SoundEvent> FIZZLE = RegistryObject.create(new ResourceLocation(Sleepstone.MOD_ID, "fizzle"), ForgeRegistries.SOUND_EVENTS);
		public static final RegistryObject<SoundEvent> CHANNEL_WAITING = RegistryObject.create(new ResourceLocation(Sleepstone.MOD_ID, "channel_waiting"), ForgeRegistries.SOUND_EVENTS);
		public static final RegistryObject<SoundEvent> NOWARP = RegistryObject.create(new ResourceLocation(Sleepstone.MOD_ID, "nowarp"), ForgeRegistries.SOUND_EVENTS);
	}
}
