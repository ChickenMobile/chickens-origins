package com.chickenmobile.melsorigins;

import com.chickenmobile.melsorigins.core.integration.Origins;
import com.chickenmobile.melsorigins.core.registry.ModItems;
import com.chickenmobile.melsorigins.core.registry.ModParticles;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Predicate;

public class MelsOrigins implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "melsorigins";
	public static final Predicate<PlayerEntity> IS_PIXIE = FabricLoader.getInstance().isModLoaded("origins") ? Origins::isPixie : entity -> false;
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModParticles.registerParticles();
		ModItems.registerItems();
	}
}
