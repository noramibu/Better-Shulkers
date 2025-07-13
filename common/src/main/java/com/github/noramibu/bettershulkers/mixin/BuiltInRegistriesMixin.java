package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.event.ComponentModificationEvent;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltInRegistries.class)
public class BuiltInRegistriesMixin {
    @Shadow @Final public static DefaultedRegistry<Item> ITEM;

    @Inject(method = "freeze", at = @At("HEAD"))
    private static void modifyDefaultItemComponents(CallbackInfo ci) {
        ComponentModificationEvent.COMPONENT_MODIFICATION_EVENT.invoke(new ComponentModificationEvent(ITEM));
    }
}
