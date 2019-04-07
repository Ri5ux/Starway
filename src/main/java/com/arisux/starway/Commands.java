package com.arisux.starway;

import com.arisux.starway.commands.CommandListGalaxies;
import com.arisux.starway.commands.CommandListPlanets;
import com.arisux.starway.commands.CommandListSolarSystems;
import com.arisux.starway.commands.CommandTeleportDimension;
import com.asx.mdx.core.mods.IInitEvent;

import net.minecraft.command.CommandBase;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class Commands implements IInitEvent
{
    public static final Commands instance = new Commands();
    public CommandBase cmdTeleportDimension;
    public CommandBase cmdListPlanets;
    public CommandBase cmdListSolarSystems;
    public CommandBase cmdListGalaxies;

    @Override
    public void init(FMLInitializationEvent event)
    {
        FMLCommonHandler.instance().bus().register(instance);
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(this.cmdTeleportDimension = new CommandTeleportDimension());
        event.registerServerCommand(this.cmdListPlanets = new CommandListPlanets());
        event.registerServerCommand(this.cmdListSolarSystems = new CommandListSolarSystems());
        event.registerServerCommand(this.cmdListGalaxies = new CommandListGalaxies());
    }
}
