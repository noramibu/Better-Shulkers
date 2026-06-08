/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.menu;

import com.github.noramibu.bettershulkers.container.VirtualContainer;
import com.github.noramibu.bettershulkers.container.VirtualShulkerBoxContainer;
import com.github.noramibu.bettershulkers.event.ItemMove;
import com.github.noramibu.bettershulkers.event.MoveItemListener;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ShulkerBoxMenu;

/**
 * The Menu for the Shulker Boxes opened from an Inventory
 */
public class VirtualShulkerBoxMenu extends ShulkerBoxMenu implements MoveItemListener {
    private final VirtualShulkerBoxContainer container;
    public VirtualShulkerBoxMenu(int containerId, Inventory inventory, Container container) {
        super(containerId, inventory, container);
        this.container = (VirtualShulkerBoxContainer) container;
    }

    @Override
    public void itemMoved(ItemMove... moves) {
        for (ItemMove move : moves) {
            if (((VirtualContainer) (Object) move.stack()).isBeingViewed()) {
                this.container.setViewedSlot(move.targetSlot());
            }
        }
    }

    /**
     * Gets the Virtual Container attached to this Menu
     * @return Virtual Container attached to the Menu
     */
    public VirtualShulkerBoxContainer getContainer() {
        return this.container;
    }
}
