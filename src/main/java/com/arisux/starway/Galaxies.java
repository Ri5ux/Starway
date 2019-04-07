package com.arisux.starway;

import com.arisux.starway.api.Galaxy;
import com.arisux.starway.dimensions.space.SpaceProvider;
import com.arisux.starway.galaxies.GalaxyMilkyWay;
import com.asx.mdx.core.mods.IInitEvent;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class Galaxies implements IInitEvent
{
    public static final Galaxies instance    = new Galaxies();
    private boolean              initialized = false;

    public void register()
    {
        SpaceManager.instance.getObjectsInSpace().add(GalaxyMilkyWay.instance);
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        this.register();

        for (Galaxy galaxy : Starway.getGalaxies())
        {
            galaxy.onInitialization(event);
        }
    }

    @SubscribeEvent
    public void onWorldTick(WorldTickEvent event)
    {
        for (Galaxy galaxy : Starway.getGalaxies())
        {
            galaxy.onGlobalTick(event);

            if (event.world.provider instanceof SpaceProvider)
            {
                if (FMLClientHandler.instance() != null && !this.hasInitialized())
                {
                    galaxy.onInitialTick(event);
                    this.initialized = true;
                }

                galaxy.onTick(event);
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        for (Galaxy galaxy : Starway.getGalaxies())
        {
            galaxy.load(event);
        }
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event)
    {
        for (Galaxy galaxy : Starway.getGalaxies())
        {
            galaxy.save(event);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event)
    {
        for (Galaxy galaxy : Starway.getGalaxies())
        {
            galaxy.unload(event);
        }
    }

    public boolean hasInitialized()
    {
        return this.initialized;
    }
}
