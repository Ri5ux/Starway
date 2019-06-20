package com.arisux.starway;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class Tabs
{
    public static final CreativeTabs TAB_MAIN = new CreativeTabs("starway.main") {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(ItemHandler.helmMK50);
        }
    };
}
