package com.github.noramibu.bettershulkers.abstraction;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.item.ItemStack;

/**
 * NeoForge and Fabric abstracted methods
 */
public interface PlatformAbstraction {
    /**
     * If the item is a shulker box
     * @param stack Item to be checked
     * @return If the item is a shulker box
     */
    boolean isShulkerBox(ItemStack stack);

    /**
     * Checks the player's permissions to see if the command can be executed
     * @param source The Command's source (the player)
     * @param id The permission id
     * @param hasProperPermissionLevel The backup vanilla-op if a permission system is not in place
     * @return If the player has sufficient permissions
     */
    boolean permissionCheck(CommandSourceStack source, String id, boolean hasProperPermissionLevel);
}
