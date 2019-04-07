package com.arisux.starway.galaxies.milkyway.solarsystems.planets.mars;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;

public class ChunkGeneratorMars implements IChunkGenerator
{
    private Random                seed;
    private World                 world;

    private double[]              heightMap;
    private double[]              depthBuffer;
    private Biome[]               biomes;
    private float[]               biomeWeights;

    private NoiseGeneratorOctaves surfaceNoise;
    private NoiseGeneratorOctaves minLimitPerlinNoise;
    private NoiseGeneratorOctaves maxLimitPerlinNoise;
    private NoiseGeneratorOctaves mainPerlinNoise;
    private NoiseGeneratorOctaves depthNoise;

    double[]                      mainRegion;
    double[]                      minRegion;
    double[]                      maxRegion;
    double[]                      depthRegion;

    public ChunkGeneratorMars(World world, long seed)
    {
        this.world = world;
        this.seed = new Random(seed);
        this.surfaceNoise = new NoiseGeneratorOctaves(this.seed, 4);
        this.minLimitPerlinNoise = new NoiseGeneratorOctaves(this.seed, 16);
        this.maxLimitPerlinNoise = new NoiseGeneratorOctaves(this.seed, 16);
        this.mainPerlinNoise = new NoiseGeneratorOctaves(this.seed, 8);
        this.depthNoise = new NoiseGeneratorOctaves(this.seed, 16);
        this.depthBuffer = new double[256];
        this.biomeWeights = new float[25];

        for (int x = -2; x <= 2; ++x)
        {
            for (int z = -2; z <= 2; ++z)
            {
                float f = 10.0F / MathHelper.sqrt((float) (x * x + z * z) + 0.2F);
                this.biomeWeights[x + 2 + (z + 2) * 5] = f;
            }
        }
    }

    private void generateHeightmap(int offsetX, int offsetY, int offsetZ, int xSize, int ySize, int zSize)
    {
        if (this.heightMap == null)
        {
            this.heightMap = new double[xSize * ySize * zSize];
        }

        float depthNoiseScaleExponent = 0.5F;
        float depthNoiseScaleX = 200.0F;
        float depthNoiseScaleZ = 200.0F;

        float mainNoiseScaleX = 80.0F;
        float mainNoiseScaleY = 160.0F;
        float mainNoiseScaleZ = 80.0F;

        this.depthRegion = this.depthNoise.generateNoiseOctaves(this.depthRegion, offsetX, offsetZ, xSize, zSize, depthNoiseScaleX, depthNoiseScaleZ, depthNoiseScaleExponent);
        double coordinateScale = 684.41200000000003D;
        double heightScale = 684.41200000000003D;
        this.mainRegion = this.mainPerlinNoise.generateNoiseOctaves(this.mainRegion, offsetX, offsetY, offsetZ, xSize, ySize, zSize, (double) (coordinateScale / mainNoiseScaleX), (double) (heightScale / mainNoiseScaleY), (double) (coordinateScale / mainNoiseScaleZ));
        this.minRegion = this.minLimitPerlinNoise.generateNoiseOctaves(this.minRegion, offsetX, offsetY, offsetZ, xSize, ySize, zSize, (double) coordinateScale, (double) heightScale, (double) coordinateScale);
        this.maxRegion = this.maxLimitPerlinNoise.generateNoiseOctaves(this.maxRegion, offsetX, offsetY, offsetZ, xSize, ySize, zSize, (double) coordinateScale, (double) heightScale, (double) coordinateScale);
        int index = 0;
        int depthIndex = 0;

        for (int x1 = 0; x1 < 5; ++x1)
        {
            for (int z1 = 0; z1 < 5; ++z1)
            {
                float heightVariation = 0.0F;
                float baseHeight = 0.0F;
                float weight = 0.0F;
                Biome biome1 = this.biomes[x1 + 2 + (z1 + 2) * 10];

                for (int x2 = -2; x2 <= 2; ++x2)
                {
                    for (int z2 = -2; z2 <= 2; ++z2)
                    {
                        Biome biome2 = this.biomes[x1 + x2 + 2 + (z1 + z2 + 2) * 10];
                        float biomeWeight = this.biomeWeights[x2 + 2 + (z2 + 2) * 5] / (biome2.getBaseHeight() + 2.0F);

                        if (biome2.getBaseHeight() > biome1.getBaseHeight())
                        {
                            biomeWeight /= 2.0F;
                        }

                        heightVariation += biome2.getHeightVariation() * biomeWeight;
                        baseHeight += biome2.getBaseHeight() * biomeWeight;
                        weight += biomeWeight;
                    }
                }

                heightVariation = heightVariation / weight;
                baseHeight = baseHeight / weight;
                heightVariation = heightVariation * 0.9F + 0.1F;
                baseHeight = (baseHeight * 4.0F - 1.0F) / 8.0F;
                double depth = this.depthRegion[depthIndex] / 8000.0D;

                if (depth < 0.0D)
                {
                    depth = -depth * 0.3D;
                }

                depth = depth * 3.0D - 2.0D;

                if (depth < 0.0D)
                {
                    depth = depth / 2.0D;

                    if (depth < -1.0D)
                    {
                        depth = -1.0D;
                    }

                    depth = depth / 1.4D;
                    depth = depth / 2.0D;
                }
                else
                {
                    if (depth > 1.0D)
                    {
                        depth = 1.0D;
                    }

                    depth = depth / 8.0D;
                }

                ++depthIndex;

                float baseSize = 8.5F;
                double d8 = (double) baseHeight;
                double d9 = (double) heightVariation;
                d8 = d8 + depth * 0.2D;
                d8 = d8 * (double) baseSize / 8.0D;
                double d0 = (double) baseSize + d8 * 4.0D;

                double stretchY = 12;
                double lowerLimitScale = 512;
                double upperLimitScale = 512;

                for (int yLevel = 0; yLevel < 33; ++yLevel)
                {
                    double d1 = ((double) yLevel - d0) * stretchY * 128.0D / 256.0D / d9;

                    if (d1 < 0.0D)
                    {
                        d1 *= 4.0D;
                    }

                    double d2 = this.minRegion[index] / lowerLimitScale;
                    double d3 = this.maxRegion[index] / upperLimitScale;
                    double d4 = (this.mainRegion[index] / 10.0D + 1.0D) / 2.0D;
                    double d5 = MathHelper.clamp(d2, d3, d4) - d1;

                    if (yLevel > 29)
                    {
                        double d6 = (double) ((float) (yLevel - 29) / 3.0F);
                        d5 = d5 * (1.0D - d6) + -10.0D * d6;
                    }

                    this.heightMap[index] = d5;
                    ++index;
                }
            }
        }
    }

    public void setBlocksInChunk(int x, int z, ChunkPrimer primer)
    {
        this.biomes = this.world.getBiomeProvider().getBiomesForGeneration(this.biomes, x * 4 - 2, z * 4 - 2, 10, 10);
        this.generateHeightmap(x * 4, 0, z * 4, 5, 33, 5);

        for (int i = 0; i < 4; ++i)
        {
            int j = i * 5;
            int k = (i + 1) * 5;

            for (int l = 0; l < 4; ++l)
            {
                int i1 = (j + l) * 33;
                int j1 = (j + l + 1) * 33;
                int k1 = (k + l) * 33;
                int l1 = (k + l + 1) * 33;

                for (int i2 = 0; i2 < 32; ++i2)
                {
                    double d1 = this.heightMap[i1 + i2];
                    double d2 = this.heightMap[j1 + i2];
                    double d3 = this.heightMap[k1 + i2];
                    double d4 = this.heightMap[l1 + i2];
                    double d5 = (this.heightMap[i1 + i2 + 1] - d1) * 0.125D;
                    double d6 = (this.heightMap[j1 + i2 + 1] - d2) * 0.125D;
                    double d7 = (this.heightMap[k1 + i2 + 1] - d3) * 0.125D;
                    double d8 = (this.heightMap[l1 + i2 + 1] - d4) * 0.125D;

                    for (int j2 = 0; j2 < 8; ++j2)
                    {
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * 0.25D;
                        double d13 = (d4 - d2) * 0.25D;

                        for (int k2 = 0; k2 < 4; ++k2)
                        {
                            double d16 = (d11 - d10) * 0.25D;
                            double lvt_45_1_ = d10 - d16;

                            for (int l2 = 0; l2 < 4; ++l2)
                            {
                                if ((lvt_45_1_ += d16) > 0.0D)
                                {
                                    primer.setBlockState(i * 4 + k2, i2 * 8 + j2, l * 4 + l2, Blocks.RED_SANDSTONE.getDefaultState());
                                }
                                else if (i2 * 8 + j2 < 60)
                                {
                                    primer.setBlockState(i * 4 + k2, i2 * 8 + j2, l * 4 + l2, Blocks.AIR.getDefaultState());
                                }
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }

    public void replaceBiomeBlocks(int x, int z, ChunkPrimer primer, Biome[] biomesIn)
    {
        byte var5 = 63;
        double var6 = 0.03125D;
        this.depthBuffer = this.surfaceNoise.generateNoiseOctaves(this.depthBuffer, x * 16, z * 16, 0, 16, 16, 1, var6 * 2.0D, var6 * 2.0D, var6 * 2.0D);

        for (int blockX = 0; blockX < 16; blockX++)
        {
            for (int blockZ = 0; blockZ < 16; blockZ++)
            {
                Biome biome = biomesIn[(blockZ + blockX * 16)];
                int var12 = (int) (this.depthBuffer[(blockX + blockZ * 16)] / 3.0D + 3.0D + this.seed.nextDouble() * 0.25D);
                int var13 = -1;
                IBlockState top = biome.topBlock;
                IBlockState filler = biome.fillerBlock;

                for (int blockY = 127; blockY >= 0; blockY--)
                {
                    if (blockY <= 0 + this.seed.nextInt(5))
                    {
                        primer.setBlockState(blockX, blockY, blockZ, Blocks.BEDROCK.getDefaultState());
                    }
                    else
                    {
                        Block block = primer.getBlockState(blockX, blockY, blockZ).getBlock();

                        if (block == Blocks.AIR)
                        {
                            var13 = -1;
                        }
                        else
                        {
                            if (block == Blocks.STONE)
                                continue;
                            if (var13 == -1)
                            {
                                if (var12 <= 0)
                                {
                                    top = Blocks.AIR.getDefaultState();
                                    filler = Blocks.RED_SANDSTONE.getDefaultState();
                                }
                                else if ((blockY >= var5 - 4) && (blockY <= var5 + 1))
                                {
                                    top = biome.topBlock;
                                    filler = biome.fillerBlock;
                                }

                                if ((blockY < var5) && (top == Blocks.AIR))
                                {
                                    top = Blocks.WATER.getDefaultState();
                                }

                                var13 = var12;

                                if (blockY >= var5 - 1)
                                {
                                    primer.setBlockState(blockX, blockY, blockZ, top);
                                }
                                else
                                {
                                    primer.setBlockState(blockX, blockY, blockZ, filler);
                                }
                            }
                            else
                            {
                                if (var13 <= 0)
                                    continue;
                                var13--;
                                primer.setBlockState(blockX, blockY, blockZ, filler);

                                if ((var13 != 0) || (filler != Blocks.SAND))
                                    continue;
                                var13 = this.seed.nextInt(4);
                                filler = Blocks.SANDSTONE.getDefaultState();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Chunk generateChunk(int chunkX, int chunkZ)
    {
        this.seed.setSeed(chunkX * 341873128712L + chunkZ * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.setBlocksInChunk(chunkX, chunkZ, chunkprimer);
        this.biomes = this.world.getBiomeProvider().getBiomesForGeneration(this.biomes, chunkX * 16, chunkZ * 16, 16, 16);
        this.replaceBiomeBlocks(chunkX, chunkZ, chunkprimer, this.biomes);

        Chunk chunk = new Chunk(this.world, chunkprimer, chunkX, chunkZ);
        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public void populate(int chunkX, int chunkZ)
    {
        BlockSand.fallInstantly = true;
        int posX = chunkX * 16;
        int posZ = chunkZ * 16;
        Biome biome = this.world.getBiomeForCoordsBody(new BlockPos(posX + 16, 0, posZ + 16));
        this.seed.setSeed(this.world.getSeed());
        this.seed.setSeed(chunkX * (this.seed.nextLong() / 2L * 2L + 1L) + chunkZ * (this.seed.nextLong() / 2L * 2L + 1L) ^ this.world.getSeed());

        biome.decorate(this.world, this.seed, new BlockPos(chunkX, 0, chunkZ));
        // WorldEntitySpawner.performWorldGenSpawning(this.world, biome, posX + 8, posZ + 8, 16, 16,
        // this.randomSeed);

        BlockSand.fallInstantly = false;
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
