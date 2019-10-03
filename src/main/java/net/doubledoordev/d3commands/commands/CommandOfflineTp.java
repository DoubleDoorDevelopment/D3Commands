package net.doubledoordev.d3commands.commands;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import net.doubledoordev.d3commands.ModConfig;
import net.doubledoordev.d3commands.util.BlockPosDim;
import net.doubledoordev.d3commands.util.DimChanger;
import net.doubledoordev.d3commands.util.PlayerGetter;

public class CommandOfflineTp extends CommandBase
{
    @Override
    public String getName()
    {
        return "tpoffline";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "d3.cmd.tpoffline.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP senderAsPlayer = getCommandSenderAsPlayer(sender);
        int dim;

        switch (args.length)
        {
            default:
                throw new WrongUsageException(getUsage(sender));
            case 1:
                NBTTagCompound data = PlayerGetter.onlineOrOfflineNBT(server, args[0]);
                if (data != null)
                {
                    NBTTagList posdata = data.getTagList("Pos", 6);
                    double x = posdata.getDoubleAt(0);
                    double y = posdata.getDoubleAt(1);
                    double z = posdata.getDoubleAt(2);
                    dim = data.getInteger("Dim");

                    BlockPos offlineSpawnPos = new BlockPos(x, y, z);
                    BlockPosDim pos = new BlockPosDim(offlineSpawnPos, senderAsPlayer.dimension);
                    DimChanger.changeDim(senderAsPlayer, dim);
                    (senderAsPlayer).connection.setPlayerLocation(pos.getX(), pos.getY(), pos.getZ(), senderAsPlayer.cameraYaw, senderAsPlayer.cameraPitch);
                    sender.sendMessage(new TextComponentTranslation("d3.cmd.tpoffline.success", args[0]).appendText(" ").appendSibling(pos.toClickableChatString()));
                }
                else sender.sendMessage(new TextComponentTranslation("d3.cmd.tpoffline.fail", args[0]));
        }
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return ModConfig.offlinetpPermissionLevel;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if (args.length == 1)
        {
            Set<String> names = new HashSet<>();
            Collections.addAll(names, server.getPlayerList().getAvailablePlayerDat());
            return getListOfStringsMatchingLastWord(args, names);
        }
        return super.getTabCompletions(server, sender, args, pos);
    }
}
