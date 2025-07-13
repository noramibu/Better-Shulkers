package com.github.noramibu.bettershulkers.abstraction;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.item.ItemStack;

public interface PlatformAbstraction {
    boolean isShulkerBox(ItemStack stack);
    boolean permissionCheck(CommandSourceStack source, String id, boolean hasProperPermissionLevel);
}
