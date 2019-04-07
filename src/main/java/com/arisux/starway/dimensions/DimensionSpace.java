package com.arisux.starway.dimensions;

import com.arisux.starway.dimensions.space.SpaceProvider;
import com.arisux.starway.dimensions.space.TeleporterSpace;
import com.asx.mdx.lib.world.Dimension;

import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class DimensionSpace extends Dimension
{
    public static DimensionSpace instance = new DimensionSpace();

    public DimensionSpace()
    {
        super("Space", "SPACE", SpaceProvider.class, true);
    }
    
    @Override
    public Teleporter getTeleporter(WorldServer worldServer)
    {
        return new TeleporterSpace(worldServer);
    }
    
    @Override
    public boolean shouldRegisterWithForge()
    {
        return true;
    }
    
    @Override
    public int getInitialChunkLoadRadius()
    {
        return 1;
    }

    @Override
    public boolean shouldAutoLoad()
    {
        return true;
    }
}
