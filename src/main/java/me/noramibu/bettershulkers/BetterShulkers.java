package me.noramibu.bettershulkers;

import me.noramibu.bettershulkers.command.ShulkerCommand;
import me.noramibu.bettershulkers.config.Config;
import me.noramibu.bettershulkers.recipe.BetterShulkersRecipes;
import me.noramibu.bettershulkers.util.ShulkerUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterShulkers implements ModInitializer {
	public static final String MOD_ID = "better-shulkers";
	public static final String MATERIAL_PATH = "material";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Config.init();
		BetterShulkersRecipes.register();
		CommandRegistrationCallback.EVENT.register(ShulkerCommand::register);

		// Add custom component to shulkers
		Identifier latePhase = Identifier.of(MOD_ID, "late");
		DefaultItemComponentEvents.MODIFY.addPhaseOrdering(Event.DEFAULT_PHASE, latePhase);

		DefaultItemComponentEvents.MODIFY.register(latePhase, context -> {
			context.modify(item -> ShulkerUtil.isShulkerBox(item.getDefaultStack()), (builder, item) -> {
				NbtCompound compound = new NbtCompound();
				compound.put(MATERIAL_PATH, NbtString.of(""));
				NbtComponent component = NbtComponent.of(compound);
				builder.add(DataComponentTypes.CUSTOM_DATA, component);
			});
		});
	}
} 