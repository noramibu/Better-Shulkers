package com.github.noramibu.bettershulkers.interfaces;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public interface SimpleContainerAccessor {
    void setItems(NonNullList<ItemStack> inventory);
    void setSize(int size);
}
