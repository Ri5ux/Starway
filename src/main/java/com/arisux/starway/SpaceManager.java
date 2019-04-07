package com.arisux.starway;

import java.util.ArrayList;

import com.arisux.starway.api.IPlanet;
import com.arisux.starway.api.OrbitableObject;
import com.arisux.starway.dimensions.DimensionSpace;
import com.arisux.starway.dimensions.space.SpaceProvider;
import com.asx.mdx.core.mods.IInitEvent;
import com.asx.mdx.lib.world.Dimension;
import com.asx.mdx.lib.world.Pos;
import com.asx.mdx.lib.world.Worlds;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class SpaceManager implements IInitEvent
{
    public static final SpaceManager instance = new SpaceManager();
    private ArrayList<OrbitableObject> objectsInSpace = new ArrayList<OrbitableObject>();
    
    /** TODO: Currently starts at 0 each world load, should be stored and loaded back each time a world loads **/
    private long spacetime;
    
    public ArrayList<OrbitableObject> getObjectsInSpace()
    {
        return objectsInSpace;
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        ;
    }

    @SubscribeEvent
    public void onWorldTick(WorldTickEvent event)
    {
        this.spacetime++;
        
        Object[] entities = event.world.loadedEntityList.toArray();

        for (Object o : entities)
        {
            if (o instanceof Entity)
            {
                Entity entity = (Entity) o;

                if (event.world != null)
                {
                    if (entity instanceof EntityPlayer)
                    {
                        EntityPlayer player = (EntityPlayer) entity;
                        
//                        player.worldObj.setBlock((int)player.posX, (int)player.posY, (int)player.posZ, Blocks.diamond_block);

                        if (!(player.world.provider instanceof WorldProviderSurface) && !(player.world.provider instanceof SpaceProvider))
                        {
                            Dimension planetDimension = Starway.dimensions().getDimensionFor(player.dimension);

                            if (planetDimension != null)
                            {
                                IPlanet planet = Starway.getPlanetFor(planetDimension);

                                if (planet != null)
                                {
                                    EntityPlayerMP playerMP = (EntityPlayerMP) player;
                                    //Starway.dimensions().transferPlayerToDimension(playerMP, DimensionSpace.instance, new Pos(player));
                                }
                            }
                        }
                        else if (player.world.provider instanceof WorldProviderSurface)
                        {
                            if (player instanceof EntityPlayerMP && player.posY > event.world.getHeight() * 1.5F && Worlds.canSeeSky(new Pos(entity), event.world))
                            {
                                EntityPlayerMP playerMP = (EntityPlayerMP) player;
                                Starway.dimensions().transferPlayerToDimension(playerMP, DimensionSpace.instance, new Pos(player));
                            }
                        }

                        if (player.posY <= 1 && entity.world.provider instanceof SpaceProvider)
                        {
                            player.setPositionAndUpdate(player.posX, 512, player.posZ);
                        }
                    }
                }
            }
        }
    }

    public long getTime()
    {
        return spacetime;
    }

    public long getTimeScale()
    {
        return 100000;
    }
    
    public float getPlanetaryScale()
    {
        return 0.006F;//0.006F
    }
    
    public float getPlanetaryDistScale()
    {
        return 100000000F;//10000000F
    }
    
    public float getStarScale()
    {
        return 0.00006F;
    }
    
    public float getSolarSystemScale()
    {
        return 0.01F;
    }
    
    public float getSolarSystemDistScale()
    {
        return 0.01F;
    }
}
