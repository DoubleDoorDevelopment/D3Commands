package net.doubledoordev.d3commands.commands;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.Constants;

import net.doubledoordev.d3commands.ModConfig;

public class CommandNightVision extends CommandBase
{
    @Override
    public String getName()
    {
        return "nightvision";
    }

    @Override
    public String getUsage(ICommandSender icommandsender)
    {
        return "d3.cmd.nightvision.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP target;

        if (args.length == 0) target = getCommandSenderAsPlayer(sender);
        else target = getPlayer(server, sender, args[0]);

        NBTTagCompound targetNBT = target.getEntityData();

        if (!targetNBT.hasKey("d3nightvision", Constants.NBT.TAG_STRING))
            targetNBT.setTag("d3nightvision", new NBTTagString());

        if (targetNBT.getString("d3nightvision").equals("false"))
        {
            targetNBT.setTag("d3nightvision", new NBTTagString("true"));
            target.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 500, 0, true, false));
            sender.sendMessage(new TextComponentTranslation("d3.cmd.nightvision.enabled", target.getDisplayName()));
        }
        else
        {
            Potion nightVision = Potion.getPotionFromResourceLocation("night_vision");

            targetNBT.setTag("d3nightvision", new NBTTagString("false"));
            if (nightVision != null)
                target.removePotionEffect(nightVision);
            sender.sendMessage(new TextComponentTranslation("d3.cmd.nightvision.disabled", target.getDisplayName()));
        }
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return ModConfig.nightVisionPermissionLevel;
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
}