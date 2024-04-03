package net.plaaasma.nerdorgcore.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.plaaasma.nerdorgcore.mapdata.HomeData;
import net.plaaasma.nerdorgcore.mapdata.VanishedPlayerData;
import net.plaaasma.nerdorgcore.util.LogUtil;

import java.util.HashMap;

public class HomeCommand {
    public HomeCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("home")
                .then(Commands.literal("set")
                            .then(Commands.argument("name", MessageArgument.message())
                                    .executes((command) -> {
                                        return setHome(command.getSource(), MessageArgument.getMessage(command, "name").getString());
                                    }))));

        dispatcher.register(Commands.literal("home")
                .then(Commands.literal("remove")
                        .then(Commands.argument("name", MessageArgument.message())
                                .executes((command) -> {
                                    return delHome(command.getSource(), MessageArgument.getMessage(command, "name").getString());
                                }))));

        dispatcher.register(Commands.literal("home")
                .then(Commands.argument("name", MessageArgument.message())
                        .executes((command) -> {
                            return gotoHome(command.getSource(), MessageArgument.getMessage(command, "name").getString());
                        })));

        dispatcher.register(Commands.literal("home")
                .then(Commands.literal("list")
                        .executes((command) -> {
                            return listHomes(command.getSource());
                        })));
    }

    private int setHome(CommandSourceStack source, String homeName) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayer();
        ServerLevel overworld = source.getServer().getLevel(Level.OVERWORLD);
        HomeData homeData = HomeData.get(overworld);
        HashMap<String, CompoundTag> homeMap = homeData.getDataMap();

        CompoundTag homeTag = new CompoundTag();
        homeTag.putDouble("x", player.getX());
        homeTag.putDouble("y", player.getY());
        homeTag.putDouble("z", player.getZ());
        homeTag.putString("dim", player.level().dimension().location().getPath());
        homeTag.putDouble("xrot", player.getXRot());
        homeTag.putDouble("yrot", player.getYRot());

        homeMap.put(player.getScoreboardName() + homeName, homeTag);
        LogUtil.doSuccessMessage(player, "Created a home called " + homeName + ".");
        homeData.setDirty();

        return 1;
    }

    private int delHome(CommandSourceStack source, String homeName) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayer();
        ServerLevel overworld = source.getServer().getLevel(Level.OVERWORLD);
        HomeData homeData = HomeData.get(overworld);
        HashMap<String, CompoundTag> homeMap = homeData.getDataMap();
        String mapKey = player.getScoreboardName() + homeName;
        if (homeMap.containsKey(mapKey)) {
            LogUtil.doSuccessMessage(player, "Deleting your home called " + homeName + ".");
            homeMap.remove(mapKey);
            homeData.setDirty();
        }

        return 1;
    }

    private int gotoHome(CommandSourceStack source, String homeName) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayer();
        ServerLevel overworld = source.getServer().getLevel(Level.OVERWORLD);
        HomeData homeData = HomeData.get(overworld);
        HashMap<String, CompoundTag> homeMap = homeData.getDataMap();
        String mapKey = player.getScoreboardName() + homeName;
        if (homeMap.containsKey(mapKey)) {
            CompoundTag homeTag = homeMap.get(mapKey);
            String levelName = homeTag.getString("dim");
            ServerLevel targetLevel = null;
            for (ServerLevel iterLevel : source.getServer().getAllLevels()) {
                if (iterLevel.dimension().location().getPath().equals(levelName)) {
                    targetLevel = iterLevel;
                    break;
                }
            }
            LogUtil.doSuccessMessage(player, "Teleporting to " + homeName + ".");
            player.teleportTo(targetLevel, homeTag.getDouble("x"), homeTag.getDouble("y"), homeTag.getDouble("z"), (float) homeTag.getDouble("yrot"), (float) homeTag.getDouble("xrot"));
        }
        else {
            LogUtil.doFailureMessage(player, "You don't have a home called " + homeName + ".");
        }


        return 1;
    }

    private int listHomes(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayer();
        ServerLevel overworld = source.getServer().getLevel(Level.OVERWORLD);
        HomeData homeData = HomeData.get(overworld);
        HashMap<String, CompoundTag> homeMap = homeData.getDataMap();

        StringBuilder combinedHomeString = new StringBuilder();
        for (String key : homeMap.keySet()) {
            if (key.startsWith(player.getScoreboardName())) {
                CompoundTag homeTag = homeMap.get(key);
                double saved_x = homeTag.getDouble("x");
                double saved_y = homeTag.getDouble("y");
                double saved_z = homeTag.getDouble("z");
                String levelName = homeTag.getString("dim");

                String homeName = key.substring(player.getScoreboardName().length());
                combinedHomeString.append("\n" + homeName + ": " +
                        String.format("%.2f", saved_x) + " | " + String.format("%.2f", saved_y) + " | " + String.format("%.2f", saved_z) + " | " + levelName);
            }
        }

        LogUtil.doSuccessMessage(player, "Homes \\/" + combinedHomeString);

        return 1;
    }
}
