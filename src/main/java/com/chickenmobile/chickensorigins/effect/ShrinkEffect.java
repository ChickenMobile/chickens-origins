package com.chickenmobile.chickensorigins.effect;

import com.chickenmobile.chickensorigins.core.integration.Origins;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class ShrinkEffect extends StatusEffect {
    public ShrinkEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    /**
     * Is this an origin with native 'shrinking'.
     * Shrinking effects are reversed on a pixie!
     */
    private static boolean hasOriginShrinkPower(LivingEntity entity){
        return entity instanceof PlayerEntity player
                && Origins.getOrigin(player).hasPowerType(Origins.PIXIE_SHRINK_POWER);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (!entity.world.isClient) { // Players with innate shrinking won't be affected by this
            Origins.Shrink(entity, !hasOriginShrinkPower(entity));
        }
        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (!entity.world.isClient) {
            Origins.Shrink(entity, hasOriginShrinkPower(entity));
        }
        super.onRemoved(entity, attributes, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
