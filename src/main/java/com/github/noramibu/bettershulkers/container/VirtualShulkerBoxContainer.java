/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.container;

import com.github.noramibu.bettershulkers.ShulkerBoxUtils;
import com.github.noramibu.bettershulkers.menu.VirtualShulkerBoxMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.jspecify.annotations.Nullable;

public class VirtualShulkerBoxContainer extends SimpleContainer implements MenuProvider {
    private int cachedViewSlot;
    private VirtualShulkerBoxMenu containerMenu;
    private final Component displayName;

    public VirtualShulkerBoxContainer(ItemStack viewing, Player player) {
        super(27);
        ((MoreComplexContainer) this).setItems(this.fromItemStack(viewing));
        ((VirtualContainer) (Object) viewing).setViewing(player);
        this.cachedViewSlot = 54 + player.getInventory().getSelectedSlot();
        this.displayName = viewing.getDisplayName();
    }

    public VirtualShulkerBoxContainer(ItemStack viewing, Player player, int slot) {
        super(27);
        ((MoreComplexContainer) this).setItems(this.fromItemStack(viewing));
        ((VirtualContainer) (Object) viewing).setViewing(player);
        this.cachedViewSlot = slot;
        this.displayName = viewing.getDisplayName();
    }

    /**
     * Loads a new shulker when an existing UI is open
     * @param viewing The new shulker to view
     * @param player Player viewing
     * @param slot The inventory slot the shulker is in
     */
    public void reload(ItemStack viewing, Player player, int slot) {
        ((VirtualContainer) (Object) getViewedStack()).setViewing(null);
        ((MoreComplexContainer) this).setItems(this.fromItemStack(viewing));
        ((VirtualContainer) (Object) viewing).setViewing(player);
        this.cachedViewSlot = slot;
        // Refresh UI
        this.containerMenu.slotsChanged(this);
    }

    /**
     * Refreshes the existing shulker box inventory
     */
    public void refreshUI() {
        ((MoreComplexContainer) this).setItems(this.fromItemStack(this.getViewedStack()));
        // Refresh UI
        this.containerMenu.slotsChanged(this);
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

    /**
     * Gets the cached Virtual Container ItemStack cache
     * @return Virtual Container ItemStack
     */
    public ItemStack getViewedStack() {
        if (this.containerMenu == null) {
            return ItemStack.EMPTY;
        }

        if (this.cachedViewSlot == -2) {
            return this.containerMenu.getCarried();
        } else {
            return this.containerMenu.getSlot(this.cachedViewSlot).getItem();
        }
    }

    /**
     * Sets what slot the Viewed Stack is in
     * @param slot Slot index containing the Stack
     */
    public void setViewedSlot(int slot) {
        this.cachedViewSlot = slot;
    }

    @Override
    public void stopOpen(ContainerUser containerUser) {
        ((VirtualContainer) (Object) this.getViewedStack()).setViewing(null);
        ShulkerBoxUtils.playShulkerCloseSound(((ServerPlayer) containerUser.getLivingEntity()));
    }
}
