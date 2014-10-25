/*
 * Copyright (c) 2014, DoubleDoorDevelopment
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
 *  Neither the name of the project nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
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

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

import java.util.List;

public class CommandHeal extends CommandBase
{
    @Override
    public String getCommandName() {
        return "heal";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/heal [target player]";
    }

    @Override
    public void processCommand(final ICommandSender sender, final String[] args)
    {
        if (args.length == 1)
        {
            if (sender instanceof  MinecraftServer || MinecraftServer.getServer().getConfigurationManager().func_152596_g(MinecraftServer.getServer().getConfigurationManager().func_152612_a(sender.getCommandSenderName()).getGameProfile()))
            {
                EntityPlayerMP playerHeal = getPlayer(sender, args[0]);
                playerHeal.setHealth(20);
                playerHeal.getFoodStats().setFoodLevel(20);
                //TODO: Add succes message, need to ask dries how to get stuff from the .lang files.
            }
            else sender.addChatMessage(new ChatComponentTranslation("commands.generic.permission"));
        }
        else
        {
            EntityPlayerMP playerHeal = getCommandSenderAsPlayer(sender);
            if (args.length == 0)
            {
                playerHeal.setHealth(20);
                playerHeal.getFoodStats().setFoodLevel(20);
            }
            else
            {
                playerHeal.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
            }
        }
    }

    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args)
    {
        if (args.length == 1) return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        return null;
    }
}
