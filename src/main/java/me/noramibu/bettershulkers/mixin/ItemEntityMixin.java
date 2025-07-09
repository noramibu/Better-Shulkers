package me.noramibu.bettershulkers.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.noramibu.bettershulkers.accessor.ForceInventory;
import me.noramibu.bettershulkers.accessor.ShulkerViewer;
import me.noramibu.bettershulkers.util.ShulkerUtil;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Inject(
            method = "onPlayerCollision",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z"
            ),
            cancellable = true
    )
    private void onBeforeInsertStack(PlayerEntity player, CallbackInfo ci, @Local(ordinal = 0) ItemStack itemStack) {
        PlayerInventory playerInventory = player.getInventory();
        ItemEntity self = (ItemEntity) (Object) this;

        for (int i = 0; i < playerInventory.size(); i++) {
            ItemStack inventoryStack = playerInventory.getStack(i);
            if (ShulkerUtil.isShulkerBox(inventoryStack) &&
                    ShulkerUtil.canBeAddedToShulker(inventoryStack, itemStack)) {
                int originalCount = itemStack.getCount();
                ShulkerUtil.addToShulker(inventoryStack, itemStack);
                playerInventory.setStack(i, inventoryStack);

                if (player.currentScreenHandler instanceof ShulkerBoxScreenHandler screenHandler) {
                    Inventory screenInventory = ((ShulkerBoxScreenHandlerAccessor)screenHandler).getInventory();

                    if (((ForceInventory)screenInventory).forced() && ((ShulkerViewer)player).getViewedStack() == inventoryStack) {
                        DefaultedList<ItemStack> updatedList = ShulkerUtil.getInventoryFromShulker(inventoryStack);
                        ((ForceInventory)screenInventory).setInventory(updatedList);
                        screenHandler.sendContentUpdates();
                    }
                }

                if (itemStack.isEmpty()) {
                    player.sendPickup(self, originalCount);
                    player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    self.discard();
                    ci.cancel();
                } else {
                    int pickedUpCount = originalCount - itemStack.getCount();
                    if (pickedUpCount > 0) {
                        player.sendPickup(self, pickedUpCount);
                    }
                }
            }
            if (ci.isCancelled()) {
                break;
            }
        }
    }
}