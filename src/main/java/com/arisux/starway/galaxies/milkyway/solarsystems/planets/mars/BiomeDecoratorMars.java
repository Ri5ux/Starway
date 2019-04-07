package com.arisux.starway.galaxies.milkyway.solarsystems.planets.mars;

import java.util.Random;

import com.asx.mdx.lib.world.Worlds;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class BiomeDecoratorMars extends BiomeDecorator
{
    public Biome biome;

    public BiomeDecoratorMars(Biome biome)
    {
        super();
        this.biome = biome;
    }

    @Override
    public void decorate(World world, Random random, Biome biome, BlockPos pos)
    {
        if (this.decorating)
        {
            throw new RuntimeException("Already decorating");
        }
        else
        {

            this.chunkProviderSettings = ChunkGeneratorSettings.Factory.jsonToFactory(world.getWorldInfo().getGeneratorOptions()).build();
            this.chunkPos = pos;
            this.generateOres(world, random);
            this.genDecorations(biome, world, random);
            this.decorating = false;
        }
    }

    @Override
    protected void genDecorations(Biome biome, World world, Random random)
    {
        ;
    }

    @Override
    protected void generateOres(World world, Random random)
    {
        Worlds.generateInChunk(world, new WorldGenMinable(Blocks.SAND.getDefaultState(), 32), random, 20, 0, 128, chunkPos);
    }
}
