package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.interfaces.SimpleContainerAccessor;
import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SimpleContainer.class)
public class SimpleContainerMixin implements SimpleContainerAccessor {

    @Mutable
    @Shadow @Final
    private NonNullList<ItemStack> items;

    @Mutable
    @Shadow @Final private int size;

    @Override
    public void setItems(NonNullList<ItemStack> inventory) {
        this.items = inventory;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }
}
