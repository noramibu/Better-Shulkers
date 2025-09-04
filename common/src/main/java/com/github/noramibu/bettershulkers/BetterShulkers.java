package com.github.noramibu.bettershulkers;

import com.github.noramibu.bettershulkers.command.ShulkerCommand;
import com.github.noramibu.bettershulkers.enchantment.MaterialCollector;
import com.github.noramibu.bettershulkers.util.ShulkerUIUtils;
import com.github.noramibu.bettershulkers.util.ShulkerUtil;
/*\ <=1.21.1 || 1.21.5
import dev.architectury.event.CompoundEventResult;
\END */
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
//: 1.21.2 - 1.21.4 || >=1.21.6
import net.minecraft.world.InteractionResult;
//: END
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BetterShulkers {
    /**
     * Better Shulkers mod identifier
     */
    public static final String MOD_ID = "bettershulkers";
    /**
     * The NBT path for a shulker box's material
     */
    public static final String MATERIAL_PATH = "material";
    /**
     * The {@link Logger} for Better Shulkers
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /**
     * The initiation code for Better Shulkers
     */
    public static void init() {
        Config.init();

        // Initialize the Material Collector enchantment
        if (Config.ITEM_PICKUP_TYPE.equals(Config.PickupType.ENCHANTMENT)) {
            MaterialCollector.initialize();
        } else {
            LOGGER.info("Material Collector enchantment initialization skipped by config.");
        }


        CommandRegistrationEvent.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> {
            ShulkerCommand.register(commandDispatcher, commandBuildContext);
        });

        InteractionEvent.RIGHT_CLICK_ITEM.register((player, hand) -> {
            ItemStack stack = player.getItemInHand(hand);
            if (!player.level().isClientSide &&
                    ShulkerUtil.isShulkerBox(stack) &&
                    ShulkerUtil.hasOpenPermission(player)
            ) {
                ShulkerUIUtils.openMenu(stack, (ServerPlayer) player);
                
                /*\ <=1.21.1 || 1.21.5
                return CompoundEventResult.pass();
                \END */
                //: 1.21.2 - 1.21.4 || >=1.21.6
                return InteractionResult.SUCCESS;
                //: END
            } else {
                /*\ <=1.21.1 || 1.21.5
                return CompoundEventResult.pass();
                \END */
                //: 1.21.2 - 1.21.4 || >=1.21.6
                return InteractionResult.PASS;
                //: END
            }
        });
    }

    /**
     * Gets the default custom components to store the resource for a shulker
     * @return CustomData instance for resources
     */
    public static CustomData getShulkerCustomData() {
        CompoundTag compound = new CompoundTag();
        compound.put(MATERIAL_PATH, StringTag.valueOf(""));
        return CustomData.of(compound);
    }
}
