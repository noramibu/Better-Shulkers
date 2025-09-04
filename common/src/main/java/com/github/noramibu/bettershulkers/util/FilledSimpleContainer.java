package com.github.noramibu.bettershulkers.util;

import com.github.noramibu.bettershulkers.interfaces.SimpleContainerAccessor;
import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class FilledSimpleContainer extends SimpleContainer {
    public FilledSimpleContainer(NonNullList<ItemStack> inventory) {
        ((SimpleContainerAccessor) this).setSize(27);
        ((SimpleContainerAccessor) this).setItems(inventory);
    }
}
