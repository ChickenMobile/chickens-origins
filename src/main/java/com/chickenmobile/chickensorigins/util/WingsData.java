package com.chickenmobile.chickensorigins.util;

import com.chickenmobile.chickensorigins.ChickensOrigins;
import com.chickenmobile.chickensorigins.core.registry.ModMessages;
import com.google.gson.Gson;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;

public class WingsData extends PersistentState {
    public HashMap<String, String> WING_MAP = new HashMap<>();
    public static WingsData WINGS_DATA = new WingsData(); // Set on server start also
    private static final Gson GSON = new Gson();

    /**
     * This is to get server saved state.
     */
    public static WingsData getWingState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
        WingsData wingsData = persistentStateManager.getOrCreate(WingsData::createFromNbt, WingsData::new, ChickensOrigins.MOD_ID);
        return wingsData;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        if (nbt == null) {
            nbt = new NbtCompound();
        }
        // Used Json convert to string here because it was just easier
        // There is a hard string NBT length limit, but that should be 65,535 characters (or 2047 player UUIDs)
        // Not even the biggest servers would hit that limit...? so won't code anything special for now
        nbt.putString("wings", GSON.toJson(WING_MAP));
        return nbt;
    }

    /**
     * Generates NBT data for wings of online players only.
     *
     * @param single Whether to only return the given player's or all online players' wing type
     */
    public NbtCompound writeNbtOnlineOnly(ServerPlayerEntity player, boolean single) {
        NbtCompound nbt = new NbtCompound();
        HashMap<String, String> newWingMap = new HashMap<>();
        if (single) {
            // Gather wing type for this player (the sender) only.
            String playerUuid = player.getUuidAsString();
            newWingMap.put(playerUuid, WING_MAP.getOrDefault(playerUuid, "none"));
        } else {
            // Gather wing type for all online players.
            for (ServerPlayerEntity worldPlayer : player.getServer().getPlayerManager().getPlayerList()) {
                String playerUuid = worldPlayer.getUuidAsString();
                newWingMap.put(playerUuid, WING_MAP.getOrDefault(playerUuid, "none"));
            }
        }
        nbt.putString("wings", GSON.toJson(newWingMap));
        return nbt;
    }

    public static WingsData createFromNbt(NbtCompound nbt) {
        HashMap<String, String> newWingMap = GSON.fromJson(nbt.getString("wings"), WINGS_DATA.WING_MAP.getClass());
        if (newWingMap != null) {
            // Add player's wing data to the hashmap.
            WINGS_DATA.WING_MAP.putAll(newWingMap);
        }
        return WINGS_DATA;
    }

    /**
     * Save wing nbt of player and if sync: update to all others
     *
     * @param player      Either ServerPlayerEntity or ClientPlayerEntity
     * @param newWingType texture of wing. Default: "none"
     */
    public void setWingType(PlayerEntity player, String newWingType, boolean sync) {
        String playerUUID = player.getUuidAsString();
        String oldWingType = WING_MAP.getOrDefault(playerUUID, "none");

        // For keeping track of every player.
        WING_MAP.put(playerUUID, newWingType);

        // No point in sending a packet if nothing has actually changed.
        if (!oldWingType.equals(newWingType) && player instanceof ServerPlayerEntity serverPlayer) {
            // Message for player
            if (newWingType != "none") {
                String spacedName = newWingType.replace('_', ' ');
                serverPlayer.sendMessage(Text.literal("You are now a " + spacedName + " pixie!")
                        .fillStyle(Style.EMPTY.withColor(Formatting.AQUA)), true);
            }

            // Send packet to every other online player
            if (sync) {
                NbtCompound nbt = writeNbtOnlineOnly(serverPlayer, true);
                for (ServerPlayerEntity worldPlayer : serverPlayer.getServer().getPlayerManager().getPlayerList()) {
                    if (!worldPlayer.equals(serverPlayer)) { // Skip sender's.
                        syncWings(nbt, worldPlayer);
                    }
                }
            }
            this.markDirty(); // Tell server ready to update persistent data.
        }
    }

    /**
     * Send player(s) wing type to client or server depending on PlayerEntity type.
     */
    public static void syncWings(NbtCompound wings, PlayerEntity player) {
        PacketByteBuf buffer = PacketByteBufs.create().writeNbt(wings);

        if (player instanceof ServerPlayerEntity serverPlayer) {
            // Send to player
            ServerPlayNetworking.send(serverPlayer, ModMessages.WINGS_SYNC_ID, buffer);
        } else if (player instanceof ClientPlayerEntity) { // else if important as not to reference CLIENT-ONLY class on server (will crash)
            // Send to server
            ClientPlayNetworking.send(ModMessages.WINGS_SYNC_ID, buffer);
        }
    }
}
