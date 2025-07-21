package com.github.noramibu.bettershulkers.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerMenu.class)
public interface AbstractContainerAccessor {
    @Accessor("slots")
    NonNullList<Slot> getSlots();
}
