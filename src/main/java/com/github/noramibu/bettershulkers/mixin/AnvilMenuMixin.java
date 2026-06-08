/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.ShulkerBoxUtils;
import com.github.noramibu.bettershulkers.gamerules.BetterShulkersGameRules;
import com.github.noramibu.bettershulkers.material.ShulkerMaterialManager;
import com.github.noramibu.bettershulkers.material.enchantment.MaterialCollector;
import net.minecraft.network.protocol.game.ClientboundContainerSetDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {

    public AnvilMenuMixin(@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess, ItemCombinerMenuSlotDefinition itemCombinerMenuSlotDefinition) {
        super(menuType, i, inventory, containerLevelAccess, itemCombinerMenuSlotDefinition);
    }

    @Inject(method = "createResult()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;canStoreEnchantments(Lnet/minecraft/world/item/ItemStack;)Z"), cancellable = true)
    private void bettershulkers$checkForShulkerMaterial(CallbackInfo ci) {
        if (ShulkerBoxUtils.isServerSide(player)
                && ((ServerPlayer) player).level().getGameRules().get(BetterShulkersGameRules.SHULKER_MATERIAL_ENCHANTMENT)) {
            ItemStack stack1 = this.inputSlots.getItem(0);
            ItemStack stack2 = this.inputSlots.getItem(1);

            ItemStack material = null;
            ItemStack shulker = null;

            if (stack1.is(ItemTags.SHULKER_BOXES)) {
                shulker = stack1;
            } else {
                material = stack1;
            }

            if (!stack2.isEmpty()) {
                if (shulker == null && stack2.is(ItemTags.SHULKER_BOXES)) {
                    shulker = stack2;
                } else {
                    material = stack2;
                }
            }

            if (shulker != null && MaterialCollector.hasEnchantment(shulker, this.player.level())) {
                ItemStack clone = shulker.copy();
                if (material == null || material.isEmpty()) {
                    ShulkerMaterialManager.removeMaterial(clone);
                } else {
                    ShulkerMaterialManager.setMaterial(clone, material.getItem());
                }
                this.resultSlots.setItem(0, clone);
                this.broadcastChanges();

                // Sync cost cause Mojang doesn't like us :(
                ((ServerPlayer) player).connection.send(new ClientboundContainerSetDataPacket(
                        this.containerId,
                        0,
                        1
                ));

                ci.cancel();
            }
        }
    }

    @Inject(method = "onTake", at = @At("HEAD"), cancellable = true)
    private void handleResultTaken(Player player, ItemStack stack, CallbackInfo ci) {
        // Check if this is our custom shulker material recipe
        if (ShulkerBoxUtils.isServerSide(player)
                && ((ServerPlayer) player).level().getGameRules().get(BetterShulkersGameRules.SHULKER_MATERIAL_ENCHANTMENT)) {
            ItemStack stack1 = this.inputSlots.getItem(0);
            ItemStack stack2 = this.inputSlots.getItem(1);

            ItemStack material = null;
            ItemStack shulker = null;

            if (stack1.is(ItemTags.SHULKER_BOXES)) {
                shulker = stack1;
            } else if (!stack1.isEmpty()) {
                material = stack1;
            }

            if (!stack2.isEmpty()) {
                if (shulker == null && stack2.is(ItemTags.SHULKER_BOXES)) {
                    shulker = stack2;
                } else if (material == null) {
                    material = stack2;
                }
            }

            if (material != null &&
                    shulker != null &&
                    MaterialCollector.hasEnchantment(shulker, this.player.level())
            ) {
                // Consume only one item from the material stack
                int materialSlot = (material == stack1) ? 0 : 1;
                if (material.getCount() > 1) {
                    material.shrink(1);
                    this.inputSlots.setItem(materialSlot, material);
                } else {
                    this.inputSlots.setItem(materialSlot, ItemStack.EMPTY);
                }

                // Let the default behavior handle consuming the shulker and clearing the result slot
                // But we need to prevent it from consuming the material stack again
                // We'll handle the shulker consumption manually
                if (shulker == stack1) {
                    this.inputSlots.setItem(0, ItemStack.EMPTY);
                } else {
                    this.inputSlots.setItem(1, ItemStack.EMPTY);
                }

                // Cancel the default behavior since we've handled everything manually
                ci.cancel();
            }
        }
    }
}
