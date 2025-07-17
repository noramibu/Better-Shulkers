package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.interfaces.UpdatingAnimation;
import com.github.noramibu.bettershulkers.util.Animation;
import net.minecraft.world.entity.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Display.class)
public class ItemDisplayMixin implements UpdatingAnimation {
    private final List<Animation> animations = new ArrayList<>();
    @Override
    public void addAnimation(Animation animation) {
        this.animations.add(animation);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void checkAnimations(CallbackInfo ci) {
        this.animations.removeIf(animation -> animation.tick((Display) (Object) this));
    }
}
