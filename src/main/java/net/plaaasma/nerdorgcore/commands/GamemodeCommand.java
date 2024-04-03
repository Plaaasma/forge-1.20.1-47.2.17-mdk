package net.plaaasma.nerdorgcore.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.plaaasma.nerdorgcore.mapdata.VanishedPlayerData;
import net.plaaasma.nerdorgcore.util.LogUtil;

import java.util.HashMap;

public class GamemodeCommand {
    public GamemodeCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("gm")
        .requires(source -> source.hasPermission(4))
        .then(Commands.literal("c"))
        .executes((command) -> {
                return setCreative(command.getSource());
            }));

        dispatcher.register(Commands.literal("gm")
                .requires(source -> source.hasPermission(4))
                .then(Commands.literal("s"))
                .executes((command) -> {
                    return setSurvival(command.getSource());
                }));

        dispatcher.register(Commands.literal("gm")
                .requires(source -> source.hasPermission(4))
                .then(Commands.literal("a"))
                .executes((command) -> {
                    return setAdventure(command.getSource());
                }));

        dispatcher.register(Commands.literal("gm")
                .requires(source -> source.hasPermission(4))
                .then(Commands.literal("sp"))
                .executes((command) -> {
                    return setSpectator(command.getSource());
                }));

        dispatcher.register(Commands.literal("gmc")
                .requires(source -> source.hasPermission(4))
                .executes((command) -> {
                    return setCreative(command.getSource());
                }));

        dispatcher.register(Commands.literal("gms")
                .requires(source -> source.hasPermission(4))
                .executes((command) -> {
                    return setSurvival(command.getSource());
                }));

        dispatcher.register(Commands.literal("gma")
                .requires(source -> source.hasPermission(4))
                .executes((command) -> {
                    return setAdventure(command.getSource());
                }));

        dispatcher.register(Commands.literal("gmsp")
                .requires(source -> source.hasPermission(4))
                .executes((command) -> {
                    return setSpectator(command.getSource());
                }));
    }

    private int setCreative(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayer();
        player.setGameMode(GameType.CREATIVE);
        LogUtil.doSuccessMessage(player, "Set gamemode to creative.");
        return 1;
    }

    private int setSurvival(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayer();
        player.setGameMode(GameType.SURVIVAL);
        LogUtil.doSuccessMessage(player, "Set gamemode to survival.");
        return 1;
    }

    private int setAdventure(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayer();
        player.setGameMode(GameType.ADVENTURE);
        LogUtil.doSuccessMessage(player, "Set gamemode to adventure.");
        return 1;
    }

    private int setSpectator(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayer();
        player.setGameMode(GameType.SPECTATOR);
        LogUtil.doSuccessMessage(player, "Set gamemode to spectator.");
        return 1;
    }
}
