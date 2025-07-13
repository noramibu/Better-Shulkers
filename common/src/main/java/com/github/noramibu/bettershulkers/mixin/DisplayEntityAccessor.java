package com.github.noramibu.bettershulkers.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Display;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Display.class)
public interface DisplayEntityAccessor {
    @Accessor("DATA_SCALE_ID")
    static EntityDataAccessor<Vector3f> getScale() {
        throw new AssertionError();
    }

    @Invoker
    void invokeSetBillboardConstraints(Display.BillboardConstraints mode);
}
