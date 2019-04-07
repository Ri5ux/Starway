package com.arisux.starway.api;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.arisux.starway.ModelSphere;
import com.arisux.starway.Renderer;
import com.arisux.starway.SpaceManager;
import com.asx.mdx.lib.client.util.Color;
import com.asx.mdx.lib.client.util.Draw;
import com.asx.mdx.lib.client.util.OpenGL;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Save;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public abstract class SolarSystem extends OrbitableObject implements ISolarSystem
{
    protected ArrayList<Planet> planets = new ArrayList<Planet>();

    @Override
    public String getName()
    {
        return "Default Solar System";
    }

    @Override
    public void onInitialization(FMLInitializationEvent event)
    {
        super.onInitialization(event);

        for (Planet planet : this.getPlanets())
        {
            SpaceManager.instance.getObjectsInSpace().add(planet);
            planet.onInitialization(event);
        }
    }

    @Override
    public void onInitialTick(WorldTickEvent event)
    {
        super.onInitialTick(event);

        for (IPlanet planet : this.getPlanets())
        {
            planet.onInitialTick(event);
        }
    }

    @Override
    public void onTick(WorldTickEvent event)
    {
        super.onTick(event);

        for (Planet planet : this.getPlanets())
        {
            planet.onTick(event);
        }
    }
    
    @Override
    public void onGlobalTick(WorldTickEvent event)
    {
        super.onGlobalTick(event);

        for (Planet planet : this.getPlanets())
        {
            planet.onGlobalTick(event);
        }
    }

    @Override
    public void load(Load event)
    {
        super.load(event);

        for (IPlanet planet : this.getPlanets())
        {
            planet.load(event);
        }
    }

    @Override
    public void save(Save event)
    {
        super.save(event);

        for (IPlanet planet : this.getPlanets())
        {
            planet.save(event);
        }
    }

    @Override
    public void unload(Unload event)
    {
        super.unload(event);

        for (IPlanet planet : this.getPlanets())
        {
            planet.unload(event);
        }
    }

    @Override
    public ArrayList<Planet> getPlanets()
    {
        return this.planets;
    }

    @Override
    public float getDistanceFromObjectOrbiting()
    {
        return 27000F;
    }

    @Override
    public void drawObjectTag(Renderer renderer, float renderPartialTicks)
    {
        float sunScale = 0.00005F;
        int sunSize = (int) (sunScale * this.getObjectSize());
        int sunDiameter = sunSize / 2;

        OpenGL.pushMatrix();
        {
            Draw.drawCenteredRectWithOutline(0, 0, sunSize, sunSize, 0, 0xFFFFAA00, 0xFF444444);
            Draw.drawStringAlignCenter("Sun", 0, sunDiameter + renderer.getTextPadding(), 0xFF00CCFF, false);
        }
        OpenGL.popMatrix();

        OpenGL.pushMatrix();
        {
            Draw.drawCenteredRectWithOutline(0, 0, 1, 1, 0, 0xFF00AAFF, 0x000000);
            Draw.drawStringAlignCenter(this.getName(), 0, 0 - 20 - renderer.getTextPadding() * 2, 0xFF00CCFF, false);
            Draw.drawStringAlignCenter(String.format("(X: %s, Z: %s)", this.pos().x, this.pos().z), 0, 0 - 10 - renderer.getTextPadding() * 2, 0xFFFFFFFF, false);
        }
        OpenGL.popMatrix();
    }

    @Override
    public void renderMap(Renderer renderer, OrbitableObject parentObject, float renderPartialTicks)
    {
        int distance = (int) (SpaceManager.instance.getSolarSystemScale() * this.getDistanceFromObjectOrbiting());

        OpenGL.pushMatrix();
        {
            if (parentObject != null)
            {
                OpenGL.translate(parentObject.pos().x, parentObject.pos().z, 0);
                SolarSystem.drawOrbitMarker(1F, distance, 0);
            }
        }
        OpenGL.popMatrix();

        OpenGL.pushMatrix();
        {
            OpenGL.translate(this.pos().x, this.pos().z, 0);
            this.drawObjectTag(renderer, renderPartialTicks);
        }
        OpenGL.popMatrix();

        for (IPlanet planet : this.getPlanets())
        {
            OpenGL.pushMatrix();
            {
                planet.renderMap(renderer, this, renderPartialTicks);
            }
            OpenGL.popMatrix();
        }
    }

    @Override
    public void render(float partialTicks)
    {
        OpenGL.translate(-pos().x, -pos().y, -pos().z);

        for (IPlanet planet : getPlanets())
        {
            OpenGL.pushMatrix();
            {
                OpenGL.translate(planet.pos().x, planet.pos().y, planet.pos().z);
                planet.render(partialTicks);
            }
            OpenGL.popMatrix();
        }

        OpenGL.translate(pos().x, pos().y, pos().z);

        this.drawSun(partialTicks);
    }
    
    protected void drawSun(float partialTicks)
    {
        OpenGL.pushMatrix();
        {
            int planetSize = (int) (getObjectSize() * SpaceManager.instance.getStarScale());
            ModelSphere sphere = new ModelSphere();
            Color color = new Color(1F, 0.7F, 0F, 0.3F);

            OpenGL.enableBlend();
            OpenGL.blendClear();
            OpenGL.disableTexture2d();
            OpenGL.enableLighting();
            OpenGL.enableDepthTest();
            OpenGL.blendFunc(GL11.GL_SRC_ALPHA, 1);
            
            for (int i = 10; i > 0; i--)//10
            {
                OpenGL.pushMatrix();
                OpenGL.rotate(Minecraft.getMinecraft().world.getWorldTime() % 360 * i + partialTicks, 0, 1, 0);
                sphere.cull = false;
                sphere.setScale((planetSize) + i * 3);
                sphere.setColor(color);
                sphere.render();
                OpenGL.popMatrix();
            }
            
            OpenGL.enableTexture2d();
            OpenGL.disableBlend();
        }
        OpenGL.popMatrix();
    }
}
