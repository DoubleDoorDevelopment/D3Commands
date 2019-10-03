package net.doubledoordev.d3commands.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.Constants;

import net.doubledoordev.d3commands.ModConfig;

public class CommandLore extends CommandBase
{
    @Override
    public String getName()
    {
        return "lore";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "d3.cmd.lore.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP target = getCommandSenderAsPlayer(sender);
        ItemStack stack = target.getHeldItemMainhand();

        if (args.length == 0)
        {
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("display")) // If the stack has a compound tag and has a display key
            {
                if (stack.getTagCompound().getCompoundTag("display").hasKey("Lore")) // check the display key for the lore tag
                {
                    stack.getTagCompound().getCompoundTag("display").removeTag("Lore"); //remove the lore tag as it shouldn't be there anymore

                    if (stack.getTagCompound().getCompoundTag("display").isEmpty()) // if display is empty, remove it
                        stack.removeSubCompound("display");

                    if (stack.getTagCompound() != null && stack.getTagCompound().isEmpty()) // if the tag isn't null and the tag is empty make it null AKA clears the NBT
                        stack.setTagCompound(null);

                    sender.sendMessage(new TextComponentTranslation("d3.cmd.lore.removed"));
                }
                else
                    sender.sendMessage(new TextComponentTranslation("d3.cmd.lore.remove.fail"));
            }
            else
                sender.sendMessage(new TextComponentTranslation("d3.cmd.lore.remove.fail"));
        }
        else
        {
            // if there is no tag compound make one.
            if (!stack.hasTagCompound())
                stack.setTagCompound(new NBTTagCompound());

            // if there is no display tag add one
            if (!stack.getTagCompound().hasKey("display", Constants.NBT.TAG_COMPOUND))
                stack.getTagCompound().setTag("display", new NBTTagCompound());

            // if there is no lore tag in the display tag add it.
            if (!stack.getTagCompound().getCompoundTag("display").hasKey("Lore"))
                stack.getTagCompound().getCompoundTag("display").setTag("Lore", new NBTTagList());

            NBTTagList lore = new NBTTagList(); // tag for text
            lore.appendTag(new NBTTagString(getChatComponentFromNthArg(target, args, 0).getUnformattedText())); //fill tag with text
            stack.getTagCompound().getCompoundTag("display").setTag("Lore", lore); // set the lore tag.
            // send notification of lore added.
            sender.sendMessage(new TextComponentTranslation("d3.cmd.lore.added", getChatComponentFromNthArg(target, args, 0).getFormattedText()));
        }
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return ModConfig.lorePermissionLevel;
    }
}
