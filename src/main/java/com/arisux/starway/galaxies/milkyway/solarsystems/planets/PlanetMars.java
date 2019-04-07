package com.arisux.starway.galaxies.milkyway.solarsystems.planets;

import com.arisux.starway.api.Planet;
import com.arisux.starway.dimensions.DimensionMars;
import com.arisux.starway.galaxies.milkyway.solarsystems.SolarSystemSol;
import com.asx.mdx.lib.world.Dimension;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class PlanetMars extends Planet
{
    public static Planet     instance  = new PlanetMars();
    public static Dimension dimension = new DimensionMars();

    public void onInitialization(FMLInitializationEvent event)
    {
        this.setObjectOrbiting(SolarSystemSol.instance);
    }

    @Override
    public String getName()
    {
        return "Mars";
    }

    @Override
    public Dimension getDimension()
    {
        return dimension;
    }

    @Override
    public void onTick(WorldTickEvent event)
    {
        super.onTick(event);
    }

    @Override
    public float getDistanceFromObjectOrbiting()
    {
        return /** 12.6 Light Minutes **/
        0.000023956194387406F;
    }

    @Override
    public float getObjectSize()
    {
        return 2106F;
    }

    @Override
    public float getOrbitTime()
    {
        return 1.88F;
    }
}
