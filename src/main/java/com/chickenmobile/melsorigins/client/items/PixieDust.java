package com.chickenmobile.melsorigins.client.items;

import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

public class PixieDust extends Item {

    public PixieDust() {
        super(new Item.Settings().maxCount(1).rarity(Rarity.RARE));
    }

}