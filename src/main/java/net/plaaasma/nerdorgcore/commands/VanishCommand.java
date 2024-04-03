package net.plaaasma.nerdorgcore.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.plaaasma.nerdorgcore.mapdata.VanishedPlayerData;
import net.plaaasma.nerdorgcore.util.LogUtil;

import java.util.HashMap;

public class VanishCommand {
    public VanishCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vanish")
        .requires(source -> source.hasPermission(4))
        .executes((command) -> {
                return toggleVanish(command.getSource());
            }));
    }

    private int toggleVanish(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayer();
        ServerLevel overworld = source.getServer().getLevel(Level.OVERWORLD);
        VanishedPlayerData vanishedPlayerData = VanishedPlayerData.get(overworld);
        HashMap<String, Boolean> vanishedPlayerMap = vanishedPlayerData.getDataMap();
        String mapKey = player.getScoreboardName();
        if (vanishedPlayerMap.containsKey(mapKey)) {
            vanishedPlayerMap.put(mapKey, !vanishedPlayerMap.get(mapKey));
        }
        else {
            vanishedPlayerMap.put(mapKey, true);
        }
        Boolean vanishState = vanishedPlayerMap.get(mapKey);
        if (!vanishState) {
            player.setInvisible(false);
        }
        LogUtil.doSuccessMessage(player, "Your vanished state is now: " + vanishState + ".");
        vanishedPlayerData.setDirty();
        return 1;
    }
}
