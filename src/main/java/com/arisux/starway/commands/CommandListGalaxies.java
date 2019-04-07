package com.arisux.starway.commands;

import java.util.ArrayList;
import java.util.List;

import com.arisux.starway.Starway;
import com.arisux.starway.api.Galaxy;
import com.arisux.starway.api.IGalaxy;
import com.asx.mdx.lib.util.Chat;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import scala.actors.threadpool.Arrays;

public class CommandListGalaxies extends CommandBase
{
    @Override
    public String getName()
    {
        return "listgalaxies";
    }
    
    @Override
    public List<String> getAliases()
    {
        return Arrays.asList(new String[] { "galaxies", "lg" });
    }

    @Override
    public String getUsage(ICommandSender commandSender)
    {
        return "Sends a list of galaxies to the player's chat.";
    }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        ArrayList<Galaxy> galaxies = Starway.getGalaxies();
        StringBuilder stringGalaxies = new StringBuilder();
        String format = "&a%s&f &8[&7%s Solar System(s)&8]&f, ";

        for (IGalaxy galaxy : galaxies)
        {
            if (galaxy != null)
            {
                if (galaxies.indexOf(galaxy) == (galaxies.size() - 1))
                {
                    format = format.replace(", ", "");
                }
                stringGalaxies.append(String.format(format, galaxy.getName(), galaxy.getSolarSystems().size()));
            }
        }

        sender.sendMessage(Chat.component("Galaxies: " + stringGalaxies.toString()));
    }
}
