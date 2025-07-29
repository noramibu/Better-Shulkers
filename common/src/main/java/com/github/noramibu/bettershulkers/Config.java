package com.github.noramibu.bettershulkers;

import com.moandjiezana.toml.Toml;
import dev.architectury.platform.Platform;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Better Shulker's config
 */
public final class Config {
    private static final Path CONFIG_PATH = Platform.getConfigFolder().resolve("bettershulkers.toml");

    public static boolean REQUIRE_PERMISSION_FOR_COMMAND;
    public static boolean REQUIRE_PERMISSION_FOR_RIGHT_CLICK_OPEN;
    public static boolean OPEN_SHULKER_FROM_INVENTORY;
    public static boolean SHOW_MATERIAL_DISPLAY;
    public static PickupType ITEM_PICKUP_TYPE;

    /**
     * Instantiates the config from a saved file, or creates a new one if one is not present.
     * The values from the config file are then loaded
     */
    public static void init() {
        File file = CONFIG_PATH.toFile();

        if (!file.exists()) {
            create(file);
        }

        load(file);
    }

    /**
     * Reloads the config
     */
    public static void reload() {
        load(CONFIG_PATH.toFile());
    }

    /**
     * How a shulker should pick up an item
     */
    public enum PickupType {
        NONE,
        RECIPE,
        ENCHANTMENT;

        /**
         * Gets the PickupType from a string
         * @param string String to parse
         * @return PickupType instance
         */
        public static PickupType fromString(String string) {
            return switch (string) {
                case "RECIPE" -> RECIPE;
                case "ENCHANTMENT" -> ENCHANTMENT;
                default -> NONE;
            };
        }
    }

    private static void create(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("# Better Shulkers Configuration\n\n");

            writer.write("# Selects the type of automatic item pickup shulker boxes should be able to do. The options include: \n");
            writer.write("# NONE: Shulker boxes do not automatically pick up items.\n");
            writer.write("# RECIPE: All shulkers with a material can pick up items.\n");
            writer.write("# ENCHANTMENT: Only shulkers with a material and the pick up enchantment can pick up items.\n");
            writer.write("item-pickup-type = ENCHANTMENT\n\n");

            writer.write("# If true, players can open shulker boxes by right-clicking them in hand or in their inventory.\n");
            writer.write("open-from-inventory = true\n\n");

            writer.write("# If true, item displays will render on the shulker box lid to show the material.\n");
            writer.write("# NOTE: Displays may appear slow if players have high latency.\n");
            writer.write("show-material-display = true\n\n");

            writer.write("# If true, players will need permissions to use the /shulker command.\n");
            writer.write("require-permission-for-command = true\n\n");

            writer.write("# If true, players will need 'bettershulkers.open' permission to open shulker boxes by right-clicking them.\n");
            writer.write("require-permission-for-right-click-open-shulker = false\n\n");

            writer.write("# If true, a shulker with the Material Collector enchantment will pick up everything unless a material is applied to it.\n");
            writer.write("# If false, it will only pick up items matching its material.\n");
            writer.write("material-collector-pickup-all-without-filtering = false\n\n");

            writer.write("# --- Permission Nodes ---\n");
            writer.write("# bettershulkers.command.set   - Allows setting a shulker's material.\n");
            writer.write("# bettershulkers.command.reload - Allows reloading this configuration file.\n");
            writer.write("# bettershulkers.open          - Allows opening shulker boxes by right-clicking them in hand.\n");
        } catch (IOException e) {
            BetterShulkers.LOGGER.error("Failed to create config: ", e);
            throw new RuntimeException(e);
        }
    }

    private static void load(File file) {
        Toml toml = new Toml().read(file);
        REQUIRE_PERMISSION_FOR_COMMAND = toml.getBoolean("require-permission-for-command", true);
        REQUIRE_PERMISSION_FOR_RIGHT_CLICK_OPEN = toml.getBoolean("require-permission-for-right-click-open-shulker", false);
        OPEN_SHULKER_FROM_INVENTORY = toml.getBoolean("open-from-inventory", true);
        SHOW_MATERIAL_DISPLAY = toml.getBoolean("show-material-display", true);
        ITEM_PICKUP_TYPE = PickupType.fromString(toml.getString("item-pickup-type", "ENCHANTMENT"));
    }
}
