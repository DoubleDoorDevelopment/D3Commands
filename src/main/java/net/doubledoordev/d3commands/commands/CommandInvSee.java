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
import net.doubledoordev.d3commands.util.FakePlayerInventory;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.FakePlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CommandInvSee extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "invsee";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/invsee <target player> ['enderchest']";
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
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args)
    {
        if (args.length == 1)
        {
            Set<String> names = new HashSet<>();
            for (String name : MinecraftServer.getServer().getAllUsernames()) names.add(name);
            for (String name : MinecraftServer.getServer().getConfigurationManager().getAvailablePlayerDat()) names.add(name);
            return getListOfStringsFromIterableMatchingLastWord(args, names);
        }
        else if (args.length == 2) return getListOfStringsMatchingLastWord(args, "enderchest");
        return null;
    }

    @Override
    public void processCommand(final ICommandSender sender, final String[] args)
    {
        if (args.length < 1) throw new WrongUsageException(getCommandUsage(sender));

        EntityPlayerMP host = getCommandSenderAsPlayer(sender);

        if (args.length == 2 && args[1].equalsIgnoreCase("enderchest"))
        {
            InventoryEnderChest inv = new InventoryEnderChest()
            {
                @Override
                public void func_110134_a(IInvBasic p_110134_1_)
                {

                }

                @Override
                public void func_110132_b(IInvBasic p_110132_1_)
                {

                }

                @Override
                public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_)
                {
                    return null;
                }

                @Override
                public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_)
                {
                    return false;
                }
            };
            EntityPlayer player = null;
            try
            {
                player = getPlayer(sender, args[0]);
            }
            catch (PlayerNotFoundException e)
            {
                try
                {
                    File playerDataFolder = new File(MinecraftServer.getServer().worldServers[0].getSaveHandler().getWorldDirectory(), "playerdata");
                    File playerDataFile = new File(playerDataFolder, args[0] + ".dat");
                    NBTTagCompound playerData = CompressedStreamTools.readCompressed(new FileInputStream(playerDataFile));
                    if (playerData != null)
                    {
                        UUID uuid = UUID.fromString(args[0]);
                        GameProfile gameProfile = MinecraftServer.getServer().func_152358_ax().func_152652_a(uuid);
                        if (gameProfile == null) gameProfile = new GameProfile(uuid, "-??-");
                        player = new FakePlayer(MinecraftServer.getServer().worldServers[0], gameProfile);
                    }
                }
                catch (IOException ignored)
                {

                }
            }
            if (player == null) throw new PlayerNotFoundException();

            inv.loadInventoryFromNBT(player.getInventoryEnderChest().saveInventoryToNBT());
            inv.func_110133_a(player.getCommandSenderName() + " Unmodifiable!");
            if (inv.getInventoryName().length() > 32) inv.func_110133_a("Unmodifiable!");
            host.displayGUIChest(inv);
        }
        else // non enderchest
        {
            EntityPlayerMP inv = null;

            try
            {
                inv = getPlayer(sender, args[0]);
            }
            catch (PlayerNotFoundException e)
            {
                try
                {
                    File playerDataFolder = new File(MinecraftServer.getServer().worldServers[0].getSaveHandler().getWorldDirectory(), "playerdata");
                    File playerDataFile = new File(playerDataFolder, args[0] + ".dat");
                    NBTTagCompound playerData = CompressedStreamTools.readCompressed(new FileInputStream(playerDataFile));
                    if (playerData != null)
                    {
                        UUID uuid = UUID.fromString(args[0]);
                        GameProfile gameProfile = MinecraftServer.getServer().func_152358_ax().func_152652_a(uuid);
                        if (gameProfile == null) gameProfile = new GameProfile(uuid, "-??-");
                        inv = new FakePlayer(MinecraftServer.getServer().worldServers[0], gameProfile);
                    }
                }
                catch (IOException ignored)
                {

                }
            }

            if (inv == null) throw new PlayerNotFoundException();
            host.displayGUIChest(new FakePlayerInventory(inv));
        }
    }
}
