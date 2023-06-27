package com.chickenmobile.chickensorigins.core.registry;

import com.chickenmobile.chickensorigins.ChickensOrigins;
import com.chickenmobile.chickensorigins.networking.packet.WingsPackets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ModMessages {
    // Wings
    public static final Identifier WINGS_SET_ID = new Identifier(ChickensOrigins.MOD_ID, "wings_set");
    public static final Identifier WINGS_SYNC_ID = new Identifier(ChickensOrigins.MOD_ID, "wings_sync");
    public static final Identifier WINGS_GET_ID = new Identifier(ChickensOrigins.MOD_ID, "wings_get");

    public static void registerC2SPackets(){
        ServerPlayNetworking.registerGlobalReceiver(WINGS_SET_ID, WingsPackets::C2S_syncWings);
        ServerPlayNetworking.registerGlobalReceiver(WINGS_GET_ID, WingsPackets::C2S_getWings);
    }

    public static void registerS2CPackets(){
        ClientPlayNetworking.registerGlobalReceiver(WINGS_SYNC_ID, WingsPackets::S2C_syncWings);
    }
}
