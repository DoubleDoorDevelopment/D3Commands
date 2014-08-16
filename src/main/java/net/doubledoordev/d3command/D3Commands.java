package net.doubledoordev.d3command;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.doubledoordev.d3command.commands.CommandKill;
import net.doubledoordev.d3command.commands.CommandTps;
import net.doubledoordev.d3command.commands.CommandTpx;
import net.doubledoordev.lib.DevPerks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

import static net.doubledoordev.d3command.util.Constants.MODID;
import static net.doubledoordev.d3command.util.Constants.NAME;

@Mod(modid = MODID, name = NAME)
public class D3Commands
{
    @Mod.Instance(MODID)
    public static D3Commands instance = new D3Commands();
    private Logger logger;
    private boolean debug = false;
    private boolean tps =   true;
    private boolean tpx =   true;
    private boolean kill =  true;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);

        Configuration configuration = new Configuration(event.getSuggestedConfigurationFile());

        final String cat = MODID + ".cmd";
        configuration.addCustomCategoryComment(cat, "Set any value to false to disable the command.");

        tps =   configuration.getBoolean("tps",     cat, tps,   "A TPS command for all players, not just ops.");
        tpx =   configuration.getBoolean("tpx",     cat, tpx,   "Interdimensional TP command.");
        kill =  configuration.getBoolean("kill",    cat, kill,  "Allow you to kill other players.");

        debug = configuration.getBoolean("debug", MODID, debug, "Enable extra debug output.");
        if (configuration.getBoolean("sillyness", MODID, true, "Disable sillyness only if you want to piss off the developers XD")) MinecraftForge.EVENT_BUS.register(new DevPerks(debug));

        if (configuration.hasChanged()) configuration.save();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        if (tps)    event.registerServerCommand(new CommandTps());
        if (tpx)    event.registerServerCommand(new CommandTpx());
        if (kill)   event.registerServerCommand(new CommandKill());
    }
}

