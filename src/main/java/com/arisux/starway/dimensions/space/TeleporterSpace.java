package com.arisux.starway.dimensions.space;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterSpace extends Teleporter
{
    public TeleporterSpace(WorldServer worldServer)
    {
        super(worldServer);
    }
    
    @Override
    public void placeInPortal(Entity entityIn, float rotationYaw)
    {
        ;
    }
    
    @Override
    public boolean placeInExistingPortal(Entity entityIn, float rotationYaw)
    {
        return false;
    }

    @Override
    public boolean makePortal(Entity entity)
    {
        return true;
    }
}
