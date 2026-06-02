/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.jspecify.annotations.Nullable;

public class VirtualShulkerBoxContainer extends SimpleContainer implements MenuProvider {
    int cachedViewSlot;
    VirtualShulkerBoxMenu containerMenu;
    Player viewer;
    Component displayName;

    public VirtualShulkerBoxContainer(ItemStack viewing, Player player) {
        super(27);
        ((MoreComplexContainer) this).setItems(this.fromItemStack(viewing));
        ((VirtualContainer) (Object) viewing).setViewing(player);
        this.cachedViewSlot = 54 + player.getInventory().getSelectedSlot();
        this.viewer = player;
        this.displayName = viewing.getDisplayName();
    }

    // Use this one if an inventory is already open
    public VirtualShulkerBoxContainer(ItemStack viewing, Player player, int slot) {
        super(27);
        ((MoreComplexContainer) this).setItems(this.fromItemStack(viewing));
        ((VirtualContainer) (Object) viewing).setViewing(player);
        this.cachedViewSlot = slot;
        this.viewer = player;
        this.displayName = viewing.getDisplayName();
    }

    @Override
    public Component getDisplayName() {
        return this.displayName;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        this.containerMenu = new VirtualShulkerBoxMenu(containerId, inventory, this);
        return this.containerMenu;
    }

    @Override
    public void setChanged() {
        this.sync();
    }

    private NonNullList<ItemStack> fromItemStack(ItemStack item) {
        ItemContainerContents contents = item.get(DataComponents.CONTAINER);
        NonNullList<ItemStack> list = NonNullList.withSize(27, ItemStack.EMPTY);

        int index = 0;
        var iterator = contents.allItemsCopyStream().iterator();
        while (iterator.hasNext() && index < list.size()) {
            list.set(index++, iterator.next());
        }
        return list;
    }

    private void sync() {
        ItemContainerContents containerContents = ItemContainerContents.fromItems(this.getItems());
        this.getViewedStack().set(DataComponents.CONTAINER, containerContents);
    }

    public ItemStack getViewedStack() {
        if (this.cachedViewSlot == -2) {
            return this.containerMenu.getCarried();
        } else {
            return this.containerMenu.getSlot(this.cachedViewSlot).getItem();
        }
    }

    public void syncViewedStack(int slot) {
        System.out.println("Synced: " + slot);
        this.cachedViewSlot = slot;
    }
}
