package net.plaaasma.nerdorgcore.events;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.profiling.jfr.event.PacketEvent;
import net.minecraft.util.profiling.jfr.event.PacketSentEvent;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import net.plaaasma.nerdorgcore.NerdOrgServerMod;
import net.plaaasma.nerdorgcore.commands.GamemodeCommand;
import net.plaaasma.nerdorgcore.commands.HomeCommand;
import net.plaaasma.nerdorgcore.commands.TrashCommand;
import net.plaaasma.nerdorgcore.commands.VanishCommand;
import net.plaaasma.nerdorgcore.mapdata.VanishedPlayerData;

import java.util.HashMap;

@Mod.EventBusSubscriber(modid = NerdOrgServerMod.MODID)
public class ModEvents {
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new VanishCommand(event.getDispatcher());
        new TrashCommand(event.getDispatcher());
        new HomeCommand(event.getDispatcher());
        new GamemodeCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerEvent(PlayerEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            ServerLevel overworld = serverPlayer.getServer().getLevel(Level.OVERWORLD);
            VanishedPlayerData vanishedPlayerData = VanishedPlayerData.get(overworld);
            HashMap<String, Boolean> vanishedPlayerMap = vanishedPlayerData.getDataMap();
            if (vanishedPlayerMap.containsKey(serverPlayer.getScoreboardName())) {
                if (vanishedPlayerMap.get(serverPlayer.getScoreboardName())) {
                    if (serverPlayer.connection != null) {
                        serverPlayer.displayClientMessage(Component.literal("Vanished").withStyle(ChatFormatting.AQUA), true);
                        serverPlayer.setInvisible(true);
                    }
                }
            }
        }
    }
}
