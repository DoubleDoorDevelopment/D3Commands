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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import net.doubledoordev.d3commands.ModConfig;
import net.doubledoordev.d3commands.util.BlockPosDim;
import net.doubledoordev.d3commands.util.DimChanger;
import net.doubledoordev.d3commands.util.PlayerGetter;

import static net.minecraft.entity.player.EntityPlayer.getBedSpawnLocation;

public class CommandBedTp extends CommandBase
{
    @Override
    public String getName()
    {
        return "tpbed";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "d3.cmd.tpbed.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        // TODO: Handle if target is in the dim already. ????
        // TODO: Document this mess.
        EntityPlayerMP senderAsPlayer = getCommandSenderAsPlayer(sender);
        EntityPlayer target;
        int dim;
        BlockPos bedpos;

        switch (args.length)
        {
            default:
                throw new WrongUsageException(getUsage(sender));
            case 0:
                target = PlayerGetter.onlineOrOffline(server, sender, sender.getName());
                if (target != null && target.bedLocation != null)
                {
                    bedpos = getBedSpawnLocation(target.world, target.bedLocation, true);
                    dim = target.world.provider.getDimension();
                    BlockPosDim pos = new BlockPosDim(bedpos, target.dimension);
                    DimChanger.changeDim(target, dim);
                    ((EntityPlayerMP) target).connection.setPlayerLocation(pos.getX(), pos.getY(), pos.getZ(), target.cameraYaw, target.cameraPitch);
                    sender.sendMessage(new TextComponentTranslation("d3.cmd.tpbed.success").appendText(" ").appendSibling(pos.toClickableChatString()));
                }
                else
                    sender.sendMessage(new TextComponentTranslation("d3.cmd.tpbed.fail"));
                break;

            case 1:
                target = PlayerGetter.onlineOrOffline(server, sender, args[0]);
                if (target != null && target.bedLocation != null)
                {
                    bedpos = getBedSpawnLocation(target.world, target.bedLocation, true);
                    BlockPosDim pos = new BlockPosDim(bedpos, target.dimension);
                    dim = target.world.provider.getDimension();
                    DimChanger.changeDim(senderAsPlayer, dim);
                    (senderAsPlayer).connection.setPlayerLocation(pos.getX(), pos.getY(), pos.getZ(), senderAsPlayer.cameraYaw, senderAsPlayer.cameraPitch);
                    sender.sendMessage(new TextComponentTranslation("d3.cmd.tpbed.success.other", target.getDisplayName()).appendText(" ").appendSibling(pos.toClickableChatString()));
                }
                else
                {
                    NBTTagCompound data = PlayerGetter.onlineOrOfflineNBT(server, args[0]);
                    if (data != null)
                    {
                        int x = data.getInteger("SpawnX");
                        int y = data.getInteger("SpawnY");
                        int z = data.getInteger("SpawnZ");
                        BlockPos offlineSpawnPos = new BlockPos(x, y, z);
                        BlockPosDim pos = new BlockPosDim(offlineSpawnPos, senderAsPlayer.dimension);

                        dim = senderAsPlayer.world.provider.getDimension();
                        DimChanger.changeDim(senderAsPlayer, dim);
                        (senderAsPlayer).connection.setPlayerLocation(pos.getX(), pos.getY(), pos.getZ(), senderAsPlayer.cameraYaw, senderAsPlayer.cameraPitch);
                        sender.sendMessage(new TextComponentTranslation("d3.cmd.tpbed.success.other", target.getDisplayName()).appendText(" ").appendSibling(pos.toClickableChatString()));
                    }
                    else
                        sender.sendMessage(new TextComponentTranslation("d3.cmd.tpbed.fail.other", target.getDisplayName()));
                }
                break;
        }
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return ModConfig.bedTpPermissionLevel;
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
        return super.getTabCompletions(server, sender, args, pos);
    }
}
