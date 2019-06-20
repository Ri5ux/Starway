package com.arisux.starway.galaxies.milkyway.solarsystems.planets.mars;

import com.arisux.starway.Starway;
import com.arisux.starway.api.BiomeGenStarway;

import net.minecraft.block.BlockSand;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MarsBiome extends BiomeGenStarway
{
    public static MarsBiome mars = (MarsBiome) new MarsBiome(new BiomeProperties("Mars").setBaseHeight(0.2F).setHeightVariation(0.5F).setTemperature(0.25F).setRainDisabled()).setRegistryName(Starway.Properties.ID, "mars");

    @Mod.EventBusSubscriber(modid = Starway.Properties.ID)
    public static class RegistrationHandler
    {
        @SubscribeEvent
        public static void registerBiomes(final RegistryEvent.Register<Biome> event)
        {
            event.getRegistry().register(MarsBiome.mars);
        }
    }
    
    public MarsBiome(BiomeProperties properties)
    {
        super(properties);
        this.topBlock = Blocks.SAND.blockState.getBaseState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND);//?
        this.fillerBlock = Blocks.RED_SANDSTONE.getDefaultState();
        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
    }

    @Override
    public float getSpawningChance()
    {
        return 0F;
    }
}
