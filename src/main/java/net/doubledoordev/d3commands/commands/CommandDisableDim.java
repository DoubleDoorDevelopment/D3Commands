//package net.doubledoordev.d3commands.commands;
//
//import java.util.Arrays;
//
//import net.minecraft.command.CommandBase;
//import net.minecraft.command.CommandException;
//import net.minecraft.command.ICommandSender;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.util.text.TextComponentTranslation;
//
//import net.minecraftforge.fml.common.Mod;
//
//import net.doubledoordev.d3commands.D3Commands;
//import net.doubledoordev.d3commands.ModConfig;
// TODO: Make this work someday.
//public class CommandDisableDim extends CommandBase
//{
//    @Override
//    public String getName()
//    {
//        return "disableDim";
//    }
//
//    @Override
//    public String getUsage(ICommandSender sender)
//    {
//        return "d3.cmd.disabledim.usage";
//    }
//
//    @Override
//    public int getRequiredPermissionLevel()
//    {
//        return ModConfig.disableDimPermissionLevel;
//    }
//
//    @Override
//    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
//    {
//        // /disabledim [int] or /disabledim clear or /disabledim toggle
//
//        if (args.length == 0);
//        {
//            sender.sendMessage(new TextComponentTranslation("d3.cmd.disabledim.info", ModConfig.blockDimListWhitelist, Arrays.toString(ModConfig.blockedDims)));
//        }
//        else
//        {
//            switch (args[1])
//            {
//                case "clear":
//                {
//
//                }
//                case "toggle":
//                {
//
//                }
//                default:
//                {
//
//                }
//            }
//        }
//    }
//}