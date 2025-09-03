package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.interfaces.MutableContainerInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMixin implements MutableContainerInventory {

    @Shadow @Final public NonNullList<Slot> slots;

    @Override
    public void setInventory(NonNullList<ItemStack> inventory) {
        for (int i = 0; i < 27; i++) {
            Slot slot = slots.get(i);
            if (i < inventory.size()) {
                slot.set(inventory.get(i));
            } else {
                slot.set(ItemStack.EMPTY);
            }
        }
    }
}
