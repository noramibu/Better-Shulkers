package me.noramibu.bettershulkers.accessor;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public interface RemoteInventory {
    void openInventory(ServerPlayerEntity player, ItemStack stack);
}
