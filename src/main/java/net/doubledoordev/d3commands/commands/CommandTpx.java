package net.doubledoordev.d3commands.commands;

import net.doubledoordev.d3commands.util.Location;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.util.List;

public class CommandTpx extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "tpx";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/tpx <target player> <destination player> OR /tp <target player> [Dimension ID] [x] [y] [z]";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public boolean isUsernameIndex(final String[] args, final int userIndex)
    {
        return userIndex == 0 || userIndex == 1;
    }

    public static boolean isNumeric(final String str)
    {
        try
        {// Try to parse the string as a double
            final double d = Double.parseDouble(str);
        }
        catch (final NumberFormatException nfe)
        {// if an exception is created the it is not a number
            return false;
        }
        // if we get here an exception was not raised thus it is a number, and we can return true
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        Location targetLocation = null;
        EntityPlayerMP targetPlayer = null;

        if (args.length == 1) // [dim] OR [Location target]
        {
            if (isNumeric(args[0])) // tp to dimension
            {
                final World dimension = DimensionManager.getWorld(Integer.parseInt(args[0]));
                if (dimension != null)
                {
                    targetPlayer = getCommandSenderAsPlayer(sender);
                    targetLocation = new Location(dimension.getSpawnPoint(), dimension.provider.dimensionId);
                }
            }
            else // tp to player
            {
                targetPlayer = getCommandSenderAsPlayer(sender);
                targetLocation = new Location(getPlayer(sender, args[0]));
            }
        }
        else if (args.length == 2) // [TP target] [dim] OR [TP target] [Location target]
        {
            if (isNumeric(args[1]))
            {
                final World dimension = DimensionManager.getWorld(Integer.parseInt(args[1]));
                if (dimension != null)
                {
                    targetLocation = new Location(dimension.getSpawnPoint(), dimension.provider.dimensionId);
                    targetPlayer = getPlayer(sender, args[0]);
                }
            }
            else
            {
                targetLocation = new Location(getPlayer(sender, args[1]));
                targetPlayer = getPlayer(sender, args[0]);
            }
        }
        else if (args.length == 4) // [dim] [x] [y] [z]
        {
            int dim = parseInt(sender, args[0]);
            int x = parseIntBounded(sender, args[1], -30000000, 30000000);
            int y = parseIntWithMin(sender, args[2], -10);
            int z = parseIntBounded(sender, args[3], -30000000, 30000000);

            targetLocation = new Location(x, y, z, dim);
            targetPlayer = getCommandSenderAsPlayer(sender);
        }
        else if (args.length == 5) // [TP target] [dim] [x] [y] [z]
        {
            int dim = parseInt(sender, args[1]);
            int x = parseIntBounded(sender, args[2], -30000000, 30000000);
            int y = parseIntWithMin(sender, args[3], -10);
            int z = parseIntBounded(sender, args[4], -30000000, 30000000);

            targetLocation = new Location(x, y, z, dim);
            targetPlayer = getPlayer(sender, args[0]);
        }

        if (targetLocation == null || targetPlayer == null) throw new WrongUsageException(getCommandUsage(sender));

        targetLocation.teleport(targetPlayer);
        sender.addChatMessage(new ChatComponentTranslation("d3.cmd.tp.success", targetPlayer.getDisplayName()).appendText(" ").appendSibling(targetLocation.toClickableChatString()));
    }

    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args)
    {
        if (args.length > 2) return null;
        return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    }
}
