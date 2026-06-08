/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.container;

import org.jetbrains.annotations.Nullable;

/**
 * Used to interact with the Player who is viewing a Virtual Container
 */
public interface VirtualContainerHolder {
    /**
     * Sets the Virtual Container cache for the Player
     * @param container Virtual Container to cache
     */
    void setVirtualContainer(@Nullable VirtualShulkerBoxContainer container);

    /**
     * Gets the Virtual Container cache
     * @return Virtual Container cache
     */
    VirtualShulkerBoxContainer getVirtualContainer();
}
