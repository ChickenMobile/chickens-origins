package com.chickenmobile.chickensorigins.core.integration;

import com.chickenmobile.chickensorigins.ChickensOrigins;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.apace100.origins.origin.Origin;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class Origins {
    public static final Identifier PIXIE = new Identifier(ChickensOrigins.MOD_ID, "pixie");
    public static final Identifier PIXIE_PROPERTIES_POWER_ID = new Identifier(ChickensOrigins.MOD_ID, "pixie/pixie_properties");
    public static final PowerType<?> PIXIE_SHRINK_POWER = PowerTypeRegistry.get(PIXIE_PROPERTIES_POWER_ID);

    public static boolean isPixie(PlayerEntity entity) {
        return isOrigin(entity, PIXIE);
    }

    // This is a helper method.
    public static Origin getOrigin(PlayerEntity entity) {
        return Origin.get(entity).values().stream()
                .findFirst().orElse(null);
    }

    public static boolean isOrigin(PlayerEntity entity, Identifier identifier) {
        return Origin.get(entity).values().stream()
                .anyMatch(origin -> origin.getIdentifier().equals(identifier));
    }

    /**
     * Shrinks if not shrunk. Resets to normal size if shrunk.
     */
    public static void ToggleShrink(LivingEntity player) {
        if (!revokePower(player, PIXIE_SHRINK_POWER, PIXIE)) {
            grantPower(player, PIXIE_SHRINK_POWER, PIXIE);
        }
    }

    /**
     * Shrink or Unshrink the player
     * @param shrink to shrink or not to shrink... that is the boolean
     */
    public static void Shrink(LivingEntity player, boolean shrink) {
        if (shrink) {
            grantPower(player, PIXIE_SHRINK_POWER, PIXIE);
        } else {
            revokePower(player, PIXIE_SHRINK_POWER, PIXIE);
        }
    }

    // This is a duplicate of apoli command. This could probably be done better elsewhere.
    // Or perhaps there is already an existing command that doesn't require copy/pasting this private func
    private static boolean grantPower(LivingEntity entity, PowerType<?> power, Identifier source) {
        PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);
        boolean success = component.addPower(power, source);
        if (success) {
            component.sync();
            return true;
        }
        return false;
    }

    // This is a duplicate of apoli command. This could probably be done better elsewhere.
    // Or perhaps there is already an existing command that doesn't require copy/pasting this private func
    private static boolean revokePower(LivingEntity entity, PowerType<?> power, Identifier source) {
        PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);
        if (component.hasPower(power, source)) {
            component.removePower(power, source);
            component.sync();
            return true;
        }
        return false;
    }
}
