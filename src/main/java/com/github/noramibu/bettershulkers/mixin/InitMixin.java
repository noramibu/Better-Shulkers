/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.BetterShulkers;
import net.minecraft.server.MinecraftServer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class InitMixin {
    @Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/server/MinecraftServer;OVERLOADED_THRESHOLD_NANOS:J", opcode = Opcodes.PUTSTATIC))
    private static void bettershulkers$init(CallbackInfo ci) {
        BetterShulkers.init();
    }
}
