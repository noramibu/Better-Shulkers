package com.github.noramibu.bettershulkers;

import com.github.noramibu.bettershulkers.command.ShulkerCommand;
import com.github.noramibu.bettershulkers.event.ComponentModificationEvent;
import com.github.noramibu.bettershulkers.interfaces.RemoteInventory;
import com.github.noramibu.bettershulkers.interfaces.ShulkerViewer;
import com.github.noramibu.bettershulkers.recipe.BetterShulkersRecipes;
import com.github.noramibu.bettershulkers.util.DataComponentEntry;
import com.github.noramibu.bettershulkers.util.ShulkerUtil;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BetterShulkers {
    public static final String MOD_ID = "better-shulkers";
    public static final String MATERIAL_PATH = "material";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        Config.init();
        BetterShulkersRecipes.register();

        CommandRegistrationEvent.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> {
            ShulkerCommand.register(commandDispatcher, commandBuildContext);
        });

        ComponentModificationEvent.COMPONENT_MODIFICATION_EVENT.register(componentModificationEvent -> {
            CompoundTag compound = new CompoundTag();
            compound.put(MATERIAL_PATH, StringTag.valueOf(""));
            CustomData component = CustomData.of(compound);
            componentModificationEvent.modify(ShulkerUtil::earlyIsShulkerBox, new DataComponentEntry(DataComponents.CUSTOM_DATA, component));
        });

        InteractionEvent.RIGHT_CLICK_ITEM.register((player, hand) -> {
            ItemStack stack = player.getItemInHand(hand);
            if (!player.level().isClientSide) {
                if (ShulkerUtil.isShulkerBox(stack)) {
                    ShulkerBoxBlock shulker = (ShulkerBoxBlock) ((BlockItem)stack.getItem()).getBlock();
                    ((ShulkerViewer) player).setViewing(stack);
                    ((RemoteInventory)shulker).openInventory((ServerPlayer) player, stack);
                }
            }
            return InteractionResult.PASS;
        });
    }
}
