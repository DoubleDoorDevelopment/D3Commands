package net.doubledoordev.d3commands.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import net.doubledoordev.d3commands.ModConfig;
import net.doubledoordev.d3commands.util.BlockPosDim;

public class CommandLocatePortal extends CommandBase
{
    @Override
    public String getName()
    {
        return "locateportal";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "d3.cmd.portal.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP target = getCommandSenderAsPlayer(sender);
        int dim = target.world.provider.getDimension();
        BlockPos pos = new BlockPos(target.getPosition());

        switch (dim)
        {
            default:
                sender.sendMessage(new TextComponentTranslation("d3.cmd.portal.fail"));
            case -1:
                int x = pos.getX() * 8;
                int z = pos.getZ() * 8;
                BlockPosDim clickPos = new BlockPosDim(x, pos.getY(), z, 0);
                sender.sendMessage(new TextComponentTranslation("d3.cmd.portal.overworld").appendText(" ").appendSibling(clickPos.toClickableChatString()));
                break;
            case 0:
                int x1 = pos.getX() / 8;
                int z1 = pos.getZ() / 8;
                BlockPosDim clickPos1 = new BlockPosDim(x1, pos.getY(), z1, -1);
                sender.sendMessage(new TextComponentTranslation("d3.cmd.portal.nether").appendText(" ").appendSibling(clickPos1.toClickableChatString()));
                break;
        }
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return ModConfig.locateportalPermissionLevel;
    }
}
