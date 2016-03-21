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

import com.google.common.base.Strings;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.Arrays;
import java.util.List;

public class CommandGetItem extends CommandBase
{
    private final String name;
    private final String[] aliases;
    private final boolean allowUsername;
    private final String modid;
    private final String item;
    private final int meta;
    private final int stacksize;
    private final String message;
    private final String usage;
    private final String displayname;

    public CommandGetItem(String name, String[] aliases, boolean allowUsername, String modid, String item, int meta, int stacksize, String message, String displayname)
    {
        this.name = name;
        this.aliases = aliases;
        this.allowUsername = allowUsername;
        this.modid = modid;
        this.item = item;
        this.meta = meta;
        this.stacksize = stacksize;

        this.message = message;
        this.displayname = displayname;
        this.usage = '/' + name + (allowUsername ? " [username]" : "");
    }

    @Override
    public String getCommandName()
    {
        return name;
    }

    @Override
    public List getCommandAliases()
    {
        return Arrays.asList(aliases);
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return usage;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_)
    {
        return true;
    }

    @Override
    public boolean isUsernameIndex(final String[] args, final int userIndex)
    {
        return allowUsername && userIndex == 0;
    }

    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args)
    {
        if (allowUsername && args.length == 1) return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        return null;
    }

    @Override
    public void processCommand(final ICommandSender sender, final String[] args)
    {
        EntityPlayerMP target;

        if (args.length == 0) target = getCommandSenderAsPlayer(sender);
        else if (allowUsername) target = getPlayer(sender, args[0]);
        else throw new WrongUsageException(getCommandUsage(sender));

        ItemStack stack = GameRegistry.findItemStack(modid, item, stacksize);
        if (stack == null) throw new CommandException("The item " + modid + ":" + item + " doesn't seem to exist.");
        stack.setItemDamage(meta);
        if (!Strings.isNullOrEmpty(displayname)) stack.setStackDisplayName(displayname);

        EntityItem entityItem = target.dropPlayerItemWithRandomChoice(stack, false);
        entityItem.delayBeforeCanPickup = 0;
        entityItem.func_145797_a(target.getCommandSenderName());
        sender.addChatMessage(new ChatComponentText(message));
    }
}
