package com.arisux.starway.galaxies.milkyway.solarsystems.planets;

import com.arisux.starway.api.IPlanet;
import com.arisux.starway.api.Planet;
import com.arisux.starway.galaxies.milkyway.solarsystems.SolarSystemSol;
import com.asx.mdx.lib.world.Dimension;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlanetNeptune extends Planet implements IPlanet
{
    public static Planet instance = new PlanetNeptune();

    public void onInitialization(FMLInitializationEvent event)
    {
        this.setObjectOrbiting(SolarSystemSol.instance);
    }

    @Override
    public String getName()
    {
        return "Neptune";
    }

    @Override
    public Dimension getDimension()
    {
        return null;
    }

    @Override
    public float getDistanceFromObjectOrbiting()
    {
        return 0.0004755F;
    }

    @Override
    public float getObjectSize()
    {
        return 15299F;
    }

    @Override
    public float getOrbitTime()
    {
        return 164.79F;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void render(float partialTicks)
    {
        super.render(partialTicks);
        // OpenGL.scale(-1F, 1F, 1F);
        //
        // if (sphere != null)
        // {
        // OpenGL.enableBlend();
        // OpenGL.blendClear();
        // OpenGL.disableTexture2d();
        // OpenGL.enableStandardItemLighting();
        // sphere.cull = false;
        // sphere.setScale((int) (Renderer.instance.getPlanetDrawScale() * planet.getObjectSize()));
        // sphere.setColor(new Color(0.3F, 0.1F, 1F, 1F));
        // sphere.render();
        // OpenGL.disableStandardItemLighting();
        // OpenGL.enableTexture2d();
        // OpenGL.disableBlend();
        // }
    }
}
