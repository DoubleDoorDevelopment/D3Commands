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
    private static final DamageSource KILL_DAMAGE_SOURCE = new DamageSource("generic").setDamageAllowedInCreativeMode().setDamageBypassesArmor();

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/kill [target player]";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public boolean isUsernameIndex(final String[] args, final int userIndex)
    {
        return userIndex == 0;
    }

    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args)
    {
        if (args.length == 1) return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        return null;
    }

    @Override
    public void processCommand(final ICommandSender sender, final String[] args)
    {
        EntityPlayerMP target;

        if (args.length == 0) target = getCommandSenderAsPlayer(sender);
        else target = getPlayer(sender, args[0]);

        target.attackEntityFrom(KILL_DAMAGE_SOURCE, Float.MAX_VALUE);

        sender.addChatMessage(new ChatComponentTranslation("d3.cmd.kill.success", target.getDisplayName()));
    }
}
