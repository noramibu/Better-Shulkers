/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.BetterShulkers;
import java.util.function.Function;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class InitMixin {
    @Inject(method = "spin", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/atomic/AtomicReference;set(Ljava/lang/Object;)V"))
    private static <S> void bettershulkers$init(Function<Thread, S> factory, CallbackInfoReturnable<S> cir) {
        BetterShulkers.init();
    }
}
