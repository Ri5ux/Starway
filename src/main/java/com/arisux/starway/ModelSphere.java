package com.arisux.starway;

import org.lwjgl.opengl.GL11;

import com.asx.mdx.lib.client.util.Color;
import com.asx.mdx.lib.client.util.Draw;
import com.asx.mdx.lib.client.util.OpenGL;
import com.asx.mdx.lib.client.util.Vertex;
import com.asx.mdx.lib.client.util.models.Model;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class ModelSphere extends Model
{
    private Tessellator tessellator = Tessellator.getInstance();
    private Color       color;
    private float       scale;
    public boolean      cull        = true;

    private Vertex      vX          = new Vertex(1.0F, 0.0F, 0.0F).normalize();
    private Vertex      vY          = new Vertex(0.0F, 1.0F, 0.0F).normalize();
    private Vertex      vZ          = new Vertex(0.0F, 0.0F, 1.0F).normalize();
    private Vertex      vXY         = new Vertex(0.5F, 0.5F, 0.0F).normalize();
    private Vertex      vYZ         = new Vertex(0.0F, 0.5F, 0.5F).normalize();
    private Vertex      vXZ         = new Vertex(0.5F, 0.0F, 0.5F).normalize();
    private Vertex      vX1         = new Vertex(0.75F, 0.25F, 0.0F).normalize();
    private Vertex      vX2         = new Vertex(0.5F, 0.25F, 0.25F).normalize();
    private Vertex      vX3         = new Vertex(0.75F, 0.0F, 0.25F).normalize();
    private Vertex      vY1         = new Vertex(0.0F, 0.75F, 0.25F).normalize();
    private Vertex      vY2         = new Vertex(0.25F, 0.5F, 0.25F).normalize();
    private Vertex      vY3         = new Vertex(0.25F, 0.75F, 0.0F).normalize();
    private Vertex      vZ1         = new Vertex(0.25F, 0.0F, 0.75F).normalize();
    private Vertex      vZ2         = new Vertex(0.25F, 0.25F, 0.5F).normalize();
    private Vertex      vZ3         = new Vertex(0.0F, 0.25F, 0.75F).normalize();
    private Vertex      hvX         = new Vertex(-1.0F, -0.0F, 0.0F).normalize();
    private Vertex      hvY         = new Vertex(-0.0F, -1.0F, 0.0F).normalize();
    private Vertex      hvZ         = new Vertex(-0.0F, -0.0F, 1.0F).normalize();
    private Vertex      hvXY        = new Vertex(-0.5F, -0.5F, 0.0F).normalize();
    private Vertex      hvYZ        = new Vertex(-0.0F, -0.5F, 0.5F).normalize();
    private Vertex      hvXZ        = new Vertex(-0.5F, -0.0F, 0.5F).normalize();
    private Vertex      hvX1        = new Vertex(-0.75F, -0.25F, 0.0F).normalize();
    private Vertex      hvX2        = new Vertex(-0.5F, -0.25F, 0.25F).normalize();
    private Vertex      hvX3        = new Vertex(-0.75F, -0.0F, 0.25F).normalize();
    private Vertex      hvY1        = new Vertex(-0.0F, -0.75F, 0.25F).normalize();
    private Vertex      hvY2        = new Vertex(-0.25F, -0.5F, 0.25F).normalize();
    private Vertex      hvY3        = new Vertex(-0.25F, -0.75F, 0.0F).normalize();
    private Vertex      hvZ1        = new Vertex(-0.25F, -0.0F, 0.75F).normalize();
    private Vertex      hvZ2        = new Vertex(-0.25F, -0.25F, 0.5F).normalize();
    private Vertex      hvZ3        = new Vertex(-0.0F, -0.25F, 0.75F).normalize();

    public ModelSphere()
    {
        this.color = new Color(1.0F, 1.0F, 1.0F, 1.0F);
        this.scale = 2F;
    }

    private void addTri(Vertex v1, Vertex v2, Vertex v3)
    {
        float nX = v1.x * 1.1F;
        float nY = v1.y * (v1 == vY || v1 == vY1 || v1 == vY3 ? -1.1F : 1.1F);
        float nZ = v1.z * 1.1F;

        Draw.buffer().begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_TEX_COLOR);
        Draw.buffer().pos(v1.x, v1.y, v1.z).normal(nX, nY, nZ).color(this.color.r, this.color.g, this.color.b, this.color.a).endVertex();
        Draw.buffer().pos(v2.x, v2.y, v2.z).normal(nX, nY, nZ).color(this.color.r, this.color.g, this.color.b, this.color.a).endVertex();
        Draw.buffer().pos(v3.x, v3.y, v3.z).normal(nX, nY, nZ).color(this.color.r, this.color.g, this.color.b, this.color.a).endVertex();

        if (cull)
        {
            Draw.buffer().pos(v3.x, v3.y, v3.z).normal(nX, nY, nZ).color(this.color.r, this.color.g, this.color.b, this.color.a).endVertex();
            Draw.buffer().pos(v2.x, v2.y, v2.z).normal(nX, nY, nZ).color(this.color.r, this.color.g, this.color.b, this.color.a).endVertex();
            Draw.buffer().pos(v1.x, v1.y, v1.z).normal(nX, nY, nZ).color(this.color.r, this.color.g, this.color.b, this.color.a).endVertex();
        }

        tessellator.draw();
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public void setScale(float scale)
    {
        this.scale = scale;
    }

    @Override
    public void render(Object obj)
    {
        OpenGL.pushMatrix();
        {
            OpenGL.enableRescaleNormal();
            OpenGL.scale(scale, scale, scale);
            OpenGL.rotate(180F, 0F, 0F, 1F);

            for (int quadrant = 4; quadrant > 0; quadrant--)
            {
                OpenGL.pushMatrix();
                {
                    OpenGL.rotate(quadrant * 90, 0F, 1F, 0F);

                    addTri(vY, vY1, vY3);
                    addTri(vY1, vYZ, vY2);
                    addTri(vY3, vY2, vXY);
                    addTri(vY1, vY2, vY3);

                    addTri(vX, vX1, vX3);
                    addTri(vX1, vXY, vX2);
                    addTri(vX3, vX2, vXZ);
                    addTri(vX1, vX2, vX3);

                    addTri(vZ, vZ1, vZ3);
                    addTri(vZ1, vXZ, vZ2);
                    addTri(vZ3, vZ2, vYZ);
                    addTri(vZ1, vZ2, vZ3);

                    addTri(vYZ, vZ2, vY2);
                    addTri(vXY, vY2, vX2);
                    addTri(vXZ, vX2, vZ2);
                    addTri(vX2, vY2, vZ2);

                    addTri(hvY, hvY1, hvY3);
                    addTri(hvY1, hvYZ, hvY2);
                    addTri(hvY3, hvY2, hvXY);
                    addTri(hvY1, hvY2, hvY3);

                    addTri(hvX, hvX1, hvX3);
                    addTri(hvX1, hvXY, hvX2);
                    addTri(hvX3, hvX2, hvXZ);
                    addTri(hvX1, hvX2, hvX3);

                    addTri(hvZ, hvZ1, hvZ3);
                    addTri(hvZ1, hvXZ, hvZ2);
                    addTri(hvZ3, hvZ2, hvYZ);
                    addTri(hvZ1, hvZ2, hvZ3);

                    addTri(hvYZ, hvZ2, hvY2);
                    addTri(hvXY, hvY2, hvX2);
                    addTri(hvXZ, hvX2, hvZ2);
                    addTri(hvX2, hvY2, hvZ2);

                }
                OpenGL.popMatrix();
            }
            OpenGL.disableRescaleNormal();
        }
        OpenGL.popMatrix();
    }
}
