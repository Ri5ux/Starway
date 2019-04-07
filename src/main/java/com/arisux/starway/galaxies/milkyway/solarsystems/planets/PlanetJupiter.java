package com.arisux.starway.galaxies.milkyway.solarsystems.planets;

import org.lwjgl.opengl.GL11;

import com.arisux.starway.SpaceManager;
import com.arisux.starway.api.IPlanet;
import com.arisux.starway.api.Planet;
import com.arisux.starway.galaxies.milkyway.solarsystems.SolarSystemSol;
import com.asx.mdx.lib.client.util.Color;
import com.asx.mdx.lib.client.util.OpenGL;
import com.asx.mdx.lib.util.Game;
import com.asx.mdx.lib.world.Dimension;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlanetJupiter extends Planet implements IPlanet
{
    public static Planet    instance  = new PlanetJupiter();

    public void onInitialization(FMLInitializationEvent event)
    {
        this.setObjectOrbiting(SolarSystemSol.instance);
    }

    @Override
    public String getName()
    {
        return "Jupiter";
    }

    @Override
    public Dimension getDimension()
    {
        return null;
    }

    @Override
    public float getDistanceFromObjectOrbiting()
    {
        return 0.00008229F;
    }

    @Override
    public float getObjectSize()
    {
        return 43441F;
    }

    /** 1 Year **/
    @Override
    public float getOrbitTime()
    {
        return 11.86F;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void render(float partialTicks)
    {
        int planetSize = (int) (SpaceManager.instance.getPlanetaryScale() * getObjectSize());

        OpenGL.enableRescaleNormal();
        OpenGL.scale(-1F, 1F, 1F);

        if (sphere != null)
        {
            OpenGL.enableBlend();
            OpenGL.blendClear();
            OpenGL.disableTexture2d();
            Minecraft.getMinecraft().entityRenderer.enableLightmap();

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_LIGHT0);
            GL11.glEnable(GL11.GL_LIGHT1);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);

            OpenGL.pushMatrix();
            {
                float rotateSpeed = 0.1F;
                float rotation = Game.minecraft().world.getWorldTime() * rotateSpeed % 360;
                OpenGL.rotate(rotation + (rotateSpeed > 1.0F ? Game.partialTicks() : 0), 0, 1, 0);
                sphere.cull = false;
                sphere.setScale(planetSize);
                sphere.setColor(new Color(0.9F, 0.6F, 0.3F, 1F));
                sphere.render();
            }
            OpenGL.popMatrix();
            
            Minecraft.getMinecraft().entityRenderer.disableLightmap();
            OpenGL.disableLighting();
            OpenGL.enableTexture2d();
            OpenGL.disableBlend();
        }
        OpenGL.disableRescaleNormal();
    }
}
