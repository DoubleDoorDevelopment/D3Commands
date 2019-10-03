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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import it.unimi.dsi.fastutil.ints.IntSortedSet;
import net.doubledoordev.d3commands.ModConfig;
import net.doubledoordev.d3commands.util.BlockPosDim;
import net.doubledoordev.d3commands.util.DimChanger;

public class CommandTpx extends CommandBase
{
    @Override
    public String getName()
    {
        return "tpx";
    }

    @Override
    public String getUsage(ICommandSender icommandsender)
    {
        return "d3.cmd.tpx.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP target;
        EntityPlayerMP anchor;
        int dim;

        //TODO: Document.
        switch (args.length)
        {
            default:
                throw new WrongUsageException(getUsage(sender));
            case 1:
                target = getCommandSenderAsPlayer(sender);
                dim = parseInt(args[0]);
                if (target.dimension != parseInt(args[0]))
                {
                    DimChanger.changeDim(target, dim);
                    World world = target.getEntityWorld();
                    BlockPosDim pos = new BlockPosDim(world.getTopSolidOrLiquidBlock(target.world.getSpawnPoint()), target.dimension);
                    target.connection.setPlayerLocation(pos.getX(), pos.getY(), pos.getZ(), target.cameraYaw, target.cameraPitch);
                    sender.sendMessage(new TextComponentTranslation("d3.cmd.tpx.success.self", dim));
                }
                else sender.sendMessage(new TextComponentTranslation("d3.cmd.tpx.fail.self", dim));
                break;
            case 2:
                target = getPlayer(server, sender, args[1]);
                anchor = getPlayer(server, sender, args[0]);
                dim = anchor.world.provider.getDimension();
                if (target.dimension != anchor.dimension)
                {
                    DimChanger.changeDim(target, dim);
                    BlockPosDim pos1 = new BlockPosDim(anchor.getPosition(), target.dimension);
                    sender.sendMessage(new TextComponentTranslation("d3.cmd.tpx.success.other", target.getDisplayName(), anchor.getDisplayName(), dim).appendText(" ").appendSibling(pos1.toClickableChatString()));
                }
                else sender.sendMessage(new TextComponentTranslation("d3.cmd.tpx.fail.other", dim));
                break;
            case 4:
                target = getCommandSenderAsPlayer(sender);
                dim = parseInt(args[0]);
                Vec3d vec3d = sender.getPositionVector();
                CommandBase.CoordinateArg xPos = parseCoordinate(vec3d.x, args[1], true);
                CommandBase.CoordinateArg yPos = parseCoordinate(vec3d.y, args[2], -512, 512, false);
                CommandBase.CoordinateArg zPos = parseCoordinate(vec3d.z, args[3], true);
                if (target.dimension != parseInt(args[0]))
                {
                    DimChanger.changeDim(target, dim);
                    target.connection.setPlayerLocation(xPos.getResult(), yPos.getResult(), zPos.getResult(), target.cameraYaw, target.cameraPitch);
                    BlockPosDim pos2 = new BlockPosDim(target.getPosition(), target.dimension);
                    sender.sendMessage(new TextComponentTranslation("d3.cmd.tpx.success.self.pos", dim).appendText(" ").appendSibling(pos2.toClickableChatString()));
                }
                else sender.sendMessage(new TextComponentTranslation("d3.cmd.tpx.fail.self.pos", dim));
                break;
            case 5:
                target = getPlayer(server, sender, args[0]);
                dim = parseInt(args[1]);
                Vec3d vec3d1 = sender.getPositionVector();
                CommandBase.CoordinateArg xPos1 = parseCoordinate(vec3d1.x, args[1], true);
                CommandBase.CoordinateArg yPos1 = parseCoordinate(vec3d1.y, args[2], -512, 512, false);
                CommandBase.CoordinateArg zPos1 = parseCoordinate(vec3d1.z, args[3], true);
                if (target.dimension != parseInt(args[0]))
                {
                    DimChanger.changeDim(target, dim);
                    target.connection.setPlayerLocation(xPos1.getResult(), yPos1.getResult(), zPos1.getResult(), target.cameraYaw, target.cameraPitch);
                    BlockPosDim pos3 = new BlockPosDim(target.getPosition(), target.dimension);
                    sender.sendMessage(new TextComponentTranslation("d3.cmd.tpx.success.other.pos", target.getDisplayName(), dim).appendText(" ").appendSibling(pos3.toClickableChatString()));
                }
                else
                    sender.sendMessage(new TextComponentTranslation("d3.cmd.tpx.fail.other.pos", target.getDisplayName(), dim));
                break;
        }
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return ModConfig.tpxPermissionLevel;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        ArrayList<String> dims = new ArrayList<>();
        for (Map.Entry<DimensionType, IntSortedSet> data : DimensionManager.getRegisteredDimensions().entrySet())
        {
            dims.add(String.valueOf(data.getValue().iterator().next()));
        }

        switch (args.length)
        {
            case 1:
                return getListOfStringsMatchingLastWord(args, dims);
            case 2:
                return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        return super.getTabCompletions(server, sender, args, pos);
    }
}
