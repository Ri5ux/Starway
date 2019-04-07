package com.arisux.starway.dimensions;

import com.arisux.starway.galaxies.milkyway.solarsystems.planets.mars.MarsProvider;
import com.asx.mdx.lib.world.Dimension;

public class DimensionMars extends Dimension
{
    public DimensionMars()
    {
        super("Mars", "MARS", MarsProvider.class, true);
    }
}
