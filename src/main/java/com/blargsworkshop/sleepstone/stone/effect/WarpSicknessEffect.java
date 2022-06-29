package com.blargsworkshop.sleepstone.stone.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class WarpSicknessEffect extends MobEffect {

	public WarpSicknessEffect() {
		super(MobEffectCategory.NEUTRAL, 0);
	}

	@Override
	public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return false;
	}

}
