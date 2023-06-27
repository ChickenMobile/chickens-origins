package com.chickenmobile.chickensorigins.core.registry;

import com.chickenmobile.chickensorigins.ChickensOrigins;
import com.chickenmobile.chickensorigins.mixin.BrewingRecipeRegistryMixin;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModPotions {
    public static Potion SHRINK_POTION;

    public static Potion registerPotion(String name) {
        return Registry.register(Registry.POTION, new Identifier(ChickensOrigins.MOD_ID, name),
                new Potion(new StatusEffectInstance(ModEffects.SHRINK, 600, 0)));
    }

    public static void registerPotions() {
        SHRINK_POTION = registerPotion("shrink_potion");

        registerPotionRecipes();
    }

    private static void registerPotionRecipes() {
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(
                Potions.AWKWARD, ModItems.PIXIE_DUST, ModPotions.SHRINK_POTION);
    }
}
