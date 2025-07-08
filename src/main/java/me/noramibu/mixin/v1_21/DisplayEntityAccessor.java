package me.noramibu.mixin.v1_21;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.decoration.DisplayEntity;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DisplayEntity.class)
public interface DisplayEntityAccessor {

    @Accessor("SCALE")
    static TrackedData<Vector3f> getScale() {
        throw new AssertionError();
    }

    @Accessor("BRIGHTNESS")
    static TrackedData<Vector3f> getBrightness() {
        throw new AssertionError();
    }

    @Invoker
    void invokeSetViewRange(float viewRange);

    @Invoker
    void invokeSetDisplayWidth(float width);

    @Invoker
    void invokeSetDisplayHeight(float height);

    @Invoker
    void invokeSetBillboardMode(DisplayEntity.BillboardMode mode);

    @Invoker
    void invokeSetStartInterpolation(int startInterpolation);

    @Invoker
    void invokeSetInterpolationDuration(int interpolationDuration);
}
