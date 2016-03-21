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
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.util.List;
import java.util.TreeSet;

public class CommandExplorers extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "explorers";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/explorers [amount of players] [dimension] [x z]";
    }

    @Override
    public boolean isUsernameIndex(final String[] args, final int userIndex)
    {
        return false;
    }

    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args)
    {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        int n = 10;
        if (args.length > 0) n = parseIntWithMin(sender, args[0], 1);
        int dim = 0;
        if (args.length > 1) dim = parseInt(sender, args[1]);
        WorldServer worldServer = DimensionManager.getWorld(dim);
        if (worldServer == null) throw new WrongUsageException("Dim " + dim + " doesn't exist.");
        Location location = new Location(worldServer.getSpawnPoint(), dim);
        if (args.length == 5) location.set(parseInt(sender, args[2]), parseInt(sender, args[3]), parseInt(sender, args[4]));

        TreeSet<Pair> players = new TreeSet<>();
        //noinspection unchecked
        for (EntityPlayer player : (List<EntityPlayer>)worldServer.playerEntities)
        {
            int x = location.getX() - (int) player.posX;
            int z = location.getZ() - (int) player.posZ;

            int i = (int) Math.sqrt(x*x + z*z);
            players.add(new Pair(player, i));
        }

        sender.addChatMessage(new ChatComponentText("Center: ").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)).appendSibling(location.toClickableChatString()));

        int i = 0;
        for (Pair pair : players.descendingSet())
        {
            if (++i > n) break;
            sender.addChatMessage(new ChatComponentText(pair.player.getDisplayName() + " is " + pair.distance + "m away."));
        }
    }

    public static final class Pair implements Comparable
    {
        public final EntityPlayer player;
        public final int distance;

        public Pair(EntityPlayer player, int distance)
        {
            this.player = player;
            this.distance = distance;
        }

        @Override
        public int compareTo(Object o)
        {
            if (o instanceof Pair) return distance - ((Pair) o).distance;
            return 0;
        }
    }
}