/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.event;

import net.minecraft.world.item.ItemStack;

/**
 * Represents an ItemStack moving between Slots
 * @param stack ItemStack being moved
 * @param targetSlot The destination Slot index
 */
public record ItemMove(ItemStack stack, int targetSlot) {}
