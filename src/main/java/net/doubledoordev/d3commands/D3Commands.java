package net.doubledoordev.d3commands;

import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.doubledoordev.d3commands.commands.*;
import net.doubledoordev.d3core.util.ID3Mod;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.List;

import static net.doubledoordev.d3commands.util.Constants.MODID;
import static net.doubledoordev.d3commands.util.Constants.NAME;

@Mod(modid = MODID, name = NAME, canBeDeactivated = false)
public class D3Commands implements ID3Mod
{
    @Mod.Instance(MODID)
    public static D3Commands instance;

    private List<CommandEntry> commands = new ArrayList<>();

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

        commands.add(new CommandEntry(new CommandTps(), configuration.getBoolean("tps", MODID, true, "A TPS command for all players, not just ops.")));
        commands.add(new CommandEntry(new CommandTpx(), configuration.getBoolean("tpx", MODID, true, "Interdimensional TP command.")));
        commands.add(new CommandEntry(new CommandTop(), configuration.getBoolean("top", MODID, true, "Teleport yourself to the highest block above you.")));
        commands.add(new CommandEntry(new CommandKill(), configuration.getBoolean("kill", MODID, true, "Allow you to kill other players.")));
        commands.add(new CommandEntry(new CommandHeal(), configuration.getBoolean("heal", MODID, true, "Heal yourself or other players.")));
        commands.add(new CommandEntry(new CommandFeed(), configuration.getBoolean("feed", MODID, true, "Feed yourself or other players.")));
        commands.add(new CommandEntry(new CommandGetUUID(), configuration.getBoolean("getuuid", MODID, true, "Allows easy UUID grabbing.")));
        commands.add(new CommandEntry(new CommandFly(), configuration.getBoolean("fly", MODID, true, "Toggle fly mode.")));

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
        for(CommandEntry e : commands){
            if(e.isEnabled()){
                event.registerServerCommand(e.getCommand());
            }
        }
    }
}

