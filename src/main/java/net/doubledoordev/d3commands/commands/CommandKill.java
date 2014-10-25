package net.doubledoordev.d3commands.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;

import java.util.List;

public class CommandKill extends net.minecraft.command.CommandKill
{
    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/kill [target player]";
    }

    @Override
    public void processCommand(final ICommandSender sender, final String[] args)
    {
        if (args.length == 1)
        {
            if (sender instanceof  MinecraftServer || MinecraftServer.getServer().getConfigurationManager().func_152596_g(MinecraftServer.getServer().getConfigurationManager().func_152612_a(sender.getCommandSenderName()).getGameProfile()))
            {
                final EntityPlayerMP playerDead = getPlayer(sender, args[0]);
                doKill(playerDead);
            }
            else sender.addChatMessage(new ChatComponentTranslation("commands.generic.permission"));
        }
        else
        {
            EntityPlayerMP playerDead = getCommandSenderAsPlayer(sender);
            if (args.length == 0)
            {
                doKill(playerDead);
            }
            else
            {
                playerDead.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
            }
        }
    }

    private void doKill(EntityPlayerMP playerDead) {
        playerDead.attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
        playerDead.addChatMessage(new ChatComponentTranslation("commands.kill.success"));
    }

    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args)
    {
        if (args.length == 1) return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        return null;
    }
}
