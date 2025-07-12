package com.github.noramibu.bettershulkers.interfaces;

import net.minecraft.world.item.ItemStack;

public interface ShulkerViewer {
    boolean isViewingShulker();
    void setViewing(ItemStack shulker);
    ItemStack getViewedStack();
}
