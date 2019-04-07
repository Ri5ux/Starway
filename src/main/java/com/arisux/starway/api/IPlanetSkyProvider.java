package com.arisux.starway.api;

import com.asx.mdx.lib.client.util.Color;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IPlanetSkyProvider
{
    public Color getSkyColor();

    public Color getStarColor();

    public Color getCloudColor();

    public EnumFacing getCloudMovementDirection();

    public float getCloudScale();

    public float getCloudHeightSize();

    public float getCloudMovementSpeed();

    public ResourceLocation getResourceCloudMap();

    public void createSkyCallList();

    public void drawPlanetsInSky(World world, float renderPartialTicks);

    public void drawStarsInSky(World world, float renderPartialTicks);

    public void drawCloudsInSky(World world, float renderPartialTicks);

    public void drawSky(World world, float renderPartialTicks);
}
