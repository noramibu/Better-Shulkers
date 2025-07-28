package com.github.noramibu.bettershulkers.enchantment;

import com.github.noramibu.bettershulkers.mixin.ServerPlayerAccessor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.HopperMenu;
import net.minecraft.world.item.Items;

public class MaterialFilterUI extends HopperMenu {
    public MaterialFilterUI(Player player, int enchantmentLevel) {
        this(((ServerPlayerAccessor) player).getContainerCounter(), player.getInventory(), enchantmentLevel);
    }

    public MaterialFilterUI(int i, Inventory inventory, int enchantmentLevel) {
        super(i, inventory);
        for (i = 0; i < HopperMenu.CONTAINER_SIZE; i++) {
            if (i >= enchantmentLevel) {
                this.slots.get(i).set(Items.BARRIER.getDefaultInstance());
            } else {
                break;
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
