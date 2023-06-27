package com.chickenmobile.chickensorigins;

import com.chickenmobile.chickensorigins.client.models.ButterflyWingModel;
import com.chickenmobile.chickensorigins.client.particles.ParticlePixieDust;
import com.chickenmobile.chickensorigins.core.registry.ModParticles;
import com.chickenmobile.chickensorigins.core.registry.ModMessages;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ChickensOriginsClient implements ClientModInitializer {
    public static final EntityModelLayer BUTTERFLY = new EntityModelLayer(new Identifier(ChickensOrigins.MOD_ID, "monarch"), "main");

    @Override
    public void onInitializeClient(){
        // Render Models
        EntityModelLayerRegistry.registerModelLayer(BUTTERFLY, ButterflyWingModel::getTexturedModelData);

        // Particles
        ParticleFactoryRegistry.getInstance().register(ModParticles.PARTICLE_PIXIE_DUST, ParticlePixieDust.Factory::new);

        // Items
        //ColorProviderRegistry.ITEM.register();

        // Networking
        ModMessages.registerS2CPackets();

        // Indicate to the server we want our data packets for the client. Server will then send wing type back
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) ->
                ClientPlayNetworking.send(ModMessages.WINGS_GET_ID, PacketByteBufs.create()));
    }
}
