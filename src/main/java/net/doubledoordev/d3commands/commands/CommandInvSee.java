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

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.doubledoordev.d3commands.ModConfig;
import net.doubledoordev.d3commands.util.FakePlayerExtraInventory;
import net.doubledoordev.d3commands.util.PlayerGetter;

public class CommandInvSee extends CommandBase
{
    EntityPlayer target;
    @Override
    public String getName()
    {
        return "invsee";
    }

    @Override
    public String getUsage(ICommandSender icommandsender)
    {
        return "d3.cmd.invsee.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP host = getCommandSenderAsPlayer(sender);

        if (args.length == 0)
            throw new WrongUsageException(getUsage(sender));
        //TODO: Handle this if nothing is there. (I think this was done?)
        target = PlayerGetter.onlineOrOffline(server, sender, args[0]);

        // If the target is null we can't do anything.
        if (target == null) throw new PlayerNotFoundException("d3.cmd.invsee.fail.player.missing", args[0]);

        switch (args.length)
        {
            case 1:
                //Catch if the person is trying to look at there own inventory and block it.
                if (target == host)
                    throw new SyntaxErrorException("d3.cmd.invsee.fail.player.self");
                //Get the current dim of the target.
                int dim = target.dimension;
                //Display the GUI.
                host.displayGUIChest(new FakePlayerExtraInventory((EntityPlayerMP) target));
                //While the container is open check the dim of the target. Close if they don't match.
                //while (host.openContainer instanceof ContainerChest)
                if (target.dimension != dim)
                    host.closeContainer();
                //If the target disconnects then close the GUI.
                if (((EntityPlayerMP) target).hasDisconnected())
                    host.closeContainer();
                break;
            case 2:
                switch (args[1].toLowerCase())
                {
                    case "enderchest":
                        // New enderchest UI.
                        InventoryEnderChest inv = new InventoryEnderChest()
                        {
                            @Override
                            public void openInventory(EntityPlayer player) {}

                            @Override
                            public void closeInventory(EntityPlayer player) {}
                        };
                        // Load the enderchest NBT somehow?
                        inv.loadInventoryFromNBT(target.getInventoryEnderChest().saveInventoryToNBT());

                        // Set the name of the inventory
                        inv.setCustomName(target.getDisplayNameString());

                        // Display the chest.
                        host.displayGUIChest(inv);
                        break;
                }
                break;
            default:
                throw new WrongUsageException(getUsage(sender));
        }
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return ModConfig.invseePermissionLevel;
    }

    @Override
    public boolean isUsernameIndex(final String[] args, final int userIndex)
    {
        return userIndex == 0;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if (args.length == 1)
        {
            Set<String> names = new HashSet<>();
            Collections.addAll(names, server.getOnlinePlayerNames());
            Collections.addAll(names, server.getPlayerList().getAvailablePlayerDat());
            return getListOfStringsMatchingLastWord(args, names);
        }
        else if (args.length == 2) return getListOfStringsMatchingLastWord(args, "enderchest");
        return super.getTabCompletions(server, sender, args, pos);
    }

    @SubscribeEvent
    public void playerDataisSaving(PlayerEvent.SaveToFile event)
    {
        if (target.getUniqueID().toString().equals(event.getPlayerUUID()))
            target.closeScreen();

    }
}
