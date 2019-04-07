package com.arisux.starway.dimensions.space;

import java.util.List;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;

public class ChunkGeneratorSpace implements IChunkGenerator
{
    private World world;

    public ChunkGeneratorSpace(World world)
    {
        this.world = world;
    }

    @Override
    public Chunk generateChunk(int chunkX, int chunkZ)
    {
        ChunkPrimer chunkprimer = new ChunkPrimer();

        Chunk chunk = new Chunk(this.world, chunkprimer, chunkX, chunkZ);
        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public void populate(int chunkX, int chunkZ)
    {
        ;
    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z)
    {
        return false;
    }

    @Override
    public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        return null;
    }

    @Override
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored)
    {
        return null;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z)
    {
        ;
    }

    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos)
    {
        return false;
    }
}
