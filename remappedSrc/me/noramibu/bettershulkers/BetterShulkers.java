package me.noramibu.bettershulkers;

import me.noramibu.bettershulkers.command.ShulkerCommand;
import me.noramibu.bettershulkers.config.Config;
import me.noramibu.bettershulkers.event.UseItemListener;
import me.noramibu.bettershulkers.recipe.BetterShulkersRecipes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterShulkers implements ModInitializer {
	public static final String MOD_ID = "better-shulkers";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Better Shulkers has been initialized.");

		Config.init();

		CommandRegistrationCallback.EVENT.register(ShulkerCommand::register);
		BetterShulkersRecipes.register();
		UseItemListener.register();
	}
} 