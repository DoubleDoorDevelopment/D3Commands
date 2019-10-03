package net.doubledoordev.d3commands.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.Constants;

import net.doubledoordev.d3commands.ModConfig;

public class CommandName extends CommandBase
{
    @Override
    public String getName()
    {
        return "name";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "d3.cmd.name.usage";
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
                if (stack.getTagCompound().getCompoundTag("display").hasKey("Name")) // check the display key for the name tag
                {
                    stack.getTagCompound().getCompoundTag("display").removeTag("Name"); //remove the name tag as it shouldn't be there anymore

                    if (stack.getTagCompound().getCompoundTag("display").isEmpty()) // if display is empty, remove it
                        stack.removeSubCompound("display");

                    if (stack.getTagCompound() != null && stack.getTagCompound().isEmpty()) // if the tag isn't null and the tag is empty make it null AKA clears the NBT
                        stack.setTagCompound(null);

                    sender.sendMessage(new TextComponentTranslation("d3.cmd.name.removed"));
                }
                else
                    sender.sendMessage(new TextComponentTranslation("d3.cmd.name.remove.fail"));
            }
            else
                sender.sendMessage(new TextComponentTranslation("d3.cmd.name.remove.fail"));
        }
        else
        {
            // if there is no tag compound make one.
            if (!stack.hasTagCompound())
                stack.setTagCompound(new NBTTagCompound());

            // if there is no display tag add one
            if (!stack.getTagCompound().hasKey("display", Constants.NBT.TAG_COMPOUND))
                stack.getTagCompound().setTag("display", new NBTTagCompound());

            // if there is no name tag in the display tag add it.
            if (!stack.getTagCompound().getCompoundTag("display").hasKey("Name"))
                stack.getTagCompound().getCompoundTag("display").setTag("Name", new NBTTagString());

            NBTTagString name = new NBTTagString(getChatComponentFromNthArg(target, args, 0).getUnformattedText()); // tag for text
            stack.getTagCompound().getCompoundTag("display").setTag("Name", name); // set the name tag.
            // send notification of name added.
            sender.sendMessage(new TextComponentTranslation("d3.cmd.name.added", getChatComponentFromNthArg(target, args, 0).getFormattedText()));
        }
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return ModConfig.namePermissionLevel;
    }
}
