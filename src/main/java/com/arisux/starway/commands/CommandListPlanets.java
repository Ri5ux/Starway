package com.arisux.starway.commands;

import java.util.ArrayList;
import java.util.List;

import com.arisux.starway.Starway;
import com.arisux.starway.api.IPlanet;
import com.arisux.starway.api.Planet;
import com.asx.mdx.lib.util.Chat;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import scala.actors.threadpool.Arrays;

public class CommandListPlanets extends CommandBase
{
    @Override
    public String getName()
    {
        return "listplanets";
    }

    @Override
    @SuppressWarnings("all")
    public List<String> getAliases()
    {
        return Arrays.asList(new String[] { "planets", "lp" });
    }

    @Override
    public String getUsage(ICommandSender commandSender)
    {
        return "Sends a list of planets to the player's chat.";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender commandSender, String[] args)
    {
        ArrayList<Planet> planets = Starway.getPlanets();
        StringBuilder stringPlanets = new StringBuilder();
        String format = "&a%s&f &8[&7%s&8]&f, ";

        for (IPlanet planet : planets)
        {
            if (planet != null)
            {
                if (planets.indexOf(planet) == (planets.size() - 1))
                {
                    format = format.replace(", ", "");
                }
                stringPlanets.append(String.format(format, planet.getName(), planet.getDimension().getId()));
            }
        }

        commandSender.sendMessage(Chat.component("Planets: " + stringPlanets.toString()));
    }
}
