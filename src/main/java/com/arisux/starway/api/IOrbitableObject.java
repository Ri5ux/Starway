package com.arisux.starway.api;

import com.arisux.starway.Renderer;
import com.asx.mdx.lib.world.Pos;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IOrbitableObject
{
    public String getName();

    public void onInitialization(FMLInitializationEvent event);

    public void onInitialTick(WorldTickEvent event);

    public void onTick(WorldTickEvent event);
    
    public void onGlobalTick(WorldTickEvent event);

    public void load(WorldEvent.Load event);

    public void save(WorldEvent.Save event);

    public void unload(WorldEvent.Unload event);

    public void readFromNBT(NBTTagCompound tag);

    public NBTTagCompound writeToNBT(NBTTagCompound tag);

    public OrbitableObject getObjectOrbiting();

    /** Distance from the object this object is orbiting (light years) **/
    public float getDistanceFromObjectOrbiting();

    /** The size of this object **/
    public float getObjectSize();

    /** The time it takes this object to complete 1 orbit. Measured in earth years. **/
    public float getOrbitTime();

    /** The current rotation value in orbit. **/
    public float getOrbitProgress();

    /** The coordinates of this object. **/
    public Pos pos();
    
    @SideOnly(Side.CLIENT)
    public void renderMap(Renderer renderer, OrbitableObject parentObject, float partialTicks);
    
    @SideOnly(Side.CLIENT)
    public void render(float partialTicks);
}
