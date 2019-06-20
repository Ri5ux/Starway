package com.arisux.starway;

import java.util.ArrayList;

import com.arisux.starway.api.Galaxy;
import com.arisux.starway.api.IPlanet;
import com.arisux.starway.api.OrbitableObject;
import com.arisux.starway.api.Planet;
import com.arisux.starway.api.SolarSystem;
import com.arisux.starway.starships.EntityStarship;
import com.arisux.starway.starships.ItemStarship;
import com.arisux.starway.starships.RenderStarship;
import com.asx.mdx.core.mods.IInitEvent;
import com.asx.mdx.core.mods.IMod;
import com.asx.mdx.core.mods.IPostInitEvent;
import com.asx.mdx.core.mods.IPreInitEvent;
import com.asx.mdx.lib.util.Game;
import com.asx.mdx.lib.world.Dimension;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(name = "Starway", modid = Starway.Properties.ID, version = Starway.Properties.VERSION)
public class Starway implements IMod, IPreInitEvent, IPostInitEvent, IInitEvent
{
    public static class Properties
    {
        public static final String ID      = "starway";
        public static final String VERSION = "1.0";
    }

    @Mod.Instance(Starway.Properties.ID)
    private static Starway     instance;
    private static ItemHandler items;
    private ModContainer       container;

    @Override
    @EventHandler
    public void pre(FMLPreInitializationEvent event)
    {
        if (event.getSide() == Side.CLIENT)
        {
            resources().pre(event);
        }
    }

    @Override
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        System.out.println("TEST1: " + resources().ARMOR_SPACESUIT_MK50_WHITE.toString());
        galaxies().init(event);
        dimensions().init(event);
        commands().init(event);
        spaceManager().init(event);

        this.loadDebugTools(event);

        if (event.getSide() == Side.CLIENT)
        {
            renderer().init(event);
        }

        this.registerEventHandler(galaxies());
        this.registerEventHandler(spaceManager());
    }

    private Item itemStarship = new ItemStarship();

    public void loadDebugTools(FMLInitializationEvent event)
    {
        // Game.register(Starway.ID, itemStarship, "spawner.starship");
        //
        // int entityId = 0;
        // Game.register(EntityStarship.class, "Starship", entityId++, Starway.instance, 250, 1, true);

        if (event.getSide() == Side.CLIENT)
        {
            RenderingRegistry.registerEntityRenderingHandler(EntityStarship.class, new RenderStarship());
        }
    }

    @Override
    @EventHandler
    public void post(FMLPostInitializationEvent event)
    {
        System.out.println(String.format("[STARWAY] Registered %s galaxy(s), %s solar system(s), and %s planet(s).", getGalaxies().size(), getSolarSystems().size(), getPlanets().size()));
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event)
    {
        commands().onServerStarting(event);
    }

    public static ItemHandler items()
    {
        return items == null ? items = new ItemHandler() : items;
    }

    @SideOnly(Side.CLIENT)
    public static Resources resources()
    {
        return Resources.instance;
    }

    @Override
    public ModContainer container()
    {
        return this.container == null ? this.container = Game.getModContainer(Starway.Properties.ID) : this.container;
    }

    public CreativeTabs tab()
    {
        return null;
    }

    public String domain()
    {
        return Starway.Properties.ID + ":";
    }

    @SideOnly(Side.CLIENT)
    public static Renderer renderer()
    {
        return Renderer.instance;
    }

    public static SpaceManager spaceManager()
    {
        return SpaceManager.instance;
    }

    public static Commands commands()
    {
        return Commands.instance;
    }

    public static Dimensions dimensions()
    {
        return Dimensions.instance;
    }

    public static Galaxies galaxies()
    {
        return Galaxies.instance;
    }

    private void registerEventHandler(Object eventHandler)
    {
        MinecraftForge.EVENT_BUS.register(eventHandler);
    }

    /**
     * @return An ArrayList containing instances of all registered galaxies.
     */
    public static ArrayList<Galaxy> getGalaxies()
    {
        ArrayList<Galaxy> galaxies = new ArrayList<Galaxy>();

        if (galaxies.isEmpty())
        {
            for (OrbitableObject object : SpaceManager.instance.getObjectsInSpace())
            {
                if (object instanceof Galaxy)
                {
                    galaxies.add((Galaxy) object);
                }
            }
        }

        return galaxies;
    }

    /**
     * @return An ArrayList containing instances of all solar systems from all registered galaxies.
     */
    public static ArrayList<SolarSystem> getSolarSystems()
    {
        ArrayList<SolarSystem> solarsystems = new ArrayList<SolarSystem>();

        if (solarsystems.isEmpty())
        {
            for (OrbitableObject object : SpaceManager.instance.getObjectsInSpace())
            {
                if (object instanceof SolarSystem)
                {
                    solarsystems.add((SolarSystem) object);
                }
            }
        }

        return solarsystems;
    }

    /**
     * @return An ArrayList containing instances of all planets from all registered solar systems in all registered galaxies.
     */
    public static ArrayList<Planet> getPlanets()
    {
        ArrayList<Planet> planets = new ArrayList<Planet>();

        if (planets.isEmpty())
        {
            for (OrbitableObject object : SpaceManager.instance.getObjectsInSpace())
            {
                if (object instanceof Planet)
                {
                    planets.add((Planet) object);
                }
            }
        }

        return planets;
    }

    public static IPlanet getPlanetFor(Dimension dimension)
    {
        IPlanet planet = null;

        for (IPlanet p : getPlanets())
        {
            if (p.getDimension() == dimension)
            {
                planet = p;
            }
        }

        return planet;
    }
}
