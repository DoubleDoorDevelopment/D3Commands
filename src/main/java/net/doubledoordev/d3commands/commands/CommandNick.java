package net.doubledoordev.d3commands.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.Constants;

import net.doubledoordev.d3commands.ModConfig;

public class CommandNick extends CommandBase
{
    @Override
    public String getName()
    {
        return "nick";
    }

    @Override
    public String getUsage(ICommandSender icommandsender)
    {
        return "d3.cmd.nick.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP target;
        //                                0                 1           3     4
        if (args.length == 2) // /nick set|clear prefix|dispaly|suffix player name
        {
            EntityPlayerMP selftarget = getCommandSenderAsPlayer(sender);
            clearData(selftarget.getEntityData(), sender, selftarget, args);
        }

        if (args.length <= 3) target = getCommandSenderAsPlayer(sender);
        else target = getPlayer(server, sender, args[3]);
        NBTTagCompound targetNBT = target.getEntityData();

        if (args.length == 3)
        {
            if (correctFirstArg(args))
            {
                if (args[0].toLowerCase().equals("set"))
                    setData(targetNBT, sender, target, args, 2);
                else
                {
                    clearData(targetNBT, sender, target, args);
                }
            }
            else
            {
                sender.sendMessage(new TextComponentTranslation("d3.cmd.nick.error.wrongformat", args[0]));
            }
        }
        if (args.length == 4)
        {
            if (correctFirstArg(args))
            {
                if (args[0].toLowerCase().equals("set"))
                    setData(targetNBT, sender, target, args, 3);
                else
                {
                    clearData(targetNBT, sender, target, args);
                }
            }
            else
            {
                sender.sendMessage(new TextComponentTranslation("d3.cmd.nick.error.wrongformat", args[0]));
            }
        }
        target.refreshDisplayName();
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return ModConfig.nickPermissionLevel;
    }

    @Override
    public boolean isUsernameIndex(final String[] args, final int userIndex)
    {
        return userIndex == 0;
    }

    private boolean correctFirstArg(String[] args)
    {
        return args[0].toLowerCase().equals("set") | args[0].toLowerCase().equals("clear");
    }

    private void makeTags(NBTTagCompound targetNBT, String tagName, String nameData)
    {
        if (!targetNBT.hasKey("d3name", Constants.NBT.TAG_COMPOUND))
            targetNBT.setTag("d3name", new NBTTagCompound());

        if (!targetNBT.getCompoundTag("d3name").hasKey(tagName))
            targetNBT.getCompoundTag("d3name").setTag(tagName, new NBTTagString());

        targetNBT.getCompoundTag("d3name").setTag(tagName, new NBTTagString(nameData));
    }

    private void setData(NBTTagCompound targetNBT, ICommandSender sender, EntityPlayerMP target, String[] args, int dataArgument) throws CommandException
    {
        if (args[0].toLowerCase().equals("set"))
        {
            String nameData = getChatComponentFromNthArg(target, args, dataArgument).getUnformattedText();
            switch (args[1].toLowerCase())
            {
                case "prefix":
                {
                    makeTags(targetNBT, "Prefix", nameData);
                    sender.sendMessage(new TextComponentTranslation("d3.cmd.nick.prefix.set.success", nameData, target.getDisplayName()));
                    break;
                }
                case "suffix":
                {
                    makeTags(targetNBT, "Suffix", nameData);
                    sender.sendMessage(new TextComponentTranslation("d3.cmd.nick.suffix.set.success", nameData, target.getDisplayName()));
                    break;
                }
                case "display":
                {
                    makeTags(targetNBT, "Display", nameData);
                    sender.sendMessage(new TextComponentTranslation("d3.cmd.nick.display.set.success", nameData, target.getDisplayName()));
                    break;
                }
                default:
                {
                    sender.sendMessage(new TextComponentTranslation("d3.cmd.nick.error.descriptor", args[0]));
                }
            }
        }
        target.refreshDisplayName();
    }

    private void clearData(NBTTagCompound targetNBT, ICommandSender sender, EntityPlayerMP target, String[] args)
    {

        if (args[0].toLowerCase().equals("set"))
        {
            sender.sendMessage(new TextComponentTranslation("d3.cmd.nick.error.descriptor"));
            return;
        }

        if (!correctFirstArg(args))
        {
            sender.sendMessage(new TextComponentTranslation("d3.cmd.nick.error.wrongformat", args[0]));
            return;
        }

        // If the player has a compound tag
        if (targetNBT.hasKey("d3name"))

            switch (args[1].toLowerCase())
            {
                case "prefix":
                {
                    if (targetNBT.getCompoundTag("d3name").hasKey("Prefix")) // check the display key for the name tag
                    {
                        targetNBT.getCompoundTag("d3name").removeTag("Prefix"); //remove the name tag as it shouldn't be there anymore
                        sender.sendMessage(new TextComponentTranslation("d3.cmd.nick.prefix.remove.success", target.getDisplayName()));
                    }
                    else
                        sender.sendMessage(new TextComponentTranslation("d3.cmd.nick.prefix.remove.nodata", target.getDisplayName()));
                    break;
                }
                case "suffix":
                {
                    if (targetNBT.getCompoundTag("d3name").hasKey("Suffix")) // check the display key for the name tag
                    {
                        targetNBT.getCompoundTag("d3name").removeTag("Suffix"); //remove the name tag as it shouldn't be there anymore
                        sender.sendMessage(new TextComponentTranslation("d3.cmd.nick.suffix.remove.success", target.getDisplayName()));
                    }
                    else
                        sender.sendMessage(new TextComponentTranslation("d3.cmd.nick.suffix.remove.nodata", target.getDisplayName()));
                    break;
                }
                case "display":
                {
                    if (targetNBT.getCompoundTag("d3name").hasKey("Display")) // check the display key for the name tag
                    {
                        targetNBT.getCompoundTag("d3name").removeTag("Display"); //remove the name tag as it shouldn't be there anymore
                        sender.sendMessage(new TextComponentTranslation("d3.cmd.nick.display.remove.success", target.getDisplayName()));
                    }
                    else
                        sender.sendMessage(new TextComponentTranslation("d3.cmd.nick.display.remove.nodata", target.getDisplayName()));
                    break;
                }
                default:
                {
                    sender.sendMessage(new TextComponentTranslation("d3.cmd.nick.error.descriptor", args[0]));
                }
            }
        else sender.sendMessage(new TextComponentTranslation("d3.cmd.nick.clear.error.nodata", args[0]));
        target.refreshDisplayName();
    }
}