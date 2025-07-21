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
        BetterShulkers.LOGGER.info("--- Loaded Enchantments at Startup ---");
        BetterShulkers.LOGGER.info("Enchantment Registry Key: {}", Registries.ENCHANTMENT.registryKey());
        BetterShulkers.LOGGER.info("Material Collector Enchantment Key: {}", MATERIAL_COLLECTOR.location());
        BetterShulkers.LOGGER.info("Material Collector Enchantment ID: {}", MATERIAL_COLLECTOR.location().getPath());
        BetterShulkers.LOGGER.info("Material Collector Enchantment Resource Location: {}", MATERIAL_COLLECTOR.location());
        BetterShulkers.LOGGER.info("Material Collector Enchantment Resource Key: {}", MATERIAL_COLLECTOR);
        BetterShulkers.LOGGER.info("Material Collector Enchantment Registry: {}", Registries.ENCHANTMENT);
        BetterShulkers.LOGGER.info("Material Collector Enchantment Registry Name: {}:{}", MATERIAL_COLLECTOR.location().getNamespace(), MATERIAL_COLLECTOR.location().getPath());
        BetterShulkers.LOGGER.info("Material Collector Enchantment Registry ID: {}:{}", MATERIAL_COLLECTOR.location().getNamespace(), MATERIAL_COLLECTOR.location().getPath());
        BetterShulkers.LOGGER.info("--------------------------------------");

    }
}
