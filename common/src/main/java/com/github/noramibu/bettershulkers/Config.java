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
    public static boolean RIGHT_CLICK_TO_OPEN_SHULKER;
    public static boolean ADD_RECIPE_FOR_PICKABLE_SHULKER;
    public static boolean DISABLE_PICKUP_FEATURE_OF_SHULKERS;
    public static boolean SHOW_MATERIAL_DISPLAY;

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

    private static void create(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("# Better Shulkers Configuration\n\n");

            writer.write("# Disables the main feature of this mod, which is shulkers retaining their inventory when broken.\n");
            writer.write("# NOTE: This does NOT disable the recipe to craft pickable shulkers.\n");
            writer.write("disable-pickup-feature-of-shulkers = false\n\n");

            writer.write("# If true, a recipe is added to make shulkers pickable.\n");
            writer.write("# NOTE: This does NOT disable the pickup feature, only the recipe to craft them.\n");
            writer.write("add-recipe-for-pickable-shulker = true\n\n");

            writer.write("# If true, players can open shulker boxes by right-clicking them in hand.\n");
            writer.write("right-click-to-open-shulker = true\n\n");

            writer.write("# If true, item displays will render on the shulker box lid to show the material.\n");
            writer.write("# NOTE: Displays may appear slow if players have high latency.\n");
            writer.write("show-material-display = true\n\n");

            writer.write("# If true, players will need permissions to use the /shulker command.\n");
            writer.write("require-permission-for-command = true\n\n");

            writer.write("# If true, players will need 'bettershulkers.open' permission to open shulker boxes by right-clicking them.\n");
            writer.write("require-permission-for-right-click-open-shulker = false\n\n");

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
        RIGHT_CLICK_TO_OPEN_SHULKER = toml.getBoolean("right-click-to-open-shulker", true);
        ADD_RECIPE_FOR_PICKABLE_SHULKER = toml.getBoolean("add-recipe-for-pickable-shulker", true);
        DISABLE_PICKUP_FEATURE_OF_SHULKERS = toml.getBoolean("disable-pickup-feature-of-shulkers", false);
        SHOW_MATERIAL_DISPLAY = toml.getBoolean("show-material-display", true);
    }
}
