package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.interfaces.ViewingMarker;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemStack.class)
public class ItemStackMixin implements ViewingMarker {
    private ServerPlayer viewer;

    @Override
    public ServerPlayer getViewer() {
        return this.viewer;
    }

    @Override
    public void setViewer(ServerPlayer player) {
        this.viewer = player;
    }

    @Inject(method = "copy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setPopTime(I)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void bettershulkers$copyViewer(CallbackInfoReturnable<ItemStack> cir, ItemStack stack) {
        ((ViewingMarker) (Object) stack).setViewer(this.viewer);
    }
}
