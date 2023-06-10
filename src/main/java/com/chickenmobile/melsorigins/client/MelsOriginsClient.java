package com.chickenmobile.melsorigins.client;

import com.chickenmobile.melsorigins.MelsOrigins;
import com.chickenmobile.melsorigins.client.models.ButterflyWingModel;
import com.chickenmobile.melsorigins.client.particles.ParticlePixieDust;
import com.chickenmobile.melsorigins.core.registry.ModParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MelsOriginsClient implements ClientModInitializer {
    public static final EntityModelLayer BUTTERFLY = new EntityModelLayer(new Identifier(MelsOrigins.MOD_ID, "monarch"), "main");

    @Override
    public void onInitializeClient(){
        // Render Models
        EntityModelLayerRegistry.registerModelLayer(BUTTERFLY, ButterflyWingModel::getTexturedModelData);

        // Particles
        ParticleFactoryRegistry.getInstance().register(ModParticles.PARTICLE_PIXIE_DUST, ParticlePixieDust.Factory::new);

        // Items
        //ColorProviderRegistry.ITEM.register();

    }

}
