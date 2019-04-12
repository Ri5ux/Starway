package com.arisux.starway.dimensions.space;

import org.lwjgl.opengl.GL11;

import com.arisux.starway.api.IPlanetSkyProvider;
import com.arisux.starway.api.SkyProvider;
import com.asx.mdx.lib.client.util.Color;
import com.asx.mdx.lib.client.util.OpenGL;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SkyProviderSpace extends SkyProvider implements IPlanetSkyProvider
{
    @Override
    public void render(float renderPartialTicks, WorldClient world, Minecraft mc)
    {
        super.render(renderPartialTicks, world, mc);
//        OpenGL.pushMatrix();
//        EntityLivingBase rve = Minecraft.getMinecraft().renderViewEntity;
//        double x = rve.lastTickPosX + (rve.posX - rve.lastTickPosX) * (double) Game.partialTicks();
//        double y = rve.lastTickPosY + (rve.posY - rve.lastTickPosY) * (double) Game.partialTicks();
//        double z = rve.lastTickPosZ + (rve.posZ - rve.lastTickPosZ) * (double) Game.partialTicks();
//        OpenGL.translate(-x, -y, -z);
//        OpenGL.scale(1F, -1F, 1F);
//        Minecraft.getMinecraft().entityRenderer.enableLightmap();
//
//        for (IGalaxy gx : Starway.getGalaxies())
//        {
//            for (ISolarSystem ss : gx.getSolarSystems())
//            {
//                SolarSystem solarsystem = (SolarSystem) ss;
//
//                OpenGL.pushMatrix();
//                {
//                    OpenGL.translate(solarsystem.pos().x, solarsystem.pos().y, solarsystem.pos().z);
//                    solarsystem.drawSolarSystemInWorld(rve, solarsystem);
//                }
//                OpenGL.popMatrix();
//            }
//        }
//
//        Minecraft.getMinecraft().entityRenderer.disableLightmap();
//        OpenGL.popMatrix();
    }

    @Override
    public void drawSky(World world, float renderPartialTicks)
    {
        OpenGL.disable(GL11.GL_FOG);
        OpenGL.disable(GL11.GL_ALPHA_TEST);
        OpenGL.enable(GL11.GL_BLEND);
        OpenGL.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        this.drawStarsInSky(world, renderPartialTicks);

        OpenGL.color(1F, 1F, 1F, 1F);
        OpenGL.enable(GL11.GL_TEXTURE_2D);
        OpenGL.blendFunc(GL11.GL_SRC_ALPHA, 1);

        this.drawPlanetsInSky(world, renderPartialTicks);

        OpenGL.disable(GL11.GL_BLEND);
        OpenGL.enable(GL11.GL_ALPHA_TEST);
        GL11.glDepthMask(true);
        
        OpenGL.disable(GL11.GL_FOG);
        OpenGL.blendClear();
    }

    @Override
    public void drawStarsInSky(World world, float renderPartialTicks)
    {
        super.drawStarsInSky(world, renderPartialTicks);
    }

    @Override
    public void drawPlanetsInSky(World world, float renderPartialTicks)
    {
        ;
    }

    @Override
    public void drawCloudsInSky(World world, float renderPartialTicks)
    {
        ;
    }
    
    @Override
    public EnumFacing getCloudMovementDirection()
    {
        return null;
    }
    
    @Override
    public Color getStarColor()
    {
        return super.getStarColor();
    }

    @Override
    public float getCloudMovementSpeed()
    {
        return 0;
    }
}
