package com.chickenmobile.melsorigins.core.integration;

import com.chickenmobile.melsorigins.MelsOrigins;
import com.chickenmobile.melsorigins.common.powers.WingsPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Origins {
    public static final PowerFactory<Power> WINGS_FACTORY = new PowerFactory<>(
            new Identifier(MelsOrigins.MOD_ID, "wings"), new SerializableData(),
            data -> (powerType, entity) -> new WingsPower(powerType, entity, data.getId("wings_type"))
    ).allowCondition();

    public static final PowerType<Power> WINGS_POWER = new PowerTypeReference<>(new Identifier(MelsOrigins.MOD_ID, "wings"));

    public static boolean hasWings(Entity entity) {
        return WINGS_POWER.isActive(entity);
    }

    public static final PowerType<Power> PIXIE_POWER = new PowerTypeReference<>(new Identifier(MelsOrigins.MOD_ID, "pixie"));
    private static final Identifier pixie_origin_identifier = new Identifier(MelsOrigins.MOD_ID, "pixie");

    public static boolean isPixie(PlayerEntity entity) {
        return isOrigin(entity, pixie_origin_identifier);
    }

    public static boolean isOrigin(PlayerEntity entity, Identifier identifier) {
        for(Origin myOrigin : Origin.get(entity).values()){
          return myOrigin.getIdentifier().equals(identifier);
        }
        return false;
    }

//    public static Function<Entity, WingsValues> getWingValues() {
//        return entity -> IcarusOrigins.WINGS_POWER.get(entity) instanceof WingsPower wingsPower ? wingsPower : DefaultWingsValues.INSTANCE;
//    }

    public static void register() {
        Registry.register(ApoliRegistries.POWER_FACTORY, WINGS_FACTORY.getSerializerId(), WINGS_FACTORY);
    }
}
