package com.chickenmobile.chickensorigins.core.registry;

import com.chickenmobile.chickensorigins.ChickensOrigins;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static final ItemGroup CHICKENSORIGINS_GROUP = FabricItemGroupBuilder.build(
            new Identifier(ChickensOrigins.MOD_ID, "pixie_dust"),
            () -> new ItemStack(ModItems.PIXIE_DUST));
}
