package com.arisux.starway.dimensions.space;

import com.arisux.starway.api.SkyProvider;
import com.arisux.starway.dimensions.DimensionSpace;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpaceProvider extends WorldProvider
{
    @SideOnly(Side.CLIENT)
    private SkyProvider skyProvider;
    
    @Override
    protected void init()
    {
        this.hasSkyLight = true;
        this.biomeProvider = new BiomeProviderSpace(SpaceBiome.space);
    }

    @Override
    public DimensionType getDimensionType()
    {
        return DimensionSpace.instance.getType();
    }

    @Override
    public int getActualHeight()
    {
        return 256;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IRenderHandler getSkyRenderer()
    {
        return skyProvider == null ? skyProvider = new SkyProviderSpace() : skyProvider;
    }

    @Override
    public IRenderHandler getCloudRenderer()
    {
        return getSkyRenderer();
    }

    @Override
    public String getSaveFolder()
    {
        return "DIM_SPACE";
    }
    
    @Override
    public float[] calcSunriseSunsetColors(float angle, float renderPartialTicks)
    {
        return null;
    }

    @Override
    public boolean isSkyColored()
    {
        return false;
    }

    @Override
    public boolean isSurfaceWorld()
    {
        return false;
    }

    
    @Override
    public int getAverageGroundLevel()
    {
        return 128;
    }

    @Override
    public IChunkGenerator createChunkGenerator()
    {
        return new ChunkGeneratorSpace(this.world);
    }

    @Override
    public boolean canRespawnHere()
    {
        return false;
    }

    @Override
    public float getCloudHeight()
    {
        return 0.0F;
    }

    @Override
    public double getMovementFactor()
    {
        return 1.0D;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(float celestialAngle, float renderPartialTicks)
    {
        return new Vec3d(0.0F, 0.0F, 0.0F);
    }
    
    @Override
    public Vec3d getCloudColor(float partialTicks)
    {
        return new Vec3d(0.0F, 0.0F, 0.0F);
    }
    
    @Override
    public Vec3d getSkyColor(Entity cameraEntity, float partialTicks)
    {
        return new Vec3d(0D, 0D, 0D);
    }

    @Override
    public float getStarBrightness(float renderPartialTicks)
    {
        return 1F;
    }

    @Override
    public float getSunBrightness(float angle)
    {
        return 0.4F;
    }
    
    @Override
    public boolean isBlockHighHumidity(BlockPos pos)
    {
        return false;
    }
    
    @Override
    public boolean canSnowAt(BlockPos pos, boolean checkLight)
    {
        return false;
    }

    @Override
    public boolean canDoRainSnowIce(Chunk chunk)
    {
        return false;
    }

    @Override
    public boolean canDoLightning(Chunk chunk)
    {
        return false;
    }
}
