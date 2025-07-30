package com.github.noramibu.bettershulkers.neoforge.abstraction;

import com.github.noramibu.bettershulkers.BetterShulkers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.server.permission.nodes.PermissionNode;
import net.neoforged.neoforge.server.permission.nodes.PermissionTypes;

/**
 * NeoForge permissions for Better Shulkers
 */
public class CommandPermission {
    private static final ResourceLocation SET_COMMAND_PERM = ResourceLocation.fromNamespaceAndPath(BetterShulkers.MOD_ID, "command.set");
    private static final ResourceLocation RELOAD_COMMAND_PERM = ResourceLocation.fromNamespaceAndPath(BetterShulkers.MOD_ID, "command.reload");
    private static final ResourceLocation OPEN_PERM = ResourceLocation.fromNamespaceAndPath(BetterShulkers.MOD_ID, "open");
    
    /**
     * The {@link PermissionNode} for the "set" command
     */
    public static final PermissionNode<Boolean> SET_COMMAND_NODE = new PermissionNode<>(SET_COMMAND_PERM, PermissionTypes.BOOLEAN, (player, uuid, permissionDynamicContexts) -> player.hasPermissions(2));
    /**
     * The {@link PermissionNode} for the "reload" command
     */
    public static final PermissionNode<Boolean> RELOAD_COMMAND_NODE = new PermissionNode<>(RELOAD_COMMAND_PERM, PermissionTypes.BOOLEAN, (player, uuid, permissionDynamicContexts) -> player.hasPermissions(2));
    /**
     * The {@link PermissionNode} for opening shulker boxes
     */
    public static final PermissionNode<Boolean> OPEN_NODE = new PermissionNode<>(OPEN_PERM, PermissionTypes.BOOLEAN, (player, uuid, permissionDynamicContexts) -> true);

    /**
     * Gets the permission from a string id
     * @param id The permission's id
     * @return {@link PermissionNode} from the id, or null if not found
     */
    public static PermissionNode<Boolean> getPermission(String id) {
        return switch (id) {
            case "bettershulkers.command.set" -> SET_COMMAND_NODE;
            case "bettershulkers.command.reload" -> RELOAD_COMMAND_NODE;
            case "bettershulkers.open" -> OPEN_NODE;
            default -> null;
        };
    }
}
