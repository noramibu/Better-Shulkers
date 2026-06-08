/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.container.VirtualContainer;
import com.github.noramibu.bettershulkers.container.VirtualContainerHolder;
import com.github.noramibu.bettershulkers.container.VirtualShulkerBoxContainer;
import com.mojang.authlib.GameProfile;
import java.util.OptionalInt;
import net.minecraft.network.protocol.game.ClientboundSetCursorItemPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player implements VirtualContainerHolder {

    @Shadow
    public ServerGamePacketListenerImpl connection;
    private VirtualShulkerBoxContainer virtualContainer;

    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("HEAD"))
    private void bettershulkers$checkForShulkerDropped(ItemStack itemStack, boolean randomly, boolean thrownFromHand, CallbackInfoReturnable<ItemEntity> cir) {
        if (((VirtualContainer) (Object) itemStack).isBeingViewed()) {
            this.closeContainer();
        }
    }

    @Inject(method = "closeContainer", at = @At("HEAD"))
    private void bettershulkers$syncOpenContainer(CallbackInfo ci) {
        if (this.virtualContainer != null) {
            ((VirtualContainer) (Object)this.virtualContainer.getViewedStack()).setViewing(null);
            this.virtualContainer = null;
        }
    }

    @Inject(method = "openMenu", at = @At("HEAD"))
    private void bettershulkers$preventDesyncs(MenuProvider provider, CallbackInfoReturnable<OptionalInt> cir) {
        // Try to prevent desyncs
        this.connection.send(new ClientboundSetCursorItemPacket(this.containerMenu.getCarried()));
    }

    @Override
    public void setVirtualContainer(@Nullable VirtualShulkerBoxContainer container) {
        this.virtualContainer = container;
    }

    @Override
    public VirtualShulkerBoxContainer getVirtualContainer() {
        return this.virtualContainer;
    }
}
