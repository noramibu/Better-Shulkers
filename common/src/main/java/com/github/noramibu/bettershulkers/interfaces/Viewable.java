package com.github.noramibu.bettershulkers.interfaces;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface Viewable {
    @Nullable
    ServerPlayer getViewer();

    void setViewer(@Nullable ServerPlayer player);

    default void notifyViewerOfStackChange(ItemStack newStack) {
        if (this.getViewer() != null) {
            System.out.println("copy() called; Migrating itemstack");
            ((ShulkerViewer)this.getViewer()).addViewing(newStack);
        }
    }
}
