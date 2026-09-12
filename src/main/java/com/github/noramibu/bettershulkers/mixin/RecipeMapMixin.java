package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.BetterShulkers;
import com.github.noramibu.bettershulkers.recipe.ShulkerUpgradeRecipe;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.world.item.crafting.RecipeMap.class)
public class RecipeMapMixin {
    @Inject(method = "create", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMultimap$Builder;build()Lcom/google/common/collect/ImmutableMultimap;"))
    private static void injectCustomShulkerRecipe(HolderLookup<Recipe<?>> recipes, CallbackInfoReturnable<RecipeMap> cir, @Local(name = "byType") ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> byType, @Local(name = "byKey") ImmutableMap.Builder<ResourceKey<Recipe<?>>, RecipeHolder<?>> byKey) {
        ShulkerUpgradeRecipe recipe = new ShulkerUpgradeRecipe();
        Identifier resourceLocation = Identifier.fromNamespaceAndPath(BetterShulkers.MOD_ID, "shulker_upgrade");
        ResourceKey<Recipe<?>> resourceKey = ResourceKey.create(Registries.RECIPE, resourceLocation);
        RecipeHolder<ShulkerUpgradeRecipe> recipeHolder = new RecipeHolder<>(resourceKey, recipe);

        byType.put(recipe.getType(), recipeHolder);
        byKey.put(resourceKey, recipeHolder);
    }
}
