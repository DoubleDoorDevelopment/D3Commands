package net.doubledoordev.d3commands;

import net.minecraftforge.common.config.Config;

import net.doubledoordev.d3commands.util.Constants;

@Config(modid = Constants.MODID)
@Config.LangKey("d3commands.config.title")
public class ModConfig
{
    @Config.LangKey("d3commands.config.back")
    @Config.Comment({
            "Returns the user/target to the last place they died.",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int backPermissionLevel = 2;

    @Config.LangKey("d3commands.config.bedtp")
    @Config.Comment({
            "Teleports the user to thier own bed or the targets bed.",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int bedTpPermissionLevel = 2;

    @Config.LangKey("d3commands.config.explorers")
    @Config.Comment({
            "Finds the farthest X users in X dimensions spawn. Can use custom x,z cords.",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int explorersPermissionLevel = 1;

    @Config.LangKey("d3commands.config.feed")
    @Config.Comment({
            "Feeds the user/target player.",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int feedPermissionLevel = 2;

    @Config.LangKey("d3commands.config.fireworks")
    @Config.Comment({
            "Spawns X randomised fireworks on a player in X radius.",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int fireworksPermissionLevel = 2;

    @Config.LangKey("d3commands.config.fly")
    @Config.Comment({
            "Enables/Disables flying for the user/target.",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int flyPermissionLevel = 4;

    @Config.LangKey("d3commands.config.getuuid")
    @Config.Comment({
            "Gets the UUID of an online player(s).",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int getUUIDPermissionLevel = 2;

    @Config.LangKey("d3commands.config.gm")
    @Config.Comment({
            "Changes the gamemode of the user/target",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int gmPermissionLevel = 4;

    @Config.LangKey("d3commands.config.god")
    @Config.Comment({
            "Enables/Disables invulnerability for the user/target",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int godPermissionLevel = 4;

    @Config.LangKey("d3commands.config.heal")
    @Config.Comment({
            "Heals the user/target",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int healPermissionLevel = 4;

    @Config.LangKey("d3commands.config.highlight")
    @Config.Comment({
            "Toggles the glow effect on the user/target.",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int highlightPermissionLevel = 2;

    @Config.LangKey("d3commands.config.invsee")
    @Config.Comment({
            "Allows the user to look into another players vanilla inventory and modify it.",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int invseePermissionLevel = 4;

    @Config.LangKey("d3commands.config.lore")
    @Config.Comment({
            "Edits the lore on the held item.",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int lorePermissionLevel = 1;

    @Config.LangKey("d3commands.config.mem")
    @Config.Comment({
            "Displays the current, max and free RAM for the server.",
            "0 = Everyone, 1-2 Commandblocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int memPermissionLevel = 0;

    @Config.LangKey("d3commands.config.offlinetp")
    @Config.Comment({
            "Teleports the user to the targets last known location.",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int offlinetpPermissionLevel = 4;

    @Config.LangKey("d3commands.config.locateportal")
    @Config.Comment({
            "Allows the user to get the exact coordinates for nether portal placement in the opposite dim they are in. ",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int locateportalPermissionLevel = 0;

    @Config.LangKey("d3commands.config.pos")
    @Config.Comment({
            "Gets the coordinates of the target.",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int posPermissionLevel = 2;

    @Config.LangKey("d3commands.config.smite")
    @Config.Comment({
            "Strikes the user/target in X radius with X stikes.",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int smitePermissionLevel = 4;

    @Config.LangKey("d3commands.config.spawn")
    @Config.Comment({
            "Teleports the user/target to the spawn of the dimension they are in.",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int spawnPermissionLevel = 1;

    @Config.LangKey("d3commands.config.top")
    @Config.Comment({
            "Teleports the user/target to the top most block that can see sky.",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int topPermissionLevel = 4;

    @Config.LangKey("d3commands.config.tps")
    @Config.Comment({
            "Displays the current TPS for all dimensions.",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int tpsPermissionLevel = 0;

    @Config.LangKey("d3commands.config.tpx")
    @Config.Comment({
            "Teleports the user/target(s) to X dimension/location in X dimension.",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int tpxPermissionLevel = 4;

    @Config.LangKey("d3commands.config.name")
    @Config.Comment({
            "Edits the name on the held item.",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int namePermissionLevel = 1;

    @Config.LangKey("d3commands.config.nick")
    @Config.Comment({
            "Edits the name on the held item.",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int nickPermissionLevel = 1;

    @Config.LangKey("d3commands.config.nick")
    @Config.Comment({
            "Edits the name on the held item.",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int nightVisionPermissionLevel = 1;

    @Config.LangKey("d3commands.config.nick")
    @Config.Comment({
            "Edits the name on the held item.",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int homePermissionLevel = 1;

    @Config.LangKey("d3commands.config.blockdim.dimlist")
    @Config.Comment("Blocked or Allowed dims.")
    public static int[] blockedDims = new int[1];
    @Config.LangKey("d3commands.config.blockdim")
    @Config.Comment({
            "Blocks or Allows dim movement.",
            "0 = Everyone, 1-2 Command blocks can use, 3-4 are OP only depending on player OP level, Console can run all."})
    @Config.RangeInt(min = 0, max = 4)
    public static int disableDimPermissionLevel = 3;
    @Config.LangKey("d3commands.config.blockdim.listtoggle")
    @Config.Comment({
            "Whitelist/Blacklist Toggle",
            "Whitelist = true / Blacklist = false"})
    public static boolean blockDimListWhitelist = false;

    static
    {
        blockedDims[0] = -1;
    }
}
