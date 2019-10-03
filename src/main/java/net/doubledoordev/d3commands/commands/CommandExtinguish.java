package net.doubledoordev.d3commands.commands;

import java.util.List;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class CommandExtinguish extends CommandBase
{
    @Override
    public String getName()
    {
        return "extinguish";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "d3.cmd.extinguish.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        BlockPos senderPos = sender.getPosition();
        int size;

        if (args.length > 0)
            size = MathHelper.clamp(Integer.parseInt(args[0]), 0, 160);
        else
            size = 10;

        BlockPos lowerBound = new BlockPos(senderPos.getX() - size, MathHelper.clamp(senderPos.getY() - size, 0, 256), senderPos.getZ() - size);
        BlockPos upperBound = new BlockPos(senderPos.getX() + size, MathHelper.clamp(senderPos.getY() + size, 0, 256), senderPos.getZ() + size);

        World world = sender.getEntityWorld();

        // List of blocks for neighbor updates.
        List<BlockPos> list = Lists.newArrayList();

        for (int l = lowerBound.getZ(); l <= upperBound.getZ(); ++l)
        {
            for (int i1 = lowerBound.getY(); i1 <= upperBound.getY(); ++i1)
            {
                for (int j1 = lowerBound.getX(); j1 <= upperBound.getX(); ++j1)
                {
                    BlockPos blockToCheck = new BlockPos(j1, i1, l);

                    // Removes any blocks that are an instance of block fire and puts air instead.
                    if (world.getBlockState(blockToCheck).getBlock() instanceof BlockFire)
                        if (world.setBlockState(blockToCheck, Blocks.AIR.getDefaultState(), 2))
                        {
                            list.add(blockToCheck);
                        }
                }
            }
        }

        for (BlockPos blockpos5 : list)
        {
            Block block2 = world.getBlockState(blockpos5).getBlock();
            world.notifyNeighborsRespectDebug(blockpos5, block2, false);
        }

        sender.sendMessage(new TextComponentTranslation("d3.cmd.extinguish.success", list.size()));
    }
}
