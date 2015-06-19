package net.doubledoordev.d3commands.commands;

import net.doubledoordev.d3commands.util.Location;
import net.minecraft.block.material.Material;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

import java.util.List;

public class CommandTop extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "top";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/top [target]";
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
    public void processCommand(ICommandSender sender, String[] args)
    {
        EntityPlayerMP target;

        if (args.length == 0) target = getCommandSenderAsPlayer(sender);
        else target = getPlayer(sender, args[0]);

        World world = target.getEntityWorld();
        for (int y = world.getActualHeight(); y > target.getPlayerCoordinates().posY; y--)
        {
            if (world.getBlock(target.getPlayerCoordinates().posX, y, target.getPlayerCoordinates().posZ).getMaterial() != Material.air)
            {
                Location location = new Location(target.getPlayerCoordinates().posX, y + 2, target.getPlayerCoordinates().posZ, world.provider.dimensionId);
                location.teleport(target);
                sender.addChatMessage(new ChatComponentTranslation("d3.cmd.tp.success", target.getDisplayName()).appendText(" ").appendSibling(location.toClickableChatString()));
                return;
            }
        }
    }
}