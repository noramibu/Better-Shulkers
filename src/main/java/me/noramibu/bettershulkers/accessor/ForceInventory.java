package me.noramibu.bettershulkers.accessor;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public interface ForceInventory {
    void setInventory(DefaultedList<ItemStack> inventory);
    void setForced();
    boolean forced();
}
