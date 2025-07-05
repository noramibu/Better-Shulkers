package me.noramibu.bettershulkers;

import me.noramibu.bettershulkers.command.ShulkerCommand;
import me.noramibu.bettershulkers.config.Config;
import me.noramibu.bettershulkers.recipe.BetterShulkersRecipes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterShulkers implements ModInitializer {
	public static final String MOD_ID = "better-shulkers";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Config.init();
		BetterShulkersRecipes.register();
		CommandRegistrationCallback.EVENT.register(ShulkerCommand::register);
	}
} 