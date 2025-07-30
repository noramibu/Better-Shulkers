package com.github.noramibu.bettershulkers.neoforge.abstraction;

import com.github.noramibu.bettershulkers.abstraction.PlatformAbstraction;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.server.permission.PermissionAPI;

/**
 * The abstraction layer for NeoForge
 */
public class NeoForgeAbstraction implements PlatformAbstraction {
    @Override
    public boolean isShulkerBox(ItemStack stack) {
        return stack.is(Tags.Items.SHULKER_BOXES);
    }

    @Override
    public boolean permissionCheck(CommandSourceStack source, String id, boolean hasProperPermissionLevel) {
        if (source.isPlayer()) {
            var permissionNode = CommandPermission.getPermission(id);
            if (permissionNode == null) {
                // If permission node is not found, return false or true based on configuration
                return !hasProperPermissionLevel; // Default to allowing if no specific permission is configured
            }
            return PermissionAPI.getPermission(source.getPlayer(), permissionNode);
        } else {
            return false;
        }
    }
}
