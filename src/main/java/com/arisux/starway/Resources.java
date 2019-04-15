package com.arisux.starway;

import com.asx.mdx.core.mods.IPreInitEvent;
import com.asx.mdx.lib.client.model.loaders.WavefrontModelLoader;
import com.asx.mdx.lib.client.util.models.wavefront.TriangulatedWavefrontModel;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Resources implements IPreInitEvent
{
    public static final Resources instance        = new Resources();
    private final Models          models          = new Models();

    public final ResourceLocation SKY_MOON_PHASES = new ResourceLocation("textures/environment/moon_phases.png");
    public final ResourceLocation SKY_SUN         = new ResourceLocation("textures/environment/sun.png");
    public final ResourceLocation SKY_CLOUDMAP    = new ResourceLocation("textures/environment/clouds.png");
    
    public final ResourceLocation EARTH = new ResourceLocation("starway", "textures/planets/earth.png");
    public final ResourceLocation BLACKHOLE_ACCRETION_DISC = new ResourceLocation("starway", "textures/blackholes/accretion_disc.png");

    public static class Models
    {
        public final TriangulatedWavefrontModel STARSHIP = WavefrontModelLoader.load(Starway.class, Starway.ID, "starship", "/assets/starway/models/starship");
    }

    @Override
    public void pre(FMLPreInitializationEvent event)
    {
        ;
    }

    public Models models()
    {
        return models;
    }
}
