package com.arisux.starway.api;

import java.util.ArrayList;

import com.arisux.starway.Renderer;
import com.arisux.starway.SpaceManager;
import com.arisux.starway.Starway;
import com.arisux.starway.starships.EntityStarship;
import com.asx.mdx.lib.client.util.Draw;
import com.asx.mdx.lib.client.util.OpenGL;
import com.asx.mdx.lib.util.Game;
import com.asx.mdx.lib.world.Pos;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Save;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class OrbitableObject implements IOrbitableObject
{
    /** The current coordinates in-world this object is located at **/
    private Pos       pos;

    /** The object this object is orbiting, if it's currently orbiting one. **/
    private OrbitableObject objectOrbiting;

    /** The current orbit progress **/
    private float           orbitProgress;

    public OrbitableObject()
    {
        this.pos = new Pos(0, 0, 0);
    }

    @Override
    public void onInitialization(FMLInitializationEvent event)
    {
        ;
    }

    @Override
    public void onInitialTick(WorldTickEvent event)
    {
        ;
    }

    @Override
    public void onTick(WorldTickEvent event)
    {
        this.tryCollide(event.world);
    }

    @Override
    public void onGlobalTick(WorldTickEvent event)
    {
        this.updatePosition(event.world);
    }

    public void updatePosition(World world)
    {
        this.orbitProgress = Starway.spaceManager().getTime() / this.getOrbitTime() / Starway.spaceManager().getTimeScale();

        if (this.getObjectOrbiting() != null)
        {
            OrbitableObject obj = this.getObjectOrbiting();

            float scale = 1F;

            if (this instanceof Planet)
            {
                scale = SpaceManager.instance.getPlanetaryDistScale();
            }

            if (this instanceof SolarSystem)
            {
                scale = SpaceManager.instance.getSolarSystemDistScale();
            }

            float distance = this.getDistanceFromObjectOrbiting() * scale;
            float angle = (float) Math.toRadians(this.orbitProgress * 200 % 360);

            this.pos().x = obj.pos().x + (Math.cos(angle) * distance);
            this.pos().y = 56;
            this.pos().z = obj.pos().z + (Math.sin(angle) * distance);
        }
    }

    public void tryCollide(World world)
    {
        float pradius = this.getObjectSize() * SpaceManager.instance.getPlanetaryScale();
        float sradius = this.getObjectSize() * SpaceManager.instance.getStarScale();
        
        for (OrbitableObject object : new ArrayList<OrbitableObject>(SpaceManager.instance.getObjectsInSpace()))
        {
            if (object != this && object instanceof Planet && this instanceof Planet)
            {
                float radiusOther = object.getObjectSize() * SpaceManager.instance.getPlanetaryScale();

                if (this.pos.x + pradius + radiusOther > object.pos.x && this.pos.x < object.pos.x + pradius + radiusOther && this.pos.z + pradius + radiusOther > object.pos.z && this.pos.z < object.pos.z + pradius + radiusOther)
                {
                    if (distanceTo(this, object) < pradius + radiusOther)
                    {
                        this.onCollisionWith(object);
                    }
                }
            }
        }

        for (Entity entity : new ArrayList<Entity>(world.loadedEntityList))
        {
            AxisAlignedBB aabbPlanet = new AxisAlignedBB(this.pos().x - pradius, this.pos().y - pradius, this.pos().z - pradius, this.pos().x + pradius, this.pos().y + pradius, this.pos().z + pradius);
            AxisAlignedBB aabbSun = new AxisAlignedBB(this.pos().x - sradius, this.pos().y - sradius, this.pos().z - sradius, this.pos().x + sradius, this.pos().y + sradius, this.pos().z + sradius);

            if (entity != null && entity.getEntityBoundingBox() != null)
            {
                if (entity.getEntityBoundingBox().intersects(aabbPlanet))
                {
                    if (this instanceof Planet)
                    {
                        this.onCollisionWith(entity);
                    }
                }
                
                if (entity.getEntityBoundingBox().intersects(aabbSun))
                {
                    if (this instanceof SolarSystem)
                    {
                        entity.attackEntityFrom(DamageSource.IN_FIRE, 10F);
                        entity.setFire(5);
                        this.onCollisionWith(entity);
                    }
                }
            }
        }
    }

    protected void onCollisionWith(Entity entity)
    {
        System.out.println(String.format("AABB Collision (%s, %s)", this.getName(), entity.getName()));

        if (this instanceof Planet)
        {
            Planet planet = (Planet) this;

            if (planet.getDimension() != null)
            {
                if (entity instanceof EntityStarship && entity != null && entity.getRidingEntity() != null && entity.getRidingEntity() instanceof EntityPlayerMP)
                {
                    EntityPlayerMP player = (EntityPlayerMP) entity.getRidingEntity();

                    if (!entity.world.isRemote)
                    {
                        Starway.dimensions().transferPlayerToDimension(player, planet.getDimension());
                    }
                }
            }
        }
    }

    protected void onCollisionWith(OrbitableObject object)
    {
        // System.out.println(String.format("Object Collision (%s, %s)", this.getName(), object.getName()));
    }

    public double distanceTo(OrbitableObject a, OrbitableObject b)
    {
        double distance = Math.sqrt(((a.pos.x - b.pos.x) * (a.pos.x - b.pos.x)) + ((a.pos.y - b.pos.y) * (a.pos.y - b.pos.y)));

        if (distance < 0)
        {
            distance = distance * -1;
        }

        return distance;
    }

    @Override
    public void load(Load event)
    {
        ;
    }

    @Override
    public void save(Save event)
    {
        ;
    }

    @Override
    public void unload(Unload event)
    {
        ;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        this.pos = new Pos(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
        this.orbitProgress = tag.getFloat("orbitProgress");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag.setDouble("x", this.pos().x);
        tag.setDouble("y", this.pos().y);
        tag.setDouble("z", this.pos().z);
        tag.setFloat("orbitProgress", this.orbitProgress);

        return tag;
    }

    @Override
    public OrbitableObject getObjectOrbiting()
    {
        return this.objectOrbiting;
    }

    @Override
    public Pos pos()
    {
        return this.pos;
    }
    
    @Override
    public float getRotationYaw()
    {
        return (Game.minecraft().world.getWorldTime() % 360) + Game.partialTicks();
    }

    @Override
    public float getOrbitProgress()
    {
        return this.orbitProgress;
    }

    public void setCoords(Pos coords)
    {
        this.pos = coords;
    }

    public void setObjectOrbiting(OrbitableObject objectOrbiting)
    {
        this.objectOrbiting = objectOrbiting;
    }

    @SideOnly(Side.CLIENT)
    public void drawObjectTag(Renderer renderer, float renderPartialTicks)
    {
        OpenGL.pushMatrix();
        {
            Draw.drawCenteredRectWithOutline(0, 0, 1, 1, 0, 0xFF00AAFF, 0x000000);
            Draw.drawStringAlignCenter(this.getName(), 0, 0 - 10 - renderer.getTextPadding() * 2, 0xFF00CCFF, false);
            Draw.drawStringAlignCenter(String.format("(X: %s, Z: %s)", this.pos().x, this.pos().z), 0, 0 - renderer.getTextPadding() * 2, 0xFFFFFFFF, false);
        }
        OpenGL.popMatrix();
    }

    @SideOnly(Side.CLIENT)
    public static void drawOrbitMarker(float spacing, double x, double y)
    {
        float scale = 1F + Renderer.instance.zoom * (Renderer.instance.zoom) / 1;

        for (float i = 360 / spacing; i > 0; i--)
        {
            OpenGL.pushMatrix();
            OpenGL.rotate(i * spacing, 0, 0, 1);
            OpenGL.translate(x, y, 0);
            OpenGL.scale(scale, scale, 1F);
            Draw.drawRect(0, 0, 1, 1, 0xFF00AAFF);
            OpenGL.popMatrix();
        }
    }
}
