/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.ShulkerBoxUtils;
import com.github.noramibu.bettershulkers.gamerules.BetterShulkersGameRules;
import com.github.noramibu.bettershulkers.material.ShulkerMaterialManager;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @WrapOperation(method = "playerTouch", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z"))
    private boolean bettershulkers$insertIntoShulkerBox1(Inventory instance, ItemStack itemStack, Operation<Boolean> original, @Local(name = "player") Player player) {
        if (ShulkerBoxUtils.isServerSide(player)
                && ((ServerPlayer) player).level().getGameRules().get(BetterShulkersGameRules.INSERT_INTO_SHULKER_ON_PICKUP)) {
            for (int i = 9; i < 45; i++) {
                Slot slot = player.inventoryMenu.getSlot(i);
                ItemStack item = slot.getItem();
                if (item.is(ItemTags.SHULKER_BOXES)
                        && ShulkerMaterialManager.matchesMaterialFilter(item, itemStack, false)) {
                    itemStack.setCount(ShulkerBoxUtils.addStackToShulker(item, itemStack, slot, player).getCount());
                }

                if (itemStack.isEmpty()) {
                    return true;
                }
            }
        }
        return original.call(instance, itemStack);
    }
}
