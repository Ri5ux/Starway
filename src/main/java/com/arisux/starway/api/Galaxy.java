package com.arisux.starway.api;

import java.util.ArrayList;

import com.arisux.starway.ModelSphere;
import com.arisux.starway.Renderer;
import com.arisux.starway.SpaceManager;
import com.asx.mdx.lib.client.util.Color;
import com.asx.mdx.lib.client.util.OpenGL;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Save;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public abstract class Galaxy extends OrbitableObject implements IGalaxy
{
    protected ArrayList<SolarSystem> solarSystems = new ArrayList<SolarSystem>();

    @Override
    public String getName()
    {
        return "Default Galaxy";
    }

    @Override
    public void onInitialization(FMLInitializationEvent event)
    {
        for (SolarSystem solarSystem : this.getSolarSystems())
        {
            SpaceManager.instance.getObjectsInSpace().add(solarSystem);
            solarSystem.onInitialization(event);
        }
    }

    @Override
    public void onInitialTick(WorldTickEvent event)
    {
        for (ISolarSystem solarSystem : this.getSolarSystems())
        {
            solarSystem.onInitialTick(event);
        }
    }

    @Override
    public void onTick(WorldTickEvent event)
    {
        super.onTick(event);
        
        for (SolarSystem solarSystem : this.getSolarSystems())
        {
            solarSystem.onTick(event);
        }
    }
    
    @Override
    public void onGlobalTick(WorldTickEvent event)
    {
        super.onGlobalTick(event);
        
        for (SolarSystem solarSystem : this.getSolarSystems())
        {
            solarSystem.onGlobalTick(event);
        }
    }

    @Override
    public void load(Load event)
    {
        for (ISolarSystem solarSystem : this.getSolarSystems())
        {
            solarSystem.load(event);
        }
    }

    @Override
    public void save(Save event)
    {
        for (ISolarSystem solarSystem : this.getSolarSystems())
        {
            solarSystem.save(event);
        }
    }

    @Override
    public void unload(Unload event)
    {
        for (ISolarSystem solarSystem : this.getSolarSystems())
        {
            solarSystem.unload(event);
        }
    }

    @Override
    public ArrayList<SolarSystem> getSolarSystems()
    {
        return this.solarSystems;
    }

    @Override
    public float getDistanceFromObjectOrbiting()
    {
        return 0;
    }

    @Override
    public void renderMap(Renderer renderer, OrbitableObject parentObject, float renderPartialTicks)
    {
        OpenGL.pushMatrix();
        {
            OpenGL.translate(this.pos().x, this.pos().z, 0);
            this.drawObjectTag(renderer, renderPartialTicks);
        }
        OpenGL.popMatrix();

        for (ISolarSystem solarSystem : this.getSolarSystems())
        {
            OpenGL.pushMatrix();
            {
                solarSystem.renderMap(renderer, this, renderPartialTicks);
            }
            OpenGL.popMatrix();
        }
    }
    
    public void render(float partialTicks)
    {
//        this.drawBlackHole(partialTicks);
        
        OpenGL.translate(-pos().x, -pos().y, -pos().z);

        for (SolarSystem solarsystem : getSolarSystems())
        {
            OpenGL.pushMatrix();
            {
                OpenGL.translate(solarsystem.pos().x, -solarsystem.pos().y, solarsystem.pos().z);
                solarsystem.render(partialTicks);
            }
            OpenGL.popMatrix();
        }
        OpenGL.blendClear();
    }

    ModelSphere sphere = new ModelSphere();
    
    public void drawBlackHole(float partialTicks)
    {
        OpenGL.pushMatrix();
        {
            int size = (int) (getObjectSize());
            Color color = new Color(0.0F, 0.0F, 0.0F, 0.6F);
            Color color2 = new Color(1F, 0.4F, 0F, 1F);
            Color color3 = new Color(1F, 1F, 1F, 0.12F);

            OpenGL.enableBlend();
            OpenGL.blendClear();
            OpenGL.disableTexture2d();

            OpenGL.pushMatrix();
            sphere.cull = false;
            sphere.setScale((size / 100) + (50 * 1) * 1);
            OpenGL.scale(1.1F, 1F, 1.1F);
            sphere.setColor(color3);
            sphere.render();
            OpenGL.popMatrix();

            for (int i = 5; i > 0; i--)
            {
                OpenGL.pushMatrix();
                OpenGL.rotate((Minecraft.getMinecraft().world.getWorldTime() % 360 * 3) + partialTicks, 1, 1, 1);
                sphere.cull = false;
                sphere.setScale((size / 100) + (50 + i) * 1);
                sphere.setColor(i == 5 ? color : color);
                sphere.render();
                OpenGL.popMatrix();
            }

            OpenGL.enableTexture2d();
            OpenGL.disableBlend();
        }
        OpenGL.popMatrix();
    }
}
