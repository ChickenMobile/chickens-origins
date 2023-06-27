package com.chickenmobile.chickensorigins;

import com.chickenmobile.chickensorigins.core.integration.Origins;
import com.chickenmobile.chickensorigins.core.registry.ModItems;
import com.chickenmobile.chickensorigins.core.registry.ModParticles;
import com.chickenmobile.chickensorigins.core.registry.ModEffects;
import com.chickenmobile.chickensorigins.core.registry.ModMessages;
import com.chickenmobile.chickensorigins.core.registry.ModPotions;
import com.chickenmobile.chickensorigins.util.WingsData;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Predicate;

public class ChickensOrigins implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "chickensorigins";
	public static final Predicate<PlayerEntity> IS_PIXIE = FabricLoader.getInstance().isModLoaded("origins") ? Origins::isPixie : entity -> false;
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static final Identifier MOD_IDENTIFIER = new Identifier(MOD_ID);

	@Override
	public void onInitialize() {
		ModParticles.registerParticles();
		ModItems.registerItems();

		// Networking
		ModMessages.registerC2SPackets();

		// Get persistent data for players
		ServerLifecycleEvents.SERVER_STARTED.register((server) -> WingsData.WINGS_DATA = WingsData.getWingState(server));

		// Effects and Potions
		ModEffects.registerEffects();
		ModPotions.registerPotions();
	}
}
