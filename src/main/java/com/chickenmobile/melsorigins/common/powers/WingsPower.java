package com.chickenmobile.melsorigins.common.powers;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class WingsPower extends Power {
    private Item wingsItem;
    private final Identifier wingsId;

    public WingsPower(PowerType<?> type, LivingEntity entity, Identifier wingsId) {
        super(type, entity);
        this.wingsId = wingsId;
    }

    public Item getWings() {
        if(wingsItem == null) {
            //wingsItem = Registries.ITEM.get(wingsId) instanceof WingItem wings ? wings : ModItems.MONARCH_WINGS;
        }
        return wingsItem;
    }

}
