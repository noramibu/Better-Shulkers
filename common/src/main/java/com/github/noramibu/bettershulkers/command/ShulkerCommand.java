package com.github.noramibu.bettershulkers.command;

import com.github.noramibu.bettershulkers.Config;
import com.github.noramibu.bettershulkers.abstraction.AbstractionManager;
import com.github.noramibu.bettershulkers.util.ShulkerUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/**
 * Helpful commands for handling shulker boxes with materials
 */
public class ShulkerCommand {
    /**
     * Registers Better Shulker's commands
     * @param dispatcher {@link  CommandDispatcher} instance from event
     * @param registryAccess {@link CommandBuildContext} instance from event
     */
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess) {
        dispatcher.register(Commands.literal("shulker")
                .then(Commands.literal("set")
                        .requires(source -> {
                            if (Config.REQUIRE_PERMISSION_FOR_COMMAND) {
                                return AbstractionManager.ABSTRACTION.permissionCheck(source, "bettershulkers.command.set", source.hasPermission(2));
                            }
                            return true;
                        })
                        .then(Commands.argument("material", ItemArgument.item(registryAccess))
                                .executes(ShulkerCommand::execute)))
                .then(Commands.literal("reload")
                        .requires(source -> {
                            if (Config.REQUIRE_PERMISSION_FOR_COMMAND) {
                                return AbstractionManager.ABSTRACTION.permissionCheck(source, "bettershulkers.command.reload", source.hasPermission(2));
                            }
                            return true;
                        })
                        .executes(ShulkerCommand::executeReload)));
    }

    private static int executeReload(CommandContext<CommandSourceStack> context) {
        Config.reload();
        context.getSource().sendSuccess(() -> Component.literal("Better Shulkers configuration reloaded."), false);
        return 1;
    }

    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        var mainHandStack = player.getMainHandItem();
        ItemInput itemStackArgument = ItemArgument.getItem(context, "material");
        ItemStack materialStack = itemStackArgument.createItemStack(1, false);
        String materialId = ShulkerUtil.getItemId(itemStackArgument.createItemStack(1, false));

        if (ShulkerUtil.isShulkerBox(mainHandStack)) {
            ShulkerUtil.setMaterialForShulker(mainHandStack, materialStack);
            player.sendSystemMessage(Component.literal("Shulker box material set to: " + materialId), false);
            return 1;
        } else {
            player.sendSystemMessage(Component.literal("You must be holding a Shulker Box in your main hand."), true);
            return 0;
        }
    }
}
