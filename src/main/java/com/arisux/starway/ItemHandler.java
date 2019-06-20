package com.arisux.starway;

import com.arisux.starway.common.items.ItemSuitMK50;
import com.asx.mdx.lib.client.Renderers;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;

@ObjectHolder(Starway.Properties.ID)
public class ItemHandler
{
    public static final Item helmMK50 = new ItemSuitMK50(EntityEquipmentSlot.HEAD).setRegistryName("spacesuit.mk50.helm");
    public static final Item chestMK50 = new ItemSuitMK50(EntityEquipmentSlot.CHEST).setRegistryName("spacesuit.mk50.chest");
    public static final Item legsMK50 = new ItemSuitMK50(EntityEquipmentSlot.LEGS).setRegistryName("spacesuit.mk50.legs");
    public static final Item bootsMK50 = new ItemSuitMK50(EntityEquipmentSlot.FEET).setRegistryName("spacesuit.mk50.boots");

    @Mod.EventBusSubscriber(modid = Starway.Properties.ID)
    public static class RegistrationHandler
    {
        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event)
        {
            for (java.lang.reflect.Field field : Starway.items().getClass().getDeclaredFields())
            {
                try
                {
                    field.setAccessible(true);

                    Object obj = field.get(Starway.items());

                    if (obj instanceof Item)
                    {
                        Item item = (Item) obj;
                        item.setCreativeTab(Tabs.TAB_MAIN);
                        item.setTranslationKey(item.getRegistryName().getNamespace() + ":" + item.getRegistryName().getPath());
                        event.getRegistry().register(item);
                        registerIcon(item);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }
    }

    private static Item registerIcon(Item item)
    {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            Renderers.registerIcon(item);
        }

        return item;
    }
}
