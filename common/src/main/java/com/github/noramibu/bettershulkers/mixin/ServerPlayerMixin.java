package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.interfaces.ShulkerViewer;
import com.github.noramibu.bettershulkers.interfaces.Viewable;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.github.noramibu.bettershulkers.util.ShulkerUtil;

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

    private ItemStack viewing = null;

    @Override
    public @Nullable ItemStack getViewing() {
        return this.viewing;
    }

    @Override
    public void addViewing(@NotNull ItemStack stack) {
        System.out.println("Tracking itemstack");
        this.viewing = stack;
        ((Viewable) (Object) stack).setViewer(((ServerPlayer) (Object) this));
    }

    @Override
    public void removeViewing() {
        if (this.viewing != null) {
            System.out.println("Stopped tracking itemstack");
            ((Viewable) (Object) this.viewing).setViewer(null);
            this.viewing = null;
        }
    }

    @Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("HEAD"))
    private void checkIfItemIsViewedShulker(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        // Check if the dropped item is the viewed shulker
        if (this.viewing != null &&
            ShulkerUtil.isShulkerBox(stack) && 
            (stack == this.viewing)) {
            
            // Save the inventory before closing
            ShulkerUtil.saveShulkerInventory(this.containerMenu.getItems(), stack);

            // Close the container immediately
            this.closeContainer();
            this.removeViewing();
        }
    }
}