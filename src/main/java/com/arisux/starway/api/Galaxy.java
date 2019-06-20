package com.arisux.starway.api;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.arisux.starway.ModelSphere;
import com.arisux.starway.Renderer;
import com.arisux.starway.SpaceManager;
import com.arisux.starway.Starway;
import com.asx.mdx.lib.client.util.Color;
import com.asx.mdx.lib.client.util.Draw;
import com.asx.mdx.lib.client.util.OpenGL;
import com.asx.mdx.lib.util.Game;

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
        for (ISolarSystem solarSystem : this.getSolarSystems())
        {
            OpenGL.pushMatrix();
            {
                solarSystem.renderMap(renderer, this, renderPartialTicks);
            }
            OpenGL.popMatrix();
        }
        
        OpenGL.pushMatrix();
        {
            OpenGL.translate(this.pos().x, this.pos().z, 0);
            OpenGL.pushMatrix();
            OpenGL.rotate(90F, 1, 0, 0);
            this.drawBlackHole(renderPartialTicks);
            OpenGL.popMatrix();
            OpenGL.rotate(-renderer.angle, 1, 0, 0);
            OpenGL.translate(1, 1, 250);
            float antiScale = 1F / renderer.scale;
            OpenGL.scale(antiScale, antiScale, 1);
            Draw.drawStringAlignCenter(this.getName(), 0, 0 - 10 - renderer.getTextPadding() * 2, 0xFF00CCFF, false);
//            Draw.drawStringAlignCenter(String.format("(X: %s, Z: %s)", this.pos().x, this.pos().z), 0, 0 - renderer.getTextPadding() * 2, 0xFFFFFFFF, false);
        }
        OpenGL.popMatrix();
    }

    public void render(float partialTicks)
    {
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

        this.drawBlackHole(partialTicks);
    }

    @Override
    public float getAccretionDiscSize()
    {
        return 770;
    }

    @Override
    public float getRotationYaw()
    {
        return (Game.minecraft().world.getWorldTime() % 360) + Game.partialTicks();
    }

    private final ModelSphere sphere = new ModelSphere();
    private final Color       color  = new Color(0.04F, 0.04F, 0.04F, 1F);
    private final Color       color2 = new Color(0F, 0F, 0F, 0.675F);
    private final Color       color3 = new Color(0F, 0F, 0F, 1F);
    private final Color       color4 = new Color(1F, 1F, 1F, 1F);

    public void drawBlackHole(float partialTicks)
    {
        OpenGL.pushMatrix();
        {
            float scale = getObjectSize() == 0F ? 1F : getObjectSize() / 100;
            float singularitySize = 1F;
            float discSize = getAccretionDiscSize();
            float rotation = getRotationYaw();

            OpenGL.scale(scale, scale, scale);
            OpenGL.rotate(rotation, 0, 1, 0);
            OpenGL.enableBlend();
            OpenGL.disableTexture2d();

            /** Fading ring background color **/
            OpenGL.pushMatrix();
            sphere.cull = false;
            sphere.invert = true;
            OpenGL.rotate(rotation, 0, 1, 0);
            sphere.setScale((singularitySize / 100) + (50 * 1) * 1);
            OpenGL.scale(1.2F, 1.2F, 1.2F);
            sphere.setColor(color4);
            sphere.render();
            OpenGL.popMatrix();

            OpenGL.blendClear();
            
            OpenGL.pushMatrix();
            OpenGL.rotate(rotation, 0, 1, 0);
            sphere.cull = false;
            sphere.invert = false;
            sphere.setScale((singularitySize * 57));
            sphere.setColor(color3);
            sphere.render();
            sphere.invert = true;
            OpenGL.popMatrix();

            /** Fading ring around the singularity **/
            for (int i = 4; i > 0; i--)
            {
                OpenGL.pushMatrix();
                OpenGL.rotate(rotation, 0, 1, 0);
                sphere.cull = false;
                sphere.setScale((singularitySize * 51) + (2 * i));
                sphere.setColor(color2);
                sphere.render();
                OpenGL.popMatrix();
            }

            OpenGL.blendClear();

//            /** Singularity dark matter effect **/
//            OpenGL.pushMatrix();
//            sphere.cull = false;
//            sphere.setScale((singularitySize / 100) + (50 * 1) * 1);
//            OpenGL.scale(1.05F, 1F, 1.05F);
//            sphere.setColor(color);
//            sphere.render();
//            OpenGL.popMatrix();
//            OpenGL.blendClear();
//            OpenGL.pushMatrix();
//            OpenGL.rotate((Game.minecraft().world.getWorldTime() % 360 * 3) + partialTicks, 1, 0, 0);
//            sphere.cull = false;
//            sphere.setScale((singularitySize / 100) + (50 + 1) * 1);
//            sphere.setColor(color3);
//            sphere.render();
//            OpenGL.popMatrix();

            /** Draw the accretion disc **/
            OpenGL.pushMatrix();
            OpenGL.enableBlend();
            OpenGL.enableTexture2d();
            OpenGL.disableCullFace();
            OpenGL.blendClear();
            OpenGL.blendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
            OpenGL.rotate(90F, 1, 0, 0);
            Draw.drawResource(Starway.resources().BLACKHOLE_ACCRETION_DISC, Math.round(-(discSize / 2)), Math.round(-(discSize / 2)), Math.round(discSize), Math.round(discSize), 1F, 1F, 1F, 1F);
            OpenGL.blendClear();
            OpenGL.enableCullFace();
            OpenGL.disableTexture2d();
            OpenGL.popMatrix();

            OpenGL.enableTexture2d();
            OpenGL.disableBlend();
        }
        OpenGL.popMatrix();
    }
}
