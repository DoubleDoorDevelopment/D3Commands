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
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class CommandSmite extends CommandBase
{
    @Override
    public String getName()
    {
        return "smite";
    }

    @Override
    public String getUsage(ICommandSender icommandsender)
    {
        return "/smite [target] [radius] [strikes]";
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if (args.length == 1) return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        return super.getTabCompletions(server, sender, args, pos);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        int rad = 1;
        int strikes = 1;
        boolean effectOnly = false;

        if (args.length > 1) rad = parseInt(args[1], 1);
        if (args.length > 2) strikes = parseInt(args[2], 1);
        if (args.length > 3) effectOnly = parseBoolean(args[3]);

        if (args.length == 0) smite(getCommandSenderAsPlayer(sender), rad, strikes, effectOnly);
        else
        {
            for (Entity entity : getEntityList(server, sender, args[0]))
            {
                smite(entity, rad, strikes, effectOnly);
            }
        }
    }

    private void smite(Entity target, int rad, int strikes, boolean effectOnly)
    {
        while (strikes -- > 0)
        {
            target.world.addWeatherEffect(new EntityLightningBolt(target.world, target.posX + Constants.RANDOM.nextInt(rad) - rad / 2.0, target.posY, target.posZ + Constants.RANDOM.nextInt(rad) - rad / 2.0, effectOnly));
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
}