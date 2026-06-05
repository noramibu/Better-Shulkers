/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers;

import org.jetbrains.annotations.Nullable;

public interface VirtualContainerHolder {
    void setVirtualContainer(@Nullable VirtualShulkerBoxContainer container);
    VirtualShulkerBoxContainer getVirtualContainer();
}
