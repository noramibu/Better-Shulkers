/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.material.display.Animation;
import com.github.noramibu.bettershulkers.material.display.UpdatingAnimation;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Display.class)
public class DisplayMixin implements UpdatingAnimation {
    private final List<Animation> animations = new ArrayList<>();
    @Override
    public void addAnimation(Animation animation) {
        this.animations.add(animation);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void bettershulkers$checkAnimations(CallbackInfo ci) {
        this.animations.removeIf(animation -> animation.tick((Display) (Object) this));
    }
}
