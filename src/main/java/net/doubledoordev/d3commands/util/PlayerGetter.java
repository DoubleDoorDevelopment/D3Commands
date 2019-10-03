package net.doubledoordev.d3commands.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.FakePlayer;

import com.mojang.authlib.GameProfile;

import static net.minecraft.command.CommandBase.getPlayer;

public class PlayerGetter
{
    //TODO: Document this. It's many important
    public static EntityPlayer onlineOrOffline(MinecraftServer server, ICommandSender sender, String name)
    {
        try
        {
            return getPlayer(server, sender, name);
        }
        catch (PlayerNotFoundException e)
        {
            try
            {
                File playerDataFolder = new File(server.worlds[0].getSaveHandler().getWorldDirectory(), "playerdata");
                File playerDataFile = new File(playerDataFolder, name + ".dat");

                NBTTagCompound playerData = CompressedStreamTools.readCompressed(new FileInputStream(playerDataFile));
                UUID uuid = UUID.fromString(name);
                GameProfile gameProfile = server.getPlayerProfileCache().getProfileByUUID(uuid);

                if (gameProfile == null) gameProfile = new GameProfile(uuid, name);
                FakePlayer fakeplayer = new FakePlayer(server.worlds[0], gameProfile);
                fakeplayer.readEntityFromNBT(playerData);
                return fakeplayer;
            }
            catch (IOException ignored)
            {

            }
        }
        catch (CommandException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static NBTTagCompound onlineOrOfflineNBT(MinecraftServer server, String uuid)
    {
        try
        {
            File playerDataFolder = new File(server.worlds[0].getSaveHandler().getWorldDirectory(), "playerdata");
            File playerDataFile = new File(playerDataFolder, uuid + ".dat");

            NBTTagCompound playerData = CompressedStreamTools.readCompressed(new FileInputStream(playerDataFile));
            return playerData;
        }
        catch (IOException ignored)
        {

        }
        return null;
    }
}

