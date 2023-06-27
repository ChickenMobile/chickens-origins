package com.chickenmobile.chickensorigins.core.registry;

import com.chickenmobile.chickensorigins.ChickensOrigins;
import com.chickenmobile.chickensorigins.effect.ShrinkEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEffects {
    public static StatusEffect SHRINK;

    public static StatusEffect registerStatusEffect(String name) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(ChickensOrigins.MOD_ID, name),
                new ShrinkEffect(StatusEffectCategory.NEUTRAL, 0xbc5fc5)); // Pink
    }

    public static void registerEffects() {
        SHRINK = registerStatusEffect("shrink");
    }
}
