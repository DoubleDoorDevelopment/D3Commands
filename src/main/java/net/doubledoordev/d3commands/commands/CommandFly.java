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

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import net.doubledoordev.d3commands.ModConfig;

public class CommandFly extends CommandBase
{
    @Override
    public String getName()
    {
        return "fly";
    }

    @Override
    public String getUsage(ICommandSender icommandsender)
    {
        return "d3.cmd.fly.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP target;

        if (args.length == 0) target = getCommandSenderAsPlayer(sender);
        else target = getPlayer(server, sender, args[0]);

        if (target.isSpectator())
        {
            sender.sendMessage(new TextComponentTranslation("d3.cmd.fly.gamemode.fail", target.getDisplayName()));
            return;
        }

        boolean on = doFly(target);

        if (on) sender.sendMessage(new TextComponentTranslation("d3.cmd.fly.success.on", target.getDisplayName()));
        else sender.sendMessage(new TextComponentTranslation("d3.cmd.fly.success.off", target.getDisplayName()));
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return ModConfig.flyPermissionLevel;
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

    private boolean doFly(EntityPlayerMP playerFly)
    {
        playerFly.capabilities.allowFlying = !playerFly.capabilities.allowFlying;
        if (!playerFly.capabilities.allowFlying) playerFly.capabilities.isFlying = false;
        playerFly.sendPlayerAbilities();
        return playerFly.capabilities.allowFlying;
    }
}
