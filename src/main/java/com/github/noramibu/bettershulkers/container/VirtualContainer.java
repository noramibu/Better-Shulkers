/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.container;

import net.minecraft.world.entity.player.Player;

/**
 * Allows tracking if a Container is being viewed remotely
 */
public interface VirtualContainer {
    /**
     * Starts tracking a Container
     * @param viewing The Player viewing the Container Menu
     */
    void setViewing(Player viewing);

    /**
     * Gets if the Container is being viewed remotely
     * @return If the Container is being viewed remotely
     */
    boolean isBeingViewed();
}
