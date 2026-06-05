/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers;

import com.github.noramibu.bettershulkers.mixin.InventoryMixin;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
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
                System.out.println("Opening Shulker at " + slot.index);
                menu.container.reload(shulkerbox, player, slot.index);
            } else {
                // Open new menu
                System.out.println("Opening Shulker at " + getShulkerIndex(slot));
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

        System.out.println("Row: " + row + ", Col: " + column);
        return 27 + ((3 - row) * 9) + column;
    }

    public static boolean isServerSide(Player player) {
        return !player.level().isClientSide();
    }

    // Returns if there is any leftover that couldn't be added
    public static ItemStack putIntoShulkerItem(ItemStack shulker, ItemStack insert) {
        ItemContainerContents contents = shulker.get(DataComponents.CONTAINER);
        int leftover = ((MutableContainerContents) (Object) contents).add(insert, insert.count());
        if (leftover == 0) {
            return ItemStack.EMPTY;
        } else {
            insert.setCount(leftover);
            return insert;
        }
    }

    // Returns if there is any leftover that couldn't be removed
    public static int removeTypeFromShulkerItem(ItemStack shulker, ItemStack type, int amount) {
        ItemContainerContents contents = shulker.get(DataComponents.CONTAINER);
        return ((MutableContainerContents) (Object) contents).remove(type, amount);
    }

    private static int getAmountInInventory(Player player, ItemStack type) {
        int count = player.containerMenu.getCarried().getCount();

        for (ItemStack itemStack : ((InventoryMixin) player.getInventory()).getItems()) {
            if (ItemStack.isSameItemSameComponents(itemStack, type)) {
                count += itemStack.getCount();
            }
        }
        return count;
    }

    public static void dumpShulkerItemContents(Player player, ItemStack shulker) {
        Inventory playerInventory = player.getInventory();

        ItemContainerContents contents = shulker.get(DataComponents.CONTAINER);
        List<Optional<ItemStackTemplate>> templates = ((MutableContainerContents) (Object) contents).getItems();

        for (int i = 0; i < templates.size(); i++) {
            Optional<ItemStackTemplate> possibleTemplate = templates.get(i);

            if (possibleTemplate.isPresent()) {
                ItemStack stackInShulker = possibleTemplate.get().create();

                if (playerInventory.add(stackInShulker)) {
                    // If anything was added to the inventory, modify the amount in the shulker
                    ((MutableContainerContents) (Object) contents).set(i, stackInShulker);
                }
            }
        }
    }

    public static void dumpInventoryIntoShulkerItem(Player player, ItemStack shulker, ItemStack type) {
        int amount = getAmountInInventory(player, type);
        ItemContainerContents contents = shulker.get(DataComponents.CONTAINER);
        int leftOver = ((MutableContainerContents) (Object) contents).add(type, amount);
        int totalAdded = amount - leftOver;

        // Remove from inventory
        NonNullList<ItemStack> items = ((InventoryMixin) player.getInventory()).getItems();
        for (int i = 0; i < items.size(); i++) {
            if (totalAdded == 0) {
                return;
            }

            ItemStack stack = items.get(i);
            if (ItemStack.isSameItemSameComponents(stack, type)) {
                int shrinkAmount = Math.min(totalAdded, stack.count());
                stack.shrink(shrinkAmount);
                totalAdded -= shrinkAmount;
                if (stack.isEmpty()) {
                    player.getInventory().setItem(i, ItemStack.EMPTY);
                }
            }
        }

        // Remove from cursor last
        int heldShrinkAmount = Math.min(player.containerMenu.getCarried().count(), totalAdded);
        player.containerMenu.getCarried().shrink(heldShrinkAmount);
        if (player.containerMenu.getCarried().isEmpty()) {
            player.containerMenu.setCarried(ItemStack.EMPTY);
        }
    }
}
