/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.container;

import net.minecraft.world.entity.player.Player;

public interface VirtualContainer {
    void setViewing(Player viewing);
    boolean isBeingViewed();
}
