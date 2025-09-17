package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.interfaces.ShulkerViewer;
import com.github.noramibu.bettershulkers.interfaces.ViewingMarker;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.github.noramibu.bettershulkers.util.ShulkerUtil;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {

    @Shadow
    public ServerGamePacketListenerImpl connection;

    public ServerPlayerMixin(Level world, GameProfile profile) {
        /*\ <=1.21.5
        super(world, null, 1.0f, profile);
        \END */

        //: >=1.21.6
        super(world, profile);
        //: END
    }

    @Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("HEAD"))
    private void bettershulkers$checkIfItemIsViewedShulker(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        // Check if the dropped item is the viewed shulker
        if (this.containerMenu instanceof ShulkerViewer shulkerViewer && ((ViewingMarker) (Object) stack).isBeingViewed()) {
            // Save the inventory before closing
            ShulkerUtil.saveShulkerInventory(this.containerMenu.getItems(), stack);
            // Close the container immediately
            shulkerViewer.removeViewing();
            // Minimal closing code
            this.connection.send(new ClientboundContainerClosePacket(this.containerMenu.containerId));
            this.containerMenu.setCarried(ItemStack.EMPTY);
            this.inventoryMenu.transferState(this.containerMenu);
            this.containerMenu = this.inventoryMenu;
        }
    }
}