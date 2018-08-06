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

import com.mojang.authlib.GameProfile;
import net.doubledoordev.d3commands.util.FakePlayerInventory;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nullable;
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
    public String getName()
    {
        return "invsee";
    }

    @Override
    public String getUsage(ICommandSender icommandsender)
    {
        return "/invsee <target player> ['enderchest']";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1) throw new WrongUsageException(getUsage(sender));

        EntityPlayerMP host = getCommandSenderAsPlayer(sender);

        EntityPlayerMP target = getPlayerOrOffline(server, sender, args[0]);
        if (target == null) throw new PlayerNotFoundException("Player not Found!");

        if (args.length == 2 && args[1].equalsIgnoreCase("enderchest"))
        {
            InventoryEnderChest inv = new InventoryEnderChest()
            {
                @Override
                public void closeInventory(EntityPlayer player) {}

                @Override
                public void openInventory(EntityPlayer player) {}

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

            inv.loadInventoryFromNBT(target.getInventoryEnderChest().saveInventoryToNBT());
            String name = target.getDisplayNameString() + " Unmodifiable!";
            if (name.length() > 32) name = "Unmodifiable!";
            inv.setCustomName(name);
            host.displayGUIChest(inv);
        }
        else // non enderchest
        {
            host.displayGUIChest(new FakePlayerInventory(target));
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
        if (args.length == 1)
        {
            Set<String> names = new HashSet<>();
            for (String name : server.getOnlinePlayerNames()) names.add(name);
            for (String name : server.getPlayerList().getAvailablePlayerDat()) names.add(name);
            return getListOfStringsMatchingLastWord(args, names);
        }
        else if (args.length == 2) return getListOfStringsMatchingLastWord(args, "enderchest");
        return super.getTabCompletions(server, sender, args, pos);
    }

    private EntityPlayerMP getPlayerOrOffline(MinecraftServer server, ICommandSender sender, String name)
    {
        try
        {
            return getPlayer(server, sender, name);
        }
        catch (PlayerNotFoundException e)
        {
            try
            {
                File playerDataFolder = new File(server.worlds[0].getSaveHandler().getWorldDirectory(), "playerdata");
                File playerDataFile = new File(playerDataFolder, name + ".dat");
                NBTTagCompound playerData = CompressedStreamTools.readCompressed(new FileInputStream(playerDataFile));
                UUID uuid = UUID.fromString(name);
                GameProfile gameProfile = server.getPlayerProfileCache().getProfileByUUID(uuid);
                if (gameProfile == null) gameProfile = new GameProfile(uuid, name);
                FakePlayer fakeplayer = new FakePlayer(server.worlds[0], gameProfile);
                fakeplayer.readEntityFromNBT(playerData);
                return fakeplayer;
            }
            catch (IOException ignored)
            {

            }
        }
        catch (CommandException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
