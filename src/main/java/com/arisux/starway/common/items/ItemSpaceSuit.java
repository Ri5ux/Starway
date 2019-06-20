package com.arisux.starway.common.items;

import com.arisux.starway.api.ISpaceSuit;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public class ItemSpaceSuit extends ItemArmor implements ISpaceSuit
{
    public ItemSpaceSuit(ArmorMaterial materialIn, EntityEquipmentSlot equipmentSlotIn)
    {
        super(materialIn, 0, equipmentSlotIn);
    }
}
