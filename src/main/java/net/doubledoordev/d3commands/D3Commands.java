package net.doubledoordev.d3commands;

import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.doubledoordev.d3commands.commands.*;
import net.doubledoordev.d3core.util.ID3Mod;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import java.util.List;

import static net.doubledoordev.d3commands.util.Constants.MODID;
import static net.doubledoordev.d3commands.util.Constants.NAME;

@Mod(modid = MODID, name = NAME, canBeDeactivated = false)
public class D3Commands implements ID3Mod
{
    @Mod.Instance(MODID)
    public static D3Commands instance;

    private boolean tps =   true;
    private boolean tpx =   true;
    private boolean top =   true;
    private boolean kill =  true;
    private boolean getuuid = true;
    public Configuration configuration;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        configuration = new Configuration(event.getSuggestedConfigurationFile());
        syncConfig();
    }

    @Override
    public void syncConfig()
    {
        configuration.setCategoryLanguageKey(MODID, "d3.cmd.config.cmd");
        configuration.addCustomCategoryComment(MODID, "Set any value to false to disable the command.");
        configuration.setCategoryRequiresWorldRestart(MODID, true);

        tps = configuration.getBoolean("tps", MODID, tps, "A TPS command for all players, not just ops.");
        tpx = configuration.getBoolean("tpx", MODID, tpx, "Interdimensional TP command.");
        top = configuration.getBoolean("top", MODID, top, "Teleport yourself to the highest block above you.");
        kill = configuration.getBoolean("kill", MODID, kill, "Allow you to kill other players.");
        getuuid = configuration.getBoolean("getuuid", MODID, getuuid, "Allows easy UUID grabbing.");

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
        if (tps)        event.registerServerCommand(new CommandTps());
        if (tpx)        event.registerServerCommand(new CommandTpx());
        if (top)        event.registerServerCommand(new CommandTop());
        if (kill)       event.registerServerCommand(new CommandKill());
        if (getuuid)    event.registerServerCommand(new CommandGetUUID());
    }
}

