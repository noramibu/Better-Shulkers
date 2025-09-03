package com.github.noramibu.bettershulkers.interfaces;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public interface MutableContainerInventory {
    void setInventory(NonNullList<ItemStack> inventory);
}
