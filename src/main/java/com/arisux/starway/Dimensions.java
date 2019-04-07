package com.arisux.starway;

import java.util.ArrayList;

import com.arisux.starway.api.IPlanet;
import com.arisux.starway.api.Planet;
import com.arisux.starway.dimensions.DimensionSpace;
import com.asx.mdx.core.mods.IInitEvent;
import com.asx.mdx.lib.util.Chat;
import com.asx.mdx.lib.util.Game;
import com.asx.mdx.lib.world.Dimension;
import com.asx.mdx.lib.world.Pos;
import com.asx.mdx.lib.world.entity.Entities;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Dimensions implements IInitEvent
{
    public static final Dimensions instance          = new Dimensions();
    private ArrayList<Dimension>   dimensionRegistry = new ArrayList<Dimension>();
    private boolean                initialized;

    public ArrayList<Dimension> getDimensionRegistry()
    {
        return Starway.dimensions().dimensionRegistry;
    }

    public void register(Dimension dimension)
    {
        if (dimension != null)
        {
            if (!getDimensionRegistry().contains(dimension))
            {
                if (dimension.getId() == 0 && dimension.shouldRegisterWithForge())
                {
                    dimension.findNextAvailableID();
                }

                IPlanet planet = Starway.getPlanetFor(dimension);

                getDimensionRegistry().add(dimension);
                System.out.println("[STARWAY] Added dimension " + dimension.getName() + " with id " + (dimension.getId()));

                if (dimension.shouldRegisterWithForge())
                {
                    if (!DimensionManager.isDimensionRegistered(dimension.getId()))
                    {
                        if (planet != null)
                        {
                            dimension.register();
                            System.out.println("[FORGE] Registered dimension " + dimension.getName() + " with id " + (dimension.getId()) + " for planet " + planet.getName());
                        }
                        else
                        {
                            dimension.register();
                            System.out.println("[FORGE] Registered dimension " + dimension.getName() + " with id " + (dimension.getId()));
                        }
                    }
                    else
                    {
                        System.out.println("[ERROR] FORGE_DIMENSION_ALREADY_REGISTERED: " + dimension.getClass());
                    }
                }
            }
            else
            {
                System.out.println("[ERROR] STARWAY_DIMENSION_ALREADY_REGISTERED: " + dimension.getClass());
            }
        }
        else
        {
            System.out.println("[ERROR] NULL_DIMENSION");
        }
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
        register(DimensionSpace.instance);

        for (Planet planet : Starway.getPlanets())
        {
            if (planet != null && planet.getDimension() != null)
            {
                register(planet.getDimension());
            }
        }
    }

    public boolean hasInitialized()
    {
        return this.initialized;
    }

    @SideOnly(Side.SERVER)
    @SubscribeEvent
    public void onServerTick(ServerTickEvent event)
    {
        if (FMLCommonHandler.instance() != null && FMLCommonHandler.instance().getMinecraftServerInstance() != null && !this.hasInitialized())
        {
            for (Dimension dimension : this.dimensionRegistry)
            {
                if (dimension.shouldAutoLoad())
                {
                    tryLoadDimension(dimension);
                }
            }

            this.initialized = true;
        }
    }

    public WorldServer getWorldServerFor(Dimension dimension)
    {
        return getWorldServerFor(dimension.getId());
    }

    public WorldServer getWorldServerFor(int dimensionId)
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimensionId);
    }

    public Dimension getDimensionFor(int dimensionId)
    {
        Dimension dimension = null;

        for (Dimension d : this.dimensionRegistry)
        {
            if (d != null && d.getId() == dimensionId)
            {
                dimension = d;
            }
        }

        return dimension;
    }

    public void transferPlayerToDimension(EntityPlayerMP player, Dimension dimension)
    {
        World world = this.getWorldServerFor(dimension);

        if (world != null)
        {
            Pos safePos = Entities.getSafePositionAboveBelow(new Pos(player.posX, 100, player.posZ), world);

            this.transferPlayerToDimension(player, dimension, safePos);
        }
        else
        {
            System.out.println("WARNING. UNABLE TO OBTAIN DIMENSION WORLD.");
        }
    }

    public void transferPlayerToDimension(EntityPlayerMP player, int dimensionId, Pos coord)
    {
        transferPlayerToDimension(player, this.getDimensionFor(dimensionId), coord);
    }

    public void transferPlayerToDimension(EntityPlayerMP player, Dimension dimension, Pos coord)
    {
        WorldServer worldServer = player.getServerWorld();
        Teleporter teleporter = dimension.getTeleporter(worldServer);

        if (teleporter != null)
        {
            player.changeDimension(dimension.getId(), teleporter);

            if (coord == null)
            {
                player.setLocationAndAngles(worldServer.getSpawnPoint().getX(), worldServer.getSpawnPoint().getY(), worldServer.getSpawnPoint().getZ(), player.rotationYaw, player.rotationPitch);
            }
            else
            {
                player.setLocationAndAngles(coord.x, coord.y, coord.z, player.rotationYaw, player.rotationPitch);
            }
        }
        else
        {
            player.sendMessage(Chat.component("Dimension teleporter is null."));
        }
    }

    public void transferPlayerToDimension(EntityPlayerMP player, int dimensionId)
    {
        Pos safePos = Entities.getSafePositionAboveBelow(new Pos(player.posX, 100, player.posZ), this.getWorldServerFor(dimensionId));
        this.transferPlayerToDimension(player, dimensionId, safePos);
    }

    public void tryLoadDimension(Dimension dimension)
    {
        MinecraftServer server = Game.server();
        WorldServer worldServer = server.getWorld(dimension.getId());

        if (worldServer != null && worldServer instanceof WorldServer)
        {
            server.logInfo("Preparing start region for dimension " + dimension.getName() + "(" + dimension.getId() + ")");
            initialChunkLoad(server, worldServer, dimension);
        }
    }

    public static void initialChunkLoad(MinecraftServer server, WorldServer worldServer, Dimension dimension)
    {
        long startTime = System.currentTimeMillis();
        int chunkLoadRadius = dimension.getInitialChunkLoadRadius();

        for (int chunkX = -chunkLoadRadius; (chunkX <= chunkLoadRadius) && server.isServerRunning(); chunkX += 16)
        {
            for (int chunkZ = -chunkLoadRadius; (chunkZ <= chunkLoadRadius) && server.isServerRunning(); chunkZ += 16)
            {
                long curTime = System.currentTimeMillis();

                if (curTime < startTime)
                {
                    startTime = curTime;
                }

                if (curTime > startTime + 1000L)
                {
                    startTime = curTime;
                }

                worldServer.getChunkProvider().loadChunk(worldServer.getSpawnPoint().getX() + chunkX >> 4, worldServer.getSpawnPoint().getZ() + chunkZ >> 4);
            }
        }
    }
}
