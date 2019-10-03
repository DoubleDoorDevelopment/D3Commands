package net.doubledoordev.d3commands.commands;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.Constants;

import net.doubledoordev.d3commands.ModConfig;
import net.doubledoordev.d3commands.util.BlockPosDim;
import net.doubledoordev.d3commands.util.DimChanger;
import net.doubledoordev.d3commands.util.PlayerGetter;

public class CommandHome extends CommandBase
{
    @Override
    public String getName()
    {
        return "home";
    } // /home set [player]

    @Override
    public String getUsage(ICommandSender icommandsender)
    {
        return "d3.cmd.home.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayer target;

        if (args.length == 0) target = getCommandSenderAsPlayer(sender);
        else if (!args[0].equals("set")) target = PlayerGetter.onlineOrOffline(server, sender, args[0]);
        else target = getCommandSenderAsPlayer(sender);

        if (target != null)
        {
            NBTTagCompound targetNBT = target.getEntityData();
            NBTTagCompound posTag = BlockPosDim.createPosDimTag(BlockPosDim.getLocation(target));

            if (!targetNBT.hasKey("PlayerPersisted", Constants.NBT.TAG_COMPOUND))
                targetNBT.setTag("PlayerPersisted", new NBTTagCompound());

            if (args.length == 0)
            {
                if (!targetNBT.getCompoundTag("PlayerPersisted").hasKey("d3home", Constants.NBT.TAG_COMPOUND) |
                        targetNBT.getCompoundTag("PlayerPersisted").getCompoundTag("d3home").isEmpty()
                )
                {
                    sender.sendMessage(new TextComponentTranslation("d3.cmd.home.notset", target.getDisplayName()));
                }
                else
                {
                    moveTarget(target);
                    sender.sendMessage(new TextComponentTranslation("d3.cmd.home.welcome", target.getDisplayName()));
                }
            }
            else
            {
                if (args[0].equals("set"))
                {
                    if (!targetNBT.getCompoundTag("PlayerPersisted").hasKey("d3home", Constants.NBT.TAG_COMPOUND))
                        targetNBT.getCompoundTag("PlayerPersisted").setTag("d3home", new NBTTagCompound());

                    targetNBT.getCompoundTag("PlayerPersisted").setTag("d3home", posTag);
                    sender.sendMessage(new TextComponentTranslation("d3.cmd.home.set.self", target.getDisplayName()));
                }
                else
                {
                    if (!targetNBT.getCompoundTag("PlayerPersisted").hasKey("d3home", Constants.NBT.TAG_COMPOUND) |
                            targetNBT.getCompoundTag("PlayerPersisted").getCompoundTag("d3home").isEmpty()
                    )
                    {
                        sender.sendMessage(new TextComponentTranslation("d3.cmd.home.notset", target.getDisplayName()));
                    }
                    else
                    {
                        moveTarget(target);
                        sender.sendMessage(new TextComponentTranslation("d3.cmd.home.welcome", target.getDisplayName()));
                    }
                }
            }
        }
        else
        {
            sender.sendMessage(new TextComponentTranslation("d3.cmd.home.target.fail", target.getDisplayName()));
        }
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return ModConfig.homePermissionLevel;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if (args.length == 1) return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        return super.getTabCompletions(server, sender, args, pos);
    }

    @Override
    public boolean isUsernameIndex(final String[] args, final int userIndex)
    {
        return userIndex == 0;
    }

    private void moveTarget(EntityPlayer target)
    {
        NBTTagCompound targetNBT = target.getEntityData();

        BlockPosDim homeLocation = BlockPosDim.getPosDimFromTag(targetNBT.getCompoundTag("PlayerPersisted").getCompoundTag("d3home"));

        if (target.dimension != homeLocation.getDim())
        {
            DimChanger.changeDim(target, homeLocation.getDim());
            ((EntityPlayerMP) target).connection.setPlayerLocation(homeLocation.getX(), homeLocation.getY(), homeLocation.getZ(), target.cameraYaw, target.cameraPitch);
        }
        else
        {
            ((EntityPlayerMP) target).connection.setPlayerLocation(homeLocation.getX(), homeLocation.getY(), homeLocation.getZ(), target.cameraYaw, target.cameraPitch);
        }
    }
}