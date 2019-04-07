package com.arisux.starway.commands;

import java.util.ArrayList;
import java.util.List;

import com.arisux.starway.Starway;
import com.arisux.starway.api.ISolarSystem;
import com.arisux.starway.api.SolarSystem;
import com.asx.mdx.lib.util.Chat;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import scala.actors.threadpool.Arrays;

public class CommandListSolarSystems extends CommandBase
{
    @Override
    public String getName()
    {
        return "listsolarsystems";
    }

    @Override
    @SuppressWarnings("all")
    public List<String> getAliases()
    {
        return Arrays.asList(new String[] { "solarsystems", "lss" });
    }

    @Override
    public String getUsage(ICommandSender commandSender)
    {
        return "Sends a list of solar systems to the player's chat.";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender commandSender, String[] args)
    {
        ArrayList<SolarSystem> solarSystems = Starway.getSolarSystems();
        StringBuilder stringSolarSystems = new StringBuilder();
        String format = "&a%s&f &8[&7%s Planet(s)&8]&f, ";

        for (ISolarSystem solarSystem : solarSystems)
        {
            if (solarSystem != null)
            {
                if (solarSystems.indexOf(solarSystem) == (solarSystems.size() - 1))
                {
                    format = format.replace(", ", "");
                }
                stringSolarSystems.append(String.format(format, solarSystem.getName(), solarSystem.getPlanets().size()));
            }
        }

        commandSender.sendMessage(Chat.component("Solar Systems: " + stringSolarSystems.toString()));
    }
}
