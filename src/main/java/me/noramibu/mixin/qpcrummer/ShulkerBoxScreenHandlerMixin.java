package me.noramibu.mixin.qpcrummer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.noramibu.bettershulkers.accessor.ShulkerViewer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBoxScreenHandler.class)
public abstract class ShulkerBoxScreenHandlerMixin extends ScreenHandler {
    protected ShulkerBoxScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @WrapOperation(method = "canUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;canPlayerUse(Lnet/minecraft/entity/player/PlayerEntity;)Z"))
    private boolean checkIfForced(Inventory instance, PlayerEntity player, Operation<Boolean> original) {
        if (player instanceof ServerPlayerEntity serverPlayer && ((ShulkerViewer)serverPlayer).isViewingShulker()) {
            return true;
        }
        return original.call(instance, player);
    }

    @Inject(method = "onClosed", at = @At("HEAD"))
    private void checkIfShulkerIsHeld(PlayerEntity player, CallbackInfo ci) {
        if (player instanceof ServerPlayerEntity serverPlayer && ((ShulkerViewer)serverPlayer).getViewedStack().isEmpty()) {
            ItemStack stack = this.getCursorStack();
            if (!stack.isEmpty()) {
                ((ShulkerViewer)serverPlayer).setViewing(stack);
            }
        }
    }
}
