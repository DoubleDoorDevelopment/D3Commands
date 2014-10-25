package net.doubledoordev.d3commands.commands;

import net.minecraft.block.material.Material;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandTop extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "top";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/top - Teleports you to the highest surface block.";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public boolean isUsernameIndex(final String[] args, final int userIndex)
    {
        return userIndex == 0 || userIndex == 1;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        EntityPlayerMP player =  getCommandSenderAsPlayer(sender);
        for (int y = player.getEntityWorld().getActualHeight(); y > player.getPlayerCoordinates().posY; y--)
        {
            if (player.getEntityWorld().getBlock(player.getPlayerCoordinates().posX, y, player.getPlayerCoordinates().posZ).getMaterial() != Material.air)
            {
                teleportPlayer(sender, player, player.getEntityWorld().getWorldInfo().getVanillaDimension(), player.getPlayerCoordinates().posX, y + 2, player.getPlayerCoordinates().posZ);
                System.out.println("Teleported " + player.getDisplayName() + " to:" + player.getPlayerCoordinates());
                return;
            }
        }
    }

    /**
     * Teleports a player to coordinates
     */
    public  void teleportPlayer(final ICommandSender sender, final EntityPlayerMP player, final int dimension, final double x, final double y, final double z)
    {
        player.mountEntity(null);
        if (player.dimension != dimension)
        {
            MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, dimension);
        }
        player.setPositionAndUpdate(x, y, z);
        player.prevPosX = player.posX = x;
        player.prevPosY = player.posY = y;
        player.prevPosZ = player.posZ = z;
    }

}