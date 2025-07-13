package com.github.noramibu.bettershulkers;

import com.github.noramibu.bettershulkers.command.ShulkerCommand;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.InteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BetterShulkers {
    public static final String MOD_ID = "better-shulkers";
    public static final String MATERIAL_PATH = "material";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        Config.init();
        // TODO Recipes
        CommandRegistrationEvent.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> {
            ShulkerCommand.register(commandDispatcher, commandBuildContext);
        });

        // TODO Item Component Modification

        InteractionEvent.RIGHT_CLICK_ITEM.register((player, interactionHand) -> {
            // TODO Open Shulkerbox UI
            return null;
        });
    }
}
