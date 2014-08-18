package net.doubledoordev.d3commands;

import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.doubledoordev.d3commands.commands.CommandKill;
import net.doubledoordev.d3commands.commands.CommandTps;
import net.doubledoordev.d3commands.commands.CommandTpx;
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
    private boolean kill =  true;
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
        kill = configuration.getBoolean("kill", MODID, kill, "Allow you to kill other players.");

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
        if (tps)    event.registerServerCommand(new CommandTps());
        if (tpx)    event.registerServerCommand(new CommandTpx());
        if (kill)   event.registerServerCommand(new CommandKill());
    }
}

