package com.arisux.starway;

import java.util.ArrayList;

import com.arisux.starway.api.IPlanet;
import com.arisux.starway.api.IPlanetWorldProvider;
import com.arisux.starway.api.ISpaceSuit;
import com.arisux.starway.api.OrbitableObject;
import com.arisux.starway.dimensions.DimensionSpace;
import com.arisux.starway.dimensions.space.SpaceProvider;
import com.asx.mdx.core.mods.IInitEvent;
import com.asx.mdx.lib.util.Game;
import com.asx.mdx.lib.world.Dimension;
import com.asx.mdx.lib.world.Pos;
import com.asx.mdx.lib.world.Worlds;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

//TODO: EntityRenderer.setupCameraTransform() - Need to overwrite render distance for space. We can handle rendering a giant empty abyss
public class SpaceManager implements IInitEvent
{
    public static final SpaceManager   instance              = new SpaceManager();
    private ArrayList<OrbitableObject> objectsInSpace        = new ArrayList<OrbitableObject>();

    public static final double         DEFAULT_GRAVITY_LEVEL = 0.08D;
    public static final float          FLY_SPEED_SPACE       = 2.0F;
    public static final float          FLY_SPEED_NORMAL      = 0.05F;

    /** TODO: Currently starts at 0 each world load, should be stored and loaded back each time a world loads **/
    private long                       spacetime;

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
    public void onEntityTravelDimension(EntityTravelToDimensionEvent event)
    {
        /** When we are leaving space **/
        if (event.getEntity().dimension == DimensionSpace.instance.getId())
        {
            if (event.getEntity() instanceof EntityPlayerMP)
            {
                EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
                WorldServer newWorld = player.getServer().getWorld(event.getDimension());
                player.capabilities.setFlySpeed(FLY_SPEED_NORMAL);
                player.capabilities.isFlying = false;
                player.setPosition(player.getPosition().getX(), newWorld.getHeight(), player.getPosition().getZ());
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event)
    {
        if (event.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntity();

            if (player.world != null && player.world.provider != null && player.world.provider instanceof SpaceProvider)
            {
                player.capabilities.isFlying = true;
                

                if (!player.capabilities.isCreativeMode && !ISpaceSuit.isFullSetEquipped(player))
                {
                    player.capabilities.setFlySpeed(0F);
                }
                else
                {
                    player.capabilities.setFlySpeed(4F);
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
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
                    if (entity.world.provider instanceof SpaceProvider)
                    {
                        entity.setNoGravity(true);
                        // Game.minecraft().entityRenderer.farPlaneDistance = (float)(Game.minecraft().gameSettings.renderDistanceChunks * 16);
                        // Game.minecraft().gameSettings.renderDistanceChunks = 2 * 16;
                    }
                    else
                    {
                        entity.setNoGravity(false);
                    }

                    if (entity instanceof EntityPlayer)
                    {
                        EntityPlayer player = (EntityPlayer) entity;

                        if (entity.world.provider instanceof SpaceProvider)
                        {
                            // float multiplier = 100F;
                            // player.motionX = player.motionX * multiplier;
                            // player.motionY = player.motionY * multiplier;
                            // player.motionZ = player.motionZ * multiplier;
                            // player.setDead();
                            // System.out.println("test");
                        }

                        // player.worldObj.setBlock((int)player.posX, (int)player.posY, (int)player.posZ, Blocks.diamond_block);

                        if (!(player.world.provider instanceof WorldProviderSurface) && !(player.world.provider instanceof SpaceProvider))
                        {
                            Dimension planetDimension = Starway.dimensions().getDimensionFor(player.dimension);

                            if (planetDimension != null)
                            {
                                IPlanet planet = Starway.getPlanetFor(planetDimension);

                                if (planet != null)
                                {
                                    EntityPlayerMP playerMP = (EntityPlayerMP) player;
                                    Starway.dimensions().transferPlayerToDimension(playerMP, DimensionSpace.instance, new Pos(player));
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
                    }
                }
            }
        }
    }

    public static double getGravityLevel(Entity entity)
    {
        if (entity.world.provider instanceof IPlanetWorldProvider)
        {
            IPlanetWorldProvider planetWorldProvider = (IPlanetWorldProvider) entity.world.provider;

            return DEFAULT_GRAVITY_LEVEL - planetWorldProvider.getGravityLevel();
        }
        else
        {
            return DEFAULT_GRAVITY_LEVEL;
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
        return 0.006F;// 0.006F
    }

    public float getPlanetaryDistScale()
    {
        return 100000000F;// 10000000F
    }

    public float getStarScale()
    {
        return 0.0006F;
    }

    // public float getSolarSystemScale()
    // {
    // return 0.01F;//Applies only to orbit markers?
    // }

    public float getSolarSystemDistScale()
    {
        return 1F;
    }
}
