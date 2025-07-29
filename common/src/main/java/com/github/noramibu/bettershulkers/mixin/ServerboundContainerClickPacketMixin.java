package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.BetterShulkers;
import com.github.noramibu.bettershulkers.interfaces.ShulkerViewer;
import com.github.noramibu.bettershulkers.util.ShulkerUtil;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
//: >=1.21.2
import net.minecraft.network.protocol.game.ClientboundSetCursorItemPacket;
//: END
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerboundContainerClickPacketMixin {
    @Shadow public ServerPlayer player;

    @WrapWithCondition(method = "handleContainerClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;clicked(IILnet/minecraft/world/inventory/ClickType;Lnet/minecraft/world/entity/player/Player;)V"))
    private boolean checkIfShulkerIsClicked(AbstractContainerMenu instance, int slot, int buttonNum, ClickType clickType, Player player) {
        if (ShulkerUtil.isValidOpeningUI(instance) &&
                ShulkerUtil.hasOpenPermission(player) &&
                slot >= 0) {
            ItemStack stack = instance.getSlot(slot).getItem();
            ItemStack viewing = ((ShulkerViewer) this.player).getViewedStack();

            ItemStack held = instance.getCarried();

            if (ShulkerUtil.isShulkerBox(stack)) {
                switch (clickType) {
                    // Insert item, Open
                    case PICKUP -> {
                        if (buttonNum == 1) {

                            if (viewing != null) {
                                if (viewing.equals(stack)) {
                                    // Automatically insert items
                                    if (!held.isEmpty()) {
                                        ShulkerUtil.addToShulkerInventory(instance, held, true);
                                    }

                                } else {
                                    // Automatically insert items
                                    if (!held.isEmpty()) {
                                        ShulkerUtil.addToShulker(stack, held);
                                    }

                                    ShulkerUtil.seamlesslySwitchShulkerInventory((ServerPlayer) player, stack);
                                }
                            } else {
                                // Automatically insert items
                                if (!held.isEmpty()) {
                                    ShulkerUtil.addToShulker(stack, held);
                                }

                                BetterShulkers.openShulkerMenu(stack, this.player);
                            }
                        } else {
                            return true;
                        }
                    }
                    // Insert all, Extract all, Open
                    case QUICK_MOVE -> {
                        if (buttonNum == 1) {

                            if (viewing != null) {
                                if (viewing.equals(stack)) {
                                    // Quick item moving
                                    quickMove(instance, held);
                                } else {
                                    ShulkerUtil.seamlesslySwitchShulkerInventory((ServerPlayer) player, stack);
                                }
                            } else {
                                BetterShulkers.openShulkerMenu(stack, this.player);
                            }
                        } else {
                            return true;
                        }
                    }
                    default -> {
                        return true;
                    }
                }
            } else {
                return true;
            }


            // Don't delete held items
            instance.setCarried(held);
            //: >=1.21.2
            this.player.connection.send(new ClientboundSetCursorItemPacket(held));
            //: END
            this.player.connection.send(new ClientboundContainerSetSlotPacket(instance.containerId, instance.getStateId(), slot, stack));
            return false;
        }
        return true;
    }

    private void quickMove(AbstractContainerMenu containerMenu, ItemStack heldStack) {
        if (heldStack.isEmpty()) {
            quickExtract(containerMenu);
        } else {
            quickInsert(containerMenu, heldStack);
        }
        containerMenu.broadcastChanges();
    }

    private void quickInsert(AbstractContainerMenu container, ItemStack heldStack) {
        NonNullList<Slot> slots = ((AbstractContainerAccessor)container).getSlots();
        Item itemType = heldStack.getItem();
        for (int i = 27; i < 63; i++) {
            Slot slot = slots.get(i);
            ItemStack stack = slot.getItem();
            if (stack.is(itemType)) {
                ShulkerUtil.addToShulkerInventory(container, stack, false);
                if (!stack.isEmpty()) {
                    break;
                }
            }
        }
        ShulkerUtil.addToShulkerInventory(container, heldStack,false);
    }

    private void quickExtract(AbstractContainerMenu container) {
        NonNullList<Slot> slots = ((AbstractContainerAccessor)container).getSlots();
        for (int i = 0; i < 27; i++) {
            Slot slot = slots.get(i);
            ItemStack stack = slot.getItem();
            ShulkerUtil.addToPlayerInventory(container, stack, false);
        }
    }
}
