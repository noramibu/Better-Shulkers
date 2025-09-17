package com.github.noramibu.bettershulkers.interfaces;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Detects if a shulker box screen is being viewed by a player
 */
public interface ShulkerViewer {
    @Nullable
    ItemStack getViewing();

    void addViewing(@NotNull ItemStack stack, @NotNull ServerPlayer player);

    void removeViewing();

    default boolean isViewing() {
        return this.getViewing() != null;
    }

    static boolean isViewing(ItemStack testStack, ServerPlayer player) {
        return player.containerMenu instanceof ShulkerViewer shulkerViewer && testStack.equals(shulkerViewer.getViewing());
    }
}
