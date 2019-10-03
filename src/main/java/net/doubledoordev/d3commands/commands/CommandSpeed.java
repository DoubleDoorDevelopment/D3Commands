package net.doubledoordev.d3commands.commands;

import java.util.UUID;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandSpeed extends CommandBase
{
    @Override
    public String getName()
    {
        return "speed";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "d3.cmd.speed.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP senderAsPlayer = getCommandSenderAsPlayer(sender);

        switch (args.length)
        {
            default:
                throw new WrongUsageException(getUsage(sender));
            case 1:
                IAttributeInstance iattributeinstance = senderAsPlayer.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
                iattributeinstance.applyModifier(new AttributeModifier(UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D"), "D3Commands SpeedBoost", Double.parseDouble(args[0]), 0).setSaved(true));
                break;
            case 2:
                EntityPlayerMP target = getPlayer(server, sender, args[0]);
                IAttributeInstance iattributeinstance1 = target.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
                iattributeinstance1.applyModifier(new AttributeModifier(UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D"), "D3Commands SpeedBoost", Double.parseDouble(args[1]), 0).setSaved(true));
                break;
        }
    }
}
