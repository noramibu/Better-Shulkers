package me.noramibu.bettershulkers.mixin;

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

    @Invoker
    void invokeSetBillboardMode(DisplayEntity.BillboardMode mode);
}
