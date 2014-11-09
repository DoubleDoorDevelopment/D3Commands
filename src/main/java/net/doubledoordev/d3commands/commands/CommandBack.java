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
import net.minecraft.block.material.Material;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;

public class CommandBack extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "back";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/back - Teleports you back to where you died the last time.";
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
    public void processCommand(ICommandSender sender, String[] args)
    {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        if(D3Commands.instance.deathlog.containsKey(player.getUniqueID())){
            Location locto = D3Commands.instance.deathlog.get(player.getUniqueID());
            teleportPlayer(sender, player, locto);
            System.out.println("Teleported " + player.getDisplayName() + " back to death point:" + player.getPlayerCoordinates());
        }else{
            System.out.println("Could not find a death point to return to for " + player.getCommandSenderName() + ".");
        }
    }

    /**
     * Teleports a player to a location
     */
    public  void teleportPlayer(final ICommandSender sender, final EntityPlayerMP player, Location loc)
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