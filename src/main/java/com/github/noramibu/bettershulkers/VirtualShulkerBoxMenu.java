/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ShulkerBoxMenu;

public class VirtualShulkerBoxMenu extends ShulkerBoxMenu implements MoveItemListener {
    VirtualShulkerBoxContainer container;
    public VirtualShulkerBoxMenu(int containerId, Inventory inventory, Container container) {
        super(containerId, inventory, container);
        this.container = (VirtualShulkerBoxContainer) container;
    }

    @Override
    public void itemMoved(ItemMove... moves) {
        for (ItemMove move : moves) {
            if (((VirtualContainer) (Object) move.stack()).isBeingViewed()) {
                this.container.cachedViewSlot = move.targetSlot();
                System.out.println("Viewed Stack changed to " + move.targetSlot());
            }
        }
    }
}
