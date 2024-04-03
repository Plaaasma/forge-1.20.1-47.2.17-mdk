package net.plaaasma.nerdorgcore.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.plaaasma.nerdorgcore.mapdata.VanishedPlayerData;
import net.plaaasma.nerdorgcore.util.LogUtil;

import java.util.HashMap;

public class TrashCommand {
    public TrashCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("trash")
        .executes((command) -> {
                return openTrash(command.getSource());
            }));

        dispatcher.register(Commands.literal("disposal")
                .executes((command) -> {
                    return openTrash(command.getSource());
                }));

        dispatcher.register(Commands.literal("garbage")
                .executes((command) -> {
                    return openTrash(command.getSource());
                }));
    }

    private int openTrash(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayer();

        SimpleMenuProvider menuProvider = new SimpleMenuProvider((id, pInventory, p_53126_) -> {
            return ChestMenu.sixRows(id, pInventory, new Container() {
                @Override
                public int getContainerSize() {
                    return 54;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public ItemStack getItem(int pSlot) {
                    return new ItemStack(Items.AIR);
                }

                @Override
                public ItemStack removeItem(int pSlot, int pAmount) {
                    return new ItemStack(Items.AIR);
                }

                @Override
                public ItemStack removeItemNoUpdate(int pSlot) {
                    return new ItemStack(Items.AIR);
                }

                @Override
                public void setItem(int pSlot, ItemStack pStack) {
                }

                @Override
                public void setChanged() {
                }

                @Override
                public boolean stillValid(Player pPlayer) {
                    return true;
                }

                @Override
                public void clearContent() {
                }
            });
        }, Component.literal("Disposal"));

        player.openMenu(menuProvider);
        return 1;
    }
}
