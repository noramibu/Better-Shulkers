package com.github.noramibu.bettershulkers.enchantment;

import com.github.noramibu.bettershulkers.BetterShulkers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public final class MaterialCollector {
    public static final ResourceKey<Enchantment> MATERIAL_COLLECTOR = create("bettershulkers:material_collector");

    private static ResourceKey<Enchantment> create(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse(name));
    }

    public static void initialize() {
        BetterShulkers.LOGGER.info("Better Shulkers enchantment registered");

    }
}
