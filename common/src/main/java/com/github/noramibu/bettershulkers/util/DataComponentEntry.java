package com.github.noramibu.bettershulkers.util;

import net.minecraft.core.component.DataComponentType;

/**
 * Stores data for a Component
 * @param type The type of component: {@link DataComponentType}
 * @param value The value to insert into the Component
 * @param <T> The type of the value
 */
public record DataComponentEntry<T>(DataComponentType<T> type, T value) {
}
