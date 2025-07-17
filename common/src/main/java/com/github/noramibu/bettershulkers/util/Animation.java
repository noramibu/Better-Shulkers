package com.github.noramibu.bettershulkers.util;

import com.github.noramibu.bettershulkers.mixin.DisplayEntityAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Display;

public class Animation {
    private int ticks = 1;
    private final int duration;
    private final float rotation;
    private final float startRotation;
    private final float height;
    private final float startHeight;
    private final float heightOffset;

    public Animation(int duration, float startingRotation, float endingRotation, float startingHeight, float endingHeight, float heightOffset) {
        this.duration = duration;
        this.startHeight = startingHeight;
        this.startRotation = startingRotation;
        this.rotation = (endingRotation - startingRotation) / duration;
        this.height = (endingHeight - startingHeight) / duration;
        this.heightOffset = heightOffset;
    }

    public boolean tick(Display display) {
        ticks++;
        execute(display);
        return ticks == this.duration;
    }

    public void execute(Display display) {
        display.getEntityData().set(DisplayEntityAccessor.getInterpolationDelta(), 0);
        display.getEntityData().set(DisplayEntityAccessor.getTransInterpolationDuration(), 2);
        DisplayEntityInterpolater interpolater = new DisplayEntityInterpolater();
        if (this.ticks != this.duration) {
            interpolater.moveVertical(startHeight + (height * ticks) - heightOffset);
        } else {
            interpolater.moveVertical(startHeight + (height * ticks));
        }
        interpolater.roll((startRotation + rotation * ticks) * Mth.DEG_TO_RAD);
        interpolater.build((Display.ItemDisplay) display);
        display.getEntityData().set(DisplayEntityAccessor.getInterpolationDelta(), -1);
    }
}
