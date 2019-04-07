package com.arisux.starway.galaxies.milkyway.solarsystems;

import com.arisux.starway.api.ISolarSystem;
import com.arisux.starway.api.SolarSystem;
import com.arisux.starway.galaxies.GalaxyMilkyWay;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class SolarSystemTest extends SolarSystem implements ISolarSystem
{
    public static SolarSystemTest instance = new SolarSystemTest();

    @Override
    public void onInitialization(FMLInitializationEvent event)
    {
        super.onInitialization(event);
        this.setObjectOrbiting(GalaxyMilkyWay.instance);
    }

    @Override
    public String getName()
    {
        return "The Test Solar System";
    }

    @Override
    public float getDistanceFromObjectOrbiting()
    {
        return 27000F * 2F;
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
        return 300000000F;
    }
    
    @Override
    protected void drawSun(float partialTicks)
    {
//        OpenGL.pushMatrix();
//        {
//            int planetSize = (int) (Renderer.instance.getPlanetDrawScale() * solarsystem.getObjectSize());
//            ModelSphere sphere = new ModelSphere();
//            Color color = new Color(0.2F, 0.7F, 1F, 0.15F);
//
//            OpenGL.enableBlend();
//            OpenGL.blendClear();
//            OpenGL.disableTexture2d();
////            OpenGL.disableDepthTest();
//            OpenGL.blendFunc(GL11.GL_SRC_ALPHA, 1);
//            
//            for (int i = 10; i > 0; i--)
//            {
//                OpenGL.pushMatrix();
//                OpenGL.rotate(Minecraft.getMinecraft().theWorld.getWorldTime() % 360 * i + Game.partialTicks(), 0, 1, 0);
//                sphere.drawInternalVertices = false;
//                sphere.setScale((planetSize / 100) + i * 3);
//                sphere.setColor(color);
//                sphere.render();
//                OpenGL.popMatrix();
//            }
//            
//            OpenGL.enableTexture2d();
//            OpenGL.disableBlend();
//        }
//        OpenGL.popMatrix();
    }
}
