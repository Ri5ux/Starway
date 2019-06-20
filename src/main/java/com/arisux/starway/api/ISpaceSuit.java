package com.arisux.starway.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface ISpaceSuit
{
    public static boolean isFullSetEquipped(EntityPlayer player)
    {
        boolean isFullSuitEquipped = true;
        
        for (ItemStack stack : player.getArmorInventoryList())
        {
            if (stack != null && stack != ItemStack.EMPTY && !(stack.getItem() instanceof ISpaceSuit) || stack == ItemStack.EMPTY || stack == null)
            {
                isFullSuitEquipped = false;
            }
        }
        
        return isFullSuitEquipped;
    }
}
