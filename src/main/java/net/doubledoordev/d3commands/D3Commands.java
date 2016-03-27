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

import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.doubledoordev.d3commands.commands.*;
import net.doubledoordev.d3commands.event.PlayerDeathEventHandler;
import net.doubledoordev.d3commands.util.Location;
import net.doubledoordev.d3core.util.ID3Mod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.util.*;

import static net.doubledoordev.d3commands.util.Constants.MODID;
import static net.doubledoordev.d3commands.util.Constants.NAME;

@Mod(modid = MODID, name = NAME, canBeDeactivated = false, acceptableRemoteVersions = "*")
public class D3Commands implements ID3Mod
{
    @Mod.Instance(MODID)
    public static D3Commands instance;

    private List<CommandEntry> commands = new ArrayList<>();

    private PlayerDeathEventHandler pdEventHandler = new PlayerDeathEventHandler();

    public Map<UUID, Location> deathlog = new HashMap<>();

    public Configuration configuration;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        configuration = new Configuration(event.getSuggestedConfigurationFile());
        syncConfig();
        MinecraftForge.EVENT_BUS.register(pdEventHandler);
    }

    @Override
    public void syncConfig()
    {
        configuration.setCategoryLanguageKey(MODID, "d3.cmd.config.cmd");
        configuration.addCustomCategoryComment(MODID, "Set any value to false to disable the command.");
        configuration.setCategoryRequiresWorldRestart(MODID, true);

        commands.add(new CommandEntry(new CommandTps(), configuration.getBoolean("tps", MODID, true, "A TPS command for all players, not just ops.")));
        commands.add(new CommandEntry(new CommandMem(), configuration.getBoolean("mem", MODID, true, "Shows server memory information.")));
        commands.add(new CommandEntry(new CommandTpx(), configuration.getBoolean("tpx", MODID, true, "Interdimensional TP command.")));
        commands.add(new CommandEntry(new CommandTop(), configuration.getBoolean("top", MODID, true, "Teleport yourself to the highest block above you.")));
        commands.add(new CommandEntry(new CommandKill(), configuration.getBoolean("kill", MODID, true, "Allow you to kill other players.")));
        commands.add(new CommandEntry(new CommandHeal(), configuration.getBoolean("heal", MODID, true, "Heal yourself or other players.")));
        commands.add(new CommandEntry(new CommandFeed(), configuration.getBoolean("feed", MODID, true, "Feed yourself or other players.")));
        commands.add(new CommandEntry(new CommandGetUUID(), configuration.getBoolean("getuuid", MODID, true, "Allows easy UUID grabbing.")));
        commands.add(new CommandEntry(new CommandFly(), configuration.getBoolean("fly", MODID, true, "Toggle fly mode.")));
        commands.add(new CommandEntry(new CommandGod(), configuration.getBoolean("god", MODID, true, "Toggle god mode.")));
        commands.add(new CommandEntry(new CommandBack(), configuration.getBoolean("back", MODID, true, "Teleport back to where you died the last time.")));
        commands.add(new CommandEntry(new CommandGm(), configuration.getBoolean("gm", MODID, true, "Shorter /gamemode command.")));
        commands.add(new CommandEntry(new CommandInvSee(), configuration.getBoolean("invsee", MODID, true, "Look at someone else's inventory")));
        commands.add(new CommandEntry(new CommandSpawn(), configuration.getBoolean("spawn", MODID, true, "Teleport to spawn")));
        commands.add(new CommandEntry(new CommandPos(), configuration.getBoolean("pos", MODID, true, "Get other players coordinates.")));
        commands.add(new CommandEntry(new CommandExplorers(), configuration.getBoolean("analiselocations", MODID, true, "Analise locations of online players.")));
        commands.add(new CommandEntry(new CommandSmite(), configuration.getBoolean("smite", MODID, true, "Power! UNLIMITED POWER.")));
        commands.add(new CommandEntry(new CommandFireworks(), configuration.getBoolean("fireworks", MODID, true, "Needs more fireworks.")));

        configuration.setCategoryLanguageKey(MODID, "d3.cmd.config.cmd");
        configuration.addCustomCategoryComment(MODID, "Set any value to false to disable the command.");
        configuration.setCategoryRequiresWorldRestart(MODID, true);

        final String items = "itemcommands";
        configuration.setCategoryLanguageKey(items, "d3.cmd.config.items");
        configuration.addCustomCategoryComment(items, "Make new categories like the example to add new commands that give a specific item.");
        configuration.setCategoryRequiresWorldRestart(items, true);

        {
            final String example = items + ".key";
            configuration.addCustomCategoryComment(example, "Example, don't delete, just disable if you don't want it. Values in here are defaults, except for enabled.");
            configuration.getString("name", example, "key", "The name of the command. aka the part after the slash. Cannot have spaces. Case sensitive! Required!");
            configuration.getStringList("aliases", example, new String[] {"spectre", "spectrekey"}, "A list of alternative names. Case sensitive!");
            configuration.getBoolean("allowUsername", example, true, "Allow a username to be specified, to give the item to someone else.");
            configuration.getString("modid", example, "RandomThings", "The modid part of the item's ID. Use 'minecraft' for vanilla items. This automatically disables the command if the mod isn't loaded. Case sensitive! Required!");
            configuration.getString("itemname", example, "spectreKey", "The item name part of the item's ID. Case sensitive! Required!");
            configuration.getInt("meta", example, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, "Metadata or Damage value of the item.");
            configuration.getInt("stacksize", example, 1, 0, 64, "The stacksize. 0 is a nice troll btw :p");
            configuration.getString("message", example, "Here you go!", "The message that appears after a successful command.");
            configuration.getString("displayname", example, "", "Set a custom display name if you want it.");
            configuration.getBoolean("enabled", example, false, "Easy enable / disable here. Enabled by default!");
        }

        ConfigCategory root = configuration.getCategory(items);
        for (ConfigCategory cat : root.getChildren())
        {
            Property nameProperty = cat.get("name");
            Property aliasesProperty = cat.get("aliases");
            Property allowUsernameProperty = cat.get("allowUsername");
            Property modidProperty = cat.get("modid");
            Property itemnameProperty = cat.get("itemname");
            Property metaProperty = cat.get("meta");
            Property stacksizeProperty = cat.get("stacksize");
            Property messageProperty = cat.get("message");
            Property displaynameProperty = cat.get("displayname");
            Property enabledProperty = cat.get("enabled");
            if (nameProperty == null || modidProperty == null || itemnameProperty == null) throw new RuntimeException("Configuration error. Missing required element on " + cat.getQualifiedName());
            //public CommandGetItem(String name, String[] aliases, boolean allowUsername, String modid, String item, int meta, int stacksize, String message, String displayname)
            commands.add(new CommandEntry(new CommandGetItem(
                    nameProperty.getString(),
                    aliasesProperty == null ? new String[0] : aliasesProperty.getStringList(),
                    allowUsernameProperty == null || allowUsernameProperty.getBoolean(true),
                    modidProperty.getString(),
                    itemnameProperty.getString(),
                    metaProperty == null ? 0 : metaProperty.getInt(0),
                    stacksizeProperty == null ? 1 : stacksizeProperty.getInt(1),
                    messageProperty == null ? "" : messageProperty.getString(),
                    displaynameProperty == null ? "" : displaynameProperty.getString()
            ), (Loader.isModLoaded(modidProperty.getString()) || modidProperty.getString().equals("minecraft")) && (enabledProperty == null || enabledProperty.getBoolean(true))));
        }

        if (configuration.hasChanged()) configuration.save();
    }

    @Override
    public void addConfigElements(List<IConfigElement> configElements)
    {
        configElements.add(new ConfigElement(configuration.getCategory(MODID.toLowerCase())));
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        for (CommandEntry e : commands)
        {
            if (e.isEnabled())
            {
                event.registerServerCommand(e.getCommand());
            }
        }
    }
}

