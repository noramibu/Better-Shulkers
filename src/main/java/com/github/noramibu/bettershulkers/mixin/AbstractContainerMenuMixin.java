/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.*;
import com.github.noramibu.bettershulkers.container.VirtualContainer;
import com.github.noramibu.bettershulkers.container.VirtualContainerHolder;
import com.github.noramibu.bettershulkers.container.VirtualShulkerBoxContainer;
import com.github.noramibu.bettershulkers.event.ItemMove;
import com.github.noramibu.bettershulkers.event.MoveItemListener;
import com.github.noramibu.bettershulkers.material.ShulkerMaterialManager;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {

    @Shadow
    public abstract ItemStack getCarried();

    @Shadow
    public abstract void setCarried(ItemStack carried);

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
    private <T> void bettershulkers$checkItems2(Optional<ItemStack> instance, Consumer<? super @NotNull T> action, Operation<Void> original, @Local(name = "slot") Slot slot, @Local(name = "clickAction") ClickAction clickAction, @Local(name = "player") Player player) {
        if (instance.isPresent()) {
            ItemStack instanceStack = instance.get();

            // Check if Shulker should be opened
            if (clickAction == ClickAction.SECONDARY
                    && instanceStack.is(ItemTags.SHULKER_BOXES)
                    && !((VirtualContainer) (Object) instanceStack).isBeingViewed()) {
                ShulkerBoxUtils.openInternalShulker(instanceStack, player, slot);
                this.setCarried(ItemStack.EMPTY);
                slot.set(instanceStack);
                return;
            }

            if  (this instanceof MoveItemListener listener) {
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
            listener.itemMoved(new ItemMove(slot.getItem(), -2), new ItemMove(this.getCarried(), slot.index));
        }
        original.call(instance, carried);
    }

    // Drop 1 item
    @Inject(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;setCarried(Lnet/minecraft/world/item/ItemStack;)V", ordinal = 4), cancellable = true)
    private void bettershulkers$checkToOpenMenu1(int slotIndex, int buttonNum, ContainerInput containerInput, Player player, CallbackInfo ci, @Local(name = "slot") Slot slot, @Local(name = "clickAction") ClickAction clickAction) {
        if (ShulkerBoxUtils.isServerSide(player)
                && clickAction == ClickAction.SECONDARY
                && slot.getItem().is(ItemTags.SHULKER_BOXES)
                && ShulkerMaterialManager.matchesMaterialFilter(slot.getItem(), this.getCarried())
        ) {
            this.setCarried(ShulkerBoxUtils.addStackToShulker(slot.getItem(), this.getCarried()));
            slot.setChanged();

            VirtualShulkerBoxContainer container = ((VirtualContainerHolder) player).getVirtualContainer();
            if (container != null
                    && container.getViewedSlot() == slotIndex) {
                container.refreshUI();
            }

            ci.cancel();
        }
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

    // Quick Move
    @Inject(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;quickMoveStack(Lnet/minecraft/world/entity/player/Player;I)Lnet/minecraft/world/item/ItemStack;", ordinal = 0), cancellable = true)
    private void bettershulkers$checkClick1(int slotIndex, int buttonNum, ContainerInput containerInput, Player player, CallbackInfo ci, @Local(name = "clickAction") ClickAction clickAction, @Local(name = "slot") Slot slot) {
        if (ShulkerBoxUtils.isServerSide(player)
                && clickAction == ClickAction.SECONDARY
                && slot.getItem().is(ItemTags.SHULKER_BOXES)
        ) {
            if (this.getCarried().isEmpty()) {
                ShulkerBoxUtils.pullAllFromShulker(slot.getItem(), player);
            } else if (ShulkerMaterialManager.matchesMaterialFilter(slot.getItem(), this.getCarried())) {
                ShulkerBoxUtils.addAllToShulker(slot.getItem(), player, this.getCarried().getItem());
            }

            VirtualShulkerBoxContainer container = ((VirtualContainerHolder) player).getVirtualContainer();
            if (container != null
                    && container.getViewedSlot() == slotIndex) {
                container.refreshUI();
            }

            slot.set(slot.getItem());
            ci.cancel();
        }
    }
}
