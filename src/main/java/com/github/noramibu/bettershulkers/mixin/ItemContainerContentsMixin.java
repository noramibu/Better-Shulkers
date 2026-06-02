/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.ImprovedItemContainerContents;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.ItemContainerContents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemContainerContents.class)
public class ItemContainerContentsMixin implements ImprovedItemContainerContents {

    @Mutable
    @Shadow
    @Final
    private List<Optional<ItemStackTemplate>> items;


    @Override
    public void sync(List<Optional<ItemStackTemplate>> items) {
        this.items = items;
        System.out.println("Sync: " + this.items);
    }

    @Override
    public List<Optional<ItemStackTemplate>> mutableCopy() {
        return new ArrayList<>(this.items);
    }
}
