/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers;

import com.github.noramibu.bettershulkers.container.VirtualContainerHolder;
import com.github.noramibu.bettershulkers.container.VirtualShulkerBoxContainer;
import com.github.noramibu.bettershulkers.menu.VirtualShulkerBoxMenu;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

public final class ShulkerBoxUtils {
    public static void openExternalShulker(ItemStack shulkerbox, Player player) {
        if (isServerSide(player)) {
            VirtualShulkerBoxContainer container = new VirtualShulkerBoxContainer(shulkerbox, player);
            ((VirtualContainerHolder) player).setVirtualContainer(container);
            player.openMenu(container);
        }
    }

    public static void openInternalShulker(ItemStack shulkerbox, Player player, Slot slot) {
        // Check if item is in the inventory
        if (isServerSide(player) && isSlotInInventory(slot, player)) {
            // Check if menu is already open
            if (player.containerMenu instanceof VirtualShulkerBoxMenu menu) {
                menu.getContainer().reload(shulkerbox, player, slot.index);
            } else {
                // Open new menu
                VirtualShulkerBoxContainer container = new VirtualShulkerBoxContainer(shulkerbox, player, getShulkerIndex(slot));
                ((VirtualContainerHolder) player).setVirtualContainer(container);
                player.openMenu(container);
            }
        }
    }

    private static boolean isSlotInInventory(Slot slot, Player player) {
        return slot.container == player.getInventory();
    }

    private static int getShulkerIndex(Slot slot) {
        int index = slot.getContainerSlot();
        int column = index % 9;
        int row = index / 9;
        return 27 + ((3 - row) * 9) + column;
    }

    public static boolean isServerSide(Player player) {
        return !player.level().isClientSide();
    }

    // Add a single stack into a shulker
    public static ItemStack addStackToShulker(ItemStack shulker, ItemStack toAdd) {
        if (!canFitInShulker(toAdd)) {
            return toAdd;
        }

        ItemContainerContents contents = shulker.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        SimpleContainer tempInv = new SimpleContainer(27);
        contents.copyInto(tempInv.getItems());

        ItemStack leftover = tempInv.addItem(toAdd);

        shulker.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(tempInv.getItems()));
        return leftover;
    }

    // Add all of a single type from player inventory to shulker
    public static void addAllToShulker(ItemStack shulker, Player player, Item itemToPull) {
        if (!canFitInShulker(itemToPull.getDefaultInstance())) return;

        ItemContainerContents contents = shulker.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        SimpleContainer tempInv = new SimpleContainer(27);
        contents.copyInto(tempInv.getItems());

        Inventory playerInv = player.getInventory();
        List<ItemStack> allSlots = new ArrayList<>(playerInv.getNonEquipmentItems());
        allSlots.add(player.containerMenu.getCarried());

        for (int i = 0; i < allSlots.size(); i++) {
            ItemStack stack = allSlots.get(i);
            if (stack.isEmpty() || !stack.is(itemToPull)) continue;

            for (int j = 0; j < tempInv.getContainerSize(); j++) {
                if (stack.isEmpty()) break;

                ItemStack shulkerStack = tempInv.getItem(j);

                if (ItemStack.isSameItemSameComponents(shulkerStack, stack)) {
                    int toAdd = Math.min(stack.getCount(), shulkerStack.getMaxStackSize() - shulkerStack.getCount());
                    if (toAdd > 0) {
                        shulkerStack.grow(toAdd);
                        stack.shrink(toAdd);
                    }
                }

                if (shulkerStack.isEmpty() && !stack.isEmpty()) {
                    tempInv.setItem(j, stack.copy());
                    stack.setCount(0);
                }
            }

            if (i < playerInv.getNonEquipmentItems().size()) {
                playerInv.setItem(i, stack);
            } else {
                player.containerMenu.setCarried(stack);
            }
        }
        shulker.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(tempInv.getItems()));
    }

    // Take all from shulker into player inventory
    public static void pullAllFromShulker(ItemStack shulker, Player player) {
        ItemContainerContents contents = shulker.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        SimpleContainer tempInv = new SimpleContainer(27);
        contents.copyInto(tempInv.getItems());

        Inventory playerInv = player.getInventory();
        int shulkerIndex = 0;

        while (shulkerIndex < 28) {
            ItemStack shulkerStack = tempInv.getItem(shulkerIndex);
            if (!shulkerStack.isEmpty()) {
                playerInv.add(shulkerStack);

                if (shulkerStack.isEmpty()) {
                    tempInv.setItem(shulkerIndex, ItemStack.EMPTY);
                }
            }
            shulkerIndex++;
        }

        shulker.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(tempInv.getItems()));
    }

    private static boolean canFitInShulker(ItemStack item) {
        return !item.is(ItemTags.SHULKER_BOXES);
    }
}
