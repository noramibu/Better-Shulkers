package me.noramibu.bettershulkers.accessor;

import net.minecraft.item.ItemStack;

public interface ShulkerViewer {
    boolean isViewingShulker();
    void setViewing(ItemStack shulker);
    ItemStack getViewedStack();
}
