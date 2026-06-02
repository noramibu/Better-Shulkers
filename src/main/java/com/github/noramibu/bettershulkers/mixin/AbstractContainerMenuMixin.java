/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.ItemMove;
import com.github.noramibu.bettershulkers.MoveItemListener;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {

    // Place item in Slot
    @WrapOperation(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;setCarried(Lnet/minecraft/world/item/ItemStack;)V", ordinal = 2))
    private void bettershulkers$checkItems1(AbstractContainerMenu instance, ItemStack carried, Operation<Void> original, @Local(name = "slot") Slot slot) {
        if (this instanceof MoveItemListener listener) {
            listener.itemMoved(new ItemMove(carried, slot.index));
        }
        original.call(instance, carried);
    }

    // Take Half
    @WrapOperation(method = "doClick", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", ordinal = 0))
    private <T> void bettershulkers$checkItems2(Optional<ItemStack> instance, Consumer<? super @NotNull T> action, Operation<Void> original) {
        if (instance.isPresent()) {
            if (this instanceof MoveItemListener listener) {
                listener.itemMoved(new ItemMove(instance.get(), -2));
            }
            original.call(instance, action);
        }
    }

    // Put in slot
    @WrapOperation(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;setCarried(Lnet/minecraft/world/item/ItemStack;)V", ordinal = 3))
    private void bettershulkers$checkItems3(AbstractContainerMenu instance, ItemStack carried, Operation<Void> original, @Local(name = "slot") Slot slot) {
        if (this instanceof MoveItemListener listener) {
            listener.itemMoved(new ItemMove(carried, slot.index));
        }
        original.call(instance, carried);
    }

    // Switch items
    @WrapOperation(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;setCarried(Lnet/minecraft/world/item/ItemStack;)V", ordinal = 4))
    private void bettershulkers$checkItems4(AbstractContainerMenu instance, ItemStack carried, Operation<Void> original, @Local(name = "slot") Slot slot) {
        if (this instanceof MoveItemListener listener) {
            listener.itemMoved(new ItemMove(slot.getItem(), -2), new ItemMove(carried, slot.index));
        }
        original.call(instance, carried);
    }

    // Fill held
    @WrapOperation(method = "doClick", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", ordinal = 1))
    private <T> void bettershulkers$checkItems5(Optional<ItemStack> instance, Consumer<? super @NotNull T> action, Operation<Void> original) {
        if (instance.isPresent()) {
            if (this instanceof MoveItemListener listener) {
                listener.itemMoved(new ItemMove(instance.get(), -2));
            }
            original.call(instance, action);
        }
    }

    // Move item to hotbar
    @WrapOperation(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setItem(ILnet/minecraft/world/item/ItemStack;)V", ordinal = 0))
    private void bettershulkers$checkItems6(Inventory instance, int slot, ItemStack itemStack, Operation<Void> original) {
        if (this instanceof MoveItemListener listener) {
            listener.itemMoved(new ItemMove(itemStack, slot + 54));
        }
        original.call(instance, slot, itemStack);
    }

    // Move item from hotbar
    @WrapOperation(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;getMaxStackSize(Lnet/minecraft/world/item/ItemStack;)I", ordinal = 2))
    private int bettershulkers$checkItems7(Slot instance, ItemStack itemStack, Operation<Integer> original) {
        if (this instanceof MoveItemListener listener) {
            listener.itemMoved(new ItemMove(itemStack, instance.index));
        }
        return original.call(instance, itemStack);
    }

    // Swap items
    @WrapOperation(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;getMaxStackSize(Lnet/minecraft/world/item/ItemStack;)I", ordinal = 3))
    private int bettershulkers$checkItems8(Slot instance, ItemStack itemStack, Operation<Integer> original, @Local(name = "buttonNum") int buttonNum) {
        if (this instanceof MoveItemListener listener) {
            listener.itemMoved(new ItemMove(itemStack, instance.index), new ItemMove(instance.getItem(), buttonNum + 54));
        }
        return original.call(instance, itemStack);
    }
}
