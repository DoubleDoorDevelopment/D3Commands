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

import net.doubledoordev.d3commands.D3Commands;
import net.doubledoordev.d3commands.util.Location;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class CommandHome extends CommandBase
{
    @Override
    public String getCommandName() {
        return "home";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/home";
    }

    @Override
    public void processCommand(final ICommandSender sender, final String[] args)
    {
            EntityPlayerMP player = getCommandSenderAsPlayer(sender);
            if (args.length == 0)
            {
                if(D3Commands.instance.homes.containsKey(player)){
                    teleportPlayer(player, D3Commands.instance.homes.get(player));
                    player.addChatMessage(new ChatComponentText("Teleported back to your home."));
                }else{
                    player.addChatMessage(new ChatComponentText("You don't have a home set. Use /sethome"));
                }
            }
            else
            {
                player.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
            }
    }

    /**
     * Teleports a player to a location
     */
    public  void teleportPlayer(final EntityPlayerMP player, Location loc)
    {
        int x = loc.getCoordinates().posX;
        int y = loc.getCoordinates().posY;
        int z = loc.getCoordinates().posZ;
        player.mountEntity(null);
        if (player.dimension != loc.getDimension())
        {
            MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, loc.getDimension());
        }
        player.setPositionAndUpdate(x, y, z);
        player.prevPosX = player.posX = x;
        player.prevPosY = player.posY = y;
        player.prevPosZ = player.posZ = z;
    }

}
