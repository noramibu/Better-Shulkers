package me.noramibu.mixin.v1_21;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
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

@IfMinecraftVersion(minVersion = "1.21")
@Mixin(ShulkerBoxScreenHandler.class)
public abstract class ShulkerBoxScreenHandlerRemoteInteractionMixin extends ScreenHandler {
    protected ShulkerBoxScreenHandlerRemoteInteractionMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
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
        if (player instanceof ServerPlayerEntity serverPlayer) {
            ItemStack viewedStack = ((ShulkerViewer)serverPlayer).getViewedStack();
            if (viewedStack != null && viewedStack.isEmpty()) {
                ItemStack stack = this.getCursorStack();
                if (!stack.isEmpty()) {
                    ((ShulkerViewer)serverPlayer).setViewing(stack);
                }
            }
        }
    }
} 