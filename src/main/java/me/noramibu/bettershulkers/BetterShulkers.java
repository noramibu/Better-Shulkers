package me.noramibu.bettershulkers;

import me.noramibu.bettershulkers.accessor.RemoteInventory;
import me.noramibu.bettershulkers.accessor.ShulkerViewer;
import me.noramibu.bettershulkers.command.ShulkerCommand;
import me.noramibu.bettershulkers.config.Config;
import me.noramibu.bettershulkers.util.ShulkerUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.TypedActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterShulkers implements ModInitializer {
	public static final String MOD_ID = "better-shulkers";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Config.init();
		//BetterShulkersRecipes.register();
		CommandRegistrationCallback.EVENT.register(ShulkerCommand::register);
		// Open Shulker Box UI when right-clicking
		UseItemCallback.EVENT.register(((playerEntity, world, hand) -> {
			ItemStack stack = playerEntity.getStackInHand(hand);
			if (!world.isClient) {
				if (ShulkerUtil.isShulkerBox(stack)) {
					ShulkerBoxBlock shulker = (ShulkerBoxBlock) ((BlockItem)stack.getItem()).getBlock();
					((ShulkerViewer) playerEntity).setViewing(stack);
					((RemoteInventory)shulker).openInventory((ServerPlayerEntity) playerEntity, stack);
				}
			}
            return TypedActionResult.pass(stack);
        }));
	}
} 