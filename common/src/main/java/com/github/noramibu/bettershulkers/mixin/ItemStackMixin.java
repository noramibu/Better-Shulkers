package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.interfaces.Viewable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemStack.class)
public class ItemStackMixin implements Viewable {
    private ServerPlayer viewer = null;

    @Override
    public @Nullable ServerPlayer getViewer() {
        return this.viewer;
    }

    @Override
    public void setViewer(@Nullable ServerPlayer player) {
        this.viewer = player;
    }

    @Inject(method = "copy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setPopTime(I)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void bettershulkers$notifyViewerOfChange(CallbackInfoReturnable<ItemStack> cir, ItemStack itemStack) {
        this.notifyViewerOfStackChange(itemStack);
    }
}
