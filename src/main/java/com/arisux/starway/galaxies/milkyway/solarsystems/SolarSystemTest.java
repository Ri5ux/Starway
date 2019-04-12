package com.arisux.starway.galaxies.milkyway.solarsystems;

import com.arisux.starway.api.ISolarSystem;
import com.arisux.starway.api.SolarSystem;
import com.arisux.starway.galaxies.GalaxyMilkyWay;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class SolarSystemTest extends SolarSystem implements ISolarSystem
{
    public static SolarSystemTest instance = new SolarSystemTest();

    @Override
    public void onInitialization(FMLInitializationEvent event)
    {
        super.onInitialization(event);
        this.setObjectOrbiting(GalaxyMilkyWay.instance);
    }

    @Override
    public String getName()
    {
        return "The Test Solar System";
    }

    @Override
    public float getDistanceFromObjectOrbiting()
    {
        return 27000F * 2F;
    }

    /** Since this is a solar system, this is the size of the sun in the solar system. **/
    @Override
    public float getObjectSize()
    {
        return 432687F;
    }

    @Override
    public float getOrbitTime()
    {
        return 300000000F;
    }
    
    @Override
    protected void drawSun(float partialTicks)
    {
        super.drawSun(partialTicks);
    }
}
