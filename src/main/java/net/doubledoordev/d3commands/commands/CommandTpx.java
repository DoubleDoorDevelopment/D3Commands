/*
 * Copyright (c) 2014-2016, Dries007 & DoubleDoorDevelopment
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
