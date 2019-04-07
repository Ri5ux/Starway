package com.arisux.starway.api;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.arisux.starway.Starway;
import com.asx.mdx.lib.client.util.Color;
import com.asx.mdx.lib.client.util.Draw;
import com.asx.mdx.lib.client.util.OpenGL;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class SkyProvider extends IRenderHandler implements IPlanetSkyProvider
{
    protected static final Tessellator tessellator = Tessellator.getInstance();
    protected static final Minecraft mc = Minecraft.getMinecraft();
    
    protected int glStarList;
    protected int glSkyList;

    public SkyProvider()
    {
        this.glStarList = GLAllocation.generateDisplayLists(3);
        this.generateStars();
        this.createSkyCallList();
    }

    private void generateStars()
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();

        if (this.glStarList >= 0)
        {
            GLAllocation.deleteDisplayLists(this.glStarList);
            this.glStarList = -1;
        }

        this.glStarList = GLAllocation.generateDisplayLists(1);
        GlStateManager.pushMatrix();
        GlStateManager.glNewList(this.glStarList, 4864);
        this.renderStars(vertexbuffer);
        GlStateManager.glEndList();
        GlStateManager.popMatrix();

        this.glStarList += 1;
    }

    private void renderStars(BufferBuilder buffer)
    {
        Random random = new Random(10842L);
        buffer.begin(7, DefaultVertexFormats.POSITION);

        for (int i = 0; i < 1500; ++i)
        {
            double d0 = (double) (random.nextFloat() * 2.0F - 1.0F);
            double d1 = (double) (random.nextFloat() * 2.0F - 1.0F);
            double d2 = (double) (random.nextFloat() * 2.0F - 1.0F);
            double d3 = (double) (0.15F + random.nextFloat() * 0.1F);
            double d4 = d0 * d0 + d1 * d1 + d2 * d2;

            if (d4 < 1.0D && d4 > 0.01D)
            {
                d4 = 1.0D / Math.sqrt(d4);
                d0 = d0 * d4;
                d1 = d1 * d4;
                d2 = d2 * d4;
                double d5 = d0 * 100.0D;
                double d6 = d1 * 100.0D;
                double d7 = d2 * 100.0D;
                double d8 = Math.atan2(d0, d2);
                double d9 = Math.sin(d8);
                double d10 = Math.cos(d8);
                double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
                double d12 = Math.sin(d11);
                double d13 = Math.cos(d11);
                double d14 = random.nextDouble() * Math.PI * 2.0D;
                double d15 = Math.sin(d14);
                double d16 = Math.cos(d14);

                for (int j = 0; j < 4; ++j)
                {
                    double d18 = (double) ((j & 2) - 1) * d3;
                    double d19 = (double) ((j + 1 & 2) - 1) * d3;
                    double d21 = d18 * d16 - d19 * d15;
                    double d22 = d19 * d16 + d18 * d15;
                    double d23 = d21 * d12 + 0.0D * d13;
                    double d24 = 0.0D * d12 - d21 * d13;
                    double d25 = d24 * d9 - d22 * d10;
                    double d26 = d22 * d9 + d24 * d10;
                    buffer.pos(d5 + d25, d6 + d23, d7 + d26).endVertex();
                }
            }
        }
        Tessellator.getInstance().draw();
    }

    @Override
    public void createSkyCallList()
    {
        this.glSkyList = (this.glStarList + 1);

//        GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
//        {
//            int levels = 64;
//            int size = 256 / levels + 2;
//            float skylineHeight = 60.0F;
//
//            for (int x = -levels * size; x <= levels * size; x += levels)
//            {
//                for (int z = -levels * size; z <= levels * size; z += levels)
//                {
//                    Draw.startQuads();
//                    Draw.buffer().pos(x + 0.000F, skylineHeight, z + 0.000F).endVertex();
//                    Draw.buffer().pos(x + levels, skylineHeight, z + 0.000F).endVertex();
//                    Draw.buffer().pos(x + levels, skylineHeight, z + levels).endVertex();
//                    Draw.buffer().pos(x + 0.000F, skylineHeight, z + levels).endVertex();
//                    tessellator.draw();
//                }
//            }
//        }
//        GL11.glEndList();
    }

    @Override
    public void render(float renderPartialTicks, WorldClient world, Minecraft mc)
    {
        OpenGL.disable(GL11.GL_TEXTURE_2D);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        GL11.glDepthMask(false);
        
        OpenGL.enable(GL11.GL_FOG);

        if (this.getSkyColor() != null)
        {
            GL11.glColor3f(this.getSkyColor().r, this.getSkyColor().g, this.getSkyColor().b);
        }
        
        this.drawSky(world, renderPartialTicks);
        GL11.glDepthMask(true);
        OpenGL.enable(GL11.GL_TEXTURE_2D);
    }

    @Override
    public void drawPlanetsInSky(World world, float renderPartialTicks)
    {
//        OpenGL.pushMatrix();
//        {
//            float scale = 25.0F;
//            OpenGL.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
//            OpenGL.color(1.0F, 1.0F, 1.0F, 1.0F);
//            OpenGL.rotate(world.getCelestialAngle(renderPartialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
//            Draw.bindTexture(Starway.resources().SKY_SUN);
//            tessellator.startDrawingQuads();
//            Draw.vertex(-scale, 150.0D, -scale, 0.0D, 0.0D);
//            Draw.vertex(scale, 150.0D, -scale, 1.0D, 0.0D);
//            Draw.vertex(scale, 150.0D, scale, 1.0D, 1.0D);
//            Draw.vertex(-scale, 150.0D, scale, 0.0D, 1.0D);
//            tessellator.draw();
//        }
//        OpenGL.popMatrix();
        
    }

    @Override
    public void drawStarsInSky(World world, float renderPartialTicks)
    {
        OpenGL.color(this.getStarColor().r, this.getStarColor().g, this.getStarColor().b, this.getStarColor().a);
        this.renderStars(Tessellator.getInstance().getBuffer());
        OpenGL.color(1F, 1F, 1F, 1F);
    }

    @Override
    public void drawSky(World world, float renderPartialTicks)
    {
//        GL11.glCallList(this.glSkyList);
        OpenGL.disable(GL11.GL_FOG);
        OpenGL.disable(GL11.GL_ALPHA_TEST);
        OpenGL.enable(GL11.GL_BLEND);
        OpenGL.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        this.drawStarsInSky(world, renderPartialTicks);
        

        OpenGL.color(1F, 1F, 1F, 1F);
        OpenGL.enable(GL11.GL_TEXTURE_2D);
        OpenGL.blendFunc(GL11.GL_SRC_ALPHA, 1);

        this.drawPlanetsInSky(world, renderPartialTicks);

        OpenGL.disable(GL11.GL_BLEND);
        OpenGL.enable(GL11.GL_ALPHA_TEST);
        GL11.glDepthMask(true);
        
//        if (mc.gameSettings.shouldRenderClouds() == 1)
//        {
//            OpenGL.pushMatrix();
//            {
//                if (mc.gameSettings.fancyGraphics)
//                {
//                    OpenGL.enable(GL11.GL_FOG);
//                }
//
//                this.drawCloudsInSky(world, renderPartialTicks);
//            }
//            OpenGL.popMatrix();
//        }

        OpenGL.disable(GL11.GL_FOG);
    }

    @Override
    public void drawCloudsInSky(World world, float renderPartialTicks)
    {
        double time = (mc.world.getWorldTime() * this.getCloudMovementSpeed()) + renderPartialTicks * (this.getCloudMovementSpeed());
        double viewX = (mc.renderViewEntity.prevPosX + (mc.renderViewEntity.posX - mc.renderViewEntity.prevPosX) * renderPartialTicks + time * 0.029999999329447746D) / this.getCloudScale();
        double viewZ = (mc.renderViewEntity.prevPosZ + (mc.renderViewEntity.posZ - mc.renderViewEntity.prevPosZ) * renderPartialTicks) / this.getCloudScale() + 0.33000001311302185D;
        float cloudY = mc.world.provider.getCloudHeight() - (float) (mc.renderViewEntity.lastTickPosY + (mc.renderViewEntity.posY - mc.renderViewEntity.lastTickPosY) * renderPartialTicks);
        viewX -= (MathHelper.floor(viewX / 2048.0D)) * 2048;
        viewZ -= (MathHelper.floor(viewZ / 2048.0D)) * 2048;
        float scaleUV = 0.00390625F;
        float offsetU = MathHelper.floor(viewX) * scaleUV;
        float offsetV = MathHelper.floor(viewZ) * scaleUV;
        byte dist = (byte) (mc.gameSettings.renderDistanceChunks);
        byte cloudSections = 4;

        OpenGL.disableCullFace();
        Draw.bindTexture(this.getResourceCloudMap());
        OpenGL.enableBlend();
        OpenGlHelper.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        OpenGL.rotate(this.getRotationForDirection(this.getCloudMovementDirection()), 0, 1, 0);
        OpenGL.scale(this.getCloudScale(), 1.0F, this.getCloudScale());
        GL11.glColorMask(true, true, true, true);

        for (int x = -cloudSections + 1; x <= cloudSections; ++x)
        {
            for (int z = -cloudSections + 1; z <= cloudSections; ++z)
            {
                float cloudU = x * dist;
                float cloudV = z * dist;
                float cloudX = cloudU - ((float) (viewX - MathHelper.floor(viewX)));
                float cloudZ = cloudV - ((float) (viewZ - MathHelper.floor(viewZ)));

                Draw.startQuads();

                if (cloudY > -this.getCloudHeightSize() - 1.0F)
                {
                    //TODO: This might not work, might need to do this per vertex
                    Draw.buffer().color(this.getCloudColor().r * 0.7F, this.getCloudColor().g * 0.7F, this.getCloudColor().b * 0.7F, this.getCloudColor().a + 0.1F);
                    Draw.buffer().normal(0.0F, -1.0F, 0.0F);
                    Draw.vertex(cloudX + 0.0F, cloudY + 0.0F, cloudZ + dist, (cloudU + 0.0F) * scaleUV + offsetU, (cloudV + dist) * scaleUV + offsetV).endVertex();
                    Draw.vertex(cloudX + dist, cloudY + 0.0F, cloudZ + dist, (cloudU + dist) * scaleUV + offsetU, (cloudV + dist) * scaleUV + offsetV).endVertex();
                    Draw.vertex(cloudX + dist, cloudY + 0.0F, cloudZ + 0.0F, (cloudU + dist) * scaleUV + offsetU, (cloudV + 0.0F) * scaleUV + offsetV).endVertex();
                    Draw.vertex(cloudX + 0.0F, cloudY + 0.0F, cloudZ + 0.0F, (cloudU + 0.0F) * scaleUV + offsetU, (cloudV + 0.0F) * scaleUV + offsetV).endVertex();
                }

                if (cloudY <= this.getCloudHeightSize() + 1.0F)
                {
                    Draw.buffer().color(this.getCloudColor().r, this.getCloudColor().g, this.getCloudColor().b, this.getCloudColor().a + 0.15F);
                    Draw.buffer().normal(0.0F, 1.0F, 0.0F);
                    Draw.vertex(cloudX + 0.0F, cloudY + this.getCloudHeightSize(), cloudZ + dist, (cloudU + 0.0F) * scaleUV + offsetU, (cloudV + dist) * scaleUV + offsetV).endVertex();
                    Draw.vertex(cloudX + dist, cloudY + this.getCloudHeightSize(), cloudZ + dist, (cloudU + dist) * scaleUV + offsetU, (cloudV + dist) * scaleUV + offsetV).endVertex();
                    Draw.vertex(cloudX + dist, cloudY + this.getCloudHeightSize(), cloudZ + 0.0F, (cloudU + dist) * scaleUV + offsetU, (cloudV + 0.0F) * scaleUV + offsetV).endVertex();
                    Draw.vertex(cloudX + 0.0F, cloudY + this.getCloudHeightSize(), cloudZ + 0.0F, (cloudU + 0.0F) * scaleUV + offsetU, (cloudV + 0.0F) * scaleUV + offsetV).endVertex();
                }

                Draw.buffer().color(this.getCloudColor().r * 0.9F, this.getCloudColor().g * 0.9F, this.getCloudColor().b * 0.9F, this.getCloudColor().a);

                if (x > -1)
                {
                    Draw.buffer().normal(-1.0F, 0.0F, 0.0F);

                    for (int size = 0; size < dist; ++size)
                    {
                        Draw.vertex(cloudX + size + 0.0F, cloudY + 0.0F, cloudZ + dist, (cloudU + size + 0.5F) * scaleUV + offsetU, (cloudV + dist) * scaleUV + offsetV).endVertex();
                        Draw.vertex(cloudX + size + 0.0F, cloudY + this.getCloudHeightSize(), cloudZ + dist, (cloudU + size + 0.5F) * scaleUV + offsetU, (cloudV + dist) * scaleUV + offsetV).endVertex();
                        Draw.vertex(cloudX + size + 0.0F, cloudY + this.getCloudHeightSize(), cloudZ + 0.0F, (cloudU + size + 0.5F) * scaleUV + offsetU, (cloudV + 0.0F) * scaleUV + offsetV).endVertex();
                        Draw.vertex(cloudX + size + 0.0F, cloudY + 0.0F, cloudZ + 0.0F, (cloudU + size + 0.5F) * scaleUV + offsetU, (cloudV + 0.0F) * scaleUV + offsetV).endVertex();
                    }
                }

                if (x <= 1)
                {
                    Draw.buffer().normal(1.0F, 0.0F, 0.0F);

                    for (int size = 0; size < dist; ++size)
                    {
                        Draw.vertex(cloudX + size + 1.0F, cloudY + 0.0F, cloudZ + dist, (cloudU + size + 0.5F) * scaleUV + offsetU, (cloudV + dist) * scaleUV + offsetV).endVertex();
                        Draw.vertex(cloudX + size + 1.0F, cloudY + this.getCloudHeightSize(), cloudZ + dist, (cloudU + size + 0.5F) * scaleUV + offsetU, (cloudV + dist) * scaleUV + offsetV).endVertex();
                        Draw.vertex(cloudX + size + 1.0F, cloudY + this.getCloudHeightSize(), cloudZ + 0.0F, (cloudU + size + 0.5F) * scaleUV + offsetU, (cloudV + 0.0F) * scaleUV + offsetV).endVertex();
                        Draw.vertex(cloudX + size + 1.0F, cloudY + 0.0F, cloudZ + 0.0F, (cloudU + size + 0.5F) * scaleUV + offsetU, (cloudV + 0.0F) * scaleUV + offsetV).endVertex();
                    }
                }

                Draw.buffer().color(this.getCloudColor().r * 0.8F, this.getCloudColor().g * 0.8F, this.getCloudColor().b * 0.8F, 0.8F);

                if (z > -1)
                {
                    Draw.buffer().normal(0.0F, 0.0F, -1.0F);

                    for (int size = 0; size < dist; ++size)
                    {
                        Draw.vertex(cloudX + 0.0F, cloudY + this.getCloudHeightSize(), cloudZ + size + 0.0F, (cloudU + 0.0F) * scaleUV + offsetU, (cloudV + size + 0.5F) * scaleUV + offsetV).endVertex();
                        Draw.vertex(cloudX + dist, cloudY + this.getCloudHeightSize(), cloudZ + size + 0.0F, (cloudU + dist) * scaleUV + offsetU, (cloudV + size + 0.5F) * scaleUV + offsetV).endVertex();
                        Draw.vertex(cloudX + dist, cloudY + 0.0F, cloudZ + size + 0.0F, (cloudU + dist) * scaleUV + offsetU, (cloudV + size + 0.5F) * scaleUV + offsetV).endVertex();
                        Draw.vertex(cloudX + 0.0F, cloudY + 0.0F, cloudZ + size + 0.0F, (cloudU + 0.0F) * scaleUV + offsetU, (cloudV + size + 0.5F) * scaleUV + offsetV).endVertex();
                    }
                }

                if (z <= 1)
                {
                    for (int size = 0; size < dist; ++size)
                    {
                        Draw.vertex(cloudX + 0.0F, cloudY + this.getCloudHeightSize(), cloudZ + size + 1.0F, (cloudU + 0.0F) * scaleUV + offsetU, (cloudV + size + 0.5F) * scaleUV + offsetV).normal(0.0F, 0.0F, 1.0F).endVertex();;
                        Draw.vertex(cloudX + dist, cloudY + this.getCloudHeightSize(), cloudZ + size + 1.0F, (cloudU + dist) * scaleUV + offsetU, (cloudV + size + 0.5F) * scaleUV + offsetV).normal(0.0F, 0.0F, 1.0F).endVertex();;
                        Draw.vertex(cloudX + dist, cloudY + 0.0F, cloudZ + size + 1.0F, (cloudU + dist) * scaleUV + offsetU, (cloudV + size + 0.5F) * scaleUV + offsetV).normal(0.0F, 0.0F, 1.0F).endVertex();;
                        Draw.vertex(cloudX + 0.0F, cloudY + 0.0F, cloudZ + size + 1.0F, (cloudU + 0.0F) * scaleUV + offsetU, (cloudV + size + 0.5F) * scaleUV + offsetV).normal(0.0F, 0.0F, 1.0F).endVertex();;
                    }
                }

                tessellator.draw();
            }
        }

        OpenGL.color(1.0F, 1.0F, 1.0F, 1.0F);
        OpenGL.disable(GL11.GL_BLEND);
        OpenGL.enable(GL11.GL_CULL_FACE);
    }
    
    public void renderOrbitalObjectBelow(IOrbitableObject object, ResourceLocation objectResource)
    {
        float angle = MathHelper.sqrt(((float) (mc.player.posY) - 200) / 1000.0F);
        int renderDistance = mc.gameSettings.renderDistanceChunks * 16;
        
        if ((mc.player.posY - 64) > renderDistance)
        {
            angle *= 400.0F;

            float color = Math.max(Math.min(angle / 100.0F - 0.2F, 0.5F), 0.0F);
            float scale = 850 * (0.25F - angle / 10000.0F);
            scale = Math.max(scale, 0.2F);

            OpenGL.pushMatrix();
            {
                OpenGL.disableDepthTest();
                OpenGL.enableTexture2d();
                OpenGL.disableFog();
                OpenGL.enableBlend();
                OpenGL.blendClear();
                OpenGL.scale(scale, 1.0F, scale);
                OpenGL.translate(0.0F, -mc.player.posY, 0.0F);
                Draw.bindTexture(objectResource);
                OpenGL.color(color, color, color, 1.0F);
                
                float s = 1.0F;
                float u = 0.0F;
                float v = 1.0F - u;

                Draw.startQuads();
                
                Draw.vertex(-s, 0, +s, u, v).endVertex();
                Draw.vertex(+s, 0, +s, v, v).endVertex();
                Draw.vertex(+s, 0, -s, v, u).endVertex();
                Draw.vertex(-s, 0, -s, u, u).endVertex();
                tessellator.draw();

                OpenGL.disableBlend();
                OpenGL.disableTexture2d();
                OpenGL.enableDepthTest();
            }
            OpenGL.popMatrix();
        }
    }

    private float getRotationForDirection(EnumFacing direction)
    {
        if (direction != null)
        {
            switch (direction)
            {
                case NORTH:
                    return -90F;
                case SOUTH:
                    return 90F;
                case EAST:
                    return 180F;
                case WEST:
                    return 0F;
                default:
                    return 0F;
            }
        }

        return 0F;
    }

    @Override
    public Color getSkyColor()
    {
        Vec3d c = mc.world.provider.getSkyColor(mc.player, 1.0F);
        return new Color((float) c.x, (float) c.y, (float) c.z, 1.0F);
    }

    @Override
    public Color getStarColor()
    {
        return new Color(1F, 1F, 1F, mc.world.provider.getStarBrightness(1.0F));
    }

    @Override
    public Color getCloudColor()
    {
        Vec3d c = mc.world.provider.getCloudColor(1.0F);
        return new Color((float) c.x, (float) c.y, (float) c.z, 1.0F);
    }

    @Override
    public ResourceLocation getResourceCloudMap()
    {
        return Starway.resources().SKY_CLOUDMAP;
    }
    
    @Override
    public EnumFacing getCloudMovementDirection()
    {
      return EnumFacing.WEST;
    }

    @Override
    public float getCloudScale()
    {
        return 16F;
    }

    @Override
    public float getCloudHeightSize()
    {
        return 12F;
    }

    @Override
    public float getCloudMovementSpeed()
    {
        return 10F;
    }
}
