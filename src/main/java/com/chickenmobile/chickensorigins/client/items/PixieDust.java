package com.chickenmobile.chickensorigins.client.items;

import com.chickenmobile.chickensorigins.ChickensOrigins;
import com.chickenmobile.chickensorigins.core.integration.Origins;
import com.chickenmobile.chickensorigins.core.registry.ModItemGroup;
import com.chickenmobile.chickensorigins.util.WingsData;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class PixieDust extends Item {

    private final WingColor WING_COLOR;

    public PixieDust() {
        this(WingColor.NONE, 64);
    }

    public PixieDust(WingColor wingColor) {
        this(wingColor, 64);
    }

    private PixieDust(WingColor wingColor, int stackSize) {
        super(new FabricItemSettings()
                .group(ModItemGroup.CHICKENSORIGINS_GROUP)
                .maxCount(stackSize)
                .rarity(Rarity.RARE));
        this.WING_COLOR = wingColor;
    }

    // Used for texture-location and colour of dye.
    public enum WingColor {
        NONE("none", Items.AIR),
        BLUE("blue_morpho", Items.BLUE_DYE),
        BROWN("buckeye", Items.BROWN_DYE),
        GREEN("green_spotted_triangle", Items.GREEN_DYE),
        LIME("birdwing", Items.LIME_DYE),
        ORANGE("monarch", Items.ORANGE_DYE),
        PINK("pink_rose", Items.PINK_DYE),
        PURPLE("purple_emperor", Items.PURPLE_DYE),
        RED("red_lacewing", Items.RED_DYE),
        YELLOW("tiger_swallowtail", Items.YELLOW_DYE);
        //WHITE("white", Items.WHITE_DYE),
        //BLACK("black", Items.BLACK_DYE);

        public final String texture_name;
        public final Item dye;

        WingColor(String texture_name, Item dye) {
            this.texture_name = texture_name;
            this.dye = dye;
        }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player) {
            // Server
            if (user instanceof ServerPlayerEntity serverPlayerEntity) {
                Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
                serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
            }

            // Wings
            if (ChickensOrigins.IS_PIXIE.test(player)) {
                WingsData.WINGS_DATA.setWingType(player, this.WING_COLOR.texture_name, true);
            }

            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }
        }

        return stack;
    }

    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }
}