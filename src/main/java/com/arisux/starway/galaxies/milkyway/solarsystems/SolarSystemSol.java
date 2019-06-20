package com.arisux.starway.galaxies.milkyway.solarsystems;

import com.arisux.starway.Renderer;
import com.arisux.starway.api.ISolarSystem;
import com.arisux.starway.api.OrbitableObject;
import com.arisux.starway.api.SolarSystem;
import com.arisux.starway.galaxies.GalaxyMilkyWay;
import com.arisux.starway.galaxies.milkyway.solarsystems.planets.PlanetEarth;
import com.arisux.starway.galaxies.milkyway.solarsystems.planets.PlanetJupiter;
import com.arisux.starway.galaxies.milkyway.solarsystems.planets.PlanetMars;
import com.arisux.starway.galaxies.milkyway.solarsystems.planets.PlanetMercury;
import com.arisux.starway.galaxies.milkyway.solarsystems.planets.PlanetNeptune;
import com.arisux.starway.galaxies.milkyway.solarsystems.planets.PlanetPluto;
import com.arisux.starway.galaxies.milkyway.solarsystems.planets.PlanetSaturn;
import com.arisux.starway.galaxies.milkyway.solarsystems.planets.PlanetUranus;
import com.arisux.starway.galaxies.milkyway.solarsystems.planets.PlanetVenus;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class SolarSystemSol extends SolarSystem implements ISolarSystem
{
    public static SolarSystemSol instance = new SolarSystemSol();

    public SolarSystemSol()
    {
        this.planets.add(PlanetMercury.instance);
        this.planets.add(PlanetVenus.instance);
        this.planets.add(PlanetEarth.instance);
        this.planets.add(PlanetMars.instance);
        this.planets.add(PlanetJupiter.instance);
        this.planets.add(PlanetSaturn.instance);
        this.planets.add(PlanetUranus.instance);
        this.planets.add(PlanetNeptune.instance);
        this.planets.add(PlanetPluto.instance);
    }

    @Override
    public void onInitialization(FMLInitializationEvent event)
    {
        super.onInitialization(event);
        this.setObjectOrbiting(GalaxyMilkyWay.instance);
    }

    @Override
    public String getName()
    {
        return "The Solar System";
    }

    /** Distance from the center of the galaxy in light years. **/
    @Override
    public float getDistanceFromObjectOrbiting()
    {
        return 26092F;
    }

    /** Since this is a solar system, this is the size of the sun in the solar system. **/
    @Override
    public float getObjectSize()
    {
        return 432687F;
    }

    @Override
    public float getOrbitTime()
    {
        return 230000000F;
    }

    @Override
    public void drawObjectTag(Renderer renderer, float renderPartialTicks)
    {
        super.drawObjectTag(renderer, renderPartialTicks);
    }

    @Override
    public void renderMap(Renderer renderer, OrbitableObject parentObject, float renderPartialTicks)
    {
        super.renderMap(renderer, parentObject, renderPartialTicks);
    }
}
