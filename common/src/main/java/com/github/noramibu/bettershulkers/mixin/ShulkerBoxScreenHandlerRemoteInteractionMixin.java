package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.interfaces.ShulkerViewer;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBoxMenu.class)
public abstract class ShulkerBoxScreenHandlerRemoteInteractionMixin extends AbstractContainerMenu {
    protected ShulkerBoxScreenHandlerRemoteInteractionMixin(@Nullable MenuType<?> menuType, int i) {
        super(menuType, i);
    }

    @WrapOperation(method = "stillValid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;stillValid(Lnet/minecraft/world/entity/player/Player;)Z"))
    private boolean checkIfForced(Container instance, Player player, Operation<Boolean> original) {
        if (player instanceof ServerPlayer serverPlayer && ((ShulkerViewer)serverPlayer).isViewingShulker()) {
            return true;
        }
        return original.call(instance, player);
    }

    @Inject(method = "removed", at = @At("HEAD"))
    private void checkIfShulkerIsHeld(Player player, CallbackInfo ci) {
        if (player instanceof ServerPlayer serverPlayer) {
            ItemStack viewedStack = ((ShulkerViewer)serverPlayer).getViewedStack();
            if (viewedStack != null && viewedStack.isEmpty()) {
                ItemStack stack = this.getCarried();
                if (!stack.isEmpty()) {
                    ((ShulkerViewer)serverPlayer).setViewing(stack);
                }
            }
        }
    }
}
