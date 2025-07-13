package com.github.noramibu.bettershulkers.neoforge.abstraction;

import com.github.noramibu.bettershulkers.BetterShulkers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.server.permission.nodes.PermissionNode;
import net.neoforged.neoforge.server.permission.nodes.PermissionTypes;

public class CommandPermission {
    public static final ResourceLocation SET_COMMAND_PERM = ResourceLocation.fromNamespaceAndPath(BetterShulkers.MOD_ID, "command.set");
    public static final ResourceLocation RELOAD_COMMAND_PERM = ResourceLocation.fromNamespaceAndPath(BetterShulkers.MOD_ID, "command.reload");
    public static final PermissionNode<Boolean> SET_COMMAND_NODE = new PermissionNode<>(SET_COMMAND_PERM, PermissionTypes.BOOLEAN, (player, uuid, permissionDynamicContexts) -> player.hasPermissions(2));
    public static final PermissionNode<Boolean> RELOAD_COMMAND_NODE = new PermissionNode<>(RELOAD_COMMAND_PERM, PermissionTypes.BOOLEAN, (player, uuid, permissionDynamicContexts) -> player.hasPermissions(2));

    public static PermissionNode<Boolean> getPermission(String id) {
        switch (id) {
            case "bettershulkers.command.set" -> {
                return SET_COMMAND_NODE;
            }
            case "bettershulkers.command.reload" -> {
                return RELOAD_COMMAND_NODE;
            }
            default -> {
                return null;
            }
        }
    }
}
