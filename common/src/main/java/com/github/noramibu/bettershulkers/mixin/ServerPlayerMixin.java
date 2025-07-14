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

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player implements ShulkerViewer {

    public ServerPlayerMixin(Level world, GameProfile profile) {
        //: >=1.21.6
        super(world, profile);
        //: END

        /*\ <=1.21.5
        super(world, null, 1.0f, profile);
        \*/
    }

    private ItemStack viewingForcedShulker;

    @Override
    public boolean isViewingShulker() {
        return this.viewingForcedShulker != null;
    }

    @Override
    public void setViewing(@Nullable ItemStack stack) {
        this.viewingForcedShulker = stack;
    }

    @Override
    public ItemStack getViewedStack() {
        return this.viewingForcedShulker;
    }

    @Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("HEAD"))
    private void checkIfItemIsViewedShulker(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        // The itemstack becomes empty if it has been dropped
        if (this.viewingForcedShulker != null && this.viewingForcedShulker.isEmpty()) {
            // Close UI
            this.closeContainer();
            this.viewingForcedShulker = null;
        }
    }
}