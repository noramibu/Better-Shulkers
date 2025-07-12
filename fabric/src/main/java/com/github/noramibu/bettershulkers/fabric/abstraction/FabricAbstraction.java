package com.github.noramibu.bettershulkers.fabric.abstraction;

import com.github.noramibu.bettershulkers.abstraction.PlatformAbstraction;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.world.item.ItemStack;

public class FabricAbstraction implements PlatformAbstraction {
    @Override
    public boolean isShulkerBox(ItemStack stack) {
        return stack.is(ConventionalItemTags.SHULKER_BOXES);
    }
}
