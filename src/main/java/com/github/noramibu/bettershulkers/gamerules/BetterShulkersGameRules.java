/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.gamerules;

import net.minecraft.world.level.gamerules.GameRule;

/**
 * Stores all GameRules for Better Shulkers
 */
public class BetterShulkersGameRules {
    /**
     * Allows the Material Collector enchantment
     */
    public static GameRule<Boolean> SHULKER_MATERIAL_ENCHANTMENT;
    /**
     * Allows the material recipe
     */
    public static GameRule<Boolean> SHULKER_MATERIAL_RECIPE;
    /**
     * Allows material displays on Shulker Box lids
     */
    public static GameRule<Boolean> DO_SHULKER_MATERIAL_DISPLAYS;
    /**
     * Allows material displays to be animated
     */
    public static GameRule<Boolean> DO_SHULKER_DISPLAY_ANIMATIONS;
    /**
     * Allows players to open shulker boxes from their hotbar
     */
    public static GameRule<Boolean> OPEN_SHULKERS_FROM_HOTBAR;
    /**
     * Allows players to open shulker boxes from their inventory
     */
    public static GameRule<Boolean> OPEN_SHULKERS_FROM_INVENTORY;
    /**
     * Allows shulker boxes to automatically pick up items when they have a material assigned
     */
    public static GameRule<Boolean> INSERT_INTO_SHULKER_ON_PICKUP;
}
