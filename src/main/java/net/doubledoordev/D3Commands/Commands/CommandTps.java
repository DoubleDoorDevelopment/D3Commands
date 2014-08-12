package net.doubledoordev.D3Commands.Commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.DimensionManager;

import java.text.DecimalFormat;
import java.util.List;

public class CommandTps extends CommandBase
{
    private static final DecimalFormat timeFormatter = new DecimalFormat("########0.000");

    @Override
    public String getCommandName()
    {
        return "tps";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/tps [worldID]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        int dim = 0;
        boolean summary = true;
        if (args.length > 0)
        {
            dim = parseInt(sender, args[0]);
            summary = false;
        }
        if (summary)
        {
            for (Integer dimId : DimensionManager.getIDs())
            {
                double worldTickTime = mean(MinecraftServer.getServer().worldTickTimes.get(dimId)) * 1.0E-6D;
                double worldTPS = Math.min(1000.0 / worldTickTime, 20);
                sender.addChatMessage(IChatComponent.Serializer.func_150699_a("commands.forge.tps.summary" + String.format("Dim %d", dimId) + timeFormatter.format(worldTickTime) + timeFormatter.format(worldTPS)));
            }
            double meanTickTime = mean(MinecraftServer.getServer().tickTimeArray) * 1.0E-6D;
            double meanTPS = Math.min(1000.0 / meanTickTime, 20);
            sender.addChatMessage(IChatComponent.Serializer.func_150699_a("commands.forge.tps.summary" + "Overall" + timeFormatter.format(meanTickTime) + timeFormatter.format(meanTPS)));
        }
        else
        {
            double worldTickTime = mean(MinecraftServer.getServer().worldTickTimes.get(dim)) * 1.0E-6D;
            double worldTPS = Math.min(1000.0 / worldTickTime, 20);
            sender.addChatMessage(IChatComponent.Serializer.func_150699_a("commands.forge.tps.summary" + String.format("Dim %d", dim) + timeFormatter.format(worldTickTime) + timeFormatter.format(worldTPS)));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(final ICommandSender sender)
    {
        return true;
    }

    private static long mean(long[] values)
    {
        long sum = 0l;
        for (long v : values)
        {
            sum += v;
        }

        return sum / values.length;
    }

    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args)
    {
        if (args.length == 1)
        {
            String[] strings = new String[DimensionManager.getIDs().length];
            for (int i = 0; i < DimensionManager.getIDs().length; i++)
                strings[i] = DimensionManager.getIDs()[i].toString();
            return getListOfStringsMatchingLastWord(args, strings);
        }
        return null;
    }
}
