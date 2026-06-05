/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.container;

import java.util.List;
import java.util.Optional;
import net.minecraft.world.item.ItemStackTemplate;

public interface ImprovedItemContainerContents {
    void sync(List<Optional<ItemStackTemplate>> items);
    List<Optional<ItemStackTemplate>> mutableCopy();
}
