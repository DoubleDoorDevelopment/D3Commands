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

import net.doubledoordev.d3commands.util.BlockPosDim;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.util.TreeSet;

public class CommandExplorers extends CommandBase
{
    @Override
    public String getName()
    {
        return "explorers";
    }

    @Override
    public String getUsage(ICommandSender icommandsender)
    {
        return "/explorers [amount of players] [dimension] [x z]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        int n = 10;
        if (args.length > 0) n = parseInt(args[0], 1);
        int dim = 0;
        if (args.length > 1) dim = parseInt(args[1]);
        WorldServer worldServer = DimensionManager.getWorld(dim);
        if (worldServer == null) throw new WrongUsageException("Dim " + dim + " doesn't exist.");
        BlockPosDim location = args.length == 5 ?
                new BlockPosDim(parseInt(args[2]), parseInt(args[3]), parseInt(args[4]), dim) :
                new BlockPosDim(worldServer.getSpawnPoint(), dim);

        TreeSet<Data> players = new TreeSet<>();
        for (EntityPlayer player : worldServer.playerEntities)
        {
            int x = location.getX() - (int) player.posX;
            int z = location.getZ() - (int) player.posZ;

            int i = (int) Math.sqrt(x*x + z*z);
            players.add(new Data(player, i));
        }

        sender.sendMessage(new TextComponentString("Center: ").setStyle(new Style().setColor(TextFormatting.AQUA)).appendSibling(location.toClickableChatString()));

        int i = 0;
        for (Data data : players.descendingSet())
        {
            if (++i > n) break;
            sender.sendMessage(new TextComponentString(data.player.getDisplayNameString() + " is " + data.distance + "m away. ").appendSibling(new BlockPosDim(data.player).toClickableChatString()));
        }
    }

    public static final class Data implements Comparable
    {
        public final EntityPlayer player;
        public final int distance;

        public Data(EntityPlayer player, int distance)
        {
            this.player = player;
            this.distance = distance;
        }

        @Override
        public int compareTo(Object o)
        {
            if (o instanceof Data) return distance - ((Data) o).distance;
            return 0;
        }
    }
}