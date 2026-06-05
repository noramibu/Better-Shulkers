/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.MutableContainerContents;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.ItemContainerContents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemContainerContents.class)
public class ItemContainerContentsMixin implements MutableContainerContents {
    @Mutable
    @Shadow
    @Final
    private List<Optional<ItemStackTemplate>> items;

    @Override
    public int add(ItemStack type, int amount) {
        // Check for places to add the item
        for (int i = 0; i < this.items.size(); i++) {
            if (amount == 0) {
                return 0;
            }

            Optional<ItemStackTemplate> template = this.items.get(i);
            if (template.isPresent()) {
                ItemStackTemplate item = template.get();
                if (item.count() < item.getMaxStackSize()) {
                    ItemStack stored = item.create();
                    if (ItemStack.isSameItemSameComponents(stored, type)) {
                        int growBy = Math.min(amount, type.getMaxStackSize() - stored.count());
                        stored.grow(growBy);
                        this.items.set(i, Optional.of(ItemStackTemplate.fromNonEmptyStack(stored)));
                        amount -= growBy;
                    }
                }
            } else {
                // Store in empty space
                int stackSize =  Math.min(amount, type.getMaxStackSize());
                ItemStack fullStack = new ItemStack(type.getItem(), stackSize);
                this.items.set(i, Optional.of(ItemStackTemplate.fromNonEmptyStack(fullStack)));
                amount -= stackSize;
            }
        }

        if (amount == 0) {
            return 0;
        }

        // If there are no available space, create room
        List<Optional<ItemStackTemplate>> copy = new ArrayList<>(this.items);
        while (copy.size() < 27 && amount > 0) {
            int stackSize =  Math.min(amount, type.getMaxStackSize());
            amount -= stackSize;
            ItemStack fullStack = new ItemStack(type.getItem(), stackSize);
            copy.add(Optional.of(ItemStackTemplate.fromNonEmptyStack(fullStack)));
        }
        this.items = copy;

        return amount;
    }

    @Override
    public int remove(ItemStack type, int amount) {
        for (int i = 0; i < this.items.size(); i++) {
            if (amount == 0) {
                break;
            }

            Optional<ItemStackTemplate> template = this.items.get(i);
            if (template.isPresent()) {
                ItemStackTemplate item = template.get();
                ItemStack stored = item.create();
                if (ItemStack.isSameItemSameComponents(stored, type)) {
                    int shrinkBy = Math.min(amount, item.count());
                    stored.shrink(shrinkBy);
                    if (stored.isEmpty()) {
                        this.items.set(i, Optional.empty());
                    } else {
                        this.items.set(i, Optional.of(ItemStackTemplate.fromNonEmptyStack(stored)));
                    }
                    amount -= shrinkBy;
                }
            }
        }

        return amount;
    }

    @Override
    public void set(int i, ItemStack stack) {
        if (stack.isEmpty()) {
            this.items.set(i, Optional.empty());
        } else {
            this.items.set(i, Optional.of(ItemStackTemplate.fromNonEmptyStack(stack)));
        }
    }

    @Override
    public List<Optional<ItemStackTemplate>> getItems() {
        return this.items;
    }
}
