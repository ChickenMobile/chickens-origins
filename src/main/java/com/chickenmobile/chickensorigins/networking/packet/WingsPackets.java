package com.chickenmobile.chickensorigins.networking.packet;

import com.chickenmobile.chickensorigins.util.WingsData;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class WingsPackets {
    // C2S ------------------------------------

    /** Basic nbt save of wings through networking packet */
    public static void C2S_syncWings(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                                     PacketByteBuf buf, PacketSender responseSender) {
        WingsData.createFromNbt(buf.readNbt()); // Set WING_MAP
    }

    /** This returns all wing types for each player known on the server for saving to WingData - for rendering purposes on client */
    public static void C2S_getWings(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                                    PacketByteBuf buf, PacketSender responseSender) {
        NbtCompound allWingsNbt = WingsData.WINGS_DATA.writeNbtOnlineOnly(player, false);
        NbtCompound singleWingsNbt = WingsData.WINGS_DATA.writeNbtOnlineOnly(player, true);

        // This player has just joined the server, therefore update their wing type to everyone else!
        // This only sends a single record to other players, but gives all current online player's info to sender (helps keeps packet smaller).
        for (var serverPlayer : server.getPlayerManager().getPlayerList()) {
            if (player.equals(serverPlayer)) {
                WingsData.syncWings(allWingsNbt, serverPlayer);
            } else {
                WingsData.syncWings(singleWingsNbt, serverPlayer);
            }
        }
    }

    // S2C ------------------------------------

    public static void S2C_syncWings(MinecraftClient client, ClientPlayNetworkHandler handler,
                                     PacketByteBuf buf, PacketSender responseSender) {
        WingsData.createFromNbt(buf.readNbt());
    }
}
