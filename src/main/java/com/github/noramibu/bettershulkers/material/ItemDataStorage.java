/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.material;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.ItemEnchantments;

/**
 * Tracks data when the shulker is placed and broken
 */
public interface ItemDataStorage {
    /**
     * Stores ItemStack data in the BlockEntity
     * @param stack ItemStack that is used to create the BlockEntity
     */
    void storeItemData(ItemStack stack);

    /**
     * Gets the CustomData from the ItemStack
     * @return CustomData attached to the ItemStack
     */
    CustomData getData();

    /**
     * Gets the Lore from the ItemStack
     * @return Lore attached to the ItemStack
     */
    ItemLore getLore();

    /**
     * Gets the enchantments from the ItemStack
     * @return Enchantments attached to the ItemStack
     */
    ItemEnchantments getEnchantments();
}
