package com.github.noramibu.bettershulkers.interfaces;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public interface RemoteInventory {
    void openInventory(ServerPlayer player, ItemStack stack);
}
