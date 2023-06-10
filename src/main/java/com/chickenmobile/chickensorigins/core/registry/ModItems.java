package com.chickenmobile.chickensorigins.core.registry;

import com.chickenmobile.chickensorigins.ChickensOrigins;
import com.chickenmobile.chickensorigins.client.items.PixieDust;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

    public static final Item PIXIE_DUST = new PixieDust();

    public static void registerItems() {
        Registry.register(Registry.ITEM, new Identifier(ChickensOrigins.MOD_ID, "pixie_dust"), PIXIE_DUST);
    }

}
