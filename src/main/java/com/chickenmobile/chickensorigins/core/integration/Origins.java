package com.chickenmobile.chickensorigins.core.integration;

import com.chickenmobile.chickensorigins.ChickensOrigins;
import io.github.apace100.origins.origin.Origin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class Origins {
    private static final Identifier pixie_origin_identifier = new Identifier(ChickensOrigins.MOD_ID, "pixie");

    public static boolean isPixie(PlayerEntity entity) {
        return isOrigin(entity, pixie_origin_identifier);
    }

    public static boolean isOrigin(PlayerEntity entity, Identifier identifier) {
        for(Origin myOrigin : Origin.get(entity).values()){
          return myOrigin.getIdentifier().equals(identifier);
        }
        return false;
    }
}
