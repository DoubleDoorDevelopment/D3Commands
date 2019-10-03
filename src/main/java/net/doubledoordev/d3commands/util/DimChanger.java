package net.doubledoordev.d3commands.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Teleporter;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class DimChanger
{
    public static Entity changeDim(Entity target, int dimension)
    {
        if (target instanceof EntityPlayerMP)
        {
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            Teleporter teleporter = new Teleporter(server.getWorld(dimension))
            {
                @Override
                public void placeInPortal(Entity entityIn, float rotationYaw)
                {

                }

                @Override
                public boolean placeInExistingPortal(Entity entityIn, float rotationYaw)
                {
                    return false;
                }

                @Override
                public boolean makePortal(Entity entityIn)
                {
                    return false;
                }

                @Override
                public void removeStalePortalLocations(long worldTime)
                {

                }
            };
            server.getPlayerList().transferPlayerToDimension(((EntityPlayerMP) target), dimension, teleporter);
            return target;
        }
        return target.changeDimension(dimension);
    }
}
