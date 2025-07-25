package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.Config;
import com.github.noramibu.bettershulkers.interfaces.ForceInventory;
import com.github.noramibu.bettershulkers.interfaces.ShulkerViewer;
import com.github.noramibu.bettershulkers.util.ShulkerUtil;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
        if (Config.DISABLE_PICKUP_FEATURE_OF_SHULKERS) {
            return;
        }
        Inventory playerInventory = player.getInventory();
        ItemEntity self = (ItemEntity) (Object) this;

        for (int i = 0; i < playerInventory.getContainerSize(); i++) {
            ItemStack inventoryStack = playerInventory.getItem(i);
            if (ShulkerUtil.isShulkerBox(inventoryStack) &&
                    ShulkerUtil.canBeAddedToShulker(inventoryStack, itemStack)) {
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
            }
            if (ci.isCancelled()) {
                break;
            }
        }
    }
}
