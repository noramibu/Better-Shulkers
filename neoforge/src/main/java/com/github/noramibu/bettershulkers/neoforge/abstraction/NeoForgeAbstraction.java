package com.github.noramibu.bettershulkers.neoforge.abstraction;

import com.github.noramibu.bettershulkers.abstraction.PlatformAbstraction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;

public class NeoForgeAbstraction implements PlatformAbstraction {
    @Override
    public boolean isShulkerBox(ItemStack stack) {
        return stack.is(Tags.Items.SHULKER_BOXES);
    }
}
