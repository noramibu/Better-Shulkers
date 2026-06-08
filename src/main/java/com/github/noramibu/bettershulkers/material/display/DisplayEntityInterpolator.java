/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.material.display;

import com.github.noramibu.bettershulkers.mixin.DisplayEntityAccessor;
import com.mojang.math.Transformation;
import net.minecraft.world.entity.Display;
import org.joml.Matrix4f;

/**
 * Handles interpolation of item displays on shulker boxes
 */
public class DisplayEntityInterpolator {
    private final Matrix4f matrix = new Matrix4f();

    /**
     * Moves the item display along the Z-axis
     *
     * @param to The final destination
     */
    public void moveVertical(float to) {
        this.matrix.translate(0, 0, to);
    }

    /**
     * Rotates the item display along the Z-axis
     *
     * @param to The final rotation (must be [-Pi, Pi])
     */
    public void roll(float to) {
        this.matrix.rotateZ(to);
    }

    /**
     * Applies the translation to the ItemDisplay
     * @param display {@link net.minecraft.world.entity.Display.ItemDisplay} to apply the translation to
     */
    public void build(Display.ItemDisplay display) {
        this.matrix.scale(display.getEntityData().get(DisplayEntityAccessor.getScale()));
        Transformation transformation = new Transformation(this.matrix);
        ((DisplayEntityAccessor)display).invokeSetTransformation(transformation);
    }
}
