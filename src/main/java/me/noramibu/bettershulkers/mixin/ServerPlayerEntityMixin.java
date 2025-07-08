package me.noramibu.bettershulkers.mixin;

import com.mojang.authlib.GameProfile;
import me.noramibu.bettershulkers.accessor.ShulkerViewer;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ShulkerViewer {

    public ServerPlayerEntityMixin(World world, GameProfile profile) {
        super(world, profile);
    }

    @Shadow public abstract void closeHandledScreen();

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

    @Inject(method = "dropItem", at = @At("HEAD"))
    private void checkIfItemIsViewedShulker(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        // The itemstack becomes empty if it has been dropped
        if (this.viewingForcedShulker != null && this.viewingForcedShulker.isEmpty()) {
            // Close UI
            this.closeHandledScreen();
            this.viewingForcedShulker = null;
        }
    }
} 