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

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandGameMode;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;

import net.doubledoordev.d3commands.ModConfig;

public class CommandGm extends CommandGameMode
{
    @Override
    public String getName()
    {
        return "gm";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return ModConfig.gmPermissionLevel;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP target;
        GameType gametype;

        switch (args.length)
        {
            default:
                throw new WrongUsageException(getUsage(sender));
            case 1:
                gametype = getGameModeFromCommand(sender, args[0]);
                target = getCommandSenderAsPlayer(sender);
                target.setGameType(gametype);
                sender.sendMessage(new TextComponentTranslation("d3.cmd.gm.success.self", gametype.getName()));
                break;
            case 2:
                gametype = getGameModeFromCommand(sender, args[0]);
                target = getPlayer(server, sender, args[1]);
                target.setGameType(gametype);
                target.sendMessage(new TextComponentTranslation("d3.cmd.gm.success.target", gametype.getName()));
                sender.sendMessage(new TextComponentTranslation("d3.cmd.gm.success.on.other", target.getName(), gametype.getName()));
                break;
        }
    }
}
