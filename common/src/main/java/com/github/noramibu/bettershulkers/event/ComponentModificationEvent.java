package com.github.noramibu.bettershulkers.event;

import com.github.noramibu.bettershulkers.mixin.ItemAccessor;
import com.github.noramibu.bettershulkers.util.DataComponentEntry;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;

public record ComponentModificationEvent(DefaultedRegistry<Item> items) {
    public static final Event<ComponentModificationEvent> COMPONENT_MODIFICATION_EVENT = new Event<>();

    public void modify(Predicate<Item> filter, DataComponentEntry... components) {
        items.forEach(item -> {
            if (filter.test(item)) {
               DataComponentMap.Builder builder = DataComponentMap.builder();
               for (DataComponentEntry entry : components) {
                   builder.set(entry.type(), entry.value());
               }
               builder.addAll(((ItemAccessor)item).getComponents());
                ((ItemAccessor)item).setComponents(builder.build());
            }
        });
    }
}
