package com.arisux.starway.api;

import static org.lwjgl.opengl.GL11.GL_ONE;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.arisux.starway.ModelSphere;
import com.arisux.starway.Renderer;
import com.arisux.starway.SpaceManager;
import com.asx.mdx.lib.client.util.Color;
import com.asx.mdx.lib.client.util.Draw;
import com.asx.mdx.lib.client.util.OpenGL;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.CullFace;
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
        int distance = (int) (SpaceManager.instance.getSolarSystemDistScale() * this.getDistanceFromObjectOrbiting());

        OpenGL.pushMatrix();
        {
            OpenGL.translate(this.pos().x, this.pos().z, 0);
            OpenGL.pushMatrix();
            this.drawSun(renderPartialTicks);
            OpenGL.disableLight();
            OpenGL.popMatrix();
            float antiScale = 1F / renderer.scale;
            OpenGL.rotate(-renderer.angle, 1, 0, 0);
            OpenGL.scale(antiScale, antiScale, 1);
            Draw.drawStringAlignCenter(this.getName(), 0, 0 - 10 - renderer.getTextPadding() * 2, 0xFF00CCFF, false);
            // Draw.drawStringAlignCenter(String.format("(X: %s, Z: %s)", Math.round(this.pos().x), Math.round(this.pos().z)), 0, 0 - renderer.getTextPadding() * 2, 0xFFFFFFFF, false);
        }
        OpenGL.popMatrix();

        OpenGL.pushMatrix();
        {
            if (parentObject != null)
            {
                OpenGL.translate(parentObject.pos().x, parentObject.pos().z, 0);
                SolarSystem.drawOrbitMarker(renderer, Math.round(this.getDistanceFromObjectOrbiting() / 20), 1F, distance, 0);
            }
        }
        OpenGL.popMatrix();

        // OpenGL.pushMatrix();
        // {
        // OpenGL.translate(this.pos().x, this.pos().z, 0);
        // this.drawObjectTag(renderer, renderPartialTicks);
        // }
        // OpenGL.popMatrix();

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

    private final ModelSphere sphere = new ModelSphere();
    private final Color       color  = new Color(1.0F, 0.3F, 0.0F, 1F);
    private final Color       color2 = new Color(1.0F, 0.3F, 0.0F, 0.5F);
    private final Color       color3 = new Color(1F, 0.3F, 0F, 1F);

    protected void drawSun(float partialTicks)
    {
        int size = (int) (getObjectSize() * SpaceManager.instance.getStarScale()) / 100;
        float time = (Minecraft.getMinecraft().world.getWorldTime() % 360);
        float pt = partialTicks;
        pt = 0;
        time = 0;
        Color color2 = new Color(1.0F, 0.3F, 0.0F, 0.2F);

        OpenGL.pushMatrix();

        float s = 10;
        OpenGL.scale(s, s, s);
        OpenGL.enableBlend();
        OpenGL.blendClear();
        OpenGL.disableTexture2d();
        OpenGL.enableLighting();
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);

        OpenGL.pushMatrix();
        sphere.cull = false;
        OpenGL.rotate(-(time + pt) * 1, 1, 1, 1);
        sphere.setScale(size + (49));
        OpenGL.scale(1.065F, 1F, 1.065F);
        sphere.setColor(color3);
        sphere.render();
        OpenGL.popMatrix();

        OpenGL.blendFunc(GL_ONE, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
//        OpenGL.blendClear();

//        GlStateManager.cullFace(CullFace.FRONT);
//        for (int i = 1; i >= 0; i--)
//        {
//            OpenGL.pushMatrix();
//            OpenGL.rotate((time + (i * 40F) + pt), 1, 1, 0);
//            sphere.cull = false;
//            sphere.setScale(size + (48 + (i * 0.75F)));
//            sphere.setColor(color);
//            sphere.render();
//            OpenGL.popMatrix();
//        }
//
//        for (int i = 4; i >= 0; i--)
//        {
//            OpenGL.pushMatrix();
//            OpenGL.rotate((time + pt), 1, 1, 0);
//            sphere.cull = true;
//            sphere.setScale(size + (51 + (i * 0.75F)));
//            sphere.setColor(color2);
//            sphere.render();
//            OpenGL.popMatrix();
//        }

        GlStateManager.cullFace(CullFace.BACK);
        for (int i = 0; i <= 5; i++)
        {
            OpenGL.pushMatrix();
            OpenGL.rotate((time + (i * 40F) + pt), 1, 1, 0);
            sphere.cull = true;
            sphere.setScale(size + (48 + (i * 0.75F)));
            sphere.setColor(color);
            sphere.render();
            OpenGL.popMatrix();
        }

        for (int i = 0; i <= 4; i++)
        {
            OpenGL.pushMatrix();
            OpenGL.rotate((time + pt), 1, 1, 0);
            sphere.cull = true;
            sphere.setScale(size + (51 + (i * 0.75F)));
            sphere.setColor(color2);
            sphere.render();
            OpenGL.popMatrix();
        }

        OpenGL.enableLightMapping();
        OpenGL.enableTexture2d();
        OpenGL.disableBlend();
        OpenGL.popMatrix();
    }
}
