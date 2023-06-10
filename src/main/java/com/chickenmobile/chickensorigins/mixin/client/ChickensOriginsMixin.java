package com.chickenmobile.chickensorigins.mixin.client;

import com.chickenmobile.chickensorigins.ChickensOrigins;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class ChickensOriginsMixin {
	@Inject(at = @At("HEAD"), method = "init()V")
	private void init(CallbackInfo info) {
		ChickensOrigins.LOGGER.info("Loading Chickens Origins");
	}
}