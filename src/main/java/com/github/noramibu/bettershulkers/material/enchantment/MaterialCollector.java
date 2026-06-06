/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.material.enchantment;

import com.github.noramibu.bettershulkers.BetterShulkers;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public final class MaterialCollector {
    public static final ResourceKey<Enchantment> MATERIAL_COLLECTOR = create("bettershulkers:material_collector");

    private static ResourceKey<Enchantment> create(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, Identifier.parse(name));
    }

    public static void initialize() {
        BetterShulkers.LOGGER.info("Better Shulkers enchantment registered");
    }

    public static boolean hasEnchantment(final ItemStack itemStack, Level level) {
        Registry<Enchantment> registry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> enchantmentHolder = registry.getOrThrow(MATERIAL_COLLECTOR);
        return itemStack.getEnchantments().getLevel(enchantmentHolder) > 0;
    }
}
