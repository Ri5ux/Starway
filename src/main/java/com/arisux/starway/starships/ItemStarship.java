package com.arisux.starway.starships;

import com.asx.mdx.lib.world.entity.player.inventory.Inventories;
import com.asx.mdx.lib.world.item.HookedItem;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemStarship extends HookedItem
{
    public ItemStarship()
    {
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        if (Inventories.consumeItem(player, this))
        {
            EntityStarship starship = new EntityStarship(world, player.posX, player.posY, player.posZ);
            world.spawnEntity(starship);
        }

        return super.onItemRightClick(world, player, hand);
    }
}
