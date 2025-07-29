package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.interfaces.ShulkerViewer;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.github.noramibu.bettershulkers.util.ShulkerUtil;
import org.spongepowered.asm.mixin.Unique;
import net.minecraft.core.component.DataComponents;
import java.util.Objects;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player implements ShulkerViewer {

    public ServerPlayerMixin(Level world, GameProfile profile) {
        /*\ <=1.21.5
        super(world, null, 1.0f, profile);
        \END */
        //: >=1.21.6
        super(world, profile);
        //: END
    }

    private ItemStack viewingForcedShulker;
    @Unique
    private int originalShulkerSlot = -1;
    @Unique
    private String shulkerFingerprint = null;
    @Unique
    private boolean wasInCursor = false;

    @Override
    public boolean isViewingShulker() {
        if (this.viewingForcedShulker != null && this.viewingForcedShulker.isEmpty()) {
            // Check cursor first
            ItemStack cursorStack = this.containerMenu.getCarried();
            if (cursorStack != null && !cursorStack.isEmpty() && ShulkerUtil.isShulkerBox(cursorStack) && hasMatchingFingerprint(cursorStack)) {
                this.viewingForcedShulker = cursorStack;
                this.originalShulkerSlot = -1;
                this.wasInCursor = true;
                return true;
            }
            
            // If was in cursor, find most recently placed
            if (this.wasInCursor) {
                ItemStack recentlyPlaced = findMostRecentlyPlacedShulker();
                if (recentlyPlaced != null) {
                    this.viewingForcedShulker = recentlyPlaced;
                    this.originalShulkerSlot = findShulkerSlot(recentlyPlaced);
                    this.wasInCursor = false;
                    return true;
                }
            }
            
            // Check original slot
            if (this.originalShulkerSlot >= 0 && this.originalShulkerSlot < this.getInventory().getContainerSize()) {
                ItemStack originalSlotStack = this.getInventory().getItem(this.originalShulkerSlot);
                if (originalSlotStack != null && !originalSlotStack.isEmpty() && ShulkerUtil.isShulkerBox(originalSlotStack) && hasMatchingFingerprint(originalSlotStack)) {
                    this.viewingForcedShulker = originalSlotStack;
                    return true;
                }
            }
            
            // Search by fingerprint with proximity priority
            ItemStack foundShulker = findShulkerByFingerprint();
            if (foundShulker != null) {
                this.viewingForcedShulker = foundShulker;
                this.originalShulkerSlot = findShulkerSlot(foundShulker);
                return true;
            }
            
            // Not found, save and clear
            if (this.containerMenu != null) {
                this.containerMenu.removed(this);
            }
            this.viewingForcedShulker = null;
            this.shulkerFingerprint = null;
            this.wasInCursor = false;
            return false;
        }
        
        return this.viewingForcedShulker != null && !this.viewingForcedShulker.isEmpty();
    }
    
    @Unique
    private boolean hasMatchingFingerprint(ItemStack stack) {
        if (this.shulkerFingerprint == null) {
            return false;
        }
        
        if (stack == this.viewingForcedShulker) {
            return true;
        }
        
        String stackFingerprint = generateFingerprint(stack);
        return Objects.equals(this.shulkerFingerprint, stackFingerprint);
    }
    
    @Unique
    private String generateFingerprint(ItemStack stack) {
        StringBuilder fingerprint = new StringBuilder();
        fingerprint.append(stack.getItem().toString()).append(":").append(stack.getCount());
        
        var container = stack.get(DataComponents.CONTAINER);
        if (container != null) {
            fingerprint.append(":").append(container.hashCode());
        }
        
        var customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            fingerprint.append(":").append(customData.hashCode());
        }
        
        return fingerprint.toString();
    }
    
    @Unique
    private ItemStack findShulkerByFingerprint() {
        if (this.shulkerFingerprint == null) {
            return null;
        }
        
        // Try exact reference first
        for (int i = 0; i < this.getInventory().getContainerSize(); i++) {
            ItemStack stack = this.getInventory().getItem(i);
            if (stack == this.viewingForcedShulker) {
                return stack;
            }
        }
        
        // Search by fingerprint with proximity priority
        ItemStack closestMatch = null;
        int closestDistance = Integer.MAX_VALUE;
        
        for (int i = 0; i < this.getInventory().getContainerSize(); i++) {
            ItemStack stack = this.getInventory().getItem(i);
            if (stack != null && !stack.isEmpty() && ShulkerUtil.isShulkerBox(stack) && hasMatchingFingerprint(stack)) {
                int distance = Math.abs(i - this.originalShulkerSlot);
                if (distance < closestDistance) {
                    closestMatch = stack;
                    closestDistance = distance;
                }
            }
        }
        
        return closestMatch;
    }
    
    @Unique
    private ItemStack findMostRecentlyPlacedShulker() {
        // Search from hotbar slots first (most recently placed)
        for (int i = this.getInventory().getContainerSize() - 1; i >= 0; i--) {
            ItemStack stack = this.getInventory().getItem(i);
            if (stack != null && !stack.isEmpty() && ShulkerUtil.isShulkerBox(stack) && hasMatchingFingerprint(stack)) {
                return stack;
            }
        }
        return null;
    }
    
    @Unique
    private int findShulkerSlot(ItemStack targetStack) {
        for (int i = 0; i < this.getInventory().getContainerSize(); i++) {
            if (this.getInventory().getItem(i) == targetStack) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public void setViewing(@Nullable ItemStack stack) {
        this.viewingForcedShulker = stack;
        if (stack != null) {
            this.shulkerFingerprint = generateFingerprint(stack);
            this.originalShulkerSlot = findShulkerSlot(stack);
        } else {
            this.originalShulkerSlot = -1;
            this.shulkerFingerprint = null;
            this.wasInCursor = false;
        }
    }

    @Override
    public ItemStack getViewedStack() {
        return this.viewingForcedShulker;
    }

    @Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("HEAD"))
    private void checkIfItemIsViewedShulker(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        if (this.viewingForcedShulker != null && this.viewingForcedShulker.isEmpty()) {
            this.closeContainer();
            this.viewingForcedShulker = null;
            this.shulkerFingerprint = null;
        }
    }
}