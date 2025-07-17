package com.github.noramibu.bettershulkers.interfaces;

import com.github.noramibu.bettershulkers.util.Animation;

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
