package com.chickenmobile.chickensorigins.util;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public interface IPlayerManager {
    List<ServerPlayerEntity> getPlayers();
}
