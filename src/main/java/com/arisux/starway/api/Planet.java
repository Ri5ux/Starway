package com.arisux.starway.api;

import org.lwjgl.opengl.GL11;

import com.arisux.starway.ModelSphere;
import com.arisux.starway.Renderer;
import com.arisux.starway.SpaceManager;
import com.arisux.starway.Starway;
import com.asx.mdx.lib.client.util.Color;
import com.asx.mdx.lib.client.util.Draw;
import com.asx.mdx.lib.client.util.OpenGL;
import com.asx.mdx.lib.world.Dimension;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Save;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Planet extends OrbitableObject implements IPlanet
{
    protected final ModelSphere sphere = new ModelSphere();

    /** This is not the actual world provider instance. This is just used to obtain world colors. **/
    protected WorldProvider     provider;

    @Override
    public String getName()
    {
        return "Default Planet";
    }

    @Override
    public void onInitialization(FMLInitializationEvent event)
    {
        super.onInitialization(event);

        Starway.dimensions().register(this.getDimension());
    }

    @Override
    public void onInitialTick(WorldTickEvent event)
    {
        super.onInitialTick(event);

        if (this.getDimension() != null)
        {
            this.provider = Renderer.constructWorldProvider(this.getDimension().getProvider());
        }
    }

    @Override
    public void onTick(WorldTickEvent event)
    {
        super.onTick(event);
    }

    @Override
    public void onGlobalTick(WorldTickEvent event)
    {
        super.onGlobalTick(event);
    }

    @Override
    public void load(Load event)
    {
        super.load(event);
    }

    @Override
    public void save(Save event)
    {
        super.save(event);
    }

    @Override
    public void unload(Unload event)
    {
        super.unload(event);
    }

    @Override
    public Dimension getDimension()
    {
        return null;
    }

    @Override
    public float getDistanceFromObjectOrbiting()
    {
        return 0.000015590539204502F;
    }

    @Override
    public void drawObjectTag(Renderer renderer, float renderPartialTicks)
    {
        Vec3d color = new Vec3d(0F, 0.5F, 1F);
        
        if (this.getDimension() != null)
        {
            WorldProvider provider = Renderer.constructWorldProvider(this.getDimension().getProvider());
            color = provider.getFogColor(0F, 0F);
        }
        
        int planetColor = Color.createHexadecimal(255, (int) (color.x * 255), (int) (color.y * 255), (int) (color.z * 255));
        int planetSize = (int) (SpaceManager.instance.getPlanetaryScale() * this.getObjectSize());
        int planetDiameter = planetSize / 2;
        int curPadding = renderer.getTextPadding();

        Draw.drawStringAlignCenter(this.getName(), 0, 0 + planetDiameter + curPadding, 0xFF00CCFF, false);
        Draw.drawStringAlignCenter(String.format("(X: %s, Z: %s)", (int) this.pos().x, (int) this.pos().z), 0, 0 + planetDiameter + (curPadding += 10), 0xFFFFFFFF, false);
        Draw.drawCenteredRectWithOutline(0, 0, planetSize, planetSize, 0, planetColor, 0xFF00AAFF);
    }

    @Override
    public void renderMap(Renderer renderer, OrbitableObject parentObject, float renderPartialTicks)
    {
        int distance = (int) (this.getDistanceFromObjectOrbiting() * SpaceManager.instance.getPlanetaryDistScale());

        OpenGL.pushMatrix();
        {
            if (parentObject != null)
            {
                OpenGL.translate(parentObject.pos().x, parentObject.pos().z, 0);
                SolarSystem.drawOrbitMarker(6F, distance, 0);
            }
        }
        OpenGL.popMatrix();

        OpenGL.translate(this.pos().x, this.pos().z, 0);
        this.drawObjectTag(renderer, renderPartialTicks);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void render(float partialTicks)
    {
        Vec3d fogColor = provider != null ? provider.getFogColor(0F, 0F) : new Vec3d(0F, 0F, 0F);
        int planetSize = (int) (SpaceManager.instance.getPlanetaryScale() * getObjectSize());

        OpenGL.disableCullFace();
        OpenGL.enableRescaleNormal();
        OpenGL.scale(-1F, 1F, 1F);

        if (sphere != null)
        {
            OpenGL.enableBlend();
            OpenGL.blendClear();
            OpenGL.disableTexture2d();
            Minecraft.getMinecraft().entityRenderer.enableLightmap();

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_LIGHT0);
            GL11.glEnable(GL11.GL_LIGHT1);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);

            sphere.cull = false;
            sphere.setScale(planetSize);
            sphere.setColor(new Color((float) fogColor.x, (float) fogColor.y, (float) fogColor.z, 1F));
            sphere.render();
            Minecraft.getMinecraft().entityRenderer.disableLightmap();
            OpenGL.disableLighting();
            OpenGL.enableTexture2d();
            OpenGL.disableBlend();
        }
        OpenGL.disableRescaleNormal();
        OpenGL.enableCullFace();
    }
}
