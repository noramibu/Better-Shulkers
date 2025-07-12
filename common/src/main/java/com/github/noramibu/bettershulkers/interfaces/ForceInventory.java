package com.github.noramibu.bettershulkers.interfaces;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public interface ForceInventory {
    void setInventory(NonNullList<ItemStack> inventory);
    void setForced();
    boolean forced();
}
