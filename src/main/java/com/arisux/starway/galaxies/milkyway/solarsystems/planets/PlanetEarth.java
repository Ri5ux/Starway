package com.arisux.starway.galaxies.milkyway.solarsystems.planets;

import org.lwjgl.opengl.GL11;

import com.arisux.starway.SpaceManager;
import com.arisux.starway.api.IPlanet;
import com.arisux.starway.api.Planet;
import com.arisux.starway.dimensions.space.TeleporterSpace;
import com.arisux.starway.galaxies.milkyway.solarsystems.SolarSystemSol;
import com.asx.mdx.lib.client.util.Color;
import com.asx.mdx.lib.client.util.OpenGL;
import com.asx.mdx.lib.world.Dimension;

import net.minecraft.client.Minecraft;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlanetEarth extends Planet implements IPlanet
{
    public static Planet    instance  = new PlanetEarth();

    public static Dimension dimension = new Dimension("Earth", "Overworld", null, false)
                                       {
                                           /** Should NOT register this dimension with forge. Handled by vanilla. **/
                                           @Override
                                           public boolean shouldRegisterWithForge()
                                           {
                                               return false;
                                           }

                                           /** Create a standard Teleporter **/
                                           @Override
                                           public Teleporter getTeleporter(WorldServer worldServer)
                                           {
                                               return new TeleporterSpace(worldServer);
                                           }

                                           /** Class of the vanilla overworld WorldProvider **/
                                           @Override
                                           public Class<? extends WorldProvider> getProvider()
                                           {
                                               return WorldProviderSurface.class;
                                           }

                                           /** IGNORED since this dimension is handled by vanilla. **/
                                           @Override
                                           public int getInitialChunkLoadRadius()
                                           {
                                               return 0;
                                           }

                                           /** Should NOT auto load since vanilla handles this dimension already **/
                                           @Override
                                           public boolean shouldAutoLoad()
                                           {
                                               return false;
                                           }
                                       };

    public void onInitialization(FMLInitializationEvent event)
    {
        this.setObjectOrbiting(SolarSystemSol.instance);
    }

    @Override
    public String getName()
    {
        return "Earth";
    }

    @Override
    public Dimension getDimension()
    {
        return dimension;
    }

    /** 8.2 Light Minutes **/
    @Override
    public float getDistanceFromObjectOrbiting()
    {
        return 0.000015590539204502F;
    }

    @Override
    public float getObjectSize()
    {
        return 3959F;
    }

    /** 1 Year **/
    @Override
    public float getOrbitTime()
    {
        return 1.0F;
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
            
            sphere.cull = false;
            sphere.setScale(planetSize);
            sphere.setColor(new Color(0F, 0.2F, 0.5F, 1F));
            sphere.render();
            Minecraft.getMinecraft().entityRenderer.disableLightmap();
            OpenGL.disableLighting();
            OpenGL.enableTexture2d();
            OpenGL.disableBlend();
        }
        OpenGL.disableRescaleNormal();
    }
}
