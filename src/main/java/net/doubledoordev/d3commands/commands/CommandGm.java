/*
 * Copyright (c) 2014, DoubleDoorDevelopment
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *  Neither the name of the project nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.doubledoordev.d3commands.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.WorldSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wout on 28/10/2014.
 */
public class CommandGm extends CommandBase{

    @Override
    public String getCommandName() {
        return "gm";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/gm (mode/id) (username)";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (sender instanceof MinecraftServer || MinecraftServer.getServer().getConfigurationManager().func_152596_g(MinecraftServer.getServer().getConfigurationManager().func_152612_a(sender.getCommandSenderName()).getGameProfile())) {
            EntityPlayerMP player;
            switch (args.length) {
                case 1:
                    player = getCommandSenderAsPlayer(sender);
                    setMode(args, player);
                    break;
                case 0:
                    player = getCommandSenderAsPlayer(sender);
                    toggleMode(player);
                    break;
                default:
                    player = getCommandSenderAsPlayer(sender);
                    player.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
                    break;
                case 2:
                    try {
                        player = getPlayer(sender, args[1]);
                    }catch(Exception e){
                        sender.addChatMessage(new ChatComponentText("Player does not exist."));
                        break;
                    }
                    setMode(args, player);
                    break;
            }
        }else{
            sender.addChatMessage(new ChatComponentTranslation("commands.generic.permission"));
        }
    }

    private void setMode(String[] args, EntityPlayerMP player) {
        int id;
        try {
            id = Integer.parseInt(args[0]);
        }catch (Exception e){
            id = -1;
        }
        if(id != -1 && id >= 0 && id <= WorldSettings.GameType.values().length) {
            player.setGameType(WorldSettings.GameType.getByID(id));
        }else{
            player.setGameType(getGameModeFromString(args[0]));
        }
    }

    private WorldSettings.GameType getGameModeFromString(String st){
        for( WorldSettings.GameType t : WorldSettings.GameType.values()){
            if(t.getName().equalsIgnoreCase(st)){
                return t;
            }
        }
        return WorldSettings.GameType.SURVIVAL;
    }

    private void toggleMode(EntityPlayerMP player){
        if(player.capabilities.isCreativeMode){
            player.setGameType(WorldSettings.GameType.SURVIVAL);
        }else{
            player.setGameType(WorldSettings.GameType.CREATIVE);
        }
    }

    private List<String> getGameModes(){
        List<String> res = new ArrayList<String>();
        for( WorldSettings.GameType t : WorldSettings.GameType.values()){
            res.add(t.getName());
        }
        return res;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        List<String> res = new ArrayList<String>();
        res.addAll(getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()));
        res.addAll(this.getGameModes());
        return res;
    }


}
