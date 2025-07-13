package com.github.noramibu.bettershulkers.fabric.abstraction;

import com.github.noramibu.bettershulkers.abstraction.PlatformAbstraction;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.item.ItemStack;
import me.lucko.fabric.api.permissions.v0.Permissions;

public class FabricAbstraction implements PlatformAbstraction {
    @Override
    public boolean isShulkerBox(ItemStack stack) {
        return stack.is(ConventionalItemTags.SHULKER_BOXES);
    }

    @Override
    public boolean permissionCheck(CommandSourceStack source, String id, boolean hasProperPermissionLevel) {
        return Permissions.check(source, id, hasProperPermissionLevel);
    }
}
