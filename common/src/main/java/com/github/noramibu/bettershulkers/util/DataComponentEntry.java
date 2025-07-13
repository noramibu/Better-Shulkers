package com.github.noramibu.bettershulkers.util;

import net.minecraft.core.component.DataComponentType;

public record DataComponentEntry<T>(DataComponentType<T> type, T value) {
}
