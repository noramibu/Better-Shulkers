package com.github.noramibu.bettershulkers.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ShulkerBoxMenu.class)
public interface ShulkerBoxMenuHandlerAccessor {
    @Accessor("container")
    Container getInventory();
}
