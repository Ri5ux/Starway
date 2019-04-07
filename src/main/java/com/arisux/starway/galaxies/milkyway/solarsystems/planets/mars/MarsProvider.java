package com.arisux.starway.galaxies.milkyway.solarsystems.planets.mars;

import com.arisux.starway.api.SkyProvider;
import com.arisux.starway.galaxies.milkyway.solarsystems.planets.PlanetMars;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MarsProvider extends WorldProvider
{
    @SideOnly(Side.CLIENT)
    private SkyProvider skyProvider;

    @Override
    protected void init()
    {
        this.hasSkyLight = true;
        this.biomeProvider = new BiomeProviderMars(MarsBiome.mars);
    }

    @Override
    public DimensionType getDimensionType()
    {
        return PlanetMars.dimension.getType();
    }

    @Override
    public IChunkGenerator createChunkGenerator()
    {
        return new ChunkGeneratorMars(this.world, 1000);
    }

    @Override
    public int getActualHeight()
    {
        return 256;
    }

    /** Does this cause a re-render 3 times? **/
    @Override
    @SideOnly(Side.CLIENT)
    public IRenderHandler getSkyRenderer()
    {
        return skyProvider == null ? skyProvider = new SkyProviderMars() : skyProvider;
    }

    @Override
    public IRenderHandler getCloudRenderer()
    {
        return null;
    }
    
    @Override
    public IRenderHandler getWeatherRenderer()
    {
        return null;
    }

    @Override
    public String getSaveFolder()
    {
        return "DIM_MARS";
    }

    @Override
    public int getAverageGroundLevel()
    {
        return 64;
    }

    @Override
    public boolean canRespawnHere()
    {
        return true;
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
    public Vec3d getSkyColor(Entity cameraEntity, float partialTicks)
    {
        return super.getSkyColor(cameraEntity, partialTicks);
    }

    @Override
    public Vec3d getFogColor(float celestialAngle, float renderPartialTicks)
    {
        return new Vec3d(0.8F, 0.45F, 0.2F);
    }

    @Override
    public Vec3d getCloudColor(float partialTicks)
    {
        return new Vec3d(0.0F, 0.0F, 0.0F);
    }

    @Override
    public float getStarBrightness(float renderPartialTicks)
    {
        return 1F;
    }
    
    @Override
    public float getSunBrightness(float angle)
    {
        float celestialAngle = this.world.getCelestialAngle(angle);
        float brightness = 1.0F - (MathHelper.cos(celestialAngle * (float) Math.PI * 2.0F) * 2.0F + 0.2F);

        if (brightness < 0.0F)
        {
            brightness = 0.0F;
        }

        if (brightness > 1.0F)
        {
            brightness = 1.0F;
        }

        brightness = 1.0F - brightness;
        brightness = (float) (brightness * (1.0D - this.world.getRainStrength(angle) * 5.0F / 16.0D));
        brightness = (float) (brightness * (1.0D - this.world.getThunderStrength(angle) * 5.0F / 16.0D));
        
        return brightness * 1F;
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
