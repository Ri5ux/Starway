package com.arisux.starway.commands;

import com.arisux.starway.Starway;
import com.arisux.starway.api.IPlanet;
import com.asx.mdx.lib.world.Dimension;
import com.asx.mdx.lib.world.entity.player.Players;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldProvider;

public class CommandTeleportDimension extends CommandBase
{
    @Override
    public String getName()
    {
        return "tpdim";
    }

    @Override
    public String getUsage(ICommandSender commandSender)
    {
        return "Teleport the player to the dimension with the target dimension ID.";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender commandSender, String[] args)
    {
        EntityPlayer player = Players.getPlayerForCommandSender(commandSender);

        if (args.length == 0)
        {
            commandSender.sendMessage(new TextComponentString(String.format("Unable to transport player %s to dimension, no dimension ID provided.", player.getName())));
        }

        if (args.length >= 1)
        {
            Integer dimensionId = Integer.parseInt(args[0]);
            Dimension dimension = Starway.dimensions().getDimensionFor(dimensionId);
            WorldProvider providerForDim = server.getWorld(dimension.getId()).provider;

            if (player instanceof EntityPlayerMP)
            {
                EntityPlayerMP playerMP = (EntityPlayerMP) player;

                if (dimension != null && player != null)
                {
                    IPlanet planet = Starway.getPlanetFor(dimension);

                    if (player.dimension != dimension.getId())
                    {
                        if (planet != null)
                        {
                            commandSender.sendMessage(new TextComponentString(String.format("Transferring %s to planet %s (%s).", player.getName(), planet.getName(), dimension.getId())));
                            Starway.dimensions().transferPlayerToDimension(playerMP, dimension);
                        }
                        else
                        {
                            commandSender.sendMessage(new TextComponentString(String.format("Transferring %s to dimension: %s (%s).", player.getName(), dimension.getName(), dimension.getId())));
                            Starway.dimensions().transferPlayerToDimension(playerMP, dimension);
                        }
                    }
                    else
                    {
                        if (planet != null)
                        {
                            commandSender.sendMessage(new TextComponentString(String.format("%s is already on planet %s (%s).", player.getName(), planet.getName(), dimension.getId())));
                        }
                        else
                        {
                            commandSender.sendMessage(new TextComponentString(String.format("%s is already in the target dimension: %s (%s).", player.getName(), dimension.getName(), dimension.getId())));
                        }
                    }
                }
                else if (player != null && providerForDim != null)
                {
                    if (dimensionId == dimension.getId())
                    {
                        commandSender.sendMessage(new TextComponentString(String.format("Transferring %s to dimension: %s (%s).", player.getName(), dimension.getName(), dimension.getId())));
                        Starway.dimensions().transferPlayerToDimension(playerMP, dimensionId);
                    }
                    else
                    {
                        commandSender.sendMessage(new TextComponentString(String.format("Unable to transfer %s. Dimension %s does not exist.", player.getName(), dimensionId)));
                    }
                }
                else
                {
                    System.out.println(String.format("ERROR: {%s, %s}", dimension == null, player == null));
                }
            }
        }
    }
}
