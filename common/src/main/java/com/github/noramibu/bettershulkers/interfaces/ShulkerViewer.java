package com.github.noramibu.bettershulkers.interfaces;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Detects if a shulker box screen is being viewed by a player
 */
public interface ShulkerViewer {
    @Nullable
    ItemStack getViewing();

    void addViewing(@NotNull ItemStack stack);

    void removeViewing();

    default boolean isViewing() {
        return this.getViewing() != null;
    }
}
