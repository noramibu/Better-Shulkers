package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.Config;
import com.github.noramibu.bettershulkers.util.ShulkerUtil;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.entity.player.Player;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
    @Shadow @Final private DataSlot cost;

    /*\ <=1.21.1
    public AnvilMenuMixin(@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        super(menuType, i, inventory, containerLevelAccess);
    }
    \END */

    //: >=1.21.2
    public AnvilMenuMixin(@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess, ItemCombinerMenuSlotDefinition itemCombinerMenuSlotDefinition) {
        super(menuType, i, inventory, containerLevelAccess, itemCombinerMenuSlotDefinition);
    }
    //: END

    @Inject(method = "createResult()V", at = @At("HEAD"), cancellable = true)
    private void bettershulkers$checkForShulkerMaterial(CallbackInfo ci) {
        if (Config.ITEM_PICKUP_TYPE.equals(Config.PickupType.ENCHANTMENT)) {
            ItemStack stack1 = this.inputSlots.getItem(0);
            ItemStack stack2 = this.inputSlots.getItem(1);
            
            ItemStack material = null;
            ItemStack shulker = null;

            if (ShulkerUtil.isShulkerBox(stack1)) {
                shulker = stack1;
            } else {
                material = stack1;
            }

            if (!stack2.isEmpty()) {
                if (shulker == null && ShulkerUtil.isShulkerBox(stack2)) {
                    shulker = stack2;
                } else {
                    material = stack2;
                }
            }

            if (material != null && shulker != null && ShulkerUtil.isEnchanted(shulker)) {
                ItemStack clone = shulker.copy();
                ShulkerUtil.setMaterialForShulker(clone, material);
                this.cost.set(1);
                this.resultSlots.setItem(0, clone);
                ci.cancel();
            }
        }
    }

    @Inject(method = "onTake", at = @At("HEAD"), cancellable = true)
    private void bettershulkers$handleResultTaken(Player player, ItemStack stack, CallbackInfo ci) {
        // Check if this is our custom shulker material recipe
        if (Config.ITEM_PICKUP_TYPE.equals(Config.PickupType.ENCHANTMENT)) {
            ItemStack stack1 = this.inputSlots.getItem(0);
            ItemStack stack2 = this.inputSlots.getItem(1);
            
            ItemStack material = null;
            ItemStack shulker = null;

            if (ShulkerUtil.isShulkerBox(stack1)) {
                shulker = stack1;
            } else if (!stack1.isEmpty()) {
                material = stack1;
            }

            if (!stack2.isEmpty()) {
                if (shulker == null && ShulkerUtil.isShulkerBox(stack2)) {
                    shulker = stack2;
                } else if (material == null) {
                    material = stack2;
                }
            }

            if (material != null &&
                    shulker != null &&
                    ShulkerUtil.isEnchanted(shulker)
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

    @WrapOperation(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;setEnchantments(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/enchantment/ItemEnchantments;)V"))
    private void bettershulkers$doNotSetShulkerEnchantments(ItemStack itemStack, ItemEnchantments itemEnchantments, Operation<Void> original) {
        if (ShulkerUtil.isShulkerBox(itemStack)) {
            ShulkerUtil.setMaterialForShulker(itemStack, ItemStack.EMPTY);
        } else {
            original.call(itemStack, itemEnchantments);
        }
    }
}
