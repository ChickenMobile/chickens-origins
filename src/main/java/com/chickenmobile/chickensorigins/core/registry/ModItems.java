package com.chickenmobile.chickensorigins.core.registry;

import com.chickenmobile.chickensorigins.ChickensOrigins;
import com.chickenmobile.chickensorigins.client.items.*;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Hashtable;

public class ModItems {

    public static final Item PIXIE_DUST = new PixieDust();

    public static final Hashtable<PixieDust.WingColor, Item> ItemWingDict = new Hashtable<>();

    static { // Static method to populate coloured pixie_dust items
        for (PixieDust.WingColor wingColor : PixieDust.WingColor.values()) {
            if (wingColor == PixieDust.WingColor.NONE) { // Regular ol' pixie dust is 'none'
                continue;
            }
            ItemWingDict.put(wingColor, new PixieDust(wingColor));
        }
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(ChickensOrigins.MOD_ID, name), item);
    }

    public static void registerItems() {
        registerItem("pixie_dust", PIXIE_DUST);

        // Register for each coloured wing type.
        ItemWingDict.forEach((wingColor, item) -> registerItem(wingColor.name().toLowerCase() + "_pixie_dust", item));
    }

}
