package com.github.noramibu.bettershulkers.util;

import com.github.noramibu.bettershulkers.mixin.DisplayEntityAccessor;
import com.mojang.math.Transformation;
import net.minecraft.world.entity.Display;
import org.joml.Matrix4f;

public class DisplayEntityInterpolater {
    private final Matrix4f matrix = new Matrix4f();

    public DisplayEntityInterpolater moveVertical(float to) {
        this.matrix.translate(0, 0, to);
        return this;
    }

    public DisplayEntityInterpolater roll(float to) {
        this.matrix.rotateZ(to);
        return this;
    }

    public void build(Display.ItemDisplay display) {
        this.matrix.scale(display.getEntityData().get(DisplayEntityAccessor.getScale()));
        Transformation transformation = new Transformation(this.matrix);
        ((DisplayEntityAccessor)display).invokeSetTransformation(transformation);
    }
}
