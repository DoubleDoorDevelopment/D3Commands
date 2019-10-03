package net.doubledoordev.d3commands.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

import net.doubledoordev.d3commands.ModConfig;

public class CommandClearDrops extends CommandBase
{
    @Override
    public String getName()
    {
        return "cleardrops";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "d3.cmd.ping.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        List<Entity> entities = sender.getEntityWorld().loadedEntityList;
        List<EntityItem> items = new ArrayList<>();

        for (Entity entity : entities)
        {
            if (entity instanceof EntityItem)
            {
                items.add((EntityItem) entity);
            }
        }

        for (EntityItem item : items)
        {
            if (item.lifespan > 0)
            {
                item.setDead();
            }
        }

        sender.sendMessage(new TextComponentTranslation("d3.cmd.cleardrops", items.size()));
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return ModConfig.locateportalPermissionLevel;
    }
}