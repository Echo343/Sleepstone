package com.blargsworkshop.sleepstone.stone.effect;

import com.blargsworkshop.sleepstone.Registry;
import com.blargsworkshop.sleepstone.Sleepstone;

import net.minecraft.potion.EffectInstance;

public class WarpSicknessEffectInstance extends EffectInstance {
	
	private static final int DURATION = (Sleepstone.DEBUG) ? 20 * 10 : 20 * 60 * 12; // 10s / 12m
	
	public WarpSicknessEffectInstance() {
		super(Registry.Effects.WARP_SICKNESS, DURATION, 0, false, false, true);
		this.getCurativeItems().clear();
	}
}
