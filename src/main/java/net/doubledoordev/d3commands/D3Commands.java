/*
 * Copyright (c) 2014-2016, Dries007 & DoubleDoorDevelopment
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.doubledoordev.d3commands;

import net.doubledoordev.d3commands.commands.*;
import net.doubledoordev.d3commands.entry.BasicCommandEntry;
import net.doubledoordev.d3commands.entry.CommandEntry;
import net.doubledoordev.d3commands.entry.ItemCommandEntry;
import net.doubledoordev.d3commands.event.PlayerDeathEventHandler;
import net.doubledoordev.d3core.util.CoreConstants;
import net.minecraft.command.CommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static net.doubledoordev.d3commands.util.Constants.*;

@Mod(modid = MODID, name = NAME, updateJSON = UPDATE_URL, guiFactory = MOD_GUI_FACTORY, dependencies = "after:D3Core", acceptableRemoteVersions = "*")
public class D3Commands
{
    @SuppressWarnings("WeakerAccess")
    @Mod.Instance(MODID)
    public static D3Commands instance;

    private final Map<String, CommandEntry> commandsMap = new HashMap<>();
    private boolean pastStart = false;
    private Logger logger;
    private Configuration configuration;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        configuration = new Configuration(event.getSuggestedConfigurationFile());
        updateConfig();
        MinecraftForge.EVENT_BUS.register(PlayerDeathEventHandler.I);

        final CommandEntry[] a = new CommandEntry[] {
                new BasicCommandEntry(CommandTps.class, "tps", true, "A TPS command for all players, not just ops."),
                new BasicCommandEntry(CommandMem.class, "mem", true, "Shows server memory information."),
                new BasicCommandEntry(CommandTpx.class, "tpx", true, "Interdimensional TP command."),
                new BasicCommandEntry(CommandTop.class, "top", true, "Teleport yourself to the highest block above you."),
                new BasicCommandEntry(CommandHeal.class, "heal", true, "Heal yourself or other players."),
                new BasicCommandEntry(CommandFeed.class, "feed", true, "Feed yourself or other players."),
                new BasicCommandEntry(CommandGetUUID.class, "getuuid", true, "Allows easy UUID grabbing."),
                new BasicCommandEntry(CommandFly.class, "fly", true, "Toggle fly mode."),
                new BasicCommandEntry(CommandGod.class, "god", true, "Toggle god mode."),
                new BasicCommandEntry(CommandBack.class, "back", true, "Teleport back to where you died the last time."),
                new BasicCommandEntry(CommandGm.class, "gm", true, "Shorter /gamemode command."),
                new BasicCommandEntry(CommandInvSee.class, "invsee", true, "Look at someone else's inventory"),
                new BasicCommandEntry(CommandSpawn.class, "spawn", true, "Teleport to spawn"),
                new BasicCommandEntry(CommandPos.class, "pos", true, "Get other players coordinates."),
                new BasicCommandEntry(CommandExplorers.class, "analiselocations", true, "Analise locations of online players."),
                new BasicCommandEntry(CommandSmite.class, "smite", true, "Power! UNLIMITED POWER."),
                new BasicCommandEntry(CommandFireworks.class, "fireworks", true, "Needs more fireworks.")
        };
        for (CommandEntry e : a)
        {
            commandsMap.put(e.getUniqueName(), e);
        }
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        CommandHandler ch = (CommandHandler) event.getServer().getCommandManager();
        for (CommandEntry e : commandsMap.values())
        {
            if (e.isEnabled()) ch.registerCommand(e.getInstance());
        }
        pastStart = true;
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(CoreConstants.MODID)) updateConfig();
    }

    private void updateConfig()
    {
        final String items = "itemcommands";

        configuration.setCategoryLanguageKey(items, "d3.cmd.config.items");
        configuration.addCustomCategoryComment(items, "Make new categories like the example to add new commands that give a specific item.");
        configuration.setCategoryRequiresWorldRestart(items, true);

        {
            final String example = items + ".key";
            configuration.addCustomCategoryComment(example,
                    "Example, don't delete, just disable if you don't want it. Values in here are defaults, except for enabled.\n" +
                    "CHANGES: modid became modids! All mods have to be present for the command to work. Useful for compatibility items.");
            configuration.getString("name", example, "key", "The name of the command. aka the part after the slash. Cannot have spaces. Case sensitive! Required!");
            configuration.getStringList("aliases", example, new String[] {"spectre", "spectrekey"}, "A list of alternative names. Case sensitive!");
            configuration.getBoolean("allowUsername", example, true, "Allow a username to be specified, to give the item to someone else.");
            configuration.getStringList("modids", example, new String[] {"RandomThings"}, "The modid that needs to be loaded for this command to work. Case sensitive!");
            configuration.getString("item", example, "RandomThings:spectreKey", "Like you would use in '/give' Required!");
            configuration.getInt("meta", example, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, "Metadata or Damage value of the item.");
            configuration.getInt("stacksize", example, 1, 0, 64, "The stacksize. 0 is a nice troll btw :p");
            configuration.getString("message", example, "Here you go!", "The message that appears after a successful command.");
            configuration.getString("displayname", example, "", "Set a custom display name if you want it.");
            configuration.getBoolean("enabled", example, false, "Easy enable / disable here. Enabled by default!");
        }

        ConfigCategory root = configuration.getCategory(items);
        for (ConfigCategory cat : root.getChildren())
        {
            if (!commandsMap.containsKey(cat.getQualifiedName()))
            {
                ItemCommandEntry entry = new ItemCommandEntry(cat);
                commandsMap.put(entry.getUniqueName(), entry);
            }
        }

        configuration.setCategoryLanguageKey(MODID, "d3.cmd.config.cmd");
        configuration.addCustomCategoryComment(MODID, "Set any value to false to disable the command.");
        configuration.setCategoryRequiresWorldRestart(MODID, true);

        if (pastStart)
        {
            CommandHandler ch = (CommandHandler) FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager();
            for (CommandEntry e : commandsMap.values())
            {
                boolean was = e.isEnabled();
                e.doConfig(configuration);
                boolean is = e.isEnabled();
                if (pastStart && was != is) // If we are past start, and the status has changed
                {
                    if (!is) // Remove
                    {
                        ch.getCommands().remove(e.getInstance().getCommandName());
                        for (String s : e.getInstance().getCommandAliases()) ch.getCommands().remove(s);
                    }
                    else // Add
                    {
                        ch.registerCommand(e.getInstance());
                    }
                }
            }
        }

        if (configuration.hasChanged()) configuration.save();
    }

    public static Configuration getConfig()
    {
        return instance.configuration;
    }

    public static Logger getLogger()
    {
        return instance.logger;
    }
}

