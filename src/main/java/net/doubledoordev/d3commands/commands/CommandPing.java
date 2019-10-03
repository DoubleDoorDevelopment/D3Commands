package net.doubledoordev.d3commands.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

import net.doubledoordev.d3commands.ModConfig;

public class CommandPing extends CommandBase
{
    @Override
    public String getName()
    {
        return "ping";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "d3.cmd.ping.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP target;
        if (args.length == 0) target = getCommandSenderAsPlayer(sender);
        else target = getPlayer(server, sender, args[0]);

        sender.sendMessage(new TextComponentTranslation("d3.cmd.ping", target.getDisplayName(), target.ping));
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return ModConfig.locateportalPermissionLevel;
    }
}