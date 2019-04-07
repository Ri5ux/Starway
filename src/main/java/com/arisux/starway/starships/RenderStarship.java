package com.arisux.starway.starships;

import com.arisux.starway.Starway;
import com.asx.mdx.lib.client.util.OpenGL;
import com.asx.mdx.lib.client.util.models.wavefront.Part;
import com.asx.mdx.lib.util.Game;
import com.asx.mdx.lib.util.MDXMath;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;

public class RenderStarship extends Render<EntityStarship>
{
    public RenderStarship()
    {
        super(Game.minecraft().renderManager);
    }

    @Override
    public void doRender(EntityStarship entity, double x, double y, double z, float yaw, float partialTicks)
    {
        OpenGL.pushMatrix();
        {
            float scale = 0.5F;
            OpenGL.translate(x, y, z);
            OpenGL.scale(scale, scale, scale);
            // OpenGL.rotate(-(entity.riddenByEntity.rotationYaw), 0, 1, 0);
            // OpenGL.rotate(entity.riddenByEntity.rotationPitch, 1, 0, 0);

            OpenGL.rotate(MDXMath.interpolateRotation((-(entity.prevRotationYaw) - 90), (-(entity.rotationYaw) - 90), partialTicks), 0, 1, 0);
            OpenGL.rotate(entity.rotationPitch, 1, 0, 0);

            for (Part p : Starway.resources().models().STARSHIP.parts.values())
            {
                p.draw();
            }

            // OpenGL.disableTexture2d();
            // ModelSphere sphere = new ModelSphere();
            // sphere.setColor(new Color(1F, 1F, 1F, 1F));
            // sphere.setScale(2);
            // sphere.render();
            // OpenGL.enableTexture2d();
        }
        OpenGL.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityStarship entity)
    {
        return null;
    }
}
