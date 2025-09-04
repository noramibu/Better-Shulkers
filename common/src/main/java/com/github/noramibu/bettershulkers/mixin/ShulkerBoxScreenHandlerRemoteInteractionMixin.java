package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.interfaces.ShulkerViewer;
import com.github.noramibu.bettershulkers.util.ShulkerUtil;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//: >=1.21.2
import net.minecraft.network.protocol.game.ClientboundSetCursorItemPacket;
//: END
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBoxMenu.class)
public abstract class ShulkerBoxScreenHandlerRemoteInteractionMixin extends AbstractContainerMenu implements ShulkerViewer {
    private ItemStack shulkerItem;

    protected ShulkerBoxScreenHandlerRemoteInteractionMixin(@Nullable MenuType<?> menuType, int i) {
        super(menuType, i);
    }

    @WrapOperation(method = "stillValid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;stillValid(Lnet/minecraft/world/entity/player/Player;)Z"))
    private boolean checkIfForced(Container instance, Player player, Operation<Boolean> original) {
        if (player instanceof ServerPlayer serverPlayer && ((ShulkerViewer) serverPlayer.containerMenu).isViewing()) {
            return true;
        }
        return original.call(instance, player);
    }

    @Inject(method = "removed", at = @At("HEAD"))
    private void checkIfShulkerIsHeld(Player player, CallbackInfo ci) {
        if (player instanceof ServerPlayer serverPlayer) {
            ItemStack viewedStack = ((ShulkerViewer) serverPlayer.containerMenu).getViewing();
            if (viewedStack != null) {
                // Save the shulker inventory when closing the menu
                ShulkerUtil.saveShulkerInventory(serverPlayer.containerMenu.getItems(), viewedStack);
                ((ShulkerViewer) serverPlayer.containerMenu).removeViewing();

                // Prevent ghost items
                //: >=1.21.2
                serverPlayer.connection.send(new ClientboundSetCursorItemPacket(ItemStack.EMPTY));
                //: END
                /*\ <=1.21.1
                ShulkerUtil.syncHeldItem(player.inventoryMenu, serverPlayer);
                \END */

                //: >=1.21.6
                serverPlayer.level().playSound(null, player.blockPosition(), SoundEvents.SHULKER_BOX_CLOSE, player.getSoundSource(), 1.0F, 1.0F);
                //: END

                /*\ <=1.21.5
                serverPlayer.serverLevel().playSound(null, player.blockPosition(), SoundEvents.SHULKER_BOX_CLOSE, player.getSoundSource(), 1.0F, 1.0F);
                \END */

                // TODO I have forgotten why I am doing this
                if (viewedStack.isEmpty()) {
                    ItemStack stack = this.getCarried();
                    if (!stack.isEmpty()) {
                        ((ShulkerViewer) serverPlayer.containerMenu).addViewing(stack);
                    }
                }
            }
        }
    }

    @Override
    public @Nullable ItemStack getViewing() {
        return this.shulkerItem;
    }

    @Override
    public void addViewing(@NotNull ItemStack stack) {
        this.shulkerItem = stack;
    }

    @Override
    public void removeViewing() {
        this.shulkerItem = null;
    }
}
