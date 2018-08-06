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
 *
 */

package net.doubledoordev.d3commands.entry;

import com.google.common.base.Strings;
import net.minecraft.command.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.minecraft.command.CommandBase.*;

/**
 * This actually IS the command :)
 */
public class ItemCommandEntry extends CommandEntry implements ICommand
{
    private String name;
    private String item;
    private String[] aliases;
    private boolean allowUsername;
    private int meta;
    private int stacksize;
    private String message;
    private String displayname;

    public ItemCommandEntry(ConfigCategory cat)
    {
        super(cat.getQualifiedName(), cat.containsKey("modids") ? cat.get("modids").getStringList() : null);
        doConfig(cat);
    }

    @Override
    public ICommand getInstance()
    {
        return this;
    }

    public void doConfig(ConfigCategory cat)
    {
        if (!(cat.containsKey("name") && cat.containsKey("item"))) throw new RuntimeException("Configuration error. Missing required element on " + getUniqueName());

        this.name = cat.get("name").getString();
        this.item = cat.get("item").getString();

        this.aliases = cat.containsKey("aliases") ? cat.get("aliases").getStringList() : new String[0];
        this.allowUsername = cat.containsKey("allowUsername") && cat.get("allowUsername").getBoolean();
        this.meta = cat.containsKey("meta") ? cat.get("meta").getInt() : 0;
        this.stacksize = cat.containsKey("stacksize") ? cat.get("stacksize").getInt() : 1;
        this.message = cat.containsKey("message") ? cat.get("message").getString() : null;
        this.displayname = cat.containsKey("displayname") ? cat.get("displayname").getString() : null;
        this.enabled = !cat.containsKey("enabled") || cat.get("enabled").getBoolean();
    }

    @Override
    public void doConfig(Configuration configuration)
    {
        doConfig(configuration.getCategory(getUniqueName()));
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return '/' + name + (allowUsername ? " [username]" : "");
    }

    @Override
    public List<String> getAliases()
    {
        return Arrays.asList(aliases);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP target;

        if (args.length == 0) target = getCommandSenderAsPlayer(sender);
        else if (allowUsername) target = getPlayer(server, sender, args[0]);
        else throw new WrongUsageException(getUsage(sender));
        ItemStack stack = new ItemStack(getItemByText(sender, item), stacksize, meta);
        if (!Strings.isNullOrEmpty(displayname)) stack.setStackDisplayName(displayname);
        boolean flag = target.inventory.addItemStackToInventory(stack);
        if (flag)
        {
            target.world.playSound(null, target.posX, target.posY, target.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((target.getRNG().nextFloat() - target.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            target.inventoryContainer.detectAndSendChanges();
        }
        if (flag && stack.getCount() <= 0)
        {
            stack.setCount(1);
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, stacksize);
            EntityItem entityitem1 = target.dropItem(stack, false);
            if (entityitem1 != null) entityitem1.makeFakeItem();
        }
        else
        {
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, stacksize - stack.getCount());
            EntityItem entityitem = target.dropItem(stack, false);
            if (entityitem != null)
            {
                entityitem.setNoPickupDelay();
                entityitem.setOwner(target.getName());
            }
        }
        if (!Strings.isNullOrEmpty(message)) sender.sendMessage(new TextComponentString(message));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        return allowUsername && args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.<String>emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return allowUsername && index == 0;
    }

    @Override
    public int compareTo(ICommand o)
    {
        return this.getName().compareTo(o.getName());
    }
}
