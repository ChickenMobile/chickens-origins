package com.chickenmobile.melsorigins.core.registry;

import com.chickenmobile.melsorigins.MelsOrigins;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModParticles {

    public static final DefaultParticleType PARTICLE_PIXIE_DUST = FabricParticleTypes.simple();

    public static void registerParticles() {
        Registry.register(Registry.PARTICLE_TYPE,
                new Identifier(MelsOrigins.MOD_ID, "particle_pixie_dust"), PARTICLE_PIXIE_DUST);
    }

}
