package com.arisux.starway.dimensions.space;

import com.arisux.starway.Starway;
import com.arisux.starway.api.BiomeGenStarway;
import com.arisux.starway.galaxies.milkyway.solarsystems.planets.mars.MarsBiome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SpaceBiome extends BiomeGenStarway
{
    public static SpaceBiome space = (SpaceBiome) new SpaceBiome(new BiomeProperties("Space").setRainfall(0F).setTemperature(0F).setRainDisabled()).setRegistryName(Starway.Properties.ID, "space");

    @Mod.EventBusSubscriber(modid = Starway.Properties.ID)
    public static class RegistrationHandler
    {
        @SubscribeEvent
        public static void registerBiomes(final RegistryEvent.Register<Biome> event)
        {
            event.getRegistry().register(space);
        }
    }
    
    public SpaceBiome(BiomeProperties properties)
    {
        super(properties);
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCreatureList.clear();
    }

    @Override
    public int getSkyColorByTemp(float temp)
    {
        return 0x000000;
    }

    @Override
    public float getSpawningChance()
    {
        return 0F;
    }

    @Override
    public boolean isHighHumidity()
    {
        return false;
    }

    @Override
    public BiomeDecorator createBiomeDecorator()
    {
        return super.createBiomeDecorator();
    }
}
