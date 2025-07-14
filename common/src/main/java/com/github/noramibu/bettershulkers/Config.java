package com.github.noramibu.bettershulkers;

import com.moandjiezana.toml.Toml;
import dev.architectury.platform.Platform;

import java.io.File;
import java.io.IOException;

public class Config {
    private static File file;
    private static Toml toml;

    public static boolean REQUIRE_PERMISSION_FOR_COMMAND;

    public static void init() {

        file = new File(Platform.getConfigFolder().toFile(), "bettershulkers.toml");

        if (!file.exists()) {
            create();
        }

        load();
    }

    public static void reload() {
        load();
    }

    private static void create() {
        try {
            if (file.createNewFile()) {
                // Use a direct writer to add comments
                try (java.io.FileWriter writer = new java.io.FileWriter(file)) {
                    writer.write("# Better Shulkers Configuration\n\n");
                    writer.write("# If true, players will need permissions to use the /shulker command.\n");
                    writer.write("# If false, any player can use the commands.\n");
                    writer.write("require-permission-for-command = true\n\n");
                    writer.write("# --- Permission Nodes ---\n");
                    writer.write("# bettershulkers.command.set   - Allows setting a shulker's material.\n");
                    writer.write("# bettershulkers.command.info  - Allows checking a shulker's material.\n");
                    writer.write("# bettershulkers.command.reload - Allows reloading this configuration file.\n");
                }
            }
        } catch (IOException e) {
            BetterShulkers.LOGGER.error("Failed to create config: ", e);
        }
    }

    private static void load() {
        toml = new Toml().read(file);
        REQUIRE_PERMISSION_FOR_COMMAND = toml.getBoolean("require-permission-for-command", true);
    }
}
