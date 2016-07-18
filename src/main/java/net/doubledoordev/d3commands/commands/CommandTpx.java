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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

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
        return "/tpx <target> <destination> OR /tp <target> [Dimension ID] [x] [y] [z]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1)
        {
            throw new WrongUsageException(getCommandUsage(sender));
        }
        else
        {
            int i = 0;
            Entity target;

            if (args.length != 2 && args.length != 5 && args.length != 7)
            {
                target = getCommandSenderAsPlayer(sender);
            }
            else
            {
                target = getEntity(server, sender, args[0]);
                i = 1;
            }

            if (args.length != 1 && args.length != 2)
            {
                if (args.length < i + 3)
                {
                    throw new WrongUsageException(getCommandUsage(sender));
                }
                else if (target.worldObj != null)
                {
                    int j = i;
                    int dim = parseInt(args[j++]);
                    CommandBase.CoordinateArg x = parseCoordinate(target.posX, args[j++], true);
                    CommandBase.CoordinateArg y = parseCoordinate(target.posY, args[j++], -512, 512, false);
                    CommandBase.CoordinateArg z = parseCoordinate(target.posZ, args[j++], true);
                    CommandBase.CoordinateArg yaw = parseCoordinate((double)target.rotationYaw, args.length > j ? args[j++] : "~", false);
                    CommandBase.CoordinateArg pitch = parseCoordinate((double)target.rotationPitch, args.length > j ? args[j] : "~", false);

                    BlockPosDim dest;
                    if (target.dimension != dim) target.changeDimension(dim);

                    if (target instanceof EntityPlayerMP)
                    {
                        Set<SPacketPlayerPosLook.EnumFlags> set = EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class);
                        if (x.isRelative()) set.add(SPacketPlayerPosLook.EnumFlags.X);
                        if (y.isRelative()) set.add(SPacketPlayerPosLook.EnumFlags.Y);
                        if (z.isRelative()) set.add(SPacketPlayerPosLook.EnumFlags.Z);
                        if (pitch.isRelative()) set.add(SPacketPlayerPosLook.EnumFlags.X_ROT);
                        if (yaw.isRelative()) set.add(SPacketPlayerPosLook.EnumFlags.Y_ROT);
                        float f = (float)yaw.getAmount();
                        if (!yaw.isRelative()) f = MathHelper.wrapDegrees(f);
                        float f1 = (float)pitch.getAmount();
                        if (!pitch.isRelative()) f1 = MathHelper.wrapDegrees(f1);
                        target.dismountRidingEntity();
                        ((EntityPlayerMP)target).connection.setPlayerLocation(x.getAmount(), y.getAmount(), z.getAmount(), f, f1, set);
                        target.setRotationYawHead(f);
                        dest = new BlockPosDim(x.getAmount(), y.getAmount(), z.getAmount(), dim);
                    }
                    else
                    {
                        float f2 = (float)MathHelper.wrapDegrees(yaw.getResult());
                        float f3 = (float)MathHelper.wrapDegrees(pitch.getResult());
                        f3 = MathHelper.clamp_float(f3, -90.0F, 90.0F);
                        target.setLocationAndAngles(x.getResult(), y.getResult(), z.getResult(), f2, f3);
                        target.setRotationYawHead(f2);
                        dest = new BlockPosDim(x.getResult(), y.getResult(), z.getResult(), dim);
                    }
                    sender.addChatMessage(new TextComponentTranslation("d3.cmd.tp.success", target.getDisplayName()).appendText(" ").appendSibling(dest.toClickableChatString()));
                }
            }
            else
            {
                Entity dest = getEntity(server, sender, args[args.length - 1]);

                if (target.dimension != dest.dimension) target.changeDimension(dest.dimension);
                target.dismountRidingEntity();

                if (target instanceof EntityPlayerMP)
                {
                    ((EntityPlayerMP)target).connection.setPlayerLocation(dest.posX, dest.posY, dest.posZ, dest.rotationYaw, dest.rotationPitch);
                    sender.addChatMessage(new TextComponentTranslation("d3.cmd.tp.success", target.getDisplayName()).appendText(" ").appendSibling(new TextComponentString(((EntityPlayerMP) dest).getDisplayNameString())));
                }
                else
                {
                    target.setLocationAndAngles(dest.posX, dest.posY, dest.posZ, dest.rotationYaw, dest.rotationPitch);
                    sender.addChatMessage(new TextComponentTranslation("d3.cmd.tp.success", target.getDisplayName()).appendText(" ").appendSibling(new TextComponentString(dest.getName())));
                }
            }
        }
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

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if (args.length == 1) return getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        return super.getTabCompletionOptions(server, sender, args, pos);
    }
}
