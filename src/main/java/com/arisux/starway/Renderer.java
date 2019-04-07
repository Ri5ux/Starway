package com.arisux.starway;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.arisux.starway.api.Galaxy;
import com.arisux.starway.api.IGalaxy;
import com.arisux.starway.dimensions.space.SpaceProvider;
import com.arisux.starway.starships.EntityStarship;
import com.asx.mdx.MDX;
import com.asx.mdx.core.mods.IInitEvent;
import com.asx.mdx.lib.client.util.Draw;
import com.asx.mdx.lib.client.util.OpenGL;
import com.asx.mdx.lib.util.Game;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

public class Renderer implements IInitEvent
{
    public static Renderer instance = new Renderer();
    public int             tick;
    public boolean         mapEnabled;
    public float           zoom;

    @Override
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(instance);
        FMLCommonHandler.instance().bus().register(instance);
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event)
    {
        this.tick++;

        int dwheel = Mouse.getDWheel();

        if (dwheel > 0 || dwheel < 0)
        {
            if (zoom >= -1F)
            {
                zoom = zoom + (dwheel * 0.0001F);
            }
            else
            {
                zoom = -0.99F;
            }
        }

        if (Game.minecraft().inGameHasFocus && Game.minecraft().currentScreen == null)
        {
            if (Keyboard.isKeyDown(Keyboard.KEY_M) && Keyboard.next())
            {
                mapEnabled = !mapEnabled;
            }
        }
    }

    @SubscribeEvent
    public void renderWorldLast(RenderWorldLastEvent event)
    {
        if (Minecraft.getMinecraft().player.world.provider instanceof SpaceProvider)
        {
            Entity rve = Minecraft.getMinecraft().renderViewEntity;
            double x = rve.lastTickPosX + (rve.posX - rve.lastTickPosX) * (double) event.getPartialTicks();
            double y = rve.lastTickPosY + (rve.posY - rve.lastTickPosY) * (double) event.getPartialTicks();
            double z = rve.lastTickPosZ + (rve.posZ - rve.lastTickPosZ) * (double) event.getPartialTicks();

            OpenGL.translate(-x, -y, -z);
            OpenGL.scale(1F, -1F, 1F);

            for (Galaxy galaxy : Starway.getGalaxies())
            {
                OpenGL.pushMatrix();
                {
                    OpenGL.translate(galaxy.pos().x, -galaxy.pos().y, galaxy.pos().z);
                    galaxy.render(event.getPartialTicks());
                }
                OpenGL.popMatrix();
            }
        }
    }

    @SubscribeEvent
    public void render(RenderTickEvent event)
    {
        if (mapEnabled)
        {
            GL11.glEnable(GL11.GL_BLEND);
            Minecraft mc = Minecraft.getMinecraft();
            ScaledResolution resolution = new ScaledResolution(mc);
            Draw.drawRect(0, 0, resolution.getScaledWidth(), resolution.getScaledHeight(), 0xCC000000);

            float scale = 1F + zoom;
            int centerX = (int) (resolution.getScaledWidth() / 2 / scale);
            int centerY = (int) (resolution.getScaledHeight() / 2 / scale);

            OpenGL.pushMatrix();
            {
                OpenGL.scale(scale, scale, 1);
                OpenGL.translate(centerX, centerY, 1);

                for (IGalaxy galaxy : Starway.getGalaxies())
                {
                    OpenGL.pushMatrix();
                    {
                        galaxy.renderMap(this, null, event.renderTickTime);
                    }
                    OpenGL.popMatrix();
                }

                this.drawPlayersOnMap(scale);
            }
            OpenGL.popMatrix();
        }
    }

    public int getTextPadding()
    {
        return 8;
    }

    public void drawPlayersOnMap(float scale)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.world != null)
        {
            for (Object o : mc.world.loadedEntityList)
            {
                if (o instanceof EntityStarship)
                {
                    EntityStarship starship = (EntityStarship) o;

                    if (starship != null && mc.world.provider instanceof SpaceProvider)
                    {
                        OpenGL.pushMatrix();
                        {
                            OpenGL.translate(starship.posX, starship.posZ, 0);
                            OpenGL.scale(1F / scale, 1F / scale, 0);

                            OpenGL.pushMatrix();
                            {
                                OpenGL.rotate(starship.rotationYaw + 90, 0, 0, 1);
                                OpenGL.translate(3, 0, 0);
                                Draw.drawRect(0, -1, 6, 2, 0xFFFF0000);
                            }
                            OpenGL.popMatrix();

                            Draw.drawRect(-1, -1, 2, 2, 0xFFFF0000);
                            int curPadding = 10 + this.getTextPadding();
                            Draw.drawStringAlignCenter(starship.getName(), 0, curPadding, 0xFFFF0000, false);
                            Draw.drawStringAlignCenter(String.format("(X: %s, Z: %s)", (int) starship.posX, (int) starship.posZ), 0, curPadding += 10, 0xFFFFFFFF, false);
                        }
                        OpenGL.popMatrix();
                    }
                }

                if (o instanceof EntityPlayer)
                {
                    EntityPlayer player = (EntityPlayer) o;

                    if (player != null && mc.world.provider instanceof SpaceProvider)
                    {
                        OpenGL.pushMatrix();
                        {
                            OpenGL.translate(player.posX, player.posZ, 0);
                            OpenGL.scale(1F / scale, 1F / scale, 0);

                            OpenGL.pushMatrix();
                            {
                                OpenGL.rotate(player.rotationYaw + 90, 0, 0, 1);
                                OpenGL.translate(3, 0, 0);
                                Draw.drawRect(0, -1, 6, 2, 0xFFFFFFFF);
                            }
                            OpenGL.popMatrix();

                            Draw.drawRect(-1, -1, 2, 2, 0xFFFF0000);
                            int curPadding = 10 + this.getTextPadding();
                            Draw.drawStringAlignCenter(player.getName(), 0, curPadding, 0xFFFF0000, false);
                            Draw.drawStringAlignCenter(String.format("(X: %s, Z: %s)", (int) player.posX, (int) player.posZ), 0, curPadding += 10, 0xFFFFFFFF, false);
                        }
                        OpenGL.popMatrix();
                    }
                }
            }
        }
    }

    public static WorldProvider constructWorldProvider(Class<? extends WorldProvider> c)
    {
        try
        {
            return (c.getConstructor()).newInstance(new Object[] {});
        }
        catch (Exception e)
        {
            MDX.log().warn("Failed to construct world provider: " + (c != null ? c.getName() : c));
            e.printStackTrace();
        }
        return null;
    }

    public float getZoom()
    {
        return zoom;
    }
}
