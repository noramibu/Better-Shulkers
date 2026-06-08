/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.material.display;

/**
 * Animations that update every tick
 */
public interface UpdatingAnimation {
    /**
     * Adds the animation to a Display
     * @param animation The {@link Animation} to add
     */
    void addAnimation(Animation animation);
}
