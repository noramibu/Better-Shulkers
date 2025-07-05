package me.noramibu.bettershulkers.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.lucko.fabric.api.permissions.v0.Permissions;
import me.noramibu.bettershulkers.accessor.ShulkerMaterialAccessor;
import me.noramibu.bettershulkers.config.Config;
import me.noramibu.bettershulkers.util.ShulkerUtil;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class ShulkerCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("shulker")
                .then(CommandManager.literal("set")
                        .requires(source -> {
                            if (Config.REQUIRE_PERMISSION_FOR_COMMAND) {
                                return Permissions.check(source, "bettershulkers.command.set", source.hasPermissionLevel(2));
                            }
                            return true;
                        })
                        .then(CommandManager.argument("material", ItemStackArgumentType.itemStack(registryAccess))
                                .executes(ShulkerCommand::execute)))
                .then(CommandManager.literal("info")
                        .requires(source -> {
                            if (Config.REQUIRE_PERMISSION_FOR_COMMAND) {
                                return Permissions.check(source, "bettershulkers.command.info", source.hasPermissionLevel(2));
                            }
                            return true;
                        })
                        .executes(ShulkerCommand::executeInfo))
                .then(CommandManager.literal("reload")
                        .requires(source -> {
                            if (Config.REQUIRE_PERMISSION_FOR_COMMAND) {
                                return Permissions.check(source, "bettershulkers.command.reload", source.hasPermissionLevel(2));
                            }
                            return true;
                        })
                        .executes(ShulkerCommand::executeReload)));
    }

    private static int executeReload(CommandContext<ServerCommandSource> context) {
        Config.reload();
        context.getSource().sendFeedback(() -> Text.literal("Better Shulkers configuration reloaded."), false);
        return 1;
    }

    private static int executeInfo(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        HitResult hit = player.raycast(5, 0, false);

        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) hit;
            BlockEntity blockEntity = player.getWorld().getBlockEntity(blockHit.getBlockPos());

            if (blockEntity instanceof ShulkerMaterialAccessor accessor) {
                String material = accessor.getMaterial();
                if (material != null && !material.isEmpty()) {
                    player.sendMessage(Text.literal("Shulker material: " + material), false);
                    return 1;
                } else {
                    player.sendMessage(Text.literal("This shulker has no material set."), false);
                    return 0;
                }
            }
        }

        player.sendMessage(Text.literal("You are not looking at a shulker box."), true);
        return 0;
    }

    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        var mainHandStack = player.getMainHandStack();
        ItemStackArgument itemStackArgument = ItemStackArgumentType.getItemStackArgument(context, "material");
        String materialId = ShulkerUtil.getItemId(itemStackArgument.createStack(1, false));

        if (ShulkerUtil.isShulkerBox(mainHandStack)) {
            ShulkerUtil.setShulkerMaterial(mainHandStack, materialId);
            player.sendMessage(Text.literal("Shulker box material set to: " + materialId), false);
            return 1;
        } else {
            player.sendMessage(Text.literal("You must be holding a Shulker Box in your main hand."), true);
            return 0;
        }
    }
} 