package com.github.noramibu.bettershulkers.util;

import com.github.noramibu.bettershulkers.mixin.DisplayEntityAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Display;

// TODO Reduce the amount of packets being sent
public class Animation {
    private int ticks = 1;
    private final int duration;
    private final float rotation;
    private final float startRotation;
    private final float height;
    private final float startHeight;
    private final float heightOffset;
    private final float finalJump;

    /**
     * Stores basic transformation information
     * @param duration How many ticks the animation should take place over
     * @param startingRotation The starting rotation of the Display
     * @param endingRotation The final rotation of the Display
     * @param startingHeight The starting Z-axis height of the Display
     * @param endingHeight The final Z-axis height of the Display
     * @param heightOffset Adjustment to the Z-axis during animation (removed in the last tick)
     * @param finalJump The final Z-axis translation after the duration (set to 0 to disable)
     */
    public Animation(int duration, float startingRotation, float endingRotation, float startingHeight, float endingHeight, float heightOffset, float finalJump) {
        this.duration = duration;
        this.startHeight = startingHeight;
        this.startRotation = startingRotation;
        this.rotation = (endingRotation - startingRotation) / duration;
        this.height = (endingHeight - startingHeight) / duration;
        this.heightOffset = heightOffset;
        this.finalJump = finalJump;
    }

    /**
     * Ticks the Animation
     * @param display {@link Display} to apply the Animation to
     * @return If the Animation has concluded
     */
    public boolean tick(Display display) {
        ticks++;
        execute(display);
        return this.finalJump == 0 ? ticks == this.duration : ticks == this.duration + 1;
    }

    /**
     * Runs the Animation
     * @param display {@link Display} to apply the Animation to
     */
    public void execute(Display display) {
        display.getEntityData().set(DisplayEntityAccessor.getInterpolationDelta(), 0);
        display.getEntityData().set(DisplayEntityAccessor.getTransInterpolationDuration(), 2);
        DisplayEntityInterpolator interpolater = new DisplayEntityInterpolator();
        if (this.ticks < this.duration) {
            interpolater.moveVertical(startHeight + (height * ticks) - heightOffset)
                    .roll((startRotation + rotation * ticks) * Mth.DEG_TO_RAD);
        } else if (this.ticks == this.duration){
            interpolater.moveVertical(startHeight + (height * ticks))
                    .roll((startRotation + rotation * ticks) * Mth.DEG_TO_RAD);
        } else if (this.ticks == this.duration + 1) {
            interpolater.moveVertical(startHeight + (height * (ticks - 1)) + finalJump);
        }
        interpolater.build((Display.ItemDisplay) display);
        display.getEntityData().set(DisplayEntityAccessor.getInterpolationDelta(), -1);
    }
}
