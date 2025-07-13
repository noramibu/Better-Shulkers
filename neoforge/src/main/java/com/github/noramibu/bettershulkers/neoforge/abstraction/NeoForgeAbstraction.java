package com.github.noramibu.bettershulkers.neoforge.abstraction;

import com.github.noramibu.bettershulkers.abstraction.PlatformAbstraction;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.server.permission.PermissionAPI;

public class NeoForgeAbstraction implements PlatformAbstraction {
    @Override
    public boolean isShulkerBox(ItemStack stack) {
        return stack.is(Tags.Items.SHULKER_BOXES);
    }

    @Override
    public boolean permissionCheck(CommandSourceStack source, String id, boolean hasProperPermissionLevel) {
        if (source.isPlayer()) {
            return PermissionAPI.getPermission(source.getPlayer(), CommandPermission.getPermission(id));
        } else {
            return false;
        }
    }
}
