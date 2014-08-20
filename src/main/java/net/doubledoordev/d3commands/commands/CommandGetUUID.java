/*
 * Copyright (c) 2014,
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
 *  Neither the name of the {organization} nor the names of its
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
 *
 *
 */

package net.doubledoordev.d3commands.commands;

import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

/**
 * @author Dries007
 */
public class CommandGetUUID extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "getuuid";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_)
    {
        return "/getuuid [username] [username 2] [...]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        ServerConfigurationManager scm = MinecraftServer.getServer().getConfigurationManager();
        if (args.length == 0)
        {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "All online players:"));
            for (GameProfile gp : scm.func_152600_g())
            {
                sender.addChatMessage(new ChatComponentText(gp.getName() + " -> " + gp.getId()));
            }
        }
        else
        {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "All listed players:"));
            for (String name : args)
            {
                EntityPlayerMP player = scm.func_152612_a(name);
                if (player == null) sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "No player with name " + name));
                else sender.addChatMessage(new ChatComponentText(player.getCommandSenderName() + " -> " + player.getUniqueID()));
            }
        }
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_)
    {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    }
}
