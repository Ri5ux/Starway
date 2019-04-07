package com.arisux.starway.galaxies;

import com.arisux.starway.Renderer;
import com.arisux.starway.api.Galaxy;
import com.arisux.starway.api.IGalaxy;
import com.arisux.starway.galaxies.milkyway.solarsystems.SolarSystemSol;
import com.arisux.starway.galaxies.milkyway.solarsystems.SolarSystemTest;
import com.asx.mdx.lib.client.util.Draw;
import com.asx.mdx.lib.client.util.OpenGL;

public class GalaxyMilkyWay extends Galaxy implements IGalaxy
{
    public static GalaxyMilkyWay instance = new GalaxyMilkyWay();

    public GalaxyMilkyWay()
    {
        this.solarSystems.add(SolarSystemSol.instance);
        this.solarSystems.add(SolarSystemTest.instance);
    }

    @Override
    public String getName()
    {
        return "The Milky Way";
    }

    /** Galaxies are too large, so we'll leave this at 0. **/
    @Override
    public float getObjectSize()
    {
        return 0;
    }

    /** Lets assume this galaxy doesn't orbit anything, and leave this at 0 **/
    @Override
    public float getOrbitTime()
    {
        return 0;
    }

    @Override
    public void drawObjectTag(Renderer renderer, float renderPartialTicks)
    {
        OpenGL.pushMatrix();
        {
            int blackholeSize = 100;
            Draw.drawCenteredRectWithOutline(0, 0, blackholeSize, blackholeSize, 2, 0xFF222222, 0xFF888888);
            Draw.drawStringAlignCenter(this.getName(), 0, -blackholeSize / 2 - 10 - renderer.getTextPadding() * 2, 0xFF00CCFF, false);
            Draw.drawStringAlignCenter(String.format("(X: %s, Z: %s)", this.pos().x, this.pos().z), 0, -blackholeSize / 2 - renderer.getTextPadding() * 2, 0xFFFFFFFF, false);
        }
        OpenGL.popMatrix();
    }
}
