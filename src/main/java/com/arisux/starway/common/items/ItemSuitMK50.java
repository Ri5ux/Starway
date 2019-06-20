package com.arisux.starway.common.items;

import com.arisux.starway.Starway;
import com.arisux.starway.client.model.armor.ModelSuitMK50;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSuitMK50 extends ItemSpaceSuit
{
    @SideOnly(Side.CLIENT)
    public static final ModelSuitMK50 MODEL = new ModelSuitMK50();
    
    public ItemSuitMK50(EntityEquipmentSlot equipmentSlotIn)
    {
        super(ArmorMaterial.CHAIN, equipmentSlotIn);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default)
    {
        return MODEL;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
    {
        return Starway.resources().ARMOR_SPACESUIT_MK50_WHITE.toString();
    }
}
