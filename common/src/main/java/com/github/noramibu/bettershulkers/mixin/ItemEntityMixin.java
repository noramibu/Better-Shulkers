package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.Config;
import com.github.noramibu.bettershulkers.interfaces.ForceInventory;
import com.github.noramibu.bettershulkers.interfaces.ShulkerViewer;
import com.github.noramibu.bettershulkers.util.ShulkerUtil;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.github.noramibu.bettershulkers.enchantment.MaterialCollector;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Inject(
            method = "playerTouch",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z"
            ),
            cancellable = true
    )
    private void onBeforeInsertStack(Player player, CallbackInfo ci, @Local(ordinal = 0) ItemStack itemStack) {
        // Pickup is disabled
        if (Config.ITEM_PICKUP_TYPE.equals(Config.PickupType.NONE)) {
            return;
        }

        Inventory playerInventory = player.getInventory();
        ItemEntity self = (ItemEntity) (Object) this;

        for (int i = 0; i < playerInventory.getContainerSize(); i++) {
            ItemStack inventoryStack = playerInventory.getItem(i);

            // Not Shulker, has no material, or has no enchantment (if applicable)
            if (!ShulkerUtil.isShulkerBox(inventoryStack) ||
                    ShulkerUtil.getMaterialFromShulker(inventoryStack) == null ||
                    (Config.ITEM_PICKUP_TYPE.equals(Config.PickupType.ENCHANTMENT) && !hasEnchantment(inventoryStack))) {
                continue;
            }

            if (ShulkerUtil.canBeAddedToShulker(inventoryStack, itemStack)) {
                int originalCount = itemStack.getCount();
                ShulkerUtil.addToShulker(inventoryStack, itemStack);
                playerInventory.setItem(i, inventoryStack);

                if (player.containerMenu instanceof ShulkerBoxMenu menuHandler) {
                    Container screenInventory = ((ShulkerBoxMenuHandlerAccessor)menuHandler).getInventory();

                    if (((ForceInventory)screenInventory).forced() && ((ShulkerViewer)player).getViewedStack() == inventoryStack) {
                        NonNullList<ItemStack> updatedList = ShulkerUtil.getInventoryFromShulker(inventoryStack);
                        ((ForceInventory)screenInventory).setInventory(updatedList);
                        menuHandler.broadcastChanges();
                    }
                }

                if (itemStack.isEmpty()) {
                    player.take(self, originalCount);
                    self.discard();
                    ci.cancel();
                } else {
                    int pickedUpCount = originalCount - itemStack.getCount();
                    if (pickedUpCount > 0) {
                        player.take(self, pickedUpCount);
                    }
                }
                if (ci.isCancelled()) {
                    break;
                }
            }
        }
    }

    private boolean hasEnchantment(ItemStack stack) {
        ItemEnchantments ench = stack.get(DataComponents.ENCHANTMENTS);
        if (ench != null) {
            for (Holder<Enchantment> key : ench.keySet()) {
                Optional<ResourceKey<Enchantment>> keyResource = key.unwrapKey();
                if (keyResource.isPresent() && keyResource.get().equals(MaterialCollector.MATERIAL_COLLECTOR)) {
                    return true;
                }
            }
        }
        return false;
    }
}
