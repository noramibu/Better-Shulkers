/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.ShulkerBoxUtils;
import com.github.noramibu.bettershulkers.VirtualContainer;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements DataComponentHolder, ItemInstance, VirtualContainer {
    private Player viewer;

    @WrapOperation(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;use(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;"))
    private InteractionResult bettershulkers$onUse(Item instance, Level level, Player player, InteractionHand hand, Operation<InteractionResult> original) {
        if (!level.isClientSide() && this.is(ItemTags.SHULKER_BOXES)) {
            ShulkerBoxUtils.openInventory((ItemStack) (Object) this, player);
        }
        return original.call(instance, level, player, hand);
    }

    @Inject(method = "copy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setPopTime(I)V"))
    private void bettershulkers$syncViewing(CallbackInfoReturnable<ItemStack> cir, @Local(name = "copy") ItemStack copy) {
        if (this.isBeingViewed()) {
            ((VirtualContainer) (Object) copy).setViewing(this.viewer);
        }
    }

    @Override
    public void setViewing(@Nullable Player viewing) {
        this.viewer = viewing;
    }

    @Override
    public boolean isBeingViewed() {
        return this.viewer != null;
    }
}
