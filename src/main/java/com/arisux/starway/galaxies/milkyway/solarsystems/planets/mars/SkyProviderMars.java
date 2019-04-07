package com.arisux.starway.galaxies.milkyway.solarsystems.planets.mars;

import com.arisux.starway.api.IPlanetSkyProvider;
import com.arisux.starway.api.SkyProvider;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.World;

public class SkyProviderMars extends SkyProvider implements IPlanetSkyProvider
{
    public SkyProviderMars()
    {
        ;
    }
    
    @Override
    public void render(float renderPartialTicks, WorldClient world, Minecraft mc)
    {
        super.render(renderPartialTicks, world, mc);
    }
    
    @Override
    public void drawCloudsInSky(World world, float renderPartialTicks)
    {
        ;
    }
}
