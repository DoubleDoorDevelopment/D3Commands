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

import net.doubledoordev.d3commands.util.Constants;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class CommandFireworks extends CommandBase
{
    @Override
    public String getName()
    {
        return "fireworks";
    }

    @Override
    public String getUsage(ICommandSender icommandsender)
    {
        return "/fireworks [player] [radius] [rockets]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP target;

        if (args.length == 0) target = getCommandSenderAsPlayer(sender);
        else target = getPlayer(server, sender, args[0]);

        double x = target.posX;
        double z = target.posZ;

        int rad = 1;
        if (args.length > 1) rad = parseInt(args[1], 1);

        int rockets = 1;
        if (args.length > 2) rockets = parseInt(args[2], 1);

        while (rockets -- > 0)
        {
            ItemStack itemStack = new ItemStack(Items.FIREWORKS);
            NBTTagCompound fireworks = new NBTTagCompound();
            NBTTagList explosions = new NBTTagList();

            int charges = 1 + Constants.RANDOM.nextInt(3);
            while (charges -- > 0)
            {
                NBTTagCompound explosion = new NBTTagCompound();

                if (Constants.RANDOM.nextBoolean()) explosion.setBoolean("Flicker", true);
                if (Constants.RANDOM.nextBoolean()) explosion.setBoolean("Trail", true);

                int[] colors = new int[1 + Constants.RANDOM.nextInt(3)];

                for (int i = 0; i < colors.length; i++)
                {
                    colors[i] = (Constants.RANDOM.nextInt(256) << 16) + (Constants.RANDOM.nextInt(256) << 8) + Constants.RANDOM.nextInt(256);
                }

                explosion.setIntArray("Colors", colors);
                explosion.setByte("Type", (byte) Constants.RANDOM.nextInt(5));

                if (Constants.RANDOM.nextBoolean())
                {
                    int[] fadeColors = new int[1 + Constants.RANDOM.nextInt(3)];

                    for (int i = 0; i < fadeColors.length; i++)
                    {
                        fadeColors[i] = (Constants.RANDOM.nextInt(256) << 16) + (Constants.RANDOM.nextInt(256) << 8) + Constants.RANDOM.nextInt(256);
                    }
                    explosion.setIntArray("FadeColors", fadeColors);
                }

                explosions.appendTag(explosion);
            }
            fireworks.setTag("Explosions", explosions);
            fireworks.setByte("Flight", (byte) (Constants.RANDOM.nextInt(2)));

            NBTTagCompound root = new NBTTagCompound();
            root.setTag("Fireworks", fireworks);
            itemStack.setTagCompound(root);
            target.world.spawnEntity(new EntityFireworkRocket(target.world, x + Constants.RANDOM.nextInt(rad) - rad / 2.0, target.posY, z + Constants.RANDOM.nextInt(rad) - rad / 2.0, itemStack));
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
        return userIndex == 0;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if (args.length == 1) return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        return super.getTabCompletions(server, sender, args, pos);
    }
}