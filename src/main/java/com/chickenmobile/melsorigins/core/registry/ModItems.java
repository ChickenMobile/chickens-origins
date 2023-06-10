package com.chickenmobile.melsorigins.core.registry;

import com.chickenmobile.melsorigins.MelsOrigins;
import com.chickenmobile.melsorigins.client.items.PixieDust;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

    public static final Item PIXIE_DUST = new PixieDust();

    public static void registerItems() {
        Registry.register(Registry.ITEM, new Identifier(MelsOrigins.MOD_ID, "pixie_dust"), PIXIE_DUST);
    }

}
