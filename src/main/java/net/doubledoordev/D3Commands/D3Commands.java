package net.doubledoordev.D3Commands;


import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.doubledoordev.D3Commands.Commands.CommandKill;
import net.doubledoordev.D3Commands.Commands.CommandTps;
import net.doubledoordev.D3Commands.Commands.CommandTpx;


@Mod(modid = "D3Commands", name = "d3commands", version = "1.0.0")
public class D3Commands
{


    @Mod.Instance("d3commands")
    public static D3Commands instance = new D3Commands();

    @Mod.Metadata
    private ModMetadata metadata;

    @Mod.EventHandler
    public void preInit(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandTps());
        event.registerServerCommand(new CommandTpx());
        event.registerServerCommand(new CommandKill());
}

}

