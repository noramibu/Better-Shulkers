/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.gamerules.BetterShulkersGameRules;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRules;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRules.class)
public abstract class GameRuleRegistrationMixin {
    @Shadow
    private static GameRule<Boolean> registerBoolean(
            String id, GameRuleCategory category, boolean defaultValue) {
        return null;
    }

    @Inject(
            method = "<clinit>",
            at =
            @At(
                    value = "FIELD",
                    target =
                            "Lnet/minecraft/world/level/gamerules/GameRules;FIRE_SPREAD_RADIUS_AROUND_PLAYER:Lnet/minecraft/world/level/gamerules/GameRule;",
                    opcode = Opcodes.PUTSTATIC))
    private static void nodim$registerGameRules(CallbackInfo ci) {
        BetterShulkersGameRules.SHULKER_MATERIAL_ENCHANTMENT = registerBoolean("shulker_material_enchantment", GameRuleCategory.MISC, true);
        BetterShulkersGameRules.SHULKER_MATERIAL_RECIPE = registerBoolean("shulker_material_recipe", GameRuleCategory.MISC, false);
        BetterShulkersGameRules.DO_SHULKER_MATERIAL_DISPLAYS = registerBoolean("shulker_material_displays", GameRuleCategory.MISC, true);
        BetterShulkersGameRules.DO_SHULKER_DISPLAY_ANIMATIONS = registerBoolean("shulker_display_animations", GameRuleCategory.MISC, true);
        BetterShulkersGameRules.OPEN_SHULKERS_FROM_HOTBAR = registerBoolean("open_shulkers_from_hotbar", GameRuleCategory.MISC, true);
        BetterShulkersGameRules.OPEN_SHULKERS_FROM_INVENTORY = registerBoolean("open_shulkers_from_inventory", GameRuleCategory.MISC, true);
        BetterShulkersGameRules.INSERT_INTO_SHULKER_ON_PICKUP = registerBoolean("insert_into_shulker_on_pickup", GameRuleCategory.MISC, true);
    }
}
