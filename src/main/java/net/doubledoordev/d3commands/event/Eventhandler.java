package net.doubledoordev.d3commands.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.doubledoordev.d3commands.ModConfig;

@Mod.EventBusSubscriber
public class Eventhandler
{
    @SubscribeEvent
    public void nameOnLoad(PlayerEvent.LoadFromFile event)
    {
        EntityPlayer player = event.getEntityPlayer();
        NBTTagCompound playerData = player.getEntityData();

        if (playerData.hasKey("d3name"))
        {
            if (playerData.getCompoundTag("d3name").hasKey("Prefix"))
                player.addPrefix(new TextComponentString(playerData.getCompoundTag("d3name").getString("Prefix").replace("&", "\u00A7")));

            if (playerData.getCompoundTag("d3name").hasKey("Suffix"))
                player.addSuffix(new TextComponentString(playerData.getCompoundTag("d3name").getString("Suffix").replace("&", "\u00A7")));

        }
    }

    @SubscribeEvent
    public void nameFormatEvent(PlayerEvent.NameFormat event)
    {
        EntityPlayer player = event.getEntityPlayer();
        NBTTagCompound playerData = player.getEntityData();

        if (playerData.hasKey("d3name"))
        {
            if (playerData.getCompoundTag("d3name").hasKey("Display"))
                event.setDisplayname(playerData.getCompoundTag("d3name").getString("Display").replace("&", "\u00A7"));
        }
    }

    @SubscribeEvent
    public void potionKepper(PotionEvent.PotionRemoveEvent event)
    {
        //This stops things from removing the effect like milk.
        EntityLivingBase entity = event.getEntityLiving();
        NBTTagCompound targetNBT;
        if (entity instanceof EntityPlayerMP)
        {
            targetNBT = entity.getEntityData();
            if (entity.isPotionActive(MobEffects.NIGHT_VISION))
                if (targetNBT.hasKey("d3nightvision"))
                    if (targetNBT.getString("d3nightvision").equals("true"))
                    {
                        event.setCanceled(true);
                    }
        }
    }

    @SubscribeEvent
    public void livingEntityUpdate(LivingEvent.LivingUpdateEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();
        NBTTagCompound targetNBT;
        if (entity instanceof EntityPlayerMP)
        {
            targetNBT = entity.getEntityData();
            if (entity.isPotionActive(MobEffects.NIGHT_VISION))
                if (targetNBT.hasKey("d3nightvision"))
                    if (targetNBT.getString("d3nightvision").equals("true"))
                    {
                        entity.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 500, 0, true, false));
                    }
        }
    }

    @SubscribeEvent
    public void dimBlocker(EntityTravelToDimensionEvent event)
    {
        int targetDim = event.getDimension();
        Entity target = event.getEntity();
        String[] players;

        players = target.getServer().getPlayerList().getOppedPlayerNames();

        if (target instanceof EntityPlayer)
        {
            if (((EntityPlayer) target).isCreative() | ((EntityPlayer) target).isSpectator())
                return;
            for (String player : players)
            {
                if (!target.getName().equals(player))
                {
                    for (int dim : ModConfig.blockedDims)
                    {
                        if (ModConfig.blockDimListWhitelist && targetDim != dim)
                        {
                            event.setCanceled(true);
                        }
                        else
                        {
                            if (!ModConfig.blockDimListWhitelist && targetDim == dim)
                            {
                                event.setCanceled(true);
                            }
                        }
                    }
                }
            }
        }
    }
}

