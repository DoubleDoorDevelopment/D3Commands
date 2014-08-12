package net.doubledoordev.D3Commands.Commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;

public class CommandKill extends net.minecraft.command.CommandKill
{
    @Override
    public String getCommandUsage(final ICommandSender sender)
    {
        return "commands.kill.fix.usage";
    }

    @Override
    public void processCommand(final ICommandSender sender, final String[] args)
    {
        if (args.length == 1)
        {
            final EntityPlayerMP playerDead = getPlayer(sender, args[0]);
            playerDead.attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
            playerDead.addChatMessage(IChatComponent.Serializer.func_150699_a("commands.kill.success"));
        }
        else
        {
            EntityPlayerMP playerDead = getCommandSenderAsPlayer(sender);
            if (args.length == 0)
            {
                playerDead.attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
                playerDead.addChatMessage(IChatComponent.Serializer.func_150699_a("commands.kill.success"));
            }
            else
            {
                playerDead.addChatMessage(IChatComponent.Serializer.func_150699_a(getCommandUsage(sender)));
            }
        }
    }
}
